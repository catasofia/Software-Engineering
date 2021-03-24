package pt.ulisboa.tecnico.socialsoftware.tutor.question.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.*

@DataJpaTest
class RemoveItemCombinationQuestionTest extends SpockTest {
	
	def "remove an existent question" () {
        expect: false
    }

    def "cannot remove question in a quizz" () {
        expect: false
    }

    def "cannot remove submitted question" () {
        expect: false
    }
	

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
