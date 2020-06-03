package yapily.marvel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the application was unable to find the "data" or "results" node in the request
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MissingJsonNodeException extends Exception {

    public MissingJsonNodeException() {
        super("Couldn't find the 'data' or 'results' node in the json response");
    }
}
