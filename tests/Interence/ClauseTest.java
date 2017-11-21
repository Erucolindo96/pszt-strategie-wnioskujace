package Interence;

import Inference.Predicate.Clause;
import Inference.Predicate.Terms.Constant;
import Inference.Predicate.Terms.Function;
import Inference.Predicate.Terms.Variable;
import org.junit.Assert;
import org.junit.Test;



public class ClauseTest {

    @Test
    public void createClauseWithVariableFromString() {
        String string="P(x,y) v Q(X,Y)";
        Clause clause=new Clause(string);
        Assert.assertNotNull(clause);
        Assert.assertEquals("P(x,y)", clause.getLiteral(0).toString());
        Assert.assertEquals(new Variable("x"), clause.getLiteral(0).getTerm(0));
        Assert.assertEquals(new Variable("y"), clause.getLiteral(0).getTerm(1));
    }
    @Test
    public void createClauseWithConstsFromString() {
        String string="P(x,y) v Q(X,Y)";
        Clause clause=new Clause(string);
        Assert.assertEquals("Q(X,Y)", clause.getLiteral(1).toString());
        Assert.assertEquals(new Constant("X"), clause.getLiteral(1).getTerm(0));
        Assert.assertEquals(new Constant("Y"), clause.getLiteral(1).getTerm(1));
    }
    @Test
    public void createClauseWithFunction() {
        String string="P(F(x,y)) v Q(F(X),Y)";
        Clause clause=new Clause(string);
        Assert.assertEquals("P(F(x,y))", clause.getLiteral(0).toString());
        Assert.assertEquals("Q(F(X),Y)", clause.getLiteral(1).toString());
        Assert.assertEquals(new Function("X"), clause.getLiteral(1).getTerm(0));
        Assert.assertEquals(new Constant("Y"), ((Function)clause.getLiteral(1).getTerm(1)).getArgument(0));
    }
}
