package yapily.marvel;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import yapily.marvel.exceptions.MarvelApiKeyNotFoundException;
import yapily.marvel.exceptions.MissingJsonNodeException;
import yapily.marvel.exceptions.UnableToProcessJsonException;
import yapily.marvel.service.MarvelService;

/**
 * Initializes the Cache
 */
@Component
public class CacheInit implements ApplicationListener<ApplicationReadyEvent> {
    private final MarvelService marvelService;

    public CacheInit(MarvelService marvelService) {
        this.marvelService = marvelService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //Since getting all the Marvel Characters is an expensive operation, once the Application starts, we'll call the getCharacters
        // method to store it in the cache.
        try {
            marvelService.getCharactersIds();
        } catch (MarvelApiKeyNotFoundException | UnableToProcessJsonException | MissingJsonNodeException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}