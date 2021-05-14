package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationSlot;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class StatementCombinationSlotDto implements Serializable {
    private List<StatementCombinationSlotDto> combinations;

    public StatementCombinationSlotDto(ItemCombinationSlot itemCombinationSlot) {
        this.combinations = itemCombinationSlot.getCorrectCombinations()
                .stream()
                .map(StatementCombinationSlotDto::new)
                .collect(Collectors.toList());
    }

    public List<StatementCombinationSlotDto> getCombinations() {
        return combinations;
    }

    public void setCombinations(List<StatementCombinationSlotDto> combinations) {
        this.combinations = combinations;
    }

    @Override
    public String toString() {
        return "StatementCombinationSlotDto{" +
                ", options=" + getCombinations() +
                '}';
    }
}