##Problem: Saving objects in mongodb using Multi Tenancy approach at the Database level. 
Objects correctly gets saved in the databases respective to the tenant id.
However, version column in not saved/updated for the domain classes which implements MongoEntity<Book>, MultiTenant<Book>.

This means that optimistic lock exception is thrown when same object is saved after updating some value. Probably because comparison on
version column fails as version value is null.

##To reproduce the scanario:

Two domain classes, Book and SimpleBook.


**Book implements MongoEntity<Book>, MultiTenant<Book>**


**SimpleBook does NOT (implements MongoEntity<Book>, MultiTenant<Book>**)

Run the project and Hit the url /book/saveBookWIthTenant , a new book will be saved but 
version column will be null. Then try to update via: /book/updateBookWithTenant?id=1 , 
Optimistic lock exception will be thrown

However, when saving simpleBook using: /book/saveBookWIthoutTenant , version column is saved as expected. But SimpleBook will
not be saved in tenant database but will be saved in default database (again as expected)

##Workaround
optimistic locking can be disable for domain class:
static mapping = {
        version false
    }
