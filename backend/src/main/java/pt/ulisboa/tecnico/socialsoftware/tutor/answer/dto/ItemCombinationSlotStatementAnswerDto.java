package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationSlot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemCombinationSlotStatementAnswerDto implements Serializable {
    private Integer id;
    private Integer internId;
    private List<ItemCombinationSlot> correctCombinations;

    public ItemCombinationSlotStatementAnswerDto(ItemCombinationSlot itemCombinationSlot) {
        this.id = itemCombinationSlot.getId();
        this.internId = itemCombinationSlot.getInternId();
        this.correctCombinations = new ArrayList<>(itemCombinationSlot.getCorrectCombinations());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInternId() {
        return internId;
    }

    public void setInternId(Integer internId) {
        this.internId = internId;
    }

    public List<ItemCombinationSlot> getCorrectCombinations() {
        return correctCombinations;
    }

    public void setCorrectCombinations(List<ItemCombinationSlot> correctCombinations) {
        this.correctCombinations = correctCombinations;
    }
}