package pt.ulisboa.tecnico.socialsoftware.tutor.question.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration;
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import spock.lang.Unroll;

@DataJpaTest
class ItemCombinationQuestionTest extends SpockTest {

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration { }
}
