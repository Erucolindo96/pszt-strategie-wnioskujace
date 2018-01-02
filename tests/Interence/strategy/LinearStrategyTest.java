package Interence.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
import Inference.strategy.LinearStrategy;
import Inference.strategy.Strategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class LinearStrategyTest {
    private static KnowledgeBase base = new KnowledgeBase();
    private static Strategy strategy = new LinearStrategy();
    private static boolean setUpIsDone = false;


    //String clauses_string[3];
    Clause antithesis = new Clause();
    String antithesis_str = "C(A)";


    @Before
    public void initialize() throws IOException {
        if (!setUpIsDone) {
            base.loadFromFile("clausesTrue.txt");
            antithesis.parseString(antithesis_str);
            setUpIsDone = true;
        }
    }


    @Test
    public void step1() throws IOException {

        Assert.assertEquals(0, strategy.getStep());
        ArrayList<Clause> newClauses = strategy.resolution(base, null);
        Assert.assertNotNull(newClauses);
        Assert.assertEquals("[-C(x) v -O(A)]", newClauses.toString());
        Assert.assertEquals(1, strategy.getStep());
        base.addClause(newClauses);
/*
        newClauses = strategy.resolution(base, null);
        Assert.assertEquals("[-O(A)]", newClauses.toString());
        Assert.assertEquals(2, strategy.getStep());
        base.addClause(newClauses);

        Assert.assertNull(strategy.resolution(base, null));
        Assert.assertEquals(2, strategy.getStep());
  */
    }

    @Test
    public void haveContradictionsAfterStep1() {
        Assert.assertFalse(base.haveContradiction(base.getClauseCount() - 1));

    }

    @Test
    public void step2() {
        ArrayList<Clause> newClauses = strategy.resolution(base, null);
        Assert.assertNotNull(newClauses);
        Assert.assertEquals("[-R(x)]", newClauses.toString());
        Assert.assertEquals(2, strategy.getStep());
        base.addClause(newClauses);
    }

    @Test
    public void haveContradictionsAfterStep2() {
        Assert.assertFalse(base.haveContradiction(base.getClauseCount() - 1));
    }



    @Test
    public void step3() {
        Assert.assertNull(strategy.resolution(base, null));
        Assert.assertEquals(2, strategy.getStep());
    }
}
