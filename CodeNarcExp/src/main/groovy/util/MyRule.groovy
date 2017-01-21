package util

import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractRule

/**
 * Created by Zohaib on 1/17/2017.
 */
class MyRule extends AbstractAstVisitor{

    public String getName() {
        return name
    }

    public void setName(String var1) {
        this.name = "MyRule"
    }



}
