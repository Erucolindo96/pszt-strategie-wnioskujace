package Inference.Predicate;

import Inference.Predicate.Terms.Term;

public class Literal {

    private Predicate predicate;
    private boolean negated;

    public Literal(Predicate p, boolean negation) {
        predicate = p;
        negated = negation;
    }

    public Literal(boolean negation, String name, String terms) {
        predicate = new Predicate(name, terms);
        negated = negation;
    }

    public Literal(Literal other)
    {
        predicate = new Predicate(other.predicate);
        negated = other.negated;
    }
    @Override
    public String toString() {
        if (negated) {
            return "-" + predicate.toString();
        } else {
            return predicate.toString();
        }
    }

    @Override
    public Object clone()
    {
        return new Literal(this);
    }
    public boolean isNegated() {
        return negated;
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

    public Literal unificate(Unificator unificator) {
        return new Literal(predicate.unificate(unificator), negated);
    }

    public Unificator createUnificator(Literal other) {
        return predicate.createUnificator(other.getPredicate());
    }

    public boolean isContradictory(Literal other) {//TODO niech ktoś mi powie dlaczego to na dole sie kompiluje negated is private
        if (this.negated == other.negated || !this.getName().equals(other.getName())
                || this.predicate.getTermsCount() != other.getPredicate().getTermsCount()) {
            return false;
        }
        if (this.predicate.getUnificated(other.predicate) == null) {
            return false;
        }
        return true;
    }

}
