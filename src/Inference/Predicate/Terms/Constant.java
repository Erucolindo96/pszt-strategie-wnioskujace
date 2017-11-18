package Inference.Predicate.Terms;

/**
 * @author erucolindo
 * <p>
 * Term będący stałą.
 * Musi składać się z samych wielkich liter(czemu służy metoda checkSyntax - rzuca wyjątek jeśli term składa się z innych liter)
 */
public class Constant extends Term {
    private void checkSyntax() //Raczej public static
    {
        String name = term_name.toUpperCase();
        if (!name.equals(term_name))
            throw new RuntimeException("Stale musza byc zlozone z duzych liter. Ale zmiec ten wyjatek aby byl zwyklego typu");
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
    public Term clone() {
        return new Constant(this);
    }

    @Override
    public Term merge(Term other) {//byc moze wystarczy przypisywac wartosc this  anie zwracac
        if (other.isVariable()) {
            return this;
        } else if (other.isConstant()) {
            return null;
        } else if (other.isFunction()) {
            if (((Function) other).getArgs().size() != 1 ) {
                return null;
            } else {
                Term funcTerm=((Function) other).getArgs().get(0);
                if (funcTerm.isConstant()){
                    return null;
                }
                return (((Function) other).getArgs().set(0, this.merge(funcTerm)));
            }
        }
        throw new RuntimeException("Unknown term type");
    }


}
