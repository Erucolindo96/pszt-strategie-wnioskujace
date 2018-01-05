package Inference.strategy;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShortClausuleStrategy extends Strategy
{

    private ArrayList<Pair<Clause, Clause> > getSortedPairsOfClauses(KnowledgeBase base)
    {
        ArrayList<Pair<Clause, Clause> > clauses_pairs = new ArrayList<>();
        //dodajemy pary klazul z bazy wiedzy - laczymy kazda z kazda i dokladamy
        Pair<Clause, Clause> pair_of_clauses;
        for(int i=0; i<base.getClauseCount(); ++i)
        {
            Clause i_clause = base.getClause(i);
            for(int j=i;j < base.getClauseCount(); ++j)
            {
                Clause j_clause = base.getClause(j);
                clauses_pairs.add(new Pair<>(i_clause, j_clause));
            }
        }
        //sortujemy elementy stosujac komparator, sprawdzajacy jaka dlugosc mialaby klauzula po ewentualnej rezolucji
        Collections.sort(clauses_pairs, new Comparator<Pair<Clause, Clause>>() {
            @Override
            public int compare(Pair<Clause, Clause> first, Pair<Clause, Clause> second) {
                return first.getKey().getCountAfterResolution(first.getValue()) - second.getKey().getCountAfterResolution(second.getValue());
            }
        });
        return clauses_pairs;
    }
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
        ArrayList<Pair<Clause, Clause> > clauses_pairs = getSortedPairsOfClauses(knowledgeBase);
        Clause clause;
        ArrayList<Clause> newClauses = new ArrayList<>();
        int min_length = -1;//oznaczza ze jeszcze nie znaleziono zadnej klauzuli
        //a jak juz wszystko posortowane to probujemy robić rezolucje dla kazdej pary i bierzemy z nich wszystkich klauzule najbardziej
        for(int i = 0; i < clauses_pairs.size();  ++i)
        {
            clause = clauses_pairs.get(i).getKey().getResolution(clauses_pairs.get(i).getValue());
            if(clause != null && !newClauses.contains(clause) && !knowledgeBase.haveThisClause(clause))
            {
                if(min_length == -1)
                    min_length = clause.getCount();
                if(min_length == clause.getCount() ) // jeśli analizowana kluazula nie jest dluzsza niz najmniejsza jaka znalezlismy, to dodaj do newClauses
                    newClauses.add(clause);
                else if( clause.getCount() > min_length) //przerwij petle, bo juz wiekszych nie bedzie
                    break;
            }
        }
        return newClauses.size()!=0?newClauses:null;
    }
}
