package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationQuestion;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCombinationCorrectAnswerDto extends CorrectAnswerDetailsDto {
    private List<ItemCombinationSlotCorrectAnswerDto> correctCombinations;

    public ItemCombinationCorrectAnswerDto(ItemCombinationQuestion question) {
        this.correctCombinations = question.getItems()
                .stream()
                .map(ItemCombinationSlotCorrectAnswerDto::new)
                .collect(Collectors.toList());
    }

    public List<ItemCombinationSlotCorrectAnswerDto> getCorrectCombinations() {
        return correctCombinations;
    }

    public void setCorrectCombinations(List<ItemCombinationSlotCorrectAnswerDto> correctCombinations) {
        this.correctCombinations = correctCombinations;
    }

}
