package yapily.marvel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a Character is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CharacterNotFoundException extends Exception {

    public CharacterNotFoundException(Long id, Exception e) {
        super("The Character with the id: " + id + " was not found", e);
    }
}
