package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationQuestion;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCombinationStatementQuestionDetailsDto extends StatementQuestionDetailsDto  {

    private List<StatementCombinationSlotDto> combinations;

    public ItemCombinationStatementQuestionDetailsDto() {}

    public ItemCombinationStatementQuestionDetailsDto(ItemCombinationQuestion question) {
        this.combinations = question.getItems()
                .stream()
                .map(StatementCombinationSlotDto::new)
                .collect(Collectors.toList());
    }

    public List<StatementCombinationSlotDto> getItems() {
        return combinations;
    }

    public void setItemCombinationSlots(List<StatementCombinationSlotDto> combinations) {
        this.combinations = combinations;
    }

    @Override
    public String toString() {
        return "ItemCombinationStatementAnswerDetailsDto{" +
                "combinations=" + combinations +
                '}';
    }
}
