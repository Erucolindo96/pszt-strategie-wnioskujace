package Interence;

import Inference.InferenceMachine;
import Inference.InferenceProduct;
import Inference.Predicate.KnowledgeBase;
import Inference.strategy.LinearStrategy;
import Inference.strategy.Strategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MachineTest {
    private InferenceMachine machine = new InferenceMachine();
    private KnowledgeBase base = new KnowledgeBase();
    private Strategy strategy = new LinearStrategy();
    private static boolean setUpIsDone = false;


    @Before
    public void initialize() throws IOException {
        if (!setUpIsDone) {
            base.loadFromFile("clauses1.txt");
            machine = new InferenceMachine(base, strategy);
            setUpIsDone = true;
        }
    }

    @Test
    public void resolute() throws IOException {
        Assert.assertEquals(InferenceProduct.TRUE, machine.inference(null));
    }
}
