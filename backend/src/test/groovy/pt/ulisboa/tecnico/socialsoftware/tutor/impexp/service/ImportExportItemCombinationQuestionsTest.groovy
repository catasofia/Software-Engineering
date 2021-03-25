package pt.ulisboa.tecnico.socialsoftware.tutor.impexp.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ImageDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationSlotDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto

@DataJpaTest
class ImportExportItemCombinationQuestionsTest extends SpockTest {

    def questionId

    def setup() {
        given: "a question and one item for each group with one to one relation"
        def questionDto = new QuestionDto()
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        def aItems = new HashSet<ItemCombinationSlotDto>()
        def bItems = new HashSet<ItemCombinationSlotDto>()

        def item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        item1.setInternId(1)
        aItems.add(item1)

        def item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        item2.setInternId(2)
        bItems.add(item2)

        def comb1 = new HashSet<Integer>()
        comb1.add(2)
        item1.setCorrectCombinations(comb1)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)

        and: "an image"
        def image = new ImageDto()
        image.setUrl(IMAGE_1_URL)
        image.setWidth(20)
        questionDto.setImage(image)

        questionId = questionService.createQuestion(externalCourse.getId(), questionDto).getId()
    }

    def "export item combination questions to xml" () {
        given: "a question"
        def questionsXml = questionService.exportQuestionsToXml()
        questionsXml != null
        print questionsXml
    }

    def 'export and import Item Combination Question to xml'() {
        given: 'a xml with questions'
        def questionsXml = questionService.exportQuestionsToXml()
        questionsXml != null
        print questionsXml

        and: 'a clean database'
        questionService.removeQuestion(questionId)

        when:
        questionService.importQuestionsFromXml(questionsXml)

        then:
        questionRepository.findQuestions(externalCourse.getId()).size() == 1
        def questionResult = questionService.findQuestions(externalCourse.getId()).get(0)
        questionResult.getKey() == null
        questionResult.getTitle() == QUESTION_1_TITLE
        questionResult.getContent() == QUESTION_1_CONTENT
        questionResult.getStatus() == Question.Status.AVAILABLE.name()
        questionResult.getQuestionDetailsDto().getColumnOne().size() == 1
        questionResult.getQuestionDetailsDto().getColumnTwo().size() == 1
    }

    def "export to latex"(){
        expect:false
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
