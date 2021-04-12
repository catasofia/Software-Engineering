package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovyx.net.http.HttpResponseException
import org.apache.http.HttpStatus
import groovy.json.JsonOutput
import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateQuestionWebServiceIT extends SpockTest {
    @LocalServerPort
    private int port

    def teacher
    def response

    def setup() {
        restClient = new RESTClient("http://localhost:" + port)
        
        createExternalCourseAndExecution()

        teacher = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.TEACHER, false, AuthUser.Type.TECNICO)
        teacher.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        teacher.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(teacher)
        userRepository.save(teacher)

        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)
    }

    def "create question with multiple correct options for course execution"() {
        given: "four options"
        def options = new ArrayList<OptionDto>()
        
        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(1)
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
        optionDto.setRelevance(3)
        options.add(optionDto)

        and: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1);
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        questionDto.getQuestionDetailsDto().setOptions(options)
        questionDto.setNumberOfCorrect(3)
        questionDto.setNumberOfAnswers(4)

        when:
        response = restClient.post(
                path: '/courses/' + externalCourseExecution.getId() + '/questions',
                body: JsonOutput.toJson(questionDto),
                requestContentType: 'application/json'
        )

        then: "check the response status"
        response != null
        response.status == 200
        
        and: "check if it's the correct question"
        def question = response.data
        question.id != null
        question.status == Question.Status.AVAILABLE.name()
        question.title == QUESTION_1_TITLE
        question.content == QUESTION_1_CONTENT
        question.image == null
        question.questionDetailsDto.options.size() == 4

        def resOption = question.questionDetailsDto.options.get(0)
        resOption.content == OPTION_1_CONTENT
        resOption.correct
        resOption.relevance == 1

        def resOptionTwo = question.questionDetailsDto.options.get(1)
        resOptionTwo.content == OPTION_2_CONTENT
        !resOptionTwo.correct
        resOptionTwo.relevance == -1

        def resOptionThree = question.questionDetailsDto.options.get(2)
        resOptionThree.content == OPTION_3_CONTENT
        resOptionThree.correct
        resOptionThree.relevance == 2

        def resOptionFour = question.questionDetailsDto.options.get(3)
        resOptionFour.content == OPTION_4_CONTENT
        resOptionFour.correct
        resOptionFour.relevance == 3
    }

    def "student tries to create a question with multiple correct options"() {
        given: "a student"
        def student = new User(USER_2_NAME, USER_2_EMAIL, USER_2_EMAIL,
            User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        student.authUser.setPassword(passwordEncoder.encode(USER_2_PASSWORD))
        student.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(student)
        userRepository.save(student)
        
        createdUserLogin(USER_2_EMAIL, USER_2_PASSWORD)

        and: "three options"
        def options = new ArrayList<OptionDto>()

        def optionDto = new OptionDto()
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(1)
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

        and: "a questionDto"
        def questionDto = new QuestionDto()
        questionDto.setKey(1);
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        questionDto.getQuestionDetailsDto().setOptions(options)
        questionDto.setNumberOfCorrect(2)
        questionDto.setNumberOfAnswers(3)

        when:
        response = restClient.post(
                path: '/courses/' + externalCourseExecution.getId() + '/questions',
                body: JsonOutput.toJson(questionDto),
                requestContentType: 'application/json'
        )

        then: "expect a error"
        response == null
        
        and: "check exception"
        def error = thrown(HttpResponseException)
        error.response.status == HttpStatus.SC_FORBIDDEN

        cleanup:
        userRepository.deleteById(student.getId())  
    }

    def cleanup() {
        userRepository.deleteById(teacher.getId())
        courseExecutionRepository.deleteById(externalCourseExecution.getId())
        courseRepository.deleteById(externalCourse.getId())
    }
}
