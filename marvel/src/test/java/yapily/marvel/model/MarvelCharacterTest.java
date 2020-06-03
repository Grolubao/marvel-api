package yapily.marvel.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import yapily.marvel.exceptions.MissingJsonNodeException;
import yapily.marvel.exceptions.UnableToProcessJsonException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MarvelCharacterTest {

    public static final String SAMPLE_MARVEL_CHARACTERS_JSON = "/sample_marvel_characters.json";
    public static final String MISSING_NODE_MARVEL_CHARACTERS_JSON = "/missing_node_marvel_characters.json";
    public static final String INCORRECT_SAMPLE_MARVEL_CHARACTERS_JSON = "/incorrect_sample_marvel_characters.json";

    @Test
    void getCharactersFromJson_with_correct_json_will_be_successful() throws IOException, UnableToProcessJsonException,
            URISyntaxException, MissingJsonNodeException {
        String json = new String(Files.readAllBytes(Paths.get(getClass().getResource(SAMPLE_MARVEL_CHARACTERS_JSON).toURI())));

        List<MarvelCharacter> charactersFromJson = MarvelCharacter.getCharactersFromJson(json);

        Assertions.assertThat(charactersFromJson).isNotNull();
        Assertions.assertThat(charactersFromJson.size()).isEqualTo(20);

        MarvelCharacter marvelCharacter = charactersFromJson.get(1);

        Assertions.assertThat(marvelCharacter.getId()).isEqualTo(1017100);
        Assertions.assertThat(marvelCharacter.getName()).isEqualTo("A-Bomb (HAS)");
        Assertions.assertThat(marvelCharacter.getDescription()).isEqualTo("Rick Jones has been Hulk's best bud since day one, but now " +
                "he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just" +
                " as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of " +
                "destruction! ");
        Assertions.assertThat(marvelCharacter.getThumbnail().getPath()).isEqualTo("http://i.annihil.us/u/prod/marvel/i/mg/3/20" +
                "/5232158de5b16");
        Assertions.assertThat(marvelCharacter.getThumbnail().getExtension()).isEqualTo("jpg");
    }

    @Test()
    void getCharactersFromJson_with_incorrect_json_will_throw_exception() throws IOException, URISyntaxException {
        String json = new String(Files.readAllBytes(Paths.get(getClass().getResource(INCORRECT_SAMPLE_MARVEL_CHARACTERS_JSON).toURI())));

        assertThrows(UnableToProcessJsonException.class,
                () -> {
                    MarvelCharacter.getCharactersFromJson(json);
                });
    }

    @Test()
    void getJsonResults_with_missing_node_will_throw_exception() throws IOException, URISyntaxException {
        String json = new String(Files.readAllBytes(Paths.get(getClass().getResource(MISSING_NODE_MARVEL_CHARACTERS_JSON).toURI())));

        assertThrows(MissingJsonNodeException.class,
                () -> {
                    MarvelCharacter.getJsonResults(json);
                });
    }

    @Test
    void getJsonResults_will_return_successfully() throws URISyntaxException, IOException, MissingJsonNodeException {
        String json = new String(Files.readAllBytes(Paths.get(getClass().getResource(SAMPLE_MARVEL_CHARACTERS_JSON).toURI())));

        Assertions.assertThat(MarvelCharacter.getJsonResults(json).size()).isEqualTo(20);
    }
}