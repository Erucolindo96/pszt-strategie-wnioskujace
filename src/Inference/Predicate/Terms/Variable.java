package Inference.Predicate.Terms;

/**
 * @author erucolindo
 *
 * Term reprezentujący zmienną.
 * Może się składać wyłącznie z małych liter - służy do tego metoda checkSyntax(rzuca wyjątek jeśli nazwa składa się z liter innych niż małe).
 */
public class Variable extends Term
{

    private void checkSyntax()
    {
        String name = term_name.toLowerCase();
        if(!name.equals(term_name))
            throw new RuntimeException("Stale musza byc zlozone z duzych liter. Ale zmiec ten wyjatek aby byl zwyklego typu");
    }

    public Variable()
    {
        super();
    }
    public Variable(String const_name)
    {
        super();
        checkSyntax();
    }
    public Variable(Variable other)
    {
        super(other);
    }

    public boolean isConstans()
    {
        return false;
    }
    public boolean isVariable()
    {
        return true;
    }
    public boolean isFunction()
    {
        return false;
    }
    @Override
    public Term clone()
    {
        return new Variable(this);
    }
    @Override
    public boolean equals(Object other)
    {
        Variable o;
        if(other instanceof Variable)
        {
            o = (Variable) other;
            if (o.term_name.equals(this.term_name))
                return true;
        }
        return false;
    }

}
