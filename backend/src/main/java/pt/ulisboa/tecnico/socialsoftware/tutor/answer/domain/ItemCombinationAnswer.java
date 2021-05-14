package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationSlot;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.QUESTION_COMBINATION_SLOT_MISMATCH;

@Entity
@DiscriminatorValue(Question.QuestionTypes.ITEM_COMBINATION_QUESTION)
public class ItemCombinationAnswer extends AnswerDetails {

    @ManyToMany(mappedBy = "questionAnswers")
    private Set<ItemCombinationSlot> itemCombinationSlots = new HashSet<>();

    public ItemCombinationAnswer() {
        super();
    }

    public ItemCombinationAnswer(QuestionAnswer questionAnswer) {
        super(questionAnswer);
    }
    
    public Set<ItemCombinationSlot> getItemCombinationSlots() {
        return itemCombinationSlots;
    }

    public void setItemCombinationSlots(Set<ItemCombinationSlot> itemCombinationSlots) {
        this.itemCombinationSlots = itemCombinationSlots;
    }

    @Override
    public boolean isCorrect() {
        return this.getItemCombinationSlots().stream().allMatch(ItemCombinationSlot::isCorrect);
    }

    @Override
    public AnswerDetailsDto getAnswerDetailsDto() {
        return new ItemCombinationAnswerDto(this);
    }

    @Override
    public StatementAnswerDetailsDto getStatementAnswerDetailsDto() {
        return new ItemCombinationStatementAnswerDetailsDto(this);
    }

    @Override
    public boolean isAnswered() {
        return itemCombinationSlots != null && !itemCombinationSlots.isEmpty();
    }

    @Override
    public String getAnswerRepresentation() {
        String answer = "";

        for(ItemCombinationSlot item: itemCombinationSlots){
            for (ItemCombinationSlot combination: item.getCorrectCombinations()){
                answer = answer + item.getContent() + " combines with " + combination.getContent();
            }
            answer = answer + " \n ";
        }
        return answer;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitAnswerDetails(this);
    }

    @Override
    public void remove() {
        if (itemCombinationSlots != null){
            itemCombinationSlots.forEach(itemCombinationSlot -> itemCombinationSlot.getQuestionAnswers().remove(this));
            itemCombinationSlots.clear();
        }
    }

    public void setItemCombinationSlots(ItemCombinationQuestion question, ItemCombinationStatementAnswerDetailsDto itemCombinationStatementAnswerDetailsDto) {
        ItemCombinationSlot itemCombinationSlot = null;
        itemCombinationSlots.clear();
        if (!itemCombinationStatementAnswerDetailsDto.emptyAnswer()) {
            for (ItemCombinationSlotStatementAnswerDto slot : itemCombinationStatementAnswerDetailsDto.getAnsweredSlots()) {

                    if(slot.getId() == null){
                        continue;
                    }
                    
                    itemCombinationSlot = question.getItems()
                            .stream()
                            .filter(slot1 -> slot1.getId().equals(slot.getId()))
                            .findAny()
                            .orElseThrow(() -> new TutorException(QUESTION_COMBINATION_SLOT_MISMATCH, slot.getId()));
                    
                
                    getItemCombinationSlots().add(itemCombinationSlot);
                    itemCombinationSlot.getQuestionAnswers().add(this);
                
            }
        }
    }
}
