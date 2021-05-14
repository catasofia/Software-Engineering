package pt.ulisboa.tecnico.socialsoftware.tutor.answer.webservice

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import org.springframework.boot.test.context.SpringBootTest
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.ItemCombinationAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.ItemCombinationStatementAnswerDetailsDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.ItemCombinationSlot
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationSlotDto
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementAnswerDto
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto.StatementQuizDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemCombinationQuestionQuizIT extends SpockTest {

    def user
    def quizQuestion
    def quizAnswer
    def quiz
    def aItems
    def bItems
    def item1
    def item2
    def comb1

    def setup() {
        createExternalCourseAndExecution()

        user = new User(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        user.addCourse(externalCourseExecution)
        userRepository.save(user)

        quiz = new Quiz()
        quiz.setKey(1)
        quiz.setTitle(QUIZ_TITLE)
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setCourseExecution(externalCourseExecution)
        quiz.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)

        def question = new Question()
        question.setKey(1)
        question.setTitle(QUESTION_1_TITLE)
        question.setCourse(externalCourse)

        def questionDetails = new ItemCombinationQuestion()

        aItems = new HashSet<ItemCombinationSlotDto>()
        bItems = new HashSet<ItemCombinationSlotDto>()

        item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        item1.setInternId(1)
        aItems.add(item1)

        item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        item2.setInternId(2)
        bItems.add(item2)

        comb1 = new HashSet<ItemCombinationSlotDto>()
        comb1.add(item2)
        item1.setCorrectCombinations(comb1)

        question.setQuestionDetails(questionDetails)
        questionDetailsRepository.save(questionDetails)
        questionRepository.save(question)

        quizQuestion = new QuizQuestion(quiz, question, 0)
        quizQuestionRepository.save(quizQuestion)

        quizAnswer = new QuizAnswer(user, quiz)
        quizAnswerRepository.save(quizAnswer)
    }

    /*def "submit an answer to an item combination question"() {
        given:  'a completed quiz'
        quizAnswer.completed = true

        and: 'an answer'
        def statementQuizDto = new StatementQuizDto()
        statementQuizDto.id = quiz.getId()
        statementQuizDto.quizAnswerId = quizAnswer.getId()
        
        def statementAnswerDto = new StatementAnswerDto()
        def itemCombinationAnswer = new ItemCombinationAnswer()

        def answerItem1 = new HashSet<ItemCombinationSlot>()
        def it1 = new ItemCombinationSlot(item1)
        answerItem1.add(it1)
        itemCombinationAnswer.setItemCombinationSlots(answerItem1)

        def answerDto = new ItemCombinationStatementAnswerDetailsDto(itemCombinationAnswer)

        statementAnswerDto.setAnswerDetails(answerDto)
        statementAnswerDto.setSequence(0)
        statementAnswerDto.setTimeTaken(100)
        statementQuizDto.getAnswers().add(statementAnswerDto)
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
    }*/
}