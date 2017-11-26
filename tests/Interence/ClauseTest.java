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
        String string = "P(x,y) v Q(X,Y)";
        Clause clause = new Clause(string);
        Assert.assertNotNull(clause);
        Assert.assertEquals("P(x,y)", clause.getLiteral(0).toString());
        Assert.assertEquals(new Variable("x"), clause.getLiteral(0).getTerm(0));
        Assert.assertEquals(new Variable("y"), clause.getLiteral(0).getTerm(1));
    }

    @Test
    public void createClauseWithConstsFromString() {
        String string = "P(x,y) v Q(X,Y)";
        Clause clause = new Clause(string);
        Assert.assertEquals("Q(X,Y)", clause.getLiteral(1).toString());
        Assert.assertEquals(new Constant("X"), clause.getLiteral(1).getTerm(0));
        Assert.assertEquals(new Constant("Y"), clause.getLiteral(1).getTerm(1));
    }

    @Test
    public void createClauseWithFunction() {
        String string = "Q(F(X),Y)";
        Clause clause = new Clause(string);
        Assert.assertEquals("Q(F(X),Y)", clause.toString());
        Assert.assertEquals(new Function("F", new Constant("X")), clause.getLiteral(0).getTerm(0));
        Assert.assertEquals(new Constant("Y"), clause.getLiteral(0).getTerm(1));
    }

    @Test
    public void createClauseWithFunction1() {//NIE DZIALA FUNKCJA Z WIELOMA ARGUMENTAMI I FUNKCJA W FINKCJI
        String string = "P(F(x,y)) v Q(F(X),Y)";
        Clause clause = new Clause(string);
        Assert.assertEquals("P(F(x,y))", clause.getLiteral(0).toString());
        Assert.assertEquals("Q(F(X),Y)", clause.getLiteral(1).toString());
        Assert.assertEquals(new Function("F", new Constant("X")), clause.getLiteral(1).getTerm(0));
        Assert.assertEquals(new Constant("Y"), ((Function) clause.getLiteral(1).getTerm(1)).getArgument(0));
    }

    @Test
    public void mergeClauseWithOther() {
        String string = "P(x) v Q(x,Y)";
        Clause clause = new Clause(string);
        String string1 = "P(y) v -Q(X,y)";
        Clause clause1 = new Clause(string1);
        Clause clause2 = clause.getResolution(clause1);
        Assert.assertEquals("P(X) v P(Y)", clause2.toString());
    }

    @Test
    public void mergeClauseWithOther1() {
        String string = "P(f(x)) v Q(x,Y)";
        Clause clause = new Clause(string);
        String string1 = "P(y) v -Q(Z,y)";
        Clause clause1 = new Clause(string1);
        Clause clause2 = clause.getResolution(clause1);
        Assert.assertEquals("P(f(Z)) v P(Y)", clause2.toString());
    }

    @Test
    public void mergeClauseWithOther2() {
        String string = "P(f(x)) v Q(x,Y)";
        Clause clause = new Clause(string);
        String string1 = "P(y) v -Q(Z,y) v Z(c)";
        Clause clause1 = new Clause(string1);
        Clause clause2 = clause.getResolution(clause1);
        Assert.assertNull(clause2);
    }

    @Test
    public void mergeClauseWithOther3() {
        String string = "P(f(x)) v Q(x,F(Y))";
        Clause clause = new Clause(string);
        String string1 = "R(y) v -Q(Z,y)";
        Clause clause1 = new Clause(string1);
        Clause clause2 = clause.getResolution(clause1);
        Assert.assertEquals("P(f(Z)) v R(F(Y))", clause2.toString());

    }

    @Test
    public void mergeClauseWithOther4() {
        String string = "P(y, F(y))";
        Clause clause = new Clause(string);
        String string1 = "Q(x, x)";
        Clause clause1 = new Clause(string1);
        Clause clause2 = clause.getResolution(clause1);
        Assert.assertNull(clause2);
    }
}
