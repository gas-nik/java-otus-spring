package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TestServiceImplTest {

    private LocalizedIOService ioService;

    private QuestionDao questionDao;

    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        ioService = mock(LocalizedIOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void executeTestForShouldFillResultWithSelectedAnswers() {
        var question1 = new Question("What is 2+2?", List.of(
                new Answer("4", true),
                new Answer("5", false)));
        var question2 = new Question("What is the capital of France?", List.of(
                new Answer("Paris", true),
                new Answer("Berlin", false)));
        when(questionDao.findAll()).thenReturn(List.of(question1, question2));
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(2, 1);

        TestResult testResult = testService.executeTestFor(new Student("Ivan", "Ivanov"));

        assertThat(testResult.getStudent()).isEqualTo(new Student("Ivan", "Ivanov"));
        assertThat(testResult.getAnsweredQuestions()).containsExactly(question1, question2);
        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
        verify(ioService, times(2)).printLine("");
        verify(ioService).printLineLocalized("TestService.answer.the.questions");
        verify(ioService).printFormattedLine("%d. %s", 1, "What is 2+2?");
        verify(ioService).printFormattedLine("%d) %s", 1, "4");
        verify(ioService).printFormattedLine("%d) %s", 2, "5");
        verify(ioService).printFormattedLine("%d. %s", 2, "What is the capital of France?");
        verify(ioService).printFormattedLine("%d) %s", 1, "Paris");
        verify(ioService).printFormattedLine("%d) %s", 2, "Berlin");
        verify(ioService, times(2)).readIntForRangeWithPromptLocalized(1, 2,
                "TestService.input.answer.number", "TestService.error.invalid.answer.number");
    }

    @Test
    void executeTestForShouldCreateEmptyResultWhenNoQuestions() {
        when(questionDao.findAll()).thenReturn(List.of());

        TestResult testResult = testService.executeTestFor(new Student("Ivan", "Ivanov"));

        assertThat(testResult.getAnsweredQuestions()).isEmpty();
        assertThat(testResult.getRightAnswersCount()).isZero();
    }
}
