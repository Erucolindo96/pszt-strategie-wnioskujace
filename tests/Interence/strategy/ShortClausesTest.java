package Interence.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
import Inference.strategy.JustificationSetStrategy;
import Inference.strategy.ShortClausuleStrategy;
import Inference.strategy.Strategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ShortClausesTest {

    /**
     * Przebieg testu:
     * Step1:
     *  W(A), R(A), -R(x)
     *
     * Contradictions: yes
     *
     *
     */
    private static Strategy shortClauseStrategu;
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
        knowledge.addClause(knowledge.getThesis());
        shortClauseStrategu = new ShortClausuleStrategy();
    }
    @Before
    public void setUp()
    {
        if(!wasSetUp)
        {
            setTestInFile("/home/erucolindo/Dokumenty/Projekty/Java/pszt-strategie-wnioskujace/ClausesFiles/clausesTrue.txt");
            wasSetUp = true;
        }
    }

    @Test
    public void step1()
    {
        last = knowledge.getClauseCount() - 1;
        ArrayList<Clause> newClauses = shortClauseStrategu.resolution(knowledge, justificationSet);
        Assert.assertEquals("W(A)", newClauses.get(0).toString());
        Assert.assertEquals("R(A)", newClauses.get(1).toString());
        Assert.assertEquals("-R(x)", newClauses.get(2).toString());

        Assert.assertTrue(shortClauseStrategu.getStep() == 1);
        knowledge.addClause(newClauses);

        Assert.assertTrue(knowledge.haveContradiction(last));
    }

}
