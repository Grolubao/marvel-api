package yapily.marvel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import yapily.marvel.exceptions.*;
import yapily.marvel.model.MarvelCharacter;

import java.util.List;

public interface MarvelService {
    /**
     * Gets the IDs from all the Marvel Characters. Since this method is expensive, the application stores it into the cache.
     *
     * @return - a List of all the IDs from the Marvel Characters
     * @throws MarvelApiKeyNotFoundException - thrown if the Marvel API key cannot be located
     * @throws UnableToProcessJsonException  - thrown if there was an error processing the json String
     */
    List<Long> getCharactersIds() throws MarvelApiKeyNotFoundException, UnableToProcessJsonException, MissingJsonNodeException,
            JsonProcessingException;

    /**
     * Gets a specific {@link MarvelCharacter} by ID
     *
     * @param id - the id of the Marvel Character to fetch
     * @return - the {@link MarvelCharacter}
     * @throws CharacterNotFoundException    - thrown if no Character could be found
     * @throws MarvelApiKeyNotFoundException - thrown if the Marvel API key cannot be located
     * @throws UnableToProcessJsonException  - thrown if there was an error processing the json String
     */
    MarvelCharacter getCharacter(Long id) throws CharacterNotFoundException, MarvelApiKeyNotFoundException, UnableToProcessJsonException,
            MissingJsonNodeException, JsonProcessingException;

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
    MarvelCharacter getTranslatedCharacter(Long id, String languageCode) throws CharacterNotFoundException, MarvelApiKeyNotFoundException
            , YandexApiKeyNotFoundException, UnableToProcessJsonException, MissingJsonNodeException, JsonProcessingException;
}
