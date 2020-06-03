package yapily.marvel.service;

import yapily.marvel.exceptions.YandexApiKeyNotFoundException;

public interface TranslateService {

    /**
     * Translates a text from one language to another using the Yandex API. More info here: https://cloud.yandex.com/services/translate
     *
     * @param text             - the text to translate
     * @param fromLanguageCode - A 2 character language code to translate the description from. The list of codes can be found here:
     *                         https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
     * @param toLanguageCode   - A 2 character language code to translate the description to. The list of codes can be found here:
     *                         https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
     * @return - the translated text
     * @throws YandexApiKeyNotFoundException - thrown if the Yandex API key cannot be located
     */
    String translate(String text, String fromLanguageCode, String toLanguageCode) throws YandexApiKeyNotFoundException;
}
