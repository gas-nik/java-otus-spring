package ru.otus.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.Locale;
import java.util.Map;

@Component
@Setter
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {

    @Getter
    private int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    private Map<String, String> fileNameByLocaleTag;

    public void setLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }

    @Override
    public String getTestFileName() {
        String languageTag = locale.toLanguageTag();
        String fileName = fileNameByLocaleTag.get(languageTag);
        if (fileName == null) {
            throw new QuestionReadException(
                    "No questions file configured for locale: " + languageTag);
        }
        return fileName;
    }
}
