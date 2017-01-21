## CodeNarc Example

CodeNarc is static code Analyzer which can be used to analyse Groovy/Grails project. This is a simple example to demonstarte how
can we analyse the code via test case. A comprehensive ruleset is available and properties 
of rules can be modified according to project needs.

The test case will fail if number of violations exceed the pre-defined threshold.


## Some Important Rules
 1. IllegalPackageReference rule
 2. IllegalClassReference rule
 3. RequiredString rule
 4. DuplicateStringLiteral
 5. AbcMetric
 6. CyclomaticComplexity
 7. NestedBlockDepth
 8. CompareToWithoutComparable
 9. Implementation As Type
 10. Inverted If Else

## Run
Point the data pipeline variable dirName to the directory which you want to test.
Modify the report name and run the test. A .html report will be generated in the root folder.
Comment out the rules in ruleset.groovy files if you dont want to test against those rules.
Check out the code at github in case you want to write your own rules.

## Links
1. [CodeNarc website](http://codenarc.sourceforge.net/)
2. [Hidden gems of CodeNarc](https://tenpercentnotcrap.wordpress.com/2013/08/04/codenarc-hidden-gems-using-codenarcs-generic-rules/)
3. [GitHub CodeNarc](https://github.com/CodeNarc/CodeNarc)
