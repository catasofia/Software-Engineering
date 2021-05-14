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
    private final Set<ItemCombinationSlot> items = new HashSet<>();

    public ItemCombinationQuestion(){ super(); }

    public ItemCombinationQuestion(Question question, ItemCombinationQuestionDto itemCombinationQuestionDto){
        super(question);
        setItemCombinationSlots(itemCombinationQuestionDto.getColumnOne(), itemCombinationQuestionDto.getColumnTwo());
    }

    public Set<ItemCombinationSlot> getItems(){ return items; }

    public Set<ItemCombinationSlot> getColumnTwo(){
        Set<ItemCombinationSlot> colTwo = new HashSet<ItemCombinationSlot>();

        for(ItemCombinationSlot item: items){
            if (item.getColumn() == 'b'){
                colTwo.add(item);
            }
        }
        return colTwo;
    }

    public Set<ItemCombinationSlot> getColumnOne(){
        Set<ItemCombinationSlot> colOne = new HashSet<ItemCombinationSlot>();

        for(ItemCombinationSlot item: items){
            if (item.getColumn() == 'a'){
                colOne.add(item);
            }
        }
        return colOne;
    }

    public void setItemCombinationSlots(Set<ItemCombinationSlotDto> columnOne, Set<ItemCombinationSlotDto> columnTwo){
        if(columnOne.isEmpty() || columnTwo.isEmpty()){
            throw new TutorException(AT_LEAST_ONE_SLOT_NEEDED_FOR_EACH_COLUMN);
        }

        boolean allEmpty = true;
        boolean sameSet = true;
        for(ItemCombinationSlotDto item: columnOne){
            if (!(item.getCorrectCombinations().isEmpty())){
                allEmpty = false;
            }
            for (ItemCombinationSlotDto combination: item.getCorrectCombinations()){
                for(ItemCombinationSlotDto item2: columnOne){
                    if(combination.getInternId() == item2.getInternId()){
                        sameSet = false;
                    }
                }
            }
        }

        for(ItemCombinationSlotDto item: columnTwo){
            if (!(item.getCorrectCombinations().isEmpty())){
                allEmpty = false;
            }
            for (ItemCombinationSlotDto combination: item.getCorrectCombinations()){
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

        Map<String, Set<ItemCombinationSlotDto>> combinations= new HashMap<String, Set<ItemCombinationSlotDto>>();

        for(var itemCombinationSlotDto : columnTwo) {
            itemCombinationSlotDto.setColumn('b');
            itemCombinationSlotDto.setId(null);
            if (itemCombinationSlotDto.getId() == null) {
                ItemCombinationSlot itemCombinationSlot = new ItemCombinationSlot(itemCombinationSlotDto);
                itemCombinationSlot.setQuestionDetails(this);
                this.items.add(itemCombinationSlot);
            }
            else {
                ItemCombinationSlot itemCombinationSlot = getItems()
                        .stream()
                        .filter(op -> op.getId().equals(itemCombinationSlotDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new TutorException(ITEM_COMBINATION_SLOT_NOT_FOUND, itemCombinationSlotDto.getId()));

                itemCombinationSlot.setContent(itemCombinationSlotDto.getContent());
            }
        }


        for(var itemCombinationSlotDto : columnOne){
            itemCombinationSlotDto.setColumn('a');
            itemCombinationSlotDto.setId(null);
            combinations.put(itemCombinationSlotDto.getContent(), itemCombinationSlotDto.getCorrectCombinations());
            if(itemCombinationSlotDto.getId() == null){
                ItemCombinationSlot itemCombinationSlot = new ItemCombinationSlot(itemCombinationSlotDto);
                itemCombinationSlot.setQuestionDetails(this);
                this.items.add(itemCombinationSlot);
            }
            else{
                ItemCombinationSlot itemCombinationSlot = getItems()
                        .stream()
                        .filter(op -> op.getId().equals(itemCombinationSlotDto.getId()))
                        .findFirst()
                        .orElseThrow(() -> new TutorException(ITEM_COMBINATION_SLOT_NOT_FOUND, itemCombinationSlotDto.getId()));

                itemCombinationSlot.setContent(itemCombinationSlotDto.getContent());
            }
        }
        for(String content: combinations.keySet()){
            for(var comb: combinations.get(content)){
                ItemCombinationSlot aux = getItem(comb.getContent());
                getItem(content).setCorrectCombinations(aux);
            }
        }
    }

    public void update(ItemCombinationQuestionDto questionDetails) {
        for (var item : this.items) {
            item.delete();
        }
        this.items.clear();

        setItemCombinationSlots(questionDetails.getColumnOne(), questionDetails.getColumnTwo());
    }

    public ItemCombinationSlot getItem(String content){
        ItemCombinationSlot item_aux = new ItemCombinationSlot();
        for(ItemCombinationSlot item: items){
            if(item.getContent().equals(content)){
                return item_aux = item;
            }
        }
        return item_aux;
    }

    @Override
    public CorrectAnswerDetailsDto getCorrectAnswerDetailsDto() {
        return new ItemCombinationCorrectAnswerDto(this);
    }

    @Override
    public StatementQuestionDetailsDto getStatementQuestionDetailsDto() {
        return new ItemCombinationStatementQuestionDetailsDto(this);
    }

    @Override
    public StatementAnswerDetailsDto getEmptyStatementAnswerDetailsDto() {
        return new ItemCombinationStatementAnswerDetailsDto();
    }

    @Override
    public AnswerDetailsDto getEmptyAnswerDetailsDto() {
        return new ItemCombinationAnswerDto();
    }

    @Override
    public QuestionDetailsDto getQuestionDetailsDto() {
        return new ItemCombinationQuestionDto(this);
    }

    @Override
    public void delete() {
        super.delete();
        for (var item : this.items) {
            item.delete();
        }
        this.items.clear();
    }

    @Override
    public String getCorrectAnswerRepresentation() {
        String correctItems = "";

        for(ItemCombinationSlot item: items){
            for (ItemCombinationSlot combination: item.getCorrectCombinations()){
                correctItems = correctItems + item.getContent() + " combines with " + combination.getContent();
            }
            correctItems = correctItems + " \n ";
        }
        return correctItems;
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
        for (var slot: this.getItems()) {
            slot.accept(visitor);
        }
    }

    @Override
    public String getAnswerRepresentation(List<Integer> selectedIds) {
        return null;
    }


}

