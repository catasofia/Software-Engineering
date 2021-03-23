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

    @ElementCollection
    @CollectionTable(name = "correctCombinations", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "item_combination_question_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Integer> correctCombinations = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_combination_id")
    private ItemCombinationQuestion questionDetails;

    public ItemCombinationSlot(){}

    public ItemCombinationSlot(ItemCombinationSlotDto itemCombinationSlotDto) {
        this.correctCombinations = itemCombinationSlotDto.getCorrectCombination();
        this.content = itemCombinationSlotDto.getContent();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Integer> getCorrectCombinations() {
        return correctCombinations;
    }

    public void setCorrectCombinations(Set<Integer> correctCombinations) {
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

   // TODO public void delete(){}

}