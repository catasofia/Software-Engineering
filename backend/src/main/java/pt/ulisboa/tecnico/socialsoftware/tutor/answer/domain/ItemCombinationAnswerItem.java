package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.ItemCombinationSlotStatementAnswerDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.ItemCombinationStatementAnswerDetailsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.QuestionDetails;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue(Question.QuestionTypes.ITEM_COMBINATION_QUESTION)
public class ItemCombinationAnswerItem extends QuestionAnswerItem {

    @ElementCollection
    private List<Integer> itemIds;

    public ItemCombinationAnswerItem(){}

    public ItemCombinationAnswerItem(String username, int quizId, StatementAnswerDto answer, ItemCombinationStatementAnswerDetailsDto detailsDto){
        super(username, quizId, answer);
        this.itemIds = detailsDto.getAnsweredSlots()
                .stream()
                .map(ItemCombinationSlotStatementAnswerDto::getId)
                .collect(Collectors.toList());
    }

    @Override
    public String getAnswerRepresentation(QuestionDetails questionDetails) {
        return questionDetails.getAnswerRepresentation(itemIds);
    }
}
