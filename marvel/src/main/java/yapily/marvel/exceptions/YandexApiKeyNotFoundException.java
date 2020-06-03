package yapily.marvel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when the Yandex API key is not found
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class YandexApiKeyNotFoundException extends Exception {

    public YandexApiKeyNotFoundException(String location, Exception e) {
        super("The Yandex API key was not found at the location: " + location + ". Please consult the README.md for instructions", e);
    }
}
