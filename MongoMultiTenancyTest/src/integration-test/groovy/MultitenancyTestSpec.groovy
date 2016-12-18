import MongoMultiTenancyTest.Book
import MongoMultiTenancyTest.BookService
import MongoMultiTenancyTest.SimpleBook
import grails.test.mixin.integration.Integration
import org.grails.datastore.mapping.multitenancy.resolvers.SystemPropertyTenantResolver
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.lang.Unroll
import org.grails.datastore.mapping.core.OptimisticLockingException

import javax.persistence.OptimisticLockException

/**
 * Created by Zohaib on 12/18/2016.
 */

@Integration
class MultitenancyTestSpec extends Specification{

    static boolean transactional = true

    @Autowired
    BookService bookService

    /**
     * Book should be saved in tenants database (book is tenant aware). Connection String for tenant with name "test1"
     * is in tenants collection of the default databse.
     */
    @Unroll
    void "Test save Tenant aware Book in tenants database"() {

        setup:
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "test1")

        when:
        Book book = new Book(name: "NewBook")
        Long id = bookService.saveBookWithTenant(book)
        Book savedBook = Book.findByName("NewBook")

        then:
        println book.toString()
        id == savedBook.id

        cleanup:
        bookService.deleteBookWithTenant(savedBook)
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "")
    }

    /**
     * Book is tenanat aware domain class and thus is saved in the coreesponding database. However
     * a gorm is not saving version column in database, Which will create problems for Optimistic Locking when
     * same object will be updated later.
     */
    @Unroll
    void "Test Tenant aware Book not saving Version Column"() {

        setup:
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "test1")

        when:
        Book book = new Book(name: "NewBook1")
        Long id = bookService.saveBookWithTenant(book)
        Book savedBook = Book.findByName("NewBook1")

        then:
        println book.toString()
        id == savedBook.id
        savedBook.version == null

        cleanup:
        bookService.deleteBookWithTenant(savedBook)
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "")
    }

    /**
     * Book is tenanat aware domain class and thus is saved in the corresponding database. However
     * a gorm is not saving version column in database, Which will create problems for Optimistic Locking when
     * same object will be updated later.
     */
    @Unroll
    void "Test Tenant aware Book not saving Version Column thus OptimisticLock Exception when updating"() {

        setup:
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "test1")

        when:
        Book book = new Book(name: "NewBook2")
        Long id = bookService.saveBookWithTenant(book)
        Book savedBook = Book.findByName("NewBook2")
        id == savedBook.id
        savedBook.name = "UpdatedNameForBook"
        bookService.saveBookWithTenant(savedBook)

        then:
        thrown OptimisticLockingException

        cleanup:
        bookService.deleteBookWithTenant(savedBook)
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "")
    }

    /**
     * Since Simple Book is not implementing MultiTenant trait, SimpleBook will always be saved in the
     * default database, regardless of what tenantId is (Although tenant 'test2' does not exist but it will
     * not matter as SimpleBook is not tenant aware. SimpleBook also saving Version column, as expected,
     */
    @Unroll
    void "Test save SimpleBook in default database and that Version is saved"() {

        setup:
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "test2")

        when:
        SimpleBook book = new SimpleBook(name: "NewBook")
        Long id = bookService.saveBookWithoutTenant(book)
        SimpleBook savedBook = SimpleBook.findByName("NewBook")

        then:
        println book.toString()
        id == savedBook.id
        savedBook.version == 0

        cleanup:
        bookService.deleteBook(savedBook)
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "")
    }
}
