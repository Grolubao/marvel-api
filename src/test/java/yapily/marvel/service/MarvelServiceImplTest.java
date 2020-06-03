package yapily.marvel.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import yapily.marvel.exceptions.*;
import yapily.marvel.model.MarvelCharacter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static yapily.marvel.service.MarvelServiceImpl.CHARACTERS_API;
import static yapily.marvel.service.MarvelServiceImpl.EXTERNAL_MARVEL_API;

@RunWith(MockitoJUnitRunner.class)
public class MarvelServiceImplTest {
    @Mock
    private TranslateService translateService;
    @Mock
    private RestTemplateBuilder builder;

    public static final String SAMPLE_MARVEL_CHARACTERS_JSON = "/sample_marvel_characters.json";
    public static final String EMPTY_MARVEL_CHARACTERS_JSON = "/empty_marvel_characters.json";

    @Test
    public void getCharactersIds_should_return_successfully() throws MarvelApiKeyNotFoundException, URISyntaxException, IOException,
            UnableToProcessJsonException, MissingJsonNodeException {
        MockitoAnnotations.initMocks(this);
        String json = new String(Files.readAllBytes(Paths.get(getClass().getResource(SAMPLE_MARVEL_CHARACTERS_JSON).toURI())));
        String emptyJson = new String(Files.readAllBytes(Paths.get(getClass().getResource(EMPTY_MARVEL_CHARACTERS_JSON).toURI())));

        String apiUri = "?ts=1589824139659&apikey=19906446e8f82a4809bca1f44a213063&hash=66839952294ed5de8adba5c2ff1bcbc8";

        RestTemplate restTemplateMock = mock(RestTemplate.class);
        when(builder.build()).thenReturn(restTemplateMock);

        MarvelServiceImpl marvelServiceSpy = Mockito.spy(new MarvelServiceImpl(translateService, builder));
        Mockito.doReturn(apiUri).
                when(marvelServiceSpy).getApiUri();

        when(restTemplateMock.getForObject(EXTERNAL_MARVEL_API + CHARACTERS_API + apiUri + "&offset=0", String.class)).thenReturn(json);
        when(restTemplateMock.getForObject(EXTERNAL_MARVEL_API + CHARACTERS_API + apiUri + "&offset=100", String.class)).thenReturn(emptyJson);

        List<Long> expectedIds = new ArrayList<>(Arrays.asList(1011334L, 1017100L, 1009144L, 1010699L, 1009146L, 1016823L, 1009148L,
                1009149L, 1010903L, 1011266L, 1010354L, 1010846L, 1011297L, 1011031L, 1009150L, 1011198L, 1011175L, 1011136L, 1011176L,
                1010870L));

        Assertions.assertThat(marvelServiceSpy.getCharactersIds()).isEqualTo(expectedIds);
    }

    @Test
    public void getCharacters_should_return_successfully() throws MarvelApiKeyNotFoundException, IOException,
            UnableToProcessJsonException, MissingJsonNodeException, CharacterNotFoundException {
        MockitoAnnotations.initMocks(this);
        String apiUri = "?ts=1589824139659&apikey=19906446e8f82a4809bca1f44a213063&hash=66839952294ed5de8adba5c2ff1bcbc8";

        RestTemplate restTemplateMock = mock(RestTemplate.class);
        when(builder.build()).thenReturn(restTemplateMock);

        MarvelServiceImpl marvelServiceSpy = Mockito.spy(new MarvelServiceImpl(translateService, builder));
        Mockito.doReturn(apiUri).
                when(marvelServiceSpy).getApiUri();

        MarvelCharacter marvelCharacter = new MarvelCharacter(1017100L, "A-Bomb (HAS)", "Rick Jones has been Hulk's best bud since day " +
                "one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored" +
                " skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball " +
                "of destruction! ", new MarvelCharacter.Thumbnail("http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        MarvelCharacter returnedCharacter = marvelServiceSpy.getCharacter(1017100L);

        Assertions.assertThat(returnedCharacter.getId()).isEqualTo(marvelCharacter.getId());
        Assertions.assertThat(returnedCharacter.getName()).isEqualTo(marvelCharacter.getName());
        Assertions.assertThat(returnedCharacter.getDescription()).isEqualTo(marvelCharacter.getDescription());
        Assertions.assertThat(returnedCharacter.getThumbnail().getPath()).isEqualTo(marvelCharacter.getThumbnail().getPath());
        Assertions.assertThat(returnedCharacter.getThumbnail().getExtension()).isEqualTo(marvelCharacter.getThumbnail().getExtension());
    }

    @Test
    public void getTranslatedCharacter_should_return_successfully() throws MarvelApiKeyNotFoundException, IOException,
            UnableToProcessJsonException, MissingJsonNodeException, YandexApiKeyNotFoundException, CharacterNotFoundException {
        MockitoAnnotations.initMocks(this);
        RestTemplate restTemplateMock = mock(RestTemplate.class);
        when(builder.build()).thenReturn(restTemplateMock);

        MarvelCharacter marvelCharacter = new MarvelCharacter(1017100L, "A-Bomb (HAS)", "Rick Jones has been Hulk's best bud since day " +
                "one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored" +
                " skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball " +
                "of destruction! ", new MarvelCharacter.Thumbnail("http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "jpg"));

        MarvelServiceImpl marvelServiceSpy = Mockito.spy(new MarvelServiceImpl(translateService, builder));
        Mockito.doReturn(marvelCharacter).when(marvelServiceSpy).getCharacter(1017100L);

        when(translateService.translate(marvelCharacter.getDescription(), "en", "de")).thenReturn("Rick Jones ist seit dem ersten Tag " +
                "Hulks bester Freund, aber jetzt ist er mehr als ein Freund ... er ist ein Teamkollege! Durch eine Gamma-Energieexplosion" +
                " verwandelt, ist A-Bomb's dicke, gepanzerte Haut genauso stark und kraftvoll wie blau. Und wenn er sich in Aktion rollt," +
                " benutzt er es wie eine riesige Bowlingkugel der Zerstörung!");


        MarvelCharacter returnedCharacter = marvelServiceSpy.getTranslatedCharacter(1017100L, "de");

        Assertions.assertThat(returnedCharacter.getId()).isEqualTo(marvelCharacter.getId());
        Assertions.assertThat(returnedCharacter.getName()).isEqualTo(marvelCharacter.getName());
        Assertions.assertThat(returnedCharacter.getDescription()).isEqualTo("Rick Jones ist seit dem ersten Tag Hulks bester Freund, aber" +
                " jetzt ist er mehr als ein Freund ... er ist ein Teamkollege! Durch eine Gamma-Energieexplosion verwandelt, ist A-Bomb's" +
                " dicke, gepanzerte Haut genauso stark und kraftvoll wie blau. Und wenn er sich in Aktion rollt, benutzt er es wie eine " +
                "riesige Bowlingkugel der Zerstörung!");
        Assertions.assertThat(returnedCharacter.getThumbnail().getPath()).isEqualTo(marvelCharacter.getThumbnail().getPath());
        Assertions.assertThat(returnedCharacter.getThumbnail().getExtension()).isEqualTo(marvelCharacter.getThumbnail().getExtension());

    }

}