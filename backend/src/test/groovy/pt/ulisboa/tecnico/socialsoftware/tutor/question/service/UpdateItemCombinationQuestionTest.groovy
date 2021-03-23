package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion

@DataJpaTest
class UpdateItemCombinationQuestionTest extends SpockTest {

    def "update an Item Combination Question by adding an item in A group and it's relations" () {
        expect: false
    }

    def "update an Item Combination Question by adding an item in B group and it's relations" () {
        expect: false
    }

    def "update an Item Combination Question by changing the image, the title and the content" () {
        expect: false
    }

    def "update an Item Combination Question by adding items in A group and B group and it's relations" () {
        expect: false
    }


    def "Cannot update an Item Combination Question by changing to zero correct combinations" () {
        expect: false
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}

