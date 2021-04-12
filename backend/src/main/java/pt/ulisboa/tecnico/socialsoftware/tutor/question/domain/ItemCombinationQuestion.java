package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.Updator;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationQuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationSlotDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDetailsDto;

import javax.persistence.*;
import java.util.List;
import java.util.*;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;


@Entity
@DiscriminatorValue(Question.QuestionTypes.ITEM_COMBINATION_QUESTION)
public class ItemCombinationQuestion extends QuestionDetails{

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.EAGER, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private final Set<ItemCombinationSlot> listOne = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionDetails", fetch = FetchType.EAGER, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private final Set<ItemCombinationSlot> listTwo = new HashSet<>();

    public ItemCombinationQuestion(){ super(); }

    public ItemCombinationQuestion(Question question, ItemCombinationQuestionDto itemCombinationQuestionDto){
        super(question);
        setItemCombinationSlots(itemCombinationQuestionDto.getColumnOne(), itemCombinationQuestionDto.getColumnTwo());
    }

    public Set<ItemCombinationSlot> getColumnOne(){ return listOne; }

    public Set<ItemCombinationSlot> getColumnTwo(){ return listTwo; }

    public void setItemCombinationSlots(Set<ItemCombinationSlotDto> columnOne, Set<ItemCombinationSlotDto> columnTwo){
        if(columnOne.isEmpty() || columnTwo.isEmpty()){
            throw new TutorException(AT_LEAST_ONE_SLOT_NEEDED_FOR_EACH_COLUMN);
        }

        boolean allEmpty = true;
        boolean sameSet = true;
        for(ItemCombinationSlotDto item: columnOne){
            if (!(item.getCorrectCombination().isEmpty())){
                allEmpty = false;
            }
            for (ItemCombinationSlotDto combination: item.getCorrectCombination()){
                for(ItemCombinationSlotDto item2: columnOne){
                    if(combination.getInternId() == item2.getInternId()){
                        sameSet = false;
                    }
                }
            }
        }

        for(ItemCombinationSlotDto item: columnTwo){
            if (!(item.getCorrectCombination().isEmpty())){
                allEmpty = false;
            }
            for (ItemCombinationSlotDto combination: item.getCorrectCombination()){
                for(ItemCombinationSlotDto item2: columnTwo){
                    if(combination.getInternId() == item2.getInternId()){
                        sameSet = false;
                    }
                }
            }
        }

        if(allEmpty){
            throw new TutorException(AT_LEAST_ONE_ITEM_COMBINATION_NEEDED);
        }

        if(!sameSet){
            throw new TutorException(COMBINATION_IN_SAME_SET);
        }

        for(var itemCombinationSlotDto : columnOne){
            itemCombinationSlotDto.setColumn('a');
            if(itemCombinationSlotDto.getId() == null){
                ItemCombinationSlot itemCombinationSlot = new ItemCombinationSlot(itemCombinationSlotDto);
                itemCombinationSlot.setQuestionDetails(this);
                this.listOne.add(itemCombinationSlot);

            }
            else{
                ItemCombinationSlot itemCombinationSlot = getColumnOne()
                        .stream()
                        .filter(op -> op.getId().equals(itemCombinationSlotDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new TutorException(ITEM_COMBINATION_SLOT_NOT_FOUND, itemCombinationSlotDto.getId()));

                itemCombinationSlot.setContent(itemCombinationSlotDto.getContent());
                itemCombinationSlot.setCorrectCombinations(itemCombinationSlotDto.getCorrectCombination());
            }

        }


        for(var itemCombinationSlotDto : columnTwo) {
            itemCombinationSlotDto.setColumn('b');
            if (itemCombinationSlotDto.getId() == null) {
                ItemCombinationSlot itemCombinationSlot = new ItemCombinationSlot(itemCombinationSlotDto);
                itemCombinationSlot.setQuestionDetails(this);
                this.listTwo.add(itemCombinationSlot);
            }
            else {
                ItemCombinationSlot itemCombinationSlot = getColumnTwo()
                        .stream()
                        .filter(op -> op.getId().equals(itemCombinationSlotDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new TutorException(ITEM_COMBINATION_SLOT_NOT_FOUND, itemCombinationSlotDto.getId()));

                itemCombinationSlot.setContent(itemCombinationSlotDto.getContent());
                itemCombinationSlot.setCorrectCombinations(itemCombinationSlotDto.getCorrectCombination());
            }
        }
    }

    public void update(ItemCombinationQuestionDto questionDetails) {
        for (var item : this.listOne) {
            item.delete();
        }
        for (var item : this.listTwo) {
            item.delete();
        }
        this.listOne.clear();
        this.listTwo.clear();

        setItemCombinationSlots(questionDetails.getColumnOne(), questionDetails.getColumnTwo());
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
        super.delete();
        for (var item : this.listOne) {
            item.delete();
        }
        for (var item : this.listTwo) {
            item.delete();
        }
        this.listOne.clear();
        this.listTwo.clear();
    }

    @Override
    public String getCorrectAnswerRepresentation() {
        String correctOptions = "";

        for(ItemCombinationSlot item: listOne){
            for (ItemCombinationSlotDto combination: item.getCorrectCombinations()){
                correctOptions = correctOptions + item.getInternId().toString() + " combines with " + combination.toString();
            }
            correctOptions = correctOptions + " \n ";
        }
        System.out.println(correctOptions);
        return correctOptions;
    }

    @Override
    public void update(Updator updator) {
        updator.update(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitQuestionDetails(this);
    }

    public void visitItemCombinationSlot(Visitor visitor) {
        for (var slot: this.getColumnOne()) {
            slot.accept(visitor);
        }
        for (var slot: this.getColumnTwo()) {
            slot.accept(visitor);
        }
    }

    @Override
    public String getAnswerRepresentation(List<Integer> selectedIds) {
        return null;
    }
}






















