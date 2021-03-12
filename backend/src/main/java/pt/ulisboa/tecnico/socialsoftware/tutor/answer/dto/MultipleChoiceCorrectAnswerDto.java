package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion;
import java.util.List;
import java.util.ArrayList;


public class MultipleChoiceCorrectAnswerDto extends CorrectAnswerDetailsDto {
    private List<Integer> correctOptionId;

    public MultipleChoiceCorrectAnswerDto(MultipleChoiceQuestion question) {
        this.correctOptionId = question.getCorrectOptionsId();
    }

    public List<Integer> getCorrectOptionId() {
        return correctOptionId;
    }

    public void setCorrectOptionId(List<Integer> correctOptionId) {
        this.correctOptionId = correctOptionId;
    }

    @Override
    public String toString() {
        return "MultipleChoiceCorrectAnswerDto{" +
                "correctOptionId=" + correctOptionId +
                '}';
    }
}