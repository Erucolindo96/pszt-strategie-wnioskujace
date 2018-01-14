package Interence.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
import Inference.strategy.JustificationSetStrategy;
import Inference.strategy.Strategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class JustificationSetStrategyTest2 {
    /**
     * Test drugi
     * Poprawne kroki działania strategii:
     * antyteza : -ZAWOD(CABACKI,INF)
     * Step1: ZAWOD(CABACKI,ARCHI)vZAWOD(CABACKI,INF)vZAWOD(CABACKI,LEKARZ),
     * Contradictions: no
     *
     * Step2:
     -ZAWOD(x5,ARCHI)vTS(CABACKI,x5)vZAWOD(CABACKI,LEKARZ)
     -ZAWOD(CABACKI,x9)vTS(CABACKI,ARCHI)vZAWOD(CABACKI,LEKARZ)
     * Constradictions : no
     *
     * Step3:
     * -ZAWOD(x5,ARCHI)vTS(CABACKI,x5)v-ZAWOD(CABACKI,x9)vTS(CABACKI,LEKARZ)
       -ZAWOD(CABACKI,x9)vZAWOD(CABACKI,LEKARZ)vTS(ARHI,CABACKI)
       ZAWOD(CABACKI,INF)vZAWOD(CABACKI,LEKARZ)vTS(CABACKI,ARCHI)vZAWOD(CABACKI,LEKARZ)//moze dublujace sie usunie
      -ZAWOD(CABACKI,x9)vTS(CABACKI,ARCHI)v-ZAWOD(x5,LEKARZ)vTS(CABACKI,x5)
      -ZAWOD(CABACKI,x9)vTS(CABACKI,LEKARZ)v-ZAWOD(CABACKI,x9)vTS(CABACKI,ARCHI)

     * Contradictions:no
     */

    private static Strategy justificationSetStrategy;
    private static KnowledgeBase knowledge, justificationSet;
    private static int last = 0;
    private static boolean wasSetUp = false;


    private static void setTestInFile(String file_path)
    {
        knowledge = new KnowledgeBase();
        try {
            knowledge.loadFromFile(file_path);
        } catch (Throwable e) {
            Assert.fail("Dupa, nie wczytuja sie klauzule");
        }
        knowledge.addClause(knowledge.getAntithesis());
        justificationSet = new KnowledgeBase();
        justificationSet.addClause(knowledge.getAntithesis());
        //zb uzasadnien zawiera antyteze, baza wiedzy wszystko, w tym antyteze
        justificationSetStrategy = new JustificationSetStrategy();
    }
    @Before
    public void setUp()
    {
        if(!wasSetUp)
        {
            setTestInFile(".\\ClausesFiles\\clauses11.txt");
            wasSetUp = true;
        }
    }

    @Test
    public void step1()
    {
        last = knowledge.getClauseCount() - 1;
        ArrayList<Clause> newClauses = justificationSetStrategy.resolution(knowledge, justificationSet);
        Assert.assertEquals("ZAWOD(CABACKI,ARCHI) v ZAWOD(CABACKI,LEKARZ)", newClauses.get(0).toString());
        Assert.assertTrue(justificationSetStrategy.getStep() == 1);
        knowledge.addClause(newClauses);
        justificationSet.addClause(newClauses);
        Assert.assertFalse(knowledge.haveContradiction(last));
    }

    @Test
    public void step2()
    {
        last = knowledge.getClauseCount() - 1;
        ArrayList<Clause> newClauses = justificationSetStrategy.resolution(knowledge, justificationSet);
        Assert.assertEquals("ZAWOD(CABACKI,LEKARZ) v -ZAWOD(x5,ARCHI) v TS(CABACKI,x5)", newClauses.get(0).toString());
        Assert.assertEquals("ZAWOD(CABACKI,LEKARZ) v -ZAWOD(CABACKI,x9) v TS(CABACKI,ARCHI)", newClauses.get(1).toString());
        Assert.assertTrue(justificationSetStrategy.getStep() == 2);
        knowledge.addClause(newClauses);
        justificationSet.addClause(newClauses);
        Assert.assertFalse(knowledge.haveContradiction(last));
    }

    @Test
    public void step3()
    {
        last = knowledge.getClauseCount() - 1;
        ArrayList<Clause> newClauses = justificationSetStrategy.resolution(knowledge, justificationSet);
        Assert.assertEquals("-ZAWOD(x5,ARCHI)vTS(CABACKI,x5)v-ZAWOD(CABACKI,x9)vTS(CABACKI,LEKARZ)", newClauses.get(0).toString());
        Assert.assertEquals("-ZAWOD(CABACKI,x9)vZAWOD(CABACKI,LEKARZ)vTS(ARHI,CABACKI)", newClauses.get(1).toString());
        Assert.assertEquals("ZAWOD(CABACKI,INF)vZAWOD(CABACKI,LEKARZ)vTS(CABACKI,ARCHI)vZAWOD(CABACKI,LEKARZ)", newClauses.get(2).toString());
        Assert.assertEquals("-ZAWOD(CABACKI,x9)vTS(CABACKI,ARCHI)v-ZAWOD(x5,LEKARZ)vTS(CABACKI,x5)", newClauses.get(3).toString());
        Assert.assertEquals("-ZAWOD(CABACKI,x9)vTS(CABACKI,LEKARZ)v-ZAWOD(CABACKI,x9)vTS(CABACKI,ARCHI)", newClauses.get(4).toString());

        Assert.assertTrue(justificationSetStrategy.getStep() == 3);
        knowledge.addClause(newClauses);
        justificationSet.addClause(newClauses);
        Assert.assertFalse(knowledge.haveContradiction(last));
    }
}
