package yapily.marvel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yapily.marvel.exceptions.*;
import yapily.marvel.model.MarvelCharacter;
import yapily.marvel.service.MarvelService;

import java.util.List;

@RestController
@Api("Yapily Marvel Characters API")
public class MarvelController {
    public static final String CHARACTERS = "/characters";
    public static final String CHARACTERS_ID = "/characters/{id}";
    public static final String ID = "id";
    public static final String LANGUAGE = "language";
    private final MarvelService marvelService;

    public MarvelController(MarvelService marvelService) {
        this.marvelService = marvelService;
    }

    @GetMapping(CHARACTERS)
    @ApiOperation(value = "Retrieves the list of all Marvel Character IDs", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all Marvel Character IDs"),
            @ApiResponse(code = 403, message = "Unable to retrieve the API key"),
            @ApiResponse(code = 404, message = "Unable to find the list of Marvel Characters")
    })
    public List<Long> getCharacters() throws MarvelApiKeyNotFoundException, UnableToProcessJsonException, MissingJsonNodeException,
            JsonProcessingException {
        return marvelService.getCharactersIds();
    }

    @GetMapping(CHARACTERS_ID)
    @ApiOperation(value = "Retrieves a single Marvel Character", response = MarvelCharacter.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched the Marvel Character"),
            @ApiResponse(code = 403, message = "Unable to retrieve the API key"),
            @ApiResponse(code = 404, message = "Unable to find the specified Marvel Character")
    })
    public MarvelCharacter getCharacter(@ApiParam(value = "The Marvel Character ID to fetch", required = true)
                                        @PathVariable(ID) Long id,
                                        @ApiParam(value = "An optional language code that can be used to automatically translate the " +
                                                "Marvel Character description into the desirable language")
                                        @RequestParam(name = LANGUAGE, required = false) String language)
            throws CharacterNotFoundException, MarvelApiKeyNotFoundException, YandexApiKeyNotFoundException, UnableToProcessJsonException
            , MissingJsonNodeException, JsonProcessingException {

        if (StringUtils.isEmpty(language)) {
            return marvelService.getCharacter(id);
        } else {
            return marvelService.getTranslatedCharacter(id, language);
        }
    }
}
