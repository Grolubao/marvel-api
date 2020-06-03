package yapily.marvel.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.google.common.annotations.VisibleForTesting;
import io.swagger.annotations.ApiModelProperty;
import yapily.marvel.exceptions.MissingJsonNodeException;
import yapily.marvel.exceptions.UnableToProcessJsonException;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a Marvel Character and its properties
 */
public class MarvelCharacter {
    @ApiModelProperty(notes = "The Marvel Character ID")
    private final Long id;
    @ApiModelProperty(notes = "The Marvel Character name")
    private final String name;
    @ApiModelProperty(notes = "The Marvel Character description")
    private String description;
    private final Thumbnail thumbnail;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String THUMBNAIL = "thumbnail";
    private static final String EXTENSION = "extension";
    private static final String PATH = "path";
    private static final String DATA = "data";
    private static final String RESULTS = "results";

    public MarvelCharacter(Long id, String name, String description, Thumbnail thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setTranslatedDescription(String translatedDescription) {
        this.description = translatedDescription;
    }

    /**
     * Given a json composed by an array of characters, parses it and unmarshall it into a list of {@link MarvelCharacter}
     *
     * @param json - the json to unmarshal
     * @return - {@link List} of {@link MarvelCharacter}
     * @throws UnableToProcessJsonException - this Exception will be thrown if there is any problem unmarshalling the json String
     */
    public static List<MarvelCharacter> getCharactersFromJson(String json) throws UnableToProcessJsonException, MissingJsonNodeException {
        JsonNode results;
        try {
            results = getJsonResults(json);
        } catch (JsonProcessingException e) {
            throw new UnableToProcessJsonException(e);
        }

        List<MarvelCharacter> marvelCharacters = new ArrayList<>();

        for (JsonNode result : results) {
            marvelCharacters.add(getCharacterFromJson(result));
        }

        return marvelCharacters;
    }

    /**
     * The Json that is returned by the Marvel API consists of properties and a "data" which contains the results. This method will
     * return that
     *
     * @param json - the json to process
     * @return - A {@link JsonNode} containing the array of json objects where the Marvel characters will be
     * @throws JsonProcessingException - this Exception will be thrown if there is any problem unmarshalling the json String
     */
    @VisibleForTesting
    protected static JsonNode getJsonResults(String json) throws JsonProcessingException, MissingJsonNodeException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode path = rootNode.path(DATA).path(RESULTS);

        if (path instanceof MissingNode) {
            throw new MissingJsonNodeException();
        }

        return path;
    }


    /**
     * Creates a {@link MarvelCharacter} from json
     *
     * @param result - the json node containing the character
     * @return - A {@link MarvelCharacter}
     */
    private static MarvelCharacter getCharacterFromJson(JsonNode result) {
        return new MarvelCharacter(
                result.get(ID).asLong(),
                result.get(NAME).asText(),
                result.get(DESCRIPTION).asText(),
                new Thumbnail(result.get(THUMBNAIL).get(PATH).asText(),
                        result.get(THUMBNAIL).get(EXTENSION).asText()));
    }

    /**
     * Defines a Thumbnail composed of a path and an extension
     */
    public static class Thumbnail {
        @ApiModelProperty(notes = "The Marvel Character thumbnail path")
        private final String path;
        @ApiModelProperty(notes = "The Marvel Character thumbnail extension")
        private final String extension;

        public Thumbnail(String path, String extension) {
            this.path = path;
            this.extension = extension;
        }

        public String getPath() {
            return path;
        }

        public String getExtension() {
            return extension;
        }
    }
}
