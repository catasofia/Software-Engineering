package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*

@DataJpaTest
class UpdateItemCombinationQuestionTest extends SpockTest {

    def question
    def aItems
    def bItems
    def item1
    def item2
    def comb1

    def setup(){
        given: "a question and one item for each group with one to one relation"
        question = new Question()
        question.setCourse(externalCourse)
        question.setKey(1)
        question.setTitle(QUESTION_1_TITLE)
        question.setContent(QUESTION_1_CONTENT)
        question.setStatus(Question.Status.AVAILABLE)

        def questionDetails = new ItemCombinationQuestion()

        aItems = new HashSet<ItemCombinationSlotDto>()
        bItems = new HashSet<ItemCombinationSlotDto>()

        item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        item1.setInternId(1)
        aItems.add(item1)

        item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        item2.setInternId(2)
        bItems.add(item2)

        comb1 = new HashSet<Integer>()
        comb1.add(2)
        item1.setCorrectCombinations(comb1)

        questionDetails.setItemCombinationSlots(aItems, bItems)
        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)
    }

    def "update an Item Combination Question by adding an item in A group and it's relations" () {
        given: "a changed question"
        def questionDto = new QuestionDto(question)
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: "an item to insert in A group"
        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        item3.setInternId(3)
        aItems.add(item3)

        def comb3 = new HashSet<Integer>()
        comb3.add(2)
        item3.setCorrectCombinations(comb3)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then:"the updated question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getQuestionDetails().getColumnOne().size() == 2
        result.getQuestionDetails().getColumnTwo().size() == 1
    }

    def "update an Item Combination Question by adding an item in B group and it's relations" () {
        given: "a changed question"
        def questionDto = new QuestionDto(question)
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: "an item to insert in B group"
        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        item3.setInternId(3)
        bItems.add(item3)
        comb1.add(3)
        item1.setCorrectCombinations(comb1)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then:"the updated question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getQuestionDetails().getColumnOne().size() == 1
        result.getQuestionDetails().getColumnTwo().size() == 2
    }


    def "update an Item Combination Question by adding items in A group and B group and it's relations" () {
        given: "a changed question"
        def questionDto = new QuestionDto(question)
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: "insert item in group A and B"
        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        item3.setInternId(3)
        aItems.add(item3)

        def comb3 = new HashSet<Integer>()
        comb3.add(2)
        item3.setCorrectCombinations(comb3)

        def item4 = new ItemCombinationSlotDto()
        item4.setContent(ITEM_4_CONTENT)
        item4.setInternId(4)
        bItems.add(item4)
        comb1.add(4)
        item1.setCorrectCombinations(comb1)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then:"the updated question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getQuestionDetails().getColumnOne().size() == 2
        result.getQuestionDetails().getColumnTwo().size() == 2
    }


    def "Cannot update an Item Combination Question by changing to zero correct combinations" () {
        given: "a changed question"
        def questionDto = new QuestionDto(question)
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: "change to zero combinations"
        def comb3 = new HashSet<Integer>()
        item1.setCorrectCombinations(comb3)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_ONE_ITEM_COMBINATION_NEEDED
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}


