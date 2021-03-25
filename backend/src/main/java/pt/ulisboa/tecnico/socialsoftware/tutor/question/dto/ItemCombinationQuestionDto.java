package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class ItemCombinationQuestionDto extends QuestionDetailsDto{
    private Set<ItemCombinationSlotDto> listOne = new HashSet<>();
    private Set<ItemCombinationSlotDto> listTwo = new HashSet<>();

    public ItemCombinationQuestionDto(){}

    public ItemCombinationQuestionDto(ItemCombinationQuestion question){
        this.listOne = question.getColumnOne().stream().map(ItemCombinationSlotDto::new).collect(Collectors.toSet());
        this.listTwo = question.getColumnTwo().stream().map(ItemCombinationSlotDto::new).collect(Collectors.toSet());
    }

    public Set<ItemCombinationSlotDto> getColumnOne(){ return listOne;}

    public Set<ItemCombinationSlotDto> getColumnTwo(){ return listTwo;}

    public void setItemCombinationSlots(Set<ItemCombinationSlotDto> columnOne, Set<ItemCombinationSlotDto> columnTwo){
        this.listOne = columnOne;
        this.listTwo = columnTwo;
    }

    @Override
    public String toString() {
        return "ItemCombinationQuestionDto{" +
                "columnOne=" + listOne + '\'' +
                "columnTwo=" + listTwo + '}';
    }

    @Override
    public QuestionDetails getQuestionDetails(Question question) {
        return new ItemCombinationQuestion(question, this);
    }

    @Override
    public void update(ItemCombinationQuestion question) {
        question.update(this);
    }
}
