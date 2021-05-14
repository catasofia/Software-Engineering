package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationQuestion;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCombinationStatementAnswerDetailsDto extends StatementAnswerDetailsDto{
    private List<ItemCombinationSlotStatementAnswerDto> answeredSlots = new ArrayList<>();

    public ItemCombinationStatementAnswerDetailsDto() {}

    public ItemCombinationStatementAnswerDetailsDto(ItemCombinationAnswer questionAnswer) {
        if (questionAnswer.getItemCombinationSlots() != null) {
            this.answeredSlots = questionAnswer.getItemCombinationSlots()
                    .stream()
                    .map(ItemCombinationSlotStatementAnswerDto::new)
                    .collect(Collectors.toList());
        }
    }

    public List<ItemCombinationSlotStatementAnswerDto> getAnsweredSlots() {
        return answeredSlots;
    }

    public void setAnsweredSlots(List<ItemCombinationSlotStatementAnswerDto> answeredSlots) {
        this.answeredSlots = answeredSlots;
    }

    @Override
    public String toString() {
        return "ItemCombinationStatementAnswerDetailsDto{" +
                "answer=" + answeredSlots +
                '}';
    }

    @Transient
    private ItemCombinationAnswer itemCombinationAnswer;

    @Override
    public AnswerDetails getAnswerDetails(QuestionAnswer questionAnswer) {
        itemCombinationAnswer = new ItemCombinationAnswer(questionAnswer);
        questionAnswer.getQuestion().getQuestionDetails().update(this);
        return itemCombinationAnswer;
    }

    @Override
    public void update(ItemCombinationQuestion question) {
        itemCombinationAnswer.setItemCombinationSlots(question, this);
    }

    @Override
    public boolean emptyAnswer() {
        return answeredSlots == null || answeredSlots.isEmpty();
    }

    @Override
    public QuestionAnswerItem getQuestionAnswerItem(String username, int quizId, StatementAnswerDto statementAnswerDto) {
        return new ItemCombinationAnswerItem(username, quizId, statementAnswerDto, this);
    }

}
