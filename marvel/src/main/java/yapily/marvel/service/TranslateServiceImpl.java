package yapily.marvel.service;

import com.github.vbauer.yta.model.Direction;
import com.github.vbauer.yta.model.Language;
import com.github.vbauer.yta.service.YTranslateApiImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import yapily.marvel.exceptions.YandexApiKeyNotFoundException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Service
public class TranslateServiceImpl implements TranslateService {
    @Value("${api.keys.location}")
    private String apiKeysLocation;

    @Value("${yandex.key}")
    private String yandexKey;

    @Override
    public String translate(String text, String fromLanguageCode, String toLanguageCode) throws YandexApiKeyNotFoundException {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(apiKeysLocation));
        } catch (IOException e) {
            throw new YandexApiKeyNotFoundException(apiKeysLocation, e);
        }

        YTranslateApiImpl yTranslateApi = new YTranslateApiImpl(properties.get(yandexKey).toString());

        return yTranslateApi.translationApi().translate(text, new Direction() {
            @Override
            public Optional<Language> source() {
                return Optional.of(Language.of(fromLanguageCode));
            }

            @Override
            public Language target() {
                return Language.of(toLanguageCode);
            }
        }).text();
    }
}
