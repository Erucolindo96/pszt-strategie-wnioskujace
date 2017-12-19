package Interence.Predicate;
import Inference.Predicate.Clause;
import Inference.Predicate.Predicate;
import Inference.Predicate.Terms.Constant;
import Inference.Predicate.Terms.Function;
import Inference.Predicate.Terms.Variable;
import Inference.Predicate.Unificator;
import org.junit.Assert;
import org.junit.Test;

public class UnificationTests {

    @Test
    public void unificationOfSimplePredicate() {
        String string="x,y";
        Predicate p =new Predicate("P",string);
        Assert.assertEquals("P(x,y)", p.toString());
        Unificator unificator=new Unificator();
        unificator.addPair(new Variable("x"), new Constant("Y"));


        Assert.assertEquals("P(Y,y)", p.unificate(unificator).toString());
    }

    @Test
    public void unificationOfVarAndFunction() {
        String string = "x,y";
        Predicate p = new Predicate("P", string);
        Assert.assertEquals("P(x,y)", p.toString());
        Unificator unificator = new Unificator();
        unificator.addPair(new Variable("x"), new Function("F", new Variable("y")));

        Assert.assertEquals("P(F(y),y)", p.unificate(unificator).toString());
    }

}
