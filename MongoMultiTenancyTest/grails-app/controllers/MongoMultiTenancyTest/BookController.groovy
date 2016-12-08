package MongoMultiTenancyTest

class BookController {

    BookService bookService

    def saveBook() {
        bookService.saveBookWIthTenant()
        String books = bookService.getBooksWIthTenant()
        render books
    }

    def updateBook() {
        def id = Integer.valueOf(params.id)
        String book = bookService.updateBookWithTenant(id)
        render book
    }

    def saveSimpleBook() {
        bookService.saveBookWIthoutTenant()
        String books = bookService.getBooksWIthoutTenant()
        render books
    }
}
