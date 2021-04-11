package pt.ulisboa.tecnico.socialsoftware.tutor.question

import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.apache.http.HttpStatus
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OpenAnswerQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RemoveOpenAnswerQuestionIT extends SpockTest {
    @LocalServerPort
    private int port

    def questionDto
    def user

    def setup() {
        restClient = new RESTClient("http://localhost:" + port)

        createExternalCourseAndExecution()

        given: "a questionDto"
        questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new OpenAnswerQuestionDto())
        questionDto.getQuestionDetailsDto().setSuggestion(SUGGESTION_1_CONTENT)
        questionDto = questionService.createQuestion(externalCourse.getId(), questionDto)

    }

    def "remove an open answer question as teacher"() {
        given: 'a teacher'
        user = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.TEACHER, false, AuthUser.Type.TECNICO)
        user.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        user.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(user)
        userRepository.save(user)

        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)


        when: 'a request is posted'

        def response = restClient.delete(
                path: "/questions/" + questionDto.getId(),
                requestContentType: "application/json"
        )

        then: "check the response status"
        response != null
        response.status == 200

        and: "if a question has been deleted"
        questionRepository.count() == 0L
    }

    def "update an open answer question as student"() {
        given: 'a student'
        user = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        user.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        user.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(user)
        userRepository.save(user)

        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)

        when: 'a request is posted'

        restClient.delete(
                path: "/questions/" + questionDto.getId(),
                requestContentType: "application/json"
        )

        then: 'Access is forbidden'
        def error = thrown(HttpResponseException)
        error.response.status == HttpStatus.SC_FORBIDDEN
    }

    def cleanup() {
        userRepository.deleteAll()
        questionRepository.deleteAll()

        courseExecutionRepository.deleteAll()
        courseRepository.deleteAll()
    }
}