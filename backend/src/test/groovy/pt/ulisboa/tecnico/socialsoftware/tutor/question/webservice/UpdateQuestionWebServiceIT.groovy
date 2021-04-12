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
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.MultipleChoiceQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdateQuestionWebServiceIT extends SpockTest {
    @LocalServerPort
    private int port

    def teacher
    def response
    def question
    def optionOK
    def optionKO
    def optionKK

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
        
        and: "a question"
        question = new Question()
        question.setCourse(externalCourse)
        question.setKey(1)
        question.setTitle(QUESTION_1_TITLE)
        question.setContent(QUESTION_1_CONTENT)
        question.setStatus(Question.Status.AVAILABLE)
        question.setNumberOfAnswers(3)
        question.setNumberOfCorrect(1)
        question.setQuestionDetails(new MultipleChoiceQuestion())
        questionDetailsRepository.save(question.getQuestionDetails())
        questionRepository.save(question)

        and: "three options"
        optionOK = new Option()
        optionOK.setContent(OPTION_1_CONTENT)
        optionOK.setCorrect(true)
        optionOK.setSequence(0)
        optionOK.setQuestionDetails(question.getQuestionDetails())
        optionRepository.save(optionOK)

        optionKO = new Option()
        optionKO.setContent(OPTION_1_CONTENT)
        optionKO.setCorrect(false)
        optionKO.setSequence(1)
        optionKO.setQuestionDetails(question.getQuestionDetails())
        optionRepository.save(optionKO)
        
        optionKK = new Option()
        optionKK.setContent(OPTION_1_CONTENT)
        optionKK.setCorrect(false)
        optionKK.setSequence(2)
        optionKK.setQuestionDetails(question.getQuestionDetails())
        optionRepository.save(optionKK)
    }

    def "update question to different correct options for course execution"() {
        given: "different options"
        def options = new ArrayList<OptionDto>()

        def optionDto = new OptionDto(optionOK)
        optionDto.setContent(OPTION_2_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)
        
        optionDto = new OptionDto(optionKO)
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(1)
        options.add(optionDto)
        optionDto = new OptionDto(optionKK)
        
        optionDto.setContent(OPTION_3_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(4)
        options.add(optionDto)

        and: "a changed questionDto"
        def questionDto = new QuestionDto(question)
        questionDto.setTitle(QUESTION_2_TITLE)
        questionDto.setContent(QUESTION_2_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())
        questionDto.getQuestionDetailsDto().setOptions(options)
        questionDto.setNumberOfCorrect(2)

        when:
        response = restClient.put(
            path: '/questions/' + question.getId(),
            body: JsonOutput.toJson(questionDto),
            requestContentType: 'application/json'
        )

        then: "check the response status"
        response != null
        response.status == 200
        
        and: "if it responds with the correct question"
        def question = response.data
        question.id != null
        question.status == Question.Status.AVAILABLE.name()
        question.title == QUESTION_2_TITLE
        question.content == QUESTION_2_CONTENT
        question.image == null
        question.questionDetailsDto.options.size() == 3

        def resOption = question.questionDetailsDto.options.get(0)
        resOption.content == OPTION_2_CONTENT
        !resOption.correct
        resOption.relevance == -1

        def resOptionTwo = question.questionDetailsDto.options.get(1)
        resOptionTwo.content == OPTION_1_CONTENT
        resOptionTwo.correct
        resOptionTwo.relevance == 1

        def resOptionThree = question.questionDetailsDto.options.get(2)
        resOptionThree.content == OPTION_3_CONTENT
        resOptionThree.correct
        resOptionThree.relevance == 4
    }

    def "student tries to update question"() {
        given: "a student"
        def student = new User(USER_2_NAME, USER_2_EMAIL, USER_2_EMAIL,
                User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        student.authUser.setPassword(passwordEncoder.encode(USER_2_PASSWORD))
        student.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(student)
        userRepository.save(student)
        
        createdUserLogin(USER_2_EMAIL, USER_2_PASSWORD)

        and: "different options"
        def options = new ArrayList<OptionDto>()
        
        def optionDto = new OptionDto(optionOK)
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(false)
        options.add(optionDto)
        
        optionDto = new OptionDto(optionKO)
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(1)
        options.add(optionDto)
        
        optionDto = new OptionDto(optionKK)
        optionDto.setContent(OPTION_1_CONTENT)
        optionDto.setCorrect(true)
        optionDto.setRelevance(4)
        options.add(optionDto)

        and: "a changed questionDto"
        def questionDto = new QuestionDto(question)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new MultipleChoiceQuestionDto())        
        questionDto.getQuestionDetailsDto().setOptions(options)
        questionDto.setNumberOfCorrect(2)

        when:
        response = restClient.put(
            path: '/questions/' + question.getId(),
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
