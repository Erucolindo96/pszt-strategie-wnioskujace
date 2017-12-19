package Inference.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;

import java.util.ArrayList;

public class LinearStrategy extends Strategy {

    public LinearStrategy() {
        super();
    }

    /**
     * returns null when unsuccessful
     */
    @Override
    public ArrayList<Clause> resolution(KnowledgeBase knowledgeBase) {
        Clause newClause;
        ArrayList newClausesList = new ArrayList();
        int last = knowledgeBase.getClauseCount() - 1;
        for (int i = 0; i < last; ++i) {
            newClause = knowledgeBase.getClause(i).getResolution(knowledgeBase.getClause(last));
            if (newClause != null) {
                incrementStep();
                super.newClauses = new ArrayList<>();
                super.newClauses.add(newClause);
                return super.newClauses;
            }
        }
        return null;
    }
}
