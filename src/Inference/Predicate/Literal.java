package Inference.Predicate;

import Inference.Predicate.Terms.Term;

public class Literal {

    private Predicate predicate;
    private boolean is_negated;

    //    public Literal()
//    {
//        predicate = new Predicate();
//        is_negated = false;
//    }
    public Literal(Predicate p, boolean negation) {
        predicate = p;
        is_negated = negation;
    }

    public Literal(boolean negation, String name, String terms) {
        predicate = new Predicate(name, terms);
        is_negated = negation;
    }

    @Override
    public String toString() {
        if (is_negated) {
            return "-"+predicate.toString();
        }else {
            return predicate.toString();
        }
    }

    public Literal getUnificator(Predicate other) {
        throw new RuntimeException("TODO");
    }

    public Term getTerm(int index){
        return predicate.getTerm(index);
    }


}
