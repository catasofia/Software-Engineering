package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;


import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.Updator;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.CodeOrderQuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationQuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationSlotDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDetailsDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;


@Entity
@DiscriminatorValue(Question.QuestionTypes.ITEM_COMBINATION_QUESTION)

public class ItemCombinationQuestion extends QuestionDetails{

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<ItemCombinationSlot> listOne = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<ItemCombinationSlot> listTwo = new ArrayList<>();

    public ItemCombinationQuestion(){ super(); }

    public ItemCombinationQuestion(Question question, ItemCombinationQuestionDto itemCombinationQuestionDto){
        super(question);
        //setItemCombinationSlots(itemCombinationQuestionDto.getColumnOne(), itemCombinationQuestionDto.getColumnTwo());
    }

    public List<ItemCombinationSlot> getColumnOne(){ return listOne; }

    public List<ItemCombinationSlot> getColumnTwo(){ return listTwo; }

    public void setItemCombinationSlots(List<ItemCombinationSlotDto> columnOne, List<ItemCombinationSlotDto> columnTwo){
        if(columnOne.isEmpty() || columnTwo.isEmpty()){
            throw new TutorException(AT_LEAST_ONE_SLOT_NEEDED_FOR_EACH_COLUMN);
        }

        int allEmpty = 1;
        int sameSet = 1;
        for(ItemCombinationSlotDto item: columnOne){
            //checks if there are no combinations
            if (!(item.getCorrectCombination().isEmpty())){
                allEmpty = 0;
            }
            //checks if combination is in the same set
            for (Integer combination: item.getCorrectCombination()){
                for(ItemCombinationSlotDto item2: columnOne){
                    if(combination == item2.getId()){
                        sameSet = 0;
                    }
                }
            }
        }

        for(ItemCombinationSlotDto item: columnTwo){
            //checks if there are no combinations
            if (!(item.getCorrectCombination().isEmpty())){
                allEmpty = 0;
            }
            //checks if combination is in the same set
            for (Integer combination: item.getCorrectCombination()){
                for(ItemCombinationSlotDto item2: columnTwo){
                    if(combination == item2.getId()){
                        sameSet = 0;
                    }
                }
            }
        }

        if(allEmpty == 1){
            throw new TutorException(ONE_COMBINATION_NEEDED);
        }

        if(sameSet == 0){
            throw new TutorException(COMBINATION_IN_SAME_SET);
        }

        Collections.shuffle(columnOne);
        Collections.shuffle(columnTwo);

        int sequence1 = getColumnOne().size();

        for(var itemCombinationSlotDto : columnOne){
            int newSequence = itemCombinationSlotDto.getSequence() != null ? itemCombinationSlotDto.getSequence() : sequence1++;
            if(itemCombinationSlotDto.getId() == null){
                ItemCombinationSlot itemCombinationSlot = new ItemCombinationSlot(itemCombinationSlotDto);
                itemCombinationSlot.setQuestionDetails(this);
                itemCombinationSlot.setSequence(newSequence);
                this.listOne.add(itemCombinationSlot);

            } else{
                ItemCombinationSlot itemCombinationSlot = getColumnOne()
                        .stream()
                        .filter(op -> op.getId().equals(itemCombinationSlotDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new TutorException(ORDER_SLOT_NOT_FOUND, itemCombinationSlotDto.getId()));

                        itemCombinationSlot.setContent(itemCombinationSlotDto.getContent());
                        itemCombinationSlot.setCorrectCombinations(itemCombinationSlotDto.getCorrectCombination());
                        itemCombinationSlot.setSequence(newSequence);
            }

        }

        int sequence2 = getColumnTwo().size();

        for(var itemCombinationSlotDto : columnTwo) {
            int newSequence = itemCombinationSlotDto.getSequence() != null ? itemCombinationSlotDto.getSequence() : sequence2++;
            if (itemCombinationSlotDto.getId() == null) {
                ItemCombinationSlot itemCombinationSlot = new ItemCombinationSlot(itemCombinationSlotDto);
                itemCombinationSlot.setQuestionDetails(this);
                itemCombinationSlot.setSequence(newSequence);
                this.listTwo.add(itemCombinationSlot);

            } else {
                ItemCombinationSlot itemCombinationSlot = getColumnTwo()
                        .stream()
                        .filter(op -> op.getId().equals(itemCombinationSlotDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new TutorException(ORDER_SLOT_NOT_FOUND, itemCombinationSlotDto.getId()));

                itemCombinationSlot.setContent(itemCombinationSlotDto.getContent());
                itemCombinationSlot.setCorrectCombinations(itemCombinationSlotDto.getCorrectCombination());
                itemCombinationSlot.setSequence(newSequence);
            }
        }
    }

    @Override
    public CorrectAnswerDetailsDto getCorrectAnswerDetailsDto() {
        return null;
    }

    @Override
    public StatementQuestionDetailsDto getStatementQuestionDetailsDto() {
        return null;
    }

    @Override
    public StatementAnswerDetailsDto getEmptyStatementAnswerDetailsDto() {
        return null;
    }

    @Override
    public AnswerDetailsDto getEmptyAnswerDetailsDto() {
        return null;
    }

    @Override
    public QuestionDetailsDto getQuestionDetailsDto() {
            return new ItemCombinationQuestionDto(this);
    }

    @Override
    public void delete() {
        //TODO
    }

    @Override
    public String getCorrectAnswerRepresentation() {
       String correctOptions = "";

        for(ItemCombinationSlot item: listOne){
            for (Integer combination: item.getCorrectCombinations()){
                correctOptions = correctOptions + item.getId().toString() + ", " + combination.toString();
            }
            correctOptions = correctOptions + " | ";
        }
        return correctOptions;

    }

    @Override
    public void update(Updator updator) {
        //TODO
    }

    @Override
    public void accept(Visitor visitor) {

    }

    @Override
    public String getAnswerRepresentation(List<Integer> selectedIds) {
        return null;
    }


}






















