package Inference.Predicate.Terms;

/**
 * @author erucolindo
 *
 *
 */
public class Function extends Term
{

    public Function()
    {
        super();
    }
    public Function(String func_name, Term func_arg)
    {
        super(func_name, func_arg);
    }
    public Function(Function other)
    {
        super(other);
    }

    public boolean isConstans()
    {
        return false;
    }
    public boolean isVariable()
    {
        return false;
    }
    public boolean isFunction()
    {
        return true;
    }
    @Override
    public Term clone()
    {
        return new Function(this);
    }

    @Override
    public boolean equals(Object other)
    {
        Function o;
        if(other instanceof Function)
        {
            o = (Function) other;
            if (o.term_name.equals(this.term_name)) //jesli nazwa fcji sie zgadza
            {
                return o.function_arg.equals(this.function_arg); //to sprawdzamy czy argument jest dobry
            }

        }
        return false;
    }
}
