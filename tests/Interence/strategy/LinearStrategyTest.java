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
    private KnowledgeBase base = new KnowledgeBase();
    private Strategy strategy = new LinearStrategy();
    private static boolean setUpIsDone = false;


    @Before
    public void initialize() throws IOException {
        if (!setUpIsDone) {

            base.loadFromFile("clauses1.txt");
            setUpIsDone = true;
        }
    }

    @Test
    public void step1() throws IOException {

        Assert.assertEquals(0, strategy.getStep());
        ArrayList<Clause> newClauses = strategy.resolution(base);
        Assert.assertEquals("[-C(x) v -O(A)]", newClauses.toString());
        Assert.assertEquals(1, strategy.getStep());
        base.addClause(newClauses);

        newClauses = strategy.resolution(base);
        Assert.assertEquals("[-O(A)]", newClauses.toString());
        Assert.assertEquals(2, strategy.getStep());
        base.addClause(newClauses);

        Assert.assertNull(strategy.resolution(base));
        Assert.assertEquals(2, strategy.getStep());
    }
}
