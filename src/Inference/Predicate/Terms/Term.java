package Inference.Predicate.Terms;

import Inference.Strategy.Strategy;

/**
 * @author erucolindo
 *
 * Klasa reprezentuje ogólny term. Z niego dziedziczą rzeczywiste termy.
 * Zawiera pole "term name", które w zależności od realizacji określa nazwę danego termu
 * Oraz pole function_arg - używane tylko przez term typu {@link Function} (inne nie udostępniają konstruktorów do dodawania go)
 *
 *
 */
public abstract class Term {

    protected String term_name;
    protected Term function_arg;

    protected Term()
    {
        term_name = "";
        function_arg = null;
    }
    protected Term(Term other)
    {
        term_name = new String(other.term_name);
        function_arg = other.function_arg.clone();

    }
    protected Term(String var_name, Term function_arg)
    {
        this.term_name =  var_name;
        this.function_arg = function_arg;
    }
    protected Term(String var_name)
    {
        this.term_name = var_name;
        this.function_arg = null;
    }
    //gettery i settery
    public void setName(String new_name)
    {
        term_name = new_name;
    }
    public String getName()
    {
        return new String(term_name);
    }
    public void setFunctionArgument(Term new_arg)
    {
        function_arg = new_arg;
    }
    public Term getFunctionArgument()
    {
        return function_arg.clone();
    }

    public abstract Term clone();
//  public abstract boolean isRealizationOf(Term t);
//  public abstract boolean isInstanceOf(Term t);
    public abstract boolean isConstans();
    public abstract boolean isVariable();
    public abstract boolean isFunction();




}
