package Inference.Predicate;

import Inference.Predicate.Terms.Term;

import java.util.ArrayList;

public class Unificator {
    private ArrayList<Term> oldTerms;
    private ArrayList<Term> newTerms;

    public Unificator() {
        oldTerms=new ArrayList<>();
        newTerms=new ArrayList<>();
    }

    public void addPair(Term old, Term newOne){
        oldTerms.add(old);
        newTerms.add(newOne);
    }
    public Term getNewValue(Term old){
        int i=0;
        for (Term term:oldTerms) {
            if(term.equals(old)){
                return newTerms.get(i);
            }
            ++i;
        }
        return old;//no need to change term which isn't in unification vector
    }
}
