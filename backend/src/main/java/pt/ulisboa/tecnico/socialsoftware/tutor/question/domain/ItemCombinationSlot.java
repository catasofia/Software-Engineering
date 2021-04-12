package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationSlotDto;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.Column;

@Entity
@Table(name = "item_combination_slot")
public class ItemCombinationSlot implements DomainEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    private static Set<ItemCombinationSlotDto> correctCombinations = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer internId;

    @Column(columnDefinition = "CHAR")
    private char itemsColumn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_details_id")
    private ItemCombinationQuestion questionDetails;

    public ItemCombinationSlot(){}

    public ItemCombinationSlot(ItemCombinationSlotDto itemCombinationSlotDto) {
        this.correctCombinations = itemCombinationSlotDto.getCorrectCombination();
        this.content = itemCombinationSlotDto.getContent();
        this.internId = itemCombinationSlotDto.getInternId();
        this.itemsColumn = itemCombinationSlotDto.getColumn();
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

    public void setColumn(char itemsColumn) {
        this.itemsColumn = itemsColumn;
    }

    public char getColumn() {
        return itemsColumn;
    }

    public Integer getInternId() {
        return internId;
    }

    public Set<ItemCombinationSlotDto> getCorrectCombinations() {
        return correctCombinations;
    }

    public String getCorrectCombinationsContent() {
        String content = "";

        for(ItemCombinationSlotDto item: correctCombinations){
            content += item.getContent() + " | ";
        }
        return content;
    }

    public void setCorrectCombinations(Set<ItemCombinationSlotDto> correctCombinations) {
        this.correctCombinations = correctCombinations;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ItemCombinationQuestion getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(ItemCombinationQuestion questionDetails) {
        this.questionDetails = questionDetails;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitItemCombinationSlot(this);
    }

    public void delete() {
        this.questionDetails = null;
    }

}
