package yapily.marvel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the Marvel API key is not found
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class MarvelApiKeyNotFoundException extends Exception {

    public MarvelApiKeyNotFoundException(String location, Exception e) {
        super("The Marvel API key was not found at the location: " + location + ". Please consult the README.md for instructions", e);
    }
}
