package codenarcexp

import grails.transaction.Transactional

/**
 * A sample service to test (StaticCodeAnalyzerSpec) some of the rules via static analysis of code.
 */
@Transactional
class SampleService {

    def serviceMethod() {

    }

    /**
     * This is a method to test for Static Code Analyzer. The method violates a lot of standard guidelines:
     * usage of println instead of some logging framework
     * unused parameter 'z'
     * duplicate number '2' instead of constant
     * broken oddness check (condition does not work for negative numbers) rule
     * All these violations should show up in the report when we run the test case
     */
    def printOddCoordinates(int x, int y, int z) {
        if(x % 2 == 1)
            println "x is odd."
        if(y % 2 == 1)
            println "y is odd."
    }
}
