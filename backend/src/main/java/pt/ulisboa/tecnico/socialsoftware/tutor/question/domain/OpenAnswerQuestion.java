package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.AnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.CorrectAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementQuestionDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.Updator;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenAnswerQuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDetailsDto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue(Question.QuestionTypes.OPEN_ANSWER_QUESTION)
public class OpenAnswerQuestion extends QuestionDetails {
    private String suggestion; //Without collumDefinition and Type = "TEXT" this is limited to 255 chars

    public OpenAnswerQuestion(){ super(); }

    public OpenAnswerQuestion(Question question, OpenAnswerQuestionDto openAnswerQuestionDto) {
        super(question);
        setSuggestion(openAnswerQuestionDto.getSuggestion());
    }

    public void setSuggestion(String suggestion) {
        if(suggestion == null || suggestion.isEmpty())
            throw new TutorException(ErrorMessage.INVALID_CONTENT_FOR_SUGGESTION);

        this.suggestion = suggestion;
    }
    public String getSuggestion() {
        return this.suggestion;
    }

    @Override
    public void delete() {
        super.delete();
        this.suggestion = null;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitQuestionDetails(this);
    }

    @Override
    public CorrectAnswerDetailsDto getCorrectAnswerDetailsDto() {
        return null;
    }

    @Override
    public StatementQuestionDetailsDto getStatementQuestionDetailsDto() {
        return null;
    }

    @Override
    public StatementAnswerDetailsDto getEmptyStatementAnswerDetailsDto() {
        return null;
    }

    @Override
    public AnswerDetailsDto getEmptyAnswerDetailsDto() {
        return null;
    }

    @Override
    public QuestionDetailsDto getQuestionDetailsDto() {
        return new OpenAnswerQuestionDto(this);
    }

    public void update(OpenAnswerQuestionDto questionDetails) {
        setSuggestion(questionDetails.getSuggestion());
    }

    @Override
    public void update(Updator updator) {
        updator.update(this);
    }

    @Override
    public String getCorrectAnswerRepresentation() {
        return null;
    } // shold return sugestion;

    @Override
    public String getAnswerRepresentation(List<Integer> selectedIds) {
        return null;
    }
}
