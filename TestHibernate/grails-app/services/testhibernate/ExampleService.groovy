package testhibernate

import grails.transaction.Transactional

@Transactional
class ExampleService {

    def sessionFactory
    def example2Service

    def serviceMethod() {
        List books = Book.findAll()
        println books?.size()
        addBook()
        books = Book.findAll()
        println books?.size()
        throw new Exception("custom exception")
   }

    def serviceMethod1(Book book) {
        println "bookName:"+book.name
        println sessionFactory.currentSession.toString()
        throw new RuntimeException()
    }

    def addBook(){
        Book book = new Book()
        book.isbn="isbn"
        book.name="name"
        Author a1 = new Author()
        a1.name="Author 1"
        a1.save()
        book.author= a1
        book.save(flush: true)
    }

    def updateBook(Book book) {
        println sessionFactory.currentSession.toString()
        println book.id +","+book.author
        book.name="new name"
        book.save(flush: true)
    }

    def checkTwoTransactions() {
        Author a = new Author()
        a.name="first Author"

        a.save()
        example2Service.serviceMethod()

        Author t = Author.findByName"second Author"
        println t?.getName()

        throw new RuntimeException("custom exception")
    }
}
