package testhibernate

import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

@Transactional
class Example2Service {


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    def serviceMethod() {
        Author a = new Author()
        a.name="second Author"
        a.save()
    }
}
