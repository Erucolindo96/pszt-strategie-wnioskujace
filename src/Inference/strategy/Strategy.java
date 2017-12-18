package Inference.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;

import java.util.ArrayList;

public abstract class Strategy {
    protected ArrayList<Clause> newClauses;
    private int step;

    public Strategy() {
        step=0;
        //TODO Krzysiu what to do?
    }

    public abstract boolean resolution(KnowledgeBase knowledgeBase);

    public int getStep() {
        return step;
    }
    public void incrementStep() {
        ++step;
    }
}
