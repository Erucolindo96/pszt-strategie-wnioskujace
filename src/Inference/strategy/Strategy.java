package Inference.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;

import java.util.ArrayList;

public abstract class Strategy {
    private int step;

    public Strategy() {
        step=0;
    }
    public Strategy(Strategy other)
    {
        step = other.step;
    }
    @Override
    public Object clone()
    {
        return null;
    }

    public abstract ArrayList<Clause> resolution(KnowledgeBase knowledgeBase, KnowledgeBase justification_set);

    public int getStep() {
        return step;
    }
    public void incrementStep() {
        ++step;
    }
    public void resetStep()
    {
        step = 0;
    }
}
