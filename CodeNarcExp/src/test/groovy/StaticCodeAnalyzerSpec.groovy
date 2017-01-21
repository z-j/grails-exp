import spock.lang.Specification

/**
 * Created by Zohaib on 1/17/2017.
 */


/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */

class StaticCodeAnalyzerSpec extends Specification {

    String GROOVY_FILES = '**/*.groovy'
    String RULESET_FILES = "file:grails-app/conf/ruleset.groovy"

    def setup() {
    }

    def cleanup() {
    }

    void "test coding guidelines using codeNarc"(){

        setup: "Initialize the AntBuilder"
        AntBuilder ant = new AntBuilder()

        ant.taskdef(name:'codenarc', classname:'org.codenarc.ant.CodeNarcTask')
        ant.codenarc(ruleSetFiles:RULESET_FILES, maxPriority1Violations:1, maxPriority2Violations:100) {
            fileset(dir: dirName) {
                include(name: GROOVY_FILES)
            }
            report(type: 'html', title:reportName, toFile: reportName)
        }

        where:
        dirName | reportName
        'grails-app/services' | 'report.html'
    }



}
