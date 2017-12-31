package Inference.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;

import java.util.ArrayList;

public class ShortClausuleStrategy extends Strategy
{
    public ShortClausuleStrategy()
    {
        super();
    }
    public ShortClausuleStrategy(ShortClausuleStrategy other)
    {
        super(other);
    }

    @Override
    public Object clone()
    {
        return new ShortClausuleStrategy(this);
    }

    @Override
    public ArrayList<Clause> resolution(KnowledgeBase knowledgeBase, KnowledgeBase justification_set)
    {
        //musimy wygenerowac klauzule jak najkrotsze
        //i tylko o tej najkrotszej dlugosci
        int min_length = knowledgeBase.getMaxLengthOfClause();


        return null;

    }
}
