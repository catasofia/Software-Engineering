package pt.ulisboa.tecnico.socialsoftware.tutor.question.webservice

import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.apache.http.HttpStatus
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationQuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.ItemCombinationSlotDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExportItemCombinationQuestionIT extends SpockTest {
    @LocalServerPort
    private int port

    def questionDto
    def user

    def aItems
    def bItems
    def item1
    def item2
    def item3
    def item4
    def item5
    def comb1
    def comb2
    def comb5

    def setup() {
        restClient = new RESTClient("http://localhost:" + port)

        createExternalCourseAndExecution()

        given: "a questionDto"
        questionDto = new QuestionDto()
        questionDto.setKey(1)
        questionDto.setTitle(QUESTION_1_TITLE)
        questionDto.setContent(QUESTION_1_CONTENT)
        questionDto.setStatus(Question.Status.AVAILABLE.name())
        questionDto.setQuestionDetailsDto(new ItemCombinationQuestionDto())

        and : 'two groups of items and its combinations'
        aItems = new HashSet<ItemCombinationSlotDto>()
        bItems = new HashSet<ItemCombinationSlotDto>()

        item1 = new ItemCombinationSlotDto()
        item1.setContent(ITEM_1_CONTENT)
        item1.setInternId(1)
        aItems.add(item1)

        item2 = new ItemCombinationSlotDto()
        item2.setContent(ITEM_2_CONTENT)
        item2.setInternId(2)
        aItems.add(item2)

        item3 = new ItemCombinationSlotDto()
        item3.setContent(ITEM_3_CONTENT)
        item3.setInternId(3)
        bItems.add(item3)

        item4 = new ItemCombinationSlotDto()
        item4.setContent(ITEM_4_CONTENT)
        item4.setInternId(4)
        bItems.add(item4)

        item5 = new ItemCombinationSlotDto()
        item5.setInternId(5)
        item5.setContent(ITEM_5_CONTENT)
        aItems.add(item5)

        comb1 = new HashSet<ItemCombinationSlotDto>()
        comb1.add(item3)
        comb1.add(item4)
        item1.setCorrectCombinations(comb1)

        comb2 = new HashSet<ItemCombinationSlotDto>()
        comb2.add(item3)
        item2.setCorrectCombinations(comb2)

        comb5 = new HashSet<ItemCombinationSlotDto>()
        comb5.add(item4)
        item5.setCorrectCombinations(comb5)

        questionDto.getQuestionDetailsDto().setItemCombinationSlots(aItems, bItems)
    }

    def "export an item combination question as teacher"() {
        given: 'a teacher'
        user = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.TEACHER, false, AuthUser.Type.TECNICO)
        user.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        user.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(user)
        userRepository.save(user)

        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: 'a request is posted'
        def map = restClient.get(
                path: "/questions/courses/" + externalCourse.getId() + "/export",
                requestContentType: "application/json"
        )

        then: "the response status is OK"
        assert map['response'].status == 200
        assert map['reader'] != null
    }

    def "cannot export an item combination question as student"() {
        given: 'a student'
        user = new User(USER_1_NAME, USER_1_EMAIL, USER_1_EMAIL,
                User.Role.STUDENT, false, AuthUser.Type.TECNICO)
        user.authUser.setPassword(passwordEncoder.encode(USER_1_PASSWORD))
        user.addCourse(externalCourseExecution)
        externalCourseExecution.addUser(user)
        userRepository.save(user)

        createdUserLogin(USER_1_EMAIL, USER_1_PASSWORD)

        and: 'prepare request response'
        restClient.handler.failure = { resp, reader ->
            [response:resp, reader:reader]
        }
        restClient.handler.success = { resp, reader ->
            [response:resp, reader:reader]
        }

        when: 'a request is posted'

        def map = restClient.get(
                path: "/questions/courses/" + externalCourse.getId() + "/export",
                requestContentType: "application/json"
        )

        then: "the response status is FORBIDDEN"
        assert map['response'].status == 403
    }

    def cleanup() {
        userRepository.deleteAll()
        questionRepository.deleteAll()

        courseExecutionRepository.deleteAll()
        courseRepository.deleteAll()
    }
}