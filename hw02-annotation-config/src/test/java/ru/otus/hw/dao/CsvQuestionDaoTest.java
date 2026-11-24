package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvQuestionDaoTest {

    private static final String QUESTION_FILE = "questions.csv";

    @Test
    void findAllShouldReadQuestionsFromCsvResource() {
        TestFileNameProvider fileNameProvider = fileNameProvider(QUESTION_FILE);
        CsvQuestionDao questionDao = new CsvQuestionDao(fileNameProvider);

        List<Question> questions = questionDao.findAll();

        assertThat(questions).isNotEmpty();
        assertThat(questions).allSatisfy(question -> {
            assertThat(question.text()).isNotBlank();
            assertThat(question.answers()).isNotEmpty();
            assertThat(question.answers()).extracting(Answer::isCorrect).contains(true);
        });
    }

    @Test
    void findAllShouldThrowQuestionReadExceptionWhenResourceNotFound() {
        TestFileNameProvider fileNameProvider = fileNameProvider("missing.csv");
        CsvQuestionDao questionDao = new CsvQuestionDao(fileNameProvider);

        assertThatThrownBy(questionDao::findAll)
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("missing.csv");
    }

    private TestFileNameProvider fileNameProvider(String fileName) {
        TestFileNameProvider fileNameProvider = mock(TestFileNameProvider.class);
        when(fileNameProvider.getTestFileName()).thenReturn(fileName);
        return fileNameProvider;
    }
}
