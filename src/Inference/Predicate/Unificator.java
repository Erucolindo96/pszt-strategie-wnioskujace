package Inference.Predicate;

import Inference.Predicate.Terms.Term;

import java.util.ArrayList;

public class Unificator {
    private ArrayList<Term> oldTerms;
    private ArrayList<Term> newTerms;

    public Unificator() {
        oldTerms = new ArrayList<>();
        newTerms = new ArrayList<>();
    }
    public Unificator(Unificator other)
    {
        //TODO chyba nie dziala, bo obiekty musza byc klonowane
        throw new RuntimeException("TODO");
        //oldTerms = new ArrayList<>(other.oldTerms);
        //newTerms = new ArrayList<>(other.newTerms);
    }

    public void addPair(Term old, Term newOne) {
        oldTerms.add(old);
        newTerms.add(newOne);
    }

    public Term getNewValue(Term old) {
        int i = 0;
        for (Term term : oldTerms) {
            if (term.equals(old)) {
                return newTerms.get(i);
            }
            ++i;
        }
        return old;//no need to change term which isn't in unification vector
    }

    @Override
    public Object clone()
    {
        return new Unificator(this);
    }
    public boolean termIsInUnificator(Term other) {
        for (Term term : oldTerms) {
            if (term.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public void print() {
        int i = 0;
        for (Term term : oldTerms) {
            System.out.println(oldTerms.get(i));
            System.out.println(newTerms.get(i));
            ++i;
        }
    }
}
