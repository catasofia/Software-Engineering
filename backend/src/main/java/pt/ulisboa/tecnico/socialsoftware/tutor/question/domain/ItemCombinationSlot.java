package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.ItemCombinationAnswer;
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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ItemCombinationSlot> correctCombinations = new HashSet<ItemCombinationSlot>();

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer internId;

    @Column(columnDefinition = "CHAR")
    private char itemsColumn;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean correct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_details_id")
    private ItemCombinationQuestion questionDetails;

    @ManyToMany()
    private final Set<ItemCombinationAnswer> questionAnswers = new HashSet<>();

    public ItemCombinationSlot(){}

    public ItemCombinationSlot(ItemCombinationSlotDto itemCombinationSlotDto) {
        this.content = itemCombinationSlotDto.getContent();
        this.internId = itemCombinationSlotDto.getInternId();
        this.itemsColumn = itemCombinationSlotDto.getColumn();
        this.correct = true;
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

    public Set<ItemCombinationSlot> getCorrectCombinations() {
        return correctCombinations;
    }

    public String getCorrectCombinationsContent() {
        String content = "";

        for(ItemCombinationSlot item: correctCombinations){
            content += item.getContent() + "!";
        }
        return content;
    }

    public void setCorrectCombinations(ItemCombinationSlot comb) {
        this.correctCombinations.add(comb);
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

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setQuestionDetails(ItemCombinationQuestion questionDetails) {
        this.questionDetails = questionDetails;
    }

    public Set<ItemCombinationAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitItemCombinationSlot(this);
    }

    public void delete() {
        this.questionDetails = null;
    }

}