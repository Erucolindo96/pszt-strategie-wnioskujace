package Interence.strategy;
import Inference.Predicate.KnowledgeBase;
import Inference.strategy.*;
import Inference.*;
import  Inference.Predicate.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class JustificationSetStrategyTest {

    private static Strategy justificationSetStrategy;
    private static KnowledgeBase knowledge, justificationSet;
    private static int last = 0;
    private static boolean wasSetUp = false;
    /**
     * Poprawne kroki działania strategii:
     * antyteza : -O(A) v -R(x)
     * Step1: -R(x), -C(x)v-O(A)
     * Contradictions: no
     * Step2:-C(x), -O(A),
     * Constradictions : yes (-C(x) oraz C(A) )
     * */

    @Before
    public void setUp()
    {
        if(!wasSetUp)
        {
            knowledge = new KnowledgeBase();
            try {
                knowledge.loadFromFile("/home/erucolindo/Dokumenty/Projekty/Java/pszt-strategie-wnioskujace/ClausesFiles/clausesTrue.txt");
            } catch (Throwable e) {
                Assert.fail("Dupa, nie wczytuja sie klauzule");
            }
            knowledge.addClause(knowledge.getAntithesis());
            justificationSet = new KnowledgeBase();
            justificationSet.addClause(knowledge.getAntithesis());
            //zb uzasadnien zawiera antyteze, baza wiedzy wszystko, w tym antyteze
            justificationSetStrategy = new JustificationSetStrategy();
            wasSetUp = true;
        }
    }

    @Test
    public void step1()
    {
        last = knowledge.getClauseCount() - 1;
        ArrayList<Clause> newClauses = justificationSetStrategy.resolution(knowledge, justificationSet);
        Assert.assertEquals("-O(A) v -C(x)", newClauses.get(0).toString());
        Assert.assertEquals("-R(x)", newClauses.get(1).toString() );
        Assert.assertTrue(justificationSetStrategy.getStep() == 1);
        knowledge.addClause(newClauses);
        justificationSet.addClause(newClauses);
        Assert.assertTrue(knowledge.getClauseCount() == 7);
    }

    @Test
    public void haveContradictions1()
    {
        Assert.assertTrue(knowledge.getClauseCount() == 7);
        Assert.assertFalse(knowledge.haveContradiction(last));
    }

    @Test
    public void step2()
    {
        last = knowledge.getClauseCount() - 1;
        ArrayList<Clause> newClauses = justificationSetStrategy.resolution(knowledge, justificationSet);
        Assert.assertEquals("-O(A)", newClauses.get(0).toString() );
        Assert.assertEquals("-C(x)", newClauses.get(1).toString() );
        Assert.assertTrue(justificationSetStrategy.getStep() == 2);
        knowledge.addClause(newClauses);
        justificationSet.addClause(newClauses);
    }
    @Test
    public void haveContradictions2()
    {
        Assert.assertTrue(knowledge.haveContradiction(last));
    }



}
