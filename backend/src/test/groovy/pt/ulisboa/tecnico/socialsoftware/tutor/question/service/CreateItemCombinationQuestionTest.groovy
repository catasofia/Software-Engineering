package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*

@DataJpaTest
class CreateItemCombinationQuestionTest extends SpockTest {

    def setup() {
        createExternalCourseAndExecution()
    }

    def "cannot create an Item Combination Question with no image without elements in A group"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'two items for B group'
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        bItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        bItems.add(item2)

        def aItems = new HashSet<ItemCombinationSlotDto>()

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)



        when:
        def result = QuestionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_ONE_SLOT_NEEDED_FOR_EACH_COLUMN
    }

    def "cannot create an Item Combination Question with image and without elements in B group"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'an image'
        def image = new ImageDto()
        image.setUrl(IMAGE_1_URL)
        image.setWidth(20)
        questionDto.setImage(image)

        and: 'two items for A group'
        def aItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        aItems.add(item2)

        def bItems = new HashSet<ItemCombinationSlotDto>()


        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        when:
        def result = QuestionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_ONE_SLOT_NEEDED_FOR_EACH_COLUMN

    }

    def "Cannot create an Item Combination Question with no relation between the groups of 2 elements each"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'two items for each group and no relation between them'
        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        aItems.add(item2)

        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        bItems.add(item3)

        def item4 = new ItemCombinationSlotDto()
        item4.setContent(ITEM_4_CONTENT)
        bItems.add(item4)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        when:
        def result = QuestionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.AT_LEAST_ONE_ITEM_COMBINATION_NEEDED
    }

    def "Cannot create question with combinations in the same group"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'an image'
        def image = new ImageDto()
        image.setUrl(IMAGE_1_URL)
        image.setWidth(20)
        questionDto.setImage(image)

        and: 'three items with one relation in the same group'
        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        item1.setInternId(1)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        item2.setInternId(2)
        aItems.add(item2)

        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        item3.setInternId(3)
        bItems.add(item3)

        def comb1 = new HashSet<Integer>()
        comb1.add(2)
        item1.setCorrectCombinations(comb1)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)
        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.COMBINATION_IN_SAME_SET
    }

    def "Create an Item Combination Question with image and with one to one relation between the two groups of 3 elements each"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'an image'
        def image = new ImageDto()
        image.setUrl(IMAGE_1_URL)
        image.setWidth(20)
        questionDto.setImage(image)

        and: 'three items for each group with one to one relation'
        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        item1.setInternId(1)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        item2.setInternId(2)
        aItems.add(item2)

        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        item3.setInternId(3)
        aItems.add(item3)

        def item4 = new ItemCombinationSlotDto()
        item4.setContent(ITEM_4_CONTENT)
        item4.setInternId(4)
        bItems.add(item4)

        def item5 = new ItemCombinationSlotDto()
        item5.setContent(ITEM_5_CONTENT)
        item5.setInternId(5)
        bItems.add(item5)

        def item6 = new ItemCombinationSlotDto()
        item6.setContent(ITEM_6_CONTENT)
        item6.setInternId(6)
        bItems.add(item6)


        def comb1 = new HashSet<Integer>()
        comb1.add(6)
        item1.setCorrectCombinations(comb1)
        def comb2 = new HashSet<Integer>()
        comb2.add(4)
        item2.setCorrectCombinations(comb2)
        def comb3 = new HashSet<Integer>()
        comb3.add(5)
        item3.setCorrectCombinations(comb3)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)
        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != 0
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getColumnOne().size() == 3
        result.getQuestionDetails().getColumnTwo().size() == 3
        result.getImage().getId() != null
        result.getImage().getUrl() == IMAGE_1_URL
        result.getImage().getWidth() == 20
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
    }

    def "Create an Item Combination Question where one element of A group linked to 2 elements of B group and some items don't bind to any other"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'four items for each group and some relations between them'
        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        item1.setInternId(1)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        item2.setInternId(2)
        aItems.add(item2)

        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        item3.setInternId(3)
        aItems.add(item3)

        def item4 = new ItemCombinationSlotDto()
        item4.setContent(ITEM_4_CONTENT)
        item4.setInternId(4)
        aItems.add(item4)

        def item5 = new ItemCombinationSlotDto()
        item5.setContent(ITEM_5_CONTENT)
        item5.setInternId(5)
        bItems.add(item5)

        def item6 = new ItemCombinationSlotDto()
        item6.setContent(ITEM_6_CONTENT)
        item6.setInternId(6)
        bItems.add(item6)

        def item7 = new ItemCombinationSlotDto()
        item7.setContent(ITEM_7_CONTENT)
        item7.setInternId(7)
        bItems.add(item7)

        def item8 = new ItemCombinationSlotDto()
        item8.setContent(ITEM_8_CONTENT)
        item8.setInternId(8)
        bItems.add(item8)

        def comb4 = new HashSet<Integer>()
        comb4.add(5)
        comb4.add(8)
        item2.setCorrectCombinations(comb4)
        def comb5 = new HashSet<Integer>()
        comb5.add(7)
        item4.setCorrectCombinations(comb5)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != 0
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getColumnOne().size() == 4
        result.getQuestionDetails().getColumnTwo().size() == 4
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
    }

    def "Create an Item Combination Question where all the elements of A group linked to all the elements of B group and two groups have different number of elements"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and: 'two items for A group, three items for B group and all items are connected to all'
        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        item1.setInternId(1)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        item2.setInternId(2)
        aItems.add(item2)

        def item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        item3.setInternId(3)
        bItems.add(item3)

        def item4 = new ItemCombinationSlotDto()
        item4.setContent(ITEM_4_CONTENT)
        item4.setInternId(4)
        bItems.add(item4)

        def item5 = new ItemCombinationSlotDto()
        item5.setContent(ITEM_5_CONTENT)
        item5.setInternId(5)
        bItems.add(item5)

        def comb6 = new HashSet<Integer>()
        comb6.add(3)
        comb6.add(4)
        comb6.add(5)
        item1.setCorrectCombinations(comb6)
        def comb7 = new HashSet<Integer>()
        comb7.add(3)
        comb7.add(4)
        comb7.add(5)
        item2.setCorrectCombinations(comb7)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != 0
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getQuestionDetails().getColumnOne().size() == 2
        result.getQuestionDetails().getColumnTwo().size() == 3
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)
    }



    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
