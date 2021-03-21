package pt.ulisboa.tecnico.socialsoftware.tutor.question.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCombinationQuestionDto extends QuestionDetailsDto{
    private List<ItemCombinationSlotDto> listOne = new ArrayList<>();
    private List<ItemCombinationSlotDto> listTwo = new ArrayList<>();

    public ItemCombinationQuestionDto(){}

    public ItemCombinationQuestionDto(ItemCombinationQuestion question){
        this.listOne = question.getColumnOne().stream().map(ItemCombinationSlotDto::new).collect(Collectors.toList());
        this.listTwo = question.getColumnTwo().stream().map(ItemCombinationSlotDto::new).collect(Collectors.toList());
    }

    public List<ItemCombinationSlotDto> getColumnOne(){ return listOne;}

    public List<ItemCombinationSlotDto> getColumnTwo(){ return listTwo;}

    public void setColumnOne(List<ItemCombinationSlotDto> columnOne){ this.listOne = columnOne;}

    public void setColumnTwo(List<ItemCombinationSlotDto> columnTwo){ this.listOne = columnTwo;}

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
    public void update(CodeOrderQuestion question) {
    //TODO
    }
}
