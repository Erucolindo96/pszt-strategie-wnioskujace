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

    public Literal(Literal other) {
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
    public Object clone() {
        return new Literal(this);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Literal) {
            Literal l = (Literal) other;
            if (this.predicate.equals(l.predicate) && this.negated == l.negated)
                return true;
        }
        return false;
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

    public int getTermsCount() {
        return predicate.getTermsCount();
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void negate() {
        negated = !negated;
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

    public boolean isContradictory(Literal other) {//TODO niech ktoś mi powie dlaczego to na dole sie kompiluje negated is private - przeciez negated jest prywatne ale w literale :p
        if (this.negated == other.negated || !this.getName().equals(other.getName())
                || this.predicate.getTermsCount() != other.getPredicate().getTermsCount()) {
            return false;
        }
        if (this.predicate.createUnificator(other.predicate) == null || other.predicate.createUnificator(this.predicate) == null) {
            return false;
        }
        return true;
    }
    public void changeTermsName(String oldName, String newName) {
            for (int i = 0; i < getTermsCount(); ++i) {
                getTerm(i).changeTermName(oldName, newName);
            }
    }
    public boolean containsVariableWithGivenName(String name){
        for (int i = 0; i < getTermsCount(); ++i) {
            if (getTerm(i).isOrContainsVariableWithGiven(name))
                return true;
        }
        return false;
    }
}
