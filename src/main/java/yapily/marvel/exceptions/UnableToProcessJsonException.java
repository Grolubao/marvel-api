package yapily.marvel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the application was unable to process the json String
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnableToProcessJsonException extends Exception {

    public UnableToProcessJsonException(Exception e) {
        super("Unable to process the Json entity", e);
    }
}
