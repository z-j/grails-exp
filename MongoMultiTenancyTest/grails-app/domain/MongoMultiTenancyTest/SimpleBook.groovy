package MongoMultiTenancyTest

class SimpleBook {

    String name
    static constraints = {
    }

    def String toString() {
        return "Book:"+id+","+version+","+name
    }
}
