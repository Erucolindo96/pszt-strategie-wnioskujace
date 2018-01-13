package Inference.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;

import java.util.ArrayList;

public class LinearStrategy extends Strategy {

    public LinearStrategy() {
        super();
    }
    public LinearStrategy(LinearStrategy other)
    {
        super(other);
    }
    @Override
    public Object clone()
    {
        return new LinearStrategy(this);
    }

    /**
     * returns null when unsuccessful
     */
    @Override
    public ArrayList<Clause> resolution(KnowledgeBase knowledgeBase, KnowledgeBase justification_set) {
        Clause newClause;
        ArrayList newClausesList = new ArrayList();
        int last = knowledgeBase.getClauseCount() - 1;
        for (int i = 0; i < last; ++i) {
            newClause = knowledgeBase.getClause(i).getResolution(knowledgeBase.getClause(last));
            if (newClause != null) {
                if(knowledgeBase.haveThisClause(newClause)){//to avoid loop
                    continue;
                }
                incrementStep();
                newClausesList.add(newClause);
                return newClausesList;
            }
        }
        return null;
    }
}
