package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationSlot;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;

public class ItemCombinationSlotDto implements Serializable{
    private Integer id;
    private String content;
    private Integer internId;
    private char column;

    private Set<ItemCombinationSlotDto> correctCombinations = new HashSet<>();

    public ItemCombinationSlotDto(){}

    public ItemCombinationSlotDto(ItemCombinationSlot itemCombinationSlot){
        this.id = itemCombinationSlot.getId();
        this.content = itemCombinationSlot.getContent();
        this.correctCombinations = itemCombinationSlot.getCorrectCombinations().stream().map(s -> new ItemCombinationSlotDto()).collect(Collectors.toSet());
        this.internId = itemCombinationSlot.getInternId();
        this.column = itemCombinationSlot.getColumn();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInternId(Integer internId) {
        this.internId = internId;
    }

    public Integer getInternId() {
        return internId;
    }

    public void setColumn(char column) {
        this.column = column;
    }

    public char getColumn() {
        return column;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<ItemCombinationSlotDto> getCorrectCombination() {
        return correctCombinations;
    }

    public void setCorrectCombinations(Set<ItemCombinationSlotDto> correctCombinations) {
        this.correctCombinations = correctCombinations;
    }

    public void addCombination(ItemCombinationSlotDto correctCombination) {
        this.correctCombinations.add(correctCombination);
    }


    @Override
    public String toString() {
        return "ItemCombinationSlotDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", combines_with=" + correctCombinations +
                '}';
    }
}
