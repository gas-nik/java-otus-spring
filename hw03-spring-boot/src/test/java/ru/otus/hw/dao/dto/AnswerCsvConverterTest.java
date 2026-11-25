package ru.otus.hw.dao.dto;

import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswerCsvConverterTest {

    private final AnswerCsvConverter converter = new AnswerCsvConverter();

    @Test
    void convertToReadShouldReturnAnswerWithFlagFromValue() {
        Answer answer = (Answer) converter.convertToRead("Science doesn't know this yet%true");

        assertThat(answer.text()).isEqualTo("Science doesn't know this yet");
        assertThat(answer.isCorrect()).isTrue();
    }

    @Test
    void convertToReadShouldReturnAnswerWithFalseFlag() {
        Answer answer = (Answer) converter.convertToRead("Absolutely not%false");

        assertThat(answer.text()).isEqualTo("Absolutely not");
        assertThat(answer.isCorrect()).isFalse();
    }

    @Test
    void convertToReadShouldThrowWhenCorrectnessFlagIsInvalid() {
        assertThatThrownBy(() -> converter.convertToRead("Some answer%maybe"))
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("maybe");
    }

    @Test
    void convertToReadShouldThrowWhenFormatIsInvalid() {
        assertThatThrownBy(() -> converter.convertToRead("Answer without flag"))
                .isInstanceOf(QuestionReadException.class);
    }
}
