package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.AnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OpenAnswerAnswerDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OpenAnswerStatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(Question.QuestionTypes.OPEN_ANSWER_QUESTION)
public class OpenAnswerAnswer extends AnswerDetails {
    private String answer;

    public OpenAnswerAnswer() {
        super();
    }

    public OpenAnswerAnswer(QuestionAnswer questionAnswer){
        super(questionAnswer);
    }

    public OpenAnswerAnswer(QuestionAnswer questionAnswer, String answer){
        super(questionAnswer);
        this.setAnswer(answer);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean isCorrect() {
        return answer != null && getQuestionAnswer().getQuizQuestion().getQuestion().getQuestionDetails().getCorrectAnswerRepresentation().equals(answer);
    }

    @Override
    public void remove() {
        answer = null;
    }

    @Override
    public AnswerDetailsDto getAnswerDetailsDto() {
        return new OpenAnswerAnswerDto(this);
    }

    @Override
    public String getAnswerRepresentation() {
        return getAnswer();
    }

    @Override
    public StatementAnswerDetailsDto getStatementAnswerDetailsDto() {
        return new OpenAnswerStatementAnswerDetailsDto(this);
    }

    @Override
    public boolean isAnswered() {
        return getAnswer() != null;
    }

    @Override
    public void accept(Visitor visitor) { visitor.visitAnswerDetails(this); }
}
