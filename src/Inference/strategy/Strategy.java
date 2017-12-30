package Inference.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;

import java.util.ArrayList;

public abstract class Strategy {
   // protected ArrayList<Clause> newClauses;
    private int step;

    public Strategy() {
        step=0;
        //TODO Krzysiu what to do?
        //TODO Po co tam byly pola statyczne, a nie lokalne?
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
}
