package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*

@DataJpaTest
class UpdateItemCombinationQuestionTest extends SpockTest {

    def setup(){
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())
    }

    def "update an Item Combination Question by adding an item in A group and it's relations" () {
        given: "one item for each group with one to one relation"
        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        bItems.add(item2)

        def comb1 = new HashSet<Integer>()
        comb1.add(2)
        item1.setCorrectCombinations(comb1)
        def comb2 = new HashSet<Integer>()
        comb2.add(1)
        item2.setCorrectCombinations(comb2)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        and: "insert item in group A"
        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        aItems.add(item3)

        comb2.add(3)
        item2.setCorrectCombinations(comb2)

        questionDto.getQuestionDetailsDto().update(aItems, bItems)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then:"the updated question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != 0
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getColumnOne().size() == 2
        result.getQuestionDetails().getColumnTwo().size() == 1
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

    }

    def "update an Item Combination Question by adding an item in B group and it's relations" () {
        given: "one item for each group with one to one relation"
        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        bItems.add(item2)

        def comb1 = new HashSet<Integer>()
        comb1.add(2)
        item1.setCorrectCombinations(comb1)
        def comb2 = new HashSet<Integer>()
        comb2.add(1)
        item2.setCorrectCombinations(comb2)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        and: "insert item in group B"
        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        bItems.add(item3)

        comb1.add(3)
        item1.setCorrectCombinations(comb1)

        questionDto.getQuestionDetailsDto().update(aItems, bItems)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then:"the updated question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != 0
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getColumnOne().size() == 1
        result.getQuestionDetails().getColumnTwo().size() == 2
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
    }

    def "update an Item Combination Question by changing the image, the title and the content" () {
        given: "an image"
        def image = new Image()
        image.setUrl(IMAGE_1_URL)
        image.setWidth(20)
        imageRepository.save(image)
        and: "a changed question"
        def questionDto = new QuestionDto(question)
        questionDto.setTitle(QUESTION_2_TITLE)
        questionDto.setContent(QUESTION_2_CONTENT)
        image.setUrl(IMAGE_2_URL)
        image.setWidth(20)
        imageRepository.save(image)
        questionDto.setImage(image)
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        when:
        questionService.updateQuestion(question.getId(), questionDto)

        then: "the question is changed"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() == question.getId()
        result.getTitle() == QUESTION_2_TITLE
        result.getContent() == QUESTION_2_CONTENT
        result.getImage() == IMAGE_2_URL
        and: 'are not changed'
        result.getStatus() == Question.Status.AVAILABLE
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
    }

    def "update an Item Combination Question by adding items in A group and B group and it's relations" () {
        given: "one item for each group with one to one relation"
        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        bItems.add(item2)

        def comb1 = new HashSet<Integer>()
        comb1.add(2)
        item1.setCorrectCombinations(comb1)
        def comb2 = new HashSet<Integer>()
        comb2.add(1)
        item2.setCorrectCombinations(comb2)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        and: "insert item in group A and B"
        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        aItems.add(item3)

        comb2.add(3)
        item2.setCorrectCombinations(comb2)

        def item4 = new ItemCombinationSlotDto()
        item4.setContent(ITEM_4_CONTENT)
        bItems.add(item4)

        comb1.add(4)
        item1.setCorrectCombinations(comb1)

        questionDto.getQuestionDetailsDto().update(aItems, bItems)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then:"the updated question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != 0
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getColumnOne().size() == 2
        result.getQuestionDetails().getColumnTwo().size() == 2
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
    }


    def "Cannot update an Item Combination Question by changing to zero correct combinations" () {
        given: "one item for each group with one to one relation"
        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        bItems.add(item2)


        def comb1 = new HashSet<Integer>()
        comb1.add(2)
        item1.setCorrectCombinations(comb1)
        def comb2 = new HashSet<Integer>()
        comb2.add(1)
        item2.setCorrectCombinations(comb2)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        and: "change to 0 combinations"
        def comb3 = new HashSet<Integer>()
        item1.setCorrectCombinations(comb3)
        item2.setCorrectCombinations(comb3)

        questionDto.getQuestionDetailsDto().update(aItems, bItems)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_ONE_ITEM_COMBINATION_NEEDED
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}


