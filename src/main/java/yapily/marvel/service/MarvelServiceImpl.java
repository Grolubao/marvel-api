package yapily.marvel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import yapily.marvel.exceptions.*;
import yapily.marvel.model.MarvelCharacter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class MarvelServiceImpl implements MarvelService {
    protected static final String EXTERNAL_MARVEL_API = "https://gateway.marvel.com:443/v1/public";
    protected static final String CHARACTERS_API = "/characters";
    protected static final int DEFAULT_OFFSET = 100;
    protected static final String ENGLISH_LANGUAGE_CODE = "en";

    @Value("${api.keys.location}")
    private String apiKeysLocation;

    @Value("${marvel.key.public}")
    private String marvelPublicKey;

    @Value("${marvel.key.private}")
    private String marvelPrivateKey;

    private RestTemplate restTemplate;

    private TranslateService translateService;

    public MarvelServiceImpl(TranslateService translateService, RestTemplateBuilder builder) {
        this.translateService = translateService;
        this.restTemplate = builder.build();
    }

    /**
     * Gets the IDs from all the Marvel Characters. Since this method is expensive, the application stores it into the cache.
     *
     * @return - a List of all the IDs from the Marvel Characters
     * @throws MarvelApiKeyNotFoundException - thrown if the Marvel API key cannot be located
     * @throws UnableToProcessJsonException  - thrown if there was an error processing the json String
     */
    @Override
    @Cacheable("characters")
    public List<Long> getCharactersIds() throws MarvelApiKeyNotFoundException, UnableToProcessJsonException, MissingJsonNodeException,
            JsonProcessingException {
        List<MarvelCharacter> allCharactersFromJson = new ArrayList<>();
        List<MarvelCharacter> charactersFromJson;

        //The Marvel API can only fetch 100 results at a time so we need to make multiple requests changing the offset until we can't
        // find any more results
        int currentOffset = 0;

        do {
            String json = restTemplate.getForObject(EXTERNAL_MARVEL_API + CHARACTERS_API + getApiUri() + "&offset=" + currentOffset,
                    String.class);
            charactersFromJson = MarvelCharacter.getCharactersFromJson(json);
            allCharactersFromJson.addAll(charactersFromJson);
            currentOffset += DEFAULT_OFFSET;
        } while (!charactersFromJson.isEmpty());

        return allCharactersFromJson.stream().map(MarvelCharacter::getId).collect(Collectors.toList());
    }

    /**
     * Gets a specific {@link MarvelCharacter} by ID
     *
     * @param id - the id of the Marvel Character to fetch
     * @return - the {@link MarvelCharacter}
     * @throws CharacterNotFoundException    - thrown if no Character could be found
     * @throws MarvelApiKeyNotFoundException - thrown if the Marvel API key cannot be located
     * @throws UnableToProcessJsonException  - thrown if there was an error processing the json String
     */
    @Override
    public MarvelCharacter getCharacter(Long id) throws CharacterNotFoundException, MarvelApiKeyNotFoundException,
            UnableToProcessJsonException, MissingJsonNodeException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String json = null;
        try {
            json = restTemplate.getForObject(EXTERNAL_MARVEL_API + CHARACTERS_API + "/" + id + getApiUri(), String.class);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new CharacterNotFoundException(id, e);
            }
        }

        List<MarvelCharacter> charactersFromJson = MarvelCharacter.getCharactersFromJson(json);

        return charactersFromJson.get(0);
    }

    /**
     * Gets a specific {@link MarvelCharacter} by ID translating its description to a different language
     *
     * @param id           - the id of the Marvel Character to fetch
     * @param languageCode - A 2 character language code to translate the description to. The list of codes can be found here: https://en
     *                     .wikipedia.org/wiki/List_of_ISO_639-1_codes
     * @return - the {@link MarvelCharacter} with its description translated
     * @throws CharacterNotFoundException    - thrown if no Character could be found
     * @throws MarvelApiKeyNotFoundException - thrown if the Marvel API key cannot be located
     * @throws YandexApiKeyNotFoundException - thrown if the Yandex API key cannot be located
     * @throws UnableToProcessJsonException  - thrown if there was an error processing the json String
     */
    @Override
    public MarvelCharacter getTranslatedCharacter(Long id, String languageCode) throws CharacterNotFoundException,
            MarvelApiKeyNotFoundException, YandexApiKeyNotFoundException, UnableToProcessJsonException, MissingJsonNodeException,
            JsonProcessingException {
        MarvelCharacter character = getCharacter(id);

        character.setTranslatedDescription(translateService.translate(character.getDescription(), ENGLISH_LANGUAGE_CODE, languageCode));

        return character;
    }

    /**
     * Gets the necessary attributes to make a call to the Marvel API. This includes a timestamp an apikey and a md5hash of these
     *
     * @return - the API URI
     * @throws MarvelApiKeyNotFoundException - thrown if the Marvel API key cannot be located
     */
    @VisibleForTesting
    protected String getApiUri() throws MarvelApiKeyNotFoundException {
        //Loads the Marvel API key file which contains 2 properties: marvelPublicKey and marvelPrivateKey
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(apiKeysLocation));
        } catch (IOException e) {
            throw new MarvelApiKeyNotFoundException(apiKeysLocation, e);
        }
        long timestamp = new Date().getTime();
        String stringToHash = Long.toString(timestamp) + properties.get(marvelPrivateKey) + properties.get(marvelPublicKey);

        String md5Hex = DigestUtils.md5Hex(stringToHash);

        return "?ts=" + timestamp + "&apikey=" + properties.get(marvelPublicKey) + "&hash=" + md5Hex;
    }
}
