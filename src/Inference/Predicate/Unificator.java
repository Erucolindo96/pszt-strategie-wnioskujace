package Inference.Predicate;

import Inference.Predicate.Terms.Function;
import Inference.Predicate.Terms.Term;

import java.util.ArrayList;

public class Unificator {
    boolean isUnificable = true;
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

//    public Unificator(Predicate p1, Predicate p2) {
//        oldTerms = new ArrayList<>();
//        newTerms = new ArrayList<>();
//    }


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

    public void resolveUnificatorConflicts(Unificator other) {
        for (int j = 0; j < newTerms.size(); ++j) {
            if (newTerms.get(j).isFunction()) {
                Function function = (Function) newTerms.get(j);
                for (int i = 0; i < function.getArgumentCount(); ++i) {
                    Term arg = function.getArgument(i);
                    if (other.getNewValue(arg) != arg) {
                        Term newVal=other.getNewValue(arg);
                        Function newFunction= new Function(function);
                        newFunction.swapArguments(arg, other.getNewValue(arg));
                        newTerms.set(j, newFunction);
                    }
                }
            }
        }
    }
//    private boolean create(final Predicate predicate, final Predicate otherPredicate) {
//        for (int i = 0; i < predicate.getTermsCount(); ++i) {
//            Term term = predicate.getTerm(i);
//            if (term.isFunction()) {
//                //if (!createdPairWithFunction(term, otherPredicate.getTerm(i))) ;
//                return false;
//            }
//            if (term.isConstant()) {
//                if (!createdPairWithConstant(term, otherPredicate.getTerm(i))) ;
//                return false;
//            }
//            if (term.isVariable()) {
//                //if (!createdPairWithVariable(term, otherPredicate.getTerm(i)))
//                    return false;
//            }
//        }
//        return true;
//    }
//
//    private boolean createdPairWithConstant(final Term term, final Term otherTerm) {
//        if (otherTerm.isFunction()) {
//            Function otherFunction=((Function) otherTerm);
//            if (otherFunction.getArgumentCount() > 0)
//                return false;
//           // if(otherFunction.getArgument(0).isFunction();
//        }
//        return true;
//    }

    public void print() {
        int i = 0;
        for (Term term : oldTerms) {
            System.out.println(oldTerms.get(i));
            System.out.println(newTerms.get(i));
            ++i;
        }
    }
}
