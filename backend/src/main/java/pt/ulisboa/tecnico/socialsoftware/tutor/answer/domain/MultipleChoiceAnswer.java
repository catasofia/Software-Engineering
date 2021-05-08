package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;

import javax.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.QUESTION_OPTION_MISMATCH;

@Entity
@DiscriminatorValue(Question.QuestionTypes.MULTIPLE_CHOICE_QUESTION)
public class MultipleChoiceAnswer extends AnswerDetails {
    @ManyToMany
    @JoinColumn(name = "option_id")
    private Set<Option> options;

    public MultipleChoiceAnswer() {
        super();
    }

    public MultipleChoiceAnswer(QuestionAnswer questionAnswer){
        super(questionAnswer);
        this.options = new HashSet<>();
    }

    public MultipleChoiceAnswer(QuestionAnswer questionAnswer, Option option){
        super(questionAnswer);
        this.setOption(option);
    }

    public List<Option> getOption() {
        return options.stream().sorted(Comparator.comparingInt(Option::getSequence)).collect(Collectors.toList());
    }

    public void setOption(Option option) {
        if (this.options == null) this.options = new HashSet<>();
        this.options.add(option);

        if (option != null)
            option.addQuestionAnswer(this);
    }

    public void setOption(MultipleChoiceQuestion question, MultipleChoiceStatementAnswerDetailsDto multipleChoiceStatementAnswerDetailsDto) {
        if (multipleChoiceStatementAnswerDetailsDto.getOptionsId() != null) {
            List<Option> opt = question.getOptions().stream()
                    .filter(option1 -> multipleChoiceStatementAnswerDetailsDto.getOptionsId().contains(option1.getId()))
                    .collect(Collectors.toList());

            if (this.getOption() != null) {
                this.getOption().forEach(option1 -> option1.getQuestionAnswers().remove(this));
            }

            opt.forEach(this::setOption);
        } else {
            this.setOption(null);
        }
    }

    @Override
    public boolean isCorrect() {
        return getOption() != null && getOption().size() == getOption().stream()
                .filter(Option::isCorrect).count();
    }


    public void remove() {
        if (options != null) {
            options.forEach(option1 -> option1.getQuestionAnswers().remove(this));
            options = null;
        }
    }

    @Override
    public AnswerDetailsDto getAnswerDetailsDto() {
        return new MultipleChoiceAnswerDto(this);
    }

    @Override
    public boolean isAnswered() {
        return this.getOption() != null;
    }

    @Override
    public String getAnswerRepresentation() {
        var result = this.options.stream()
                .map(x -> MultipleChoiceQuestion.convertSequenceToLetter(x.getSequence()))
                .collect(Collectors.joining("|"));
        return !result.isEmpty() ? result : "-";
    }

    @Override
    public StatementAnswerDetailsDto getStatementAnswerDetailsDto() {
        return new MultipleChoiceStatementAnswerDetailsDto(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitAnswerDetails(this);
    }
}
