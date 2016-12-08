package MongoMultiTenancyTest

import grails.gorm.MultiTenant
import grails.mongodb.MongoEntity

class Book  implements MongoEntity<Book>, MultiTenant<Book> {

    String name

    static constraints = {
    }

    def String toString() {
        return "Book:"+id+","+version+","+name
    }
}
