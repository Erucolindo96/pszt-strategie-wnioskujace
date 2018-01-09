package Inference.Predicate.Terms;

import Inference.Predicate.Unificator;

/**
 * @author erucolindo
 * <p>
 * Term będący stałą.
 * Musi składać się z samych wielkich liter(czemu służy metoda checkSyntax - rzuca wyjątek jeśli term składa się z innych liter)
 */
public class Constant extends Term {
    private void checkSyntax()
    {
        String name = term_name.toUpperCase();
        if (!name.equals(term_name))
            throw new RuntimeException("Stale musza byc zlozone z duzych liter.");
    }

    public Constant() {
        super();
    }

    public Constant(String const_name) {
        super(const_name);
        checkSyntax();
    }

    public Constant(Constant other) {
        super(other);
    }

    @Override
    public boolean equals(Object other) {
        Constant o;
        if (other instanceof Constant) {
            o = (Constant) other;
            if (o.term_name.equals(this.term_name))
                return true;
        }
        return false;
    }
    public boolean meansTheSame(Object other) {
        Variable o;
        if (other instanceof Constant && this.term_name.equals(((Constant)other).getName())) {
            return true;
        }
        return false;
    }
    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public boolean isVariable() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return false;
    }

    @Override
    public Object clone() {
        return new Constant(this);
    }

    @Override
    public Term merge(Term other) {
        if(other.equals(this)){
            return this;
        }else if (other.isVariable()) {
            return this;
        } else if (other.isConstant()) {
            return null;
        } else if (other.isFunction()) {
                return null;
        }
        else throw new RuntimeException("Unknown term type");
    }

    public Term unificate(final Unificator unificator){
        return unificator.getNewValue(this);
    }


}
