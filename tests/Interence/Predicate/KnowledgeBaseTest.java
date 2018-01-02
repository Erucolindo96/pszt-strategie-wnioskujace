package Interence.Predicate;

import Inference.Predicate.KnowledgeBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class KnowledgeBaseTest {
    @Test
    public void loadFromFile() throws IOException {
        String string = "clausesTrue.txt";
        KnowledgeBase base =new KnowledgeBase();
        base.loadFromFile(string);
        Assert.assertEquals("C(A)",base.getClause(1).getLiteral(0).toString());
        Assert.assertEquals("-C(x)",base.getClause(0).getLiteral(0).toString());
        Assert.assertEquals("R(x)",base.getClause(0).getLiteral(1).toString());
        Assert.assertEquals("-R(x)",base.getClause(3).getLiteral(1).toString());
    }

}
