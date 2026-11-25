package ru.otus.hw.config;

import org.junit.jupiter.api.Test;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppPropertiesTest {

    @Test
    void getTestFileNameShouldReturnFileForConfiguredLocale() {
        AppProperties properties = new AppProperties();
        properties.setLocale("ru-RU");
        properties.setFileNameByLocaleTag(Map.of(
                "ru-RU", "questions_ru.csv",
                "en-US", "questions.csv"));

        assertThat(properties.getTestFileName()).isEqualTo("questions_ru.csv");
    }

    @Test
    void getTestFileNameShouldThrowWhenLocaleIsNotConfigured() {
        AppProperties properties = new AppProperties();
        properties.setLocale("fr-FR");
        properties.setFileNameByLocaleTag(Map.of(
                "ru-RU", "questions_ru.csv",
                "en-US", "questions.csv"));

        assertThatThrownBy(properties::getTestFileName)
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("fr-FR");
    }
}
