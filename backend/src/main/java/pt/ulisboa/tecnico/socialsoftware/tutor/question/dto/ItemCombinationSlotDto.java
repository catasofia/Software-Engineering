package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationSlot;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

public class ItemCombinationSlotDto implements Serializable{
    private Integer id;
    private String content;
    private Integer sequence;
    private List<Integer> correctCombinations = new ArrayList<Integer>();

    public ItemCombinationSlotDto(){}

    public ItemCombinationSlotDto(ItemCombinationSlot itemCombinationSlot){
        this.id = itemCombinationSlot.getId();
        this.content = itemCombinationSlot.getContent();
        this.sequence = itemCombinationSlot.getSequence();
        this.correctCombinations = itemCombinationSlot.getCorrectCombinations();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Integer> getCorrectCombination() {
        return correctCombinations;
    }

    public void setCorrectCombination(List<Integer> correctCombinations) {
        this.correctCombinations = correctCombinations;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "ItemCombinationSlotDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", combines_with=" + correctCombinations +
                ", sequence=" + sequence +
                '}';
    }
}
