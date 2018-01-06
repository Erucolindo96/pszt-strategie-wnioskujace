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
     * Step2:
     * Constradictions :
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
            setTestInFile("/home/erucolindo/Dokumenty/Projekty/Java/pszt-strategie-wnioskujace/ClausesFiles/clauses11.txt");
            wasSetUp = true;
        }
    }

    @Test
    public void step1Test2()
    {
        last = knowledge.getClauseCount() - 1;
        ArrayList<Clause> newClauses = justificationSetStrategy.resolution(knowledge, justificationSet);
        Assert.assertEquals("ZAWOD(CABACKI,ARCHI)  v ZAWOD(CABACKI,LEKARZ)", newClauses.get(0).toString());
        Assert.assertTrue(justificationSetStrategy.getStep() == 1);
        knowledge.addClause(newClauses);
        justificationSet.addClause(newClauses);
        Assert.assertFalse(knowledge.haveContradiction(last));
    }



}
