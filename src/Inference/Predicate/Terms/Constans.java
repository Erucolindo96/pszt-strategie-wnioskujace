package Inference.Predicate.Terms;

/**
 * @author erucolindo
 *
 * Term będący stałą.
 * Musi składać się z samych wielkich liter(czemu służy metoda checkSyntax - rzuca wyjątek jeśli term składa się z innych liter)
 */
public class Constans extends Term
{
    private void checkSyntax()
    {
        String name = term_name.toUpperCase();
        if(!name.equals(term_name))
            throw new RuntimeException("Stale musza byc zlozone z duzych liter. Ale zmiec ten wyjatek aby byl zwyklego typu");
    }

    public Constans()
    {
        super();
    }
    public Constans(String const_name)
    {
        super();
        checkSyntax();
    }
    public Constans(Constans other)
    {
        super(other);
    }

    @Override
    public boolean equals(Object other)
    {
        Constans o;
        if(other instanceof Constans)
        {
            o = (Constans) other;
            if (o.term_name.equals(this.term_name))
                return true;
        }
        return false;
    }

    @Override
    public boolean isConstans()
    {
        return true;
    }
    @Override
    public boolean isVariable()
    {
        return false;
    }
    @Override
    public boolean isFunction()
    {
        return false;
    }
    @Override
    public Term clone()
    {
        return new Constans(this);
    }

}
