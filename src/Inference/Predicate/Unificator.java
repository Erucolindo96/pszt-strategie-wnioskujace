package Inference.Predicate;

import Inference.Predicate.Terms.Function;
import Inference.Predicate.Terms.Term;

import java.util.ArrayList;

public class Unificator {
    private ArrayList<Term> oldTerms;
    private ArrayList<Term> newTerms;


    public Unificator() {
        oldTerms = new ArrayList<>();
        newTerms = new ArrayList<>();
    }

    public Unificator(Unificator other) {
        oldTerms = new ArrayList<>();
        newTerms = new ArrayList<>();
        for (Term t : other.oldTerms) {
            oldTerms.add((Term) t.clone());
        }
        for (Term t : other.newTerms) {
            newTerms.add((Term) t.clone());
        }
    }

    @Override
    public boolean equals(Object other) {
        Unificator u;
        if (other instanceof Unificator) {
            u = (Unificator) other;
            return this.oldTerms.equals(u.oldTerms) && this.newTerms.equals(u.newTerms);
        }
        return false;
    }

    public void addPair(Term old, Term newOne) {
        oldTerms.add(old);
        newTerms.add(newOne);
    }

    /**
     * aby ustawić 'wezszy' unifikator gdy term się powtarza
     */
    public void setNarrowerValue(Term old, Term narrower) {
        int i = 0;
        for (Term term : oldTerms) {
            if (term.equals(old)) {
                newTerms.set(i, narrower);
            }
            ++i;
        }
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
    public Object clone() {
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

    /**if term after unifiction is an function from other Clause, check if it's argument wasn't changed by other unificator*/
    public void resolveUnificatorConflicts(Unificator other) {
        for (int j = 0; j < newTerms.size(); ++j) {
            if (newTerms.get(j).isFunction()) {
                Function function = (Function) newTerms.get(j);
                for (int i = 0; i < function.getArgumentCount(); ++i) {
                    Term arg = function.getArgument(i);
                    if (other.getNewValue(arg) != arg) {
                        Function newFunction= new Function(function);
                        newFunction.swapArguments(arg, other.getNewValue(arg));
                        newTerms.set(j, newFunction);
                    }
                }
            }
        }
    }
    //TODo not sure if necessary
    public void changeVariablesName(String old, String newName){

    }
}
