package pt.ulisboa.tecnico.socialsoftware.tutor.answer.webservice

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.OpenAnswerStatementAnswerDetailsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.OpenAnswerQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementQuizDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenAnswerQuestionQuizIT extends SpockTest {
    @LocalServerPort
    private int port

    def user
    def quizQuestion
    def quizAnswer
    def quiz
    def question

    def setup() {
        restClient = new RESTClient("http://localhost:" + port)

        createExternalCourseAndExecution()

        user = new User(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, User.Role.STUDENT, true, AuthUser.Type.TECNICO)
        user.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        user.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(user)
        userRepository.save(user)

        createdUserLogin(USER_1_USERNAME, USER_1_PASSWORD)

        quiz = new Quiz()
        quiz.setKey(1)
        quiz.setTitle(QUIZ_TITLE)
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setCourseExecution(externalCourseExecution)
        quiz.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)

        question = new Question()
        question.setKey(1)
        question.setTitle(QUESTION_1_TITLE)
        question.setContent(QUESTION_1_CONTENT)
        question.setCourse(externalCourse)

        def questionDetails = new OpenAnswerQuestion()
        questionDetails.setSuggestion(SUGGESTION_1_CONTENT)
        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)

        quizQuestion = new QuizQuestion(quiz, question, 0)
        quizQuestionRepository.save(quizQuestion)

        quizAnswer = new QuizAnswer(user, quiz)
        quizAnswerRepository.save(quizAnswer)
    }

    def "submit an answer to an open answer question"() {
        given:  'a completed quiz'
        quizAnswer.completed = true

        and: 'an answer'
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = quiz.getId()
        statementQuizDto.quizAnswerId = quizAnswer.getId()

        def statementAnswerDto = new StatementAnswerDto()
        def openAnswerQuestionDto = new OpenAnswerStatementAnswerDetailsDto()
        openAnswerQuestionDto.setAnswer(SUGGESTION_1_CONTENT)

        statementAnswerDto.setAnswerDetails(openAnswerQuestionDto)
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(quizAnswer.getQuestionAnswers().get(0).getId())

        when: 'a request is posted'
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter()

        def response = restClient.post(
                path: "/answers/" + quiz.getId() + "/submit",
                body: ow.writeValueAsString(statementAnswerDto),
                requestContentType: "application/json"
        )

        then: "check the response status"
        response != null
        response.status == 200
    }

    def "show results of quiz to an open answer question"() {
        given:  'a completed quiz'
        quizAnswer.completed = true

        and: 'an answer'
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = quiz.getId()
        statementQuizDto.quizAnswerId = quizAnswer.getId()

        def statementAnswerDto = new StatementAnswerDto()
        def openAnswerQuestionDto = new OpenAnswerStatementAnswerDetailsDto()
        openAnswerQuestionDto.setAnswer(SUGGESTION_1_CONTENT)

        statementAnswerDto.setAnswerDetails(openAnswerQuestionDto)
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(quizAnswer.getQuestionAnswers().get(0).getId())

        statementQuizDto.getAnswers().add(statementAnswerDto)
        answerService.concludeQuiz(statementQuizDto)

        when: 'a request is posted'
        def response = restClient.get(
                path: "/answers/" + externalCourseExecution.getId() + "/quizzes/solved/",
                requestContentType: "application/json"
        )

        then: "check the response status"
        response != null
        response.status == 200
    }

    def "show answers to completed quiz"() {
        given:  'a completed quiz'
        quizAnswer.completed = true

        and: 'an answer'
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = quiz.getId()
        statementQuizDto.quizAnswerId = quizAnswer.getId()

        def statementAnswerDto = new StatementAnswerDto()
        def openAnswerQuestionDto = new OpenAnswerStatementAnswerDetailsDto()
        openAnswerQuestionDto.setAnswer(SUGGESTION_1_CONTENT)

        statementAnswerDto.setAnswerDetails(openAnswerQuestionDto)
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(quizAnswer.getQuestionAnswers().get(0).getId())

        statementQuizDto.getAnswers().add(statementAnswerDto)
        answerService.concludeQuiz(statementQuizDto)

        def correctAnswers = answerService.concludeQuiz(statementQuizDto)

        when: 'a request is posted'

        def response = restClient.get(
                path: "/answers/" + quiz.getId() + "/question/" + question.getId(),
                requestContentType: "application/json"
        )

        then:
        response != null
        response.status == 200
    }


    def "export quiz with open answer question"() {
        given:  'a completed quiz'
        quizAnswer.completed = true

        and: 'an answer'
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = quiz.getId()
        statementQuizDto.quizAnswerId = quizAnswer.getId()

        def statementAnswerDto = new StatementAnswerDto()
        def openAnswerQuestionDto = new OpenAnswerStatementAnswerDetailsDto()
        openAnswerQuestionDto.setAnswer(SUGGESTION_1_CONTENT)

        statementAnswerDto.setAnswerDetails(openAnswerQuestionDto)
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementAnswerDto.setQuestionAnswerId(quizAnswer.getQuestionAnswers().get(0).getId())

        statementQuizDto.getAnswers().add(statementAnswerDto)
        answerService.concludeQuiz(statementQuizDto)

        def correctAnswers = answerService.concludeQuiz(statementQuizDto)


        and: 'teacher login'

        user = new User(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, User.Role.TEACHER, true, AuthUser.Type.TECNICO)
        user.authUser.setPassword(passwordEncoder.encode(USER_2_PASSWORD))
        user.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(user)
        userRepository.save(user)

        createdUserLogin(USER_2_USERNAME, USER_2_PASSWORD)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }
        
        when: 'a request is posted'

        def map = restClient.get(
                path: "/quizzes/" + quiz.getId() + "/export",
                requestContentType: "application/json"
        )

        then: "the response status is OK"
        assert map['response'].status == 200
        assert map['reader'] != null
    }



    def cleanup() {
        userRepository.deleteAll()
        questionRepository.deleteAll()

        courseExecutionRepository.deleteAll()
        courseRepository.deleteAll()
    }
}