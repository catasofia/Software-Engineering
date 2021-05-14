package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.ItemCombinationAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationSlotDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCombinationAnswerDto extends AnswerDetailsDto {
    private List<ItemCombinationSlotDto> answeredCombinations = new ArrayList<>();

    public ItemCombinationAnswerDto() {
    }

    public ItemCombinationAnswerDto(ItemCombinationAnswer answer) {
            if (answer.getItemCombinationSlots() != null) {
                this.answeredCombinations = answer.getItemCombinationSlots()
                        .stream()
                        .map(ItemCombinationSlotDto::new)
                        .collect(Collectors.toList());
            }
    }

    public List<ItemCombinationSlotDto> getAnsweredCombinations() {
        return answeredCombinations;
    }

    public void setAnsweredCombinations (List<ItemCombinationSlotDto> answeredCombinations) {
        this.answeredCombinations = answeredCombinations;
    }

}
