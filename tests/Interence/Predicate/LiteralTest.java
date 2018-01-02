package Interence.Predicate;

import Inference.Predicate.Clause;

import org.junit.Assert;
import org.junit.Test;


public class LiteralTest {

    @Test
    public void createLiteral() {
        String string = new String("Q(x)");
        Clause clause = new Clause(string);
        Assert.assertFalse(clause.getLiteral(0).isNegated());
    }

    @Test
    public void createNagetedLiteral() {
        String string = new String("-Q(x)");
        Clause clause = new Clause(string);
        Assert.assertTrue(clause.getLiteral(0).isNegated());
    }


}
