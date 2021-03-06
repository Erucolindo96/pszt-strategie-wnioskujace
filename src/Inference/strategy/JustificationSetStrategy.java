package Inference.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;

import java.util.ArrayList;

public class JustificationSetStrategy extends Strategy
{

    public JustificationSetStrategy()
    {
        super();
    }
    public JustificationSetStrategy(JustificationSetStrategy other)
    {
        super(other);
    }
    @Override
    public Object clone()
    {
        return new JustificationSetStrategy(this);
    }

    @Override
    public ArrayList<Clause> resolution(KnowledgeBase knowledgeBase, KnowledgeBase justification_set) {
        if(justification_set == null)
            throw new RuntimeException("Brakuje zbioru uzasadnien w strategii zbioru uzasadnien");

        ArrayList<Clause> newClauses = new ArrayList<>();
        //KnowledgeBase difference = knowledgeBase.getDifference(justification_set);
        for(int i=0; i< justification_set.getClauseCount(); ++i)
        {
            for(int j=0; j< knowledgeBase.getClauseCount();++j)
            {
                Clause new_clause = justification_set.getClause(i).getResolution(knowledgeBase.getClause(j)); //probuje zrobic rezolucje
                if(new_clause != null && !knowledgeBase.haveThisClause(new_clause) && !newClauses.contains(new_clause)) //rezolucja sie udala
                {
                    newClauses.add(new_clause);
                }
            }
        }
        incrementStep();
        if(newClauses.size() == 0)
            return null;
        else
            return newClauses;
    }
}
