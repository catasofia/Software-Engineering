package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService



@DataJpaTest
class CreateItemCombinationQuestionTest extends SpockTest {

    def "cannot create an Item Combination Question without elements in A group"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'two items for B group'
        def bItems = new ArrayList<ItemDto>()

        def item1 = new ItemDto()
        item1.setContent(ITEM_1_CONTENT)
        bItems.add(item1)

        def item2 = new ItemDto()
        item2.setContent(ITEM_2_CONTENT)
        bItems.add(item2)

        when:
        def result = QuestionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_ONE_ITEM_NEEDED_IN_A_GROUP
    }

    def "cannot create an Item Combination Question without elements in B group"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'two items for A group'
        def aItems = new ArrayList<ItemDto>()

        def item1 = new ItemDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemDto()
        item2.setContent(ITEM_2_CONTENT)
        aItems.add(item2)

        when:
        def result = QuestionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_ONE_ITEM_NEEDED_IN_B_GROUP
    }

    def "Cannot create an Item Combination Question with no relation between the groups of 2 elements each"() {
        given: "a questionDto"
        def questionDto = new questionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'two items for each group and no relation between them'
        def aItems = new ArrayList<ItemDto>()
        def bItems = new ArrayList<ItemDto>()

        def item1 = new ItemDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemDto()
        item2.setContent(ITEM_2_CONTENT)
        aItems.add(item2)

        def item3 = new ItemDto()
        item3.setContent(ITEM_3_CONTENT)
        bItems.add(item3)

        def item4 = new ItemDto()
        item4.setContent(ITEM_4_CONTENT)
        bItems.add(item4)

        questionDto.getQuestionDetailsDto().setAgroup(aItems)
        questionDto.getQuestionDetailsDto().setBgroup(bItems)

        when:
        def result = QuestionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_ONE_ITEM_COMBINATION_NEEDED
    }

    def "Create an Item Combination Question with one to one relation between the two groups of 3 elements each"() {
        given: "a questionDto"
        def questionDto = new questionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'three items for each group with one to one relation'
        def aItems = new ArrayList<ItemDto>()
        def bItems = new ArrayList<ItemDto>()
        def correctItemCombinations = new ArrayList<ArrayList<ItemDto>>()

        def item1 = new ItemDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemDto()
        item2.setContent(ITEM_2_CONTENT)
        aItems.add(item2)

        def item3 = new ItemDto()
        item3.setContent(ITEM_3_CONTENT)
        aItems.add(item3)

        def item4 = new ItemDto()
        item4.setContent(ITEM_4_CONTENT)
        bItems.add(item4)

        def item5 = new ItemDto()
        item5.setContent(ITEM_5_CONTENT)
        bItems.add(item5)

        def item6 = new ItemDto()
        item6.setContent(ITEM_6_CONTENT)
        bItems.add(item6)

        questionDto.getQuestionDetailsDto().setAgroup(aItems)
        questionDto.getQuestionDetailsDto().setBgroup(bItems)

        correctItemCombinations.add(item1, item6)
        correctItemCombinations.add(item2, item4)
        correctItemCombinations.add(item3, item5)
        itemSlot.setCorrect(correctItemCombinations)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != 0
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getAItems().size == 3
        result.getQuestionDetails().getBItems().size == 3
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

        def resCombinationOne = result.getItemsDetails().getCombinations().get(0)
        resCombinationOne.getContentAItem() == ITEM_1_CONTENT
        resCombinationOne.getContentBItem() == ITEM_6_CONTENT
        resCombinationOne.isCorrect()

        def resCombinationTwo = result.getItemsDetails().getCombinations().get(1)
        resCombinationTwo.getContentAItem() == ITEM_2_CONTENT
        resCombinationTwo.getContentBItem() == ITEM_4_CONTENT
        resCombinationTwo.isCorrect()

        def resCombinationThree = result.getItemsDetails().getCombinations().get(2)
        resCombinationThree.getContentAItem() == ITEM_3_CONTENT
        resCombinationThree.getContentBItem() == ITEM_5_CONTENT
        resCombinationThree.isCorrect()
    }

    def "Create an Item Combination Question where one element of A group linked to 2 elements of B group and some items don't bind to any other"() {
        given: "a questionDto"
        def questionDto = new questionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'four items for each group and some relations between them'
        def aItems = new ArrayList<ItemDto>()
        def bItems = new ArrayList<ItemDto>()
        def correctItemCombinations = new ArrayList<ArrayList<ItemDto>>()

        def item1 = new ItemDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemDto()
        item2.setContent(ITEM_2_CONTENT)
        aItems.add(item2)

        def item3 = new ItemDto()
        item3.setContent(ITEM_3_CONTENT)
        aItems.add(item3)

        def item4 = new ItemDto()
        item4.setContent(ITEM_4_CONTENT)
        aItems.add(item4)

        def item5 = new ItemDto()
        item5.setContent(ITEM_5_CONTENT)
        bItems.add(item5)

        def item6 = new ItemDto()
        item6.setContent(ITEM_6_CONTENT)
        bItems.add(item6)

        def item7 = new ItemDto()
        item7.setContent(ITEM_7_CONTENT)
        bItems.add(item7)

        def item8 = new ItemDto()
        item8.setContent(ITEM_8_CONTENT)
        bItems.add(item8)

        questionDto.getQuestionDetailsDto().setAgroup(aItems)
        questionDto.getQuestionDetailsDto().setBgroup(bItems)

        correctItemCombinations.add(item2, item5)
        correctItemCombinations.add(item2, item8)
        correctItemCombinations.add(item4, item7)
        itemSlot.setCorrect(correctItemCombinations)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != 0
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getAItems().size() == 4
        result.getQuestionDetails().getBItems().size() == 4
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

        def resCombinationOne = result.getItemsDetails().getCombinations().get(0)
        resCombinationOne.getContentAItem() == ITEM_2_CONTENT
        resCombinationOne.getContentBItem() == ITEM_5_CONTENT
        resCombinationOne.isCorrect()

        def resCombinationTwo = result.getItemsDetails().getCombinations().get(1)
        resCombinationTwo.getContentAItem() == ITEM_2_CONTENT
        resCombinationTwo.getContentBItem() == ITEM_8_CONTENT
        resCombinationTwo.isCorrect()

        def resCombinationThree = result.getItemsDetails().getCombinations().get(2)
        resCombinationThree.getContentAItem() == ITEM_4_CONTENT
        resCombinationThree.getContentBItem() == ITEM_7_CONTENT
        resCombinationThree.isCorrect()
    }

    def "Create an Item Combination Question where all the elements of A group linked to all the elements of B group and two groups have different number of elements"() {
        given: "a questionDto"
        def questionDto = new questionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'two items for A group, three items for B group and all items are connected to all'
        def aItems = new ArrayList<ItemDto>()
        def bItems = new ArrayList<ItemDto>()
        def correctItemCombinations = new ArrayList<ArrayList<ItemDto>>()

        def item1 = new ItemDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemDto()
        item2.setContent(ITEM_2_CONTENT)
        aItems.add(item2)

        def item3 = new ItemDto()
        item3.setContent(ITEM_3_CONTENT)
        bItems.add(item3)

        def item4 = new ItemDto()
        item4.setContent(ITEM_4_CONTENT)
        bItems.add(item4)

        def item5 = new ItemDto()
        item5.setContent(ITEM_5_CONTENT)
        bItems.add(item5)

        questionDto.getQuestionDetailsDto().setAgroup(aItems)
        questionDto.getQuestionDetailsDto().setBgroup(bItems)

        correctItemCombinations.add(item1, item3)
        correctItemCombinations.add(item1, item4)
        correctItemCombinations.add(item1, item5)
        correctItemCombinations.add(item2, item3)
        correctItemCombinations.add(item2, item4)
        correctItemCombinations.add(item2, item5)
        itemSlot.setCorrect(correctItemCombinations)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != 0
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getAItems().size() == 2
        result.getQuestionDetails().getBItems().size() == 3
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

        def resCombinationOne = result.getItemsDetails().getCombinations().get(0)
        resCombinationOne.getContentAItem() == ITEM_1_CONTENT
        resCombinationOne.getContentBItem() == ITEM_3_CONTENT
        resCombinationOne.isCorrect()

        def resCombinationTwo = result.getItemsDetails().getCombinations().get(1)
        resCombinationTwo.getContentAItem() == ITEM_1_CONTENT
        resCombinationTwo.getContentBItem() == ITEM_4_CONTENT
        resCombinationTwo.isCorrect()

        def resCombinationThree = result.getItemsDetails().getCombinations().get(2)
        resCombinationThree.getContentAItem() == ITEM_1_CONTENT
        resCombinationThree.getContentBItem() == ITEM_5_CONTENT
        resCombinationThree.isCorrect()

        def resCombinationFour = result.getItemsDetails().getCombinations().get(3)
        resCombinationFour.getContentAItem() == ITEM_2_CONTENT
        resCombinationFour.getContentBItem() == ITEM_3_CONTENT
        resCombinationFour.isCorrect()

        def resCombinationFive = result.getItemsDetails().getCombinations().get(4)
        resCombinationFive.getContentAItem() == ITEM_2_CONTENT
        resCombinationFive.getContentBItem() == ITEM_4_CONTENT
        resCombinationFive.isCorrect()

        def resCombinationSix = result.getItemsDetails().getCombinations().get(5)
        resCombinationSix.getContentAItem() == ITEM_2_CONTENT
        resCombinationSix.getContentBItem() == ITEM_5_CONTENT
        resCombinationSix.isCorrect()
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
