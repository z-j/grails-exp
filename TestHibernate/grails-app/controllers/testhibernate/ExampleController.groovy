package testhibernate

class ExampleController {

    def exampleService

    def index() {
        try{
            exampleService.serviceMethod()
        } catch(Exception e){
            println e.getMessage()
        }
        render "check logs"
    }

    def index1() {
        Book book = Book.read(3)
        if(book) {
            try{
                exampleService.serviceMethod1(book)
            } catch(Exception e){
                println e.getMessage()
            }

            try{
                exampleService.updateBook(book)
            } catch(Exception e){
                println e.getMessage()
            }
        }
        render "check logs"
    }

    def index2() {
        try{
            exampleService.checkTwoTransactions()
        } catch(Exception e){
            println e.getMessage()
        }
        render "check logs"
    }
}
