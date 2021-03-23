package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenAnswerQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;

public class OpenAnswerQuestionDto extends QuestionDetailsDto {
    private String suggestion;

    public OpenAnswerQuestionDto() { }

    public OpenAnswerQuestionDto(OpenAnswerQuestion question) {
        setSuggestion(question.getSuggestion());
    }

    @Override
    public QuestionDetails getQuestionDetails(Question question) {
        return new OpenAnswerQuestion(question, this);
    }

    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    public String getSuggestion() { return this.suggestion; }

}