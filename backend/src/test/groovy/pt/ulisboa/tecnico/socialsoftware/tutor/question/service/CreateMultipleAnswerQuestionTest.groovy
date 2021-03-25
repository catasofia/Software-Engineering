package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CodeFillInQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.CodeOrderQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*
import spock.lang.Unroll

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage

@DataJpaTest
class CreateMultipleAnswerQuestionTest extends SpockTest {
    def "create a multiple choice question with no image and two correct option"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1);
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())

        and : 'four options'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_3_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_4_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)

        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != null
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null
        result.getQuestionDetails().getOptions().size() == 4
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

        def resOption = result.getQuestionDetails().getOptions().get(0)
        resOption.getContent() == OPTION_1_CONTENT
        resOption.isCorrect()
        resOption.getRelevance() == 2

        def resOptionTwo = result.getQuestionDetails().getOptions().get(2)
        resOptionTwo.getContent() == OPTION_3_CONTENT
        resOptionTwo.isCorrect()
        resOption.getRelevance() == 2
    }

    def "create a multiple choice question with no image and three correct option"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1);
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())

        and : 'four options'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_3_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_4_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        options.add(optionDto)

        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != null
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage() == null
        result.getQuestionDetails().getOptions().size() == 4
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

        def resOption = result.getQuestionDetails().getOptions().get(0)
        resOption.getContent() == OPTION_1_CONTENT
        resOption.isCorrect()
        resOption.getRelevance() == 2

        def resOptionTwo = result.getQuestionDetails().getOptions().get(2)
        resOptionTwo.getContent() == OPTION_3_CONTENT
        resOptionTwo.isCorrect()
        resOptionTwo.getRelevance() == 2

        def resOptionThree = result.getQuestionDetails().getOptions().get(3)
        resOptionThree.getContent() == OPTION_4_CONTENT
        resOptionThree.isCorrect()
        resOptionThree.getRelevance() == 2
    }

    def "create a multiple choice question with an image and two correct option"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1);
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())

        and: 'an image'
        def image = new ImageDto()
        image.setUrl(IMAGE_1_URL)
        image.setWidth(20)
        questionDto.setImage(image)

        and : 'four options'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_3_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_4_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)

        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != null
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage().getId() != null
        result.getImage().getUrl() == IMAGE_1_URL
        result.getImage().getWidth() == 20
        result.getQuestionDetails().getOptions().size() == 4
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

        def resOption = result.getQuestionDetails().getOptions().get(0)
        resOption.getContent() == OPTION_1_CONTENT
        resOption.isCorrect()
        resOption.getRelevance() == 2

        def resOptionTwo = result.getQuestionDetails().getOptions().get(2)
        resOptionTwo.getContent() == OPTION_3_CONTENT
        resOptionTwo.isCorrect()
        resOptionTwo.getRelevance() == 2
    }

    def "create a multiple choice question with an image and three correct option"() {
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1);
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())

        and: 'an image'
        def image = new ImageDto()
        image.setUrl(IMAGE_1_URL)
        image.setWidth(20)
        questionDto.setImage(image)

        and : 'four options'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_3_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_4_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(2)
        options.add(optionDto)

        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getId() != null
        result.getKey() == 1
        result.getStatus() == Question.Status.AVAILABLE
        result.getTitle() == QUESTION_1_TITLE
        result.getContent() == QUESTION_1_CONTENT
        result.getImage().getId() != null
        result.getImage().getUrl() == IMAGE_1_URL
        result.getImage().getWidth() == 20
        result.getQuestionDetails().getOptions().size() == 4
        result.getCourse().getName() == COURSE_1_NAME
        externalCourse.getQuestions().contains(result)

        def resOption = result.getQuestionDetails().getOptions().get(0)
        resOption.getContent() == OPTION_1_CONTENT
        resOption.isCorrect()
        resOption.getRelevance() == 2

        def resOptionTwo = result.getQuestionDetails().getOptions().get(2)
        resOptionTwo.getContent() == OPTION_3_CONTENT
        resOptionTwo.isCorrect()
        resOptionTwo.getRelevance() == 2

        def resOptionThree = result.getQuestionDetails().getOptions().get(3)
        resOptionThree.getContent() == OPTION_4_CONTENT
        resOptionThree.isCorrect()
        resOptionThree.getRelevance() == 2
    }

    def "create a multiple choice question with no image and no correct options"(){
        given: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1);
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())

        and : 'three options'
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(false)
        def options = new ArrayList<OptionDto>()
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)

        optionDto = new OptionDto()
        optionDto.setContent(OPTION_3_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)

        questionDto.getQuestionDetailsDto().setOptions(options)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException);
        exception.getErrorMessage() == ErrorMessage.NO_CORRECT_OPTION;
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}