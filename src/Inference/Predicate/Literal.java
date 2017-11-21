package Inference.Predicate;

import Inference.Predicate.Terms.Term;

public class Literal {

    private Predicate predicate;
    private boolean negated;

    //    public Literal()
//    {
//        predicate = new Predicate();
//        is_negated = false;
//    }
    public Literal(Predicate p, boolean negation) {
        predicate = p;
        negated = negation;
    }

    public Literal(boolean negation, String name, String terms) {
        predicate = new Predicate(name, terms);
        negated = negation;
    }

    @Override
    public String toString() {
        if (negated) {
            return "-" + predicate.toString();
        } else {
            return predicate.toString();
        }
    }

    public boolean isNegated() {
        return negated;
    }

    public Literal getUnificator(Predicate other) {
        throw new RuntimeException("TODO");
    }

    public String getName() {
        return predicate.getName();
    }

    public Term getTerm(int index) {
        return predicate.getTerm(index);
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public boolean canBeResolutatedWith(Literal other) {
        return (getName().equals(other.getName()) && isNegated() != other.isNegated());
    }
    public Literal unificate(Unificator unificator){
        return new Literal(predicate.unificate(unificator), negated);
    }


}
