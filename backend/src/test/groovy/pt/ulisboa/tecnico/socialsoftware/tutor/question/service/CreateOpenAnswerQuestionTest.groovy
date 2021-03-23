package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*

@DataJpaTest
class CreateOpenAnswerQuestionTest extends SpockTest {

    def questionDto

    def setup() {
        given: "a questionDto"
        questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new OpenAnswerQuestionDto())
    }

    def "create an open answer question with valid suggestion"() {
        given: 'a suggestion'
        questionDto.getQuestionDetailsDto().setSuggestion(SUGGESTION_1_CONTENT)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "the correct question is inside the repository"
        questionRepository.count() == 1L
        def result = questionRepository.findAll().get(0)
        result.getQuestionDetails().getSuggestion() == SUGGESTION_1_CONTENT
    }

    def "cannot create an open question with invalid suggestion"() {
        given: 'an invalid suggestion'
        questionDto.getQuestionDetailsDto().setSuggestion(invalidSuggestion)

        when:
        questionService.createQuestion(externalCourse.getId(), questionDto)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.INVALID_CONTENT_FOR_SUGGESTION

        where:
        invalidSuggestion << [null, SUGGESTION_EMPTY_CONTENT]
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}