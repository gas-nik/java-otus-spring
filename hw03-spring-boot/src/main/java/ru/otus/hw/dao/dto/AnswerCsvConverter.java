package ru.otus.hw.dao.dto;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.exceptions.QuestionReadException;

public class AnswerCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String value) {
        var valueArr = value.split("%");
        if (valueArr.length != 2) {
            throw new QuestionReadException("Invalid answer format: " + value);
        }
        String answerText = valueArr[0];
        String isCorrect = valueArr[1];
        if (!"true".equalsIgnoreCase(isCorrect) && !"false".equalsIgnoreCase(isCorrect)) {
            throw new QuestionReadException("Invalid correctness flag for answer \"" + answerText + "\": " + isCorrect);
        }
        return new Answer(answerText, Boolean.parseBoolean(isCorrect));
    }
}
