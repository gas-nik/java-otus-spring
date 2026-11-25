package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        List<Question> questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            ioService.printFormattedLine("%d. %s", i + 1, question.text());
            var answers = question.answers();
            for (int j = 0; j < answers.size(); j++) {
                ioService.printFormattedLine("%d) %s", j + 1, answers.get(j).text());
            }
            int answerNumber = ioService.readIntForRangeWithPromptLocalized(1, answers.size(),
                    "TestService.input.answer.number", "TestService.error.invalid.answer.number");
            testResult.applyAnswer(question, answers.get(answerNumber - 1).isCorrect());
        }
        return testResult;
    }

}
