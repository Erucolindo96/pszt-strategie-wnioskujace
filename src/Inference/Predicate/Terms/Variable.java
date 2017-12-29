package Inference.Predicate.Terms;

/**
 * @author erucolindo
 * <p>
 * Term reprezentujący zmienną.
 * Może się składać wyłącznie z małych liter - służy do tego metoda checkSyntax(rzuca wyjątek jeśli nazwa składa się z liter innych niż małe).
 */
public class Variable extends Term {

    private void checkSyntax() {
        String name = term_name.toLowerCase();
        if (!name.equals(term_name))
            throw new RuntimeException("Zmienne musza byc zlozone z malych liter");
    }

    public Variable() {
        super();
    }

    public Variable(String name) {
        super(name);
        checkSyntax();
    }

    public Variable(Variable other) {
        super(other);
    }

    public boolean isConstant() {
        return false;
    }

    public boolean isVariable() {
        return true;
    }

    public boolean isFunction() {
        return false;
    }

    @Override
    public Object clone() {
        return new Variable(this);
    }

    @Override
    public boolean equals(Object other) {
        Variable o;
        if (other instanceof Variable) {
            o = (Variable) other;
            if (o.term_name.equals(this.term_name))
                return true;
        }
        return false;
    }


    public Term merge(Term other) {
        if (other.isVariable()) {
            return this;
        } else if (other.isConstant()) {
            return other;
        } else if (other.isFunction()) {
            return other;

        }
        throw new RuntimeException("Unknown term type");
    }

}
