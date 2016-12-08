package MongoMultiTenancyTest

import grails.gorm.multitenancy.Tenants
import grails.transaction.Transactional
import org.grails.datastore.mapping.multitenancy.resolvers.SystemPropertyTenantResolver

@Transactional
class BookService {

    def saveBookWIthTenant() {
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "test1")
        Book b = new Book(name:"TestBook:"+System.currentTimeSeconds())
        Tenants.withCurrent{
            b.save(flush:true)
        }
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "")
    }

    def getBooksWIthTenant() {
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "test1")
        def books = Book.list().toString()
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "")
        return books
    }

    def updateBookWithTenant(id) {
        try {
            System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "test1")
            def book = Book.get(id)
            book.name = "New Name"
            Tenants.withCurrent {
                book.save(flush: true)
            }
            System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "")
            return Book.get(id).toString()
        } catch(Exception e) {
            println e
            return "Exception:"+e.getMessage()
        }
    }

    def getBooksWIthoutTenant() {
        def books = SimpleBook.list().toString()
        return books
    }

    def saveBookWIthoutTenant() {
        SimpleBook b = new SimpleBook(name: "TestBook:"+System.currentTimeSeconds())
        b.save(flush:true)
    }
}
