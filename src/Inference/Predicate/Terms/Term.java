package Inference.Predicate.Terms;

import Inference.Predicate.Unificator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author erucolindo
 * <p>
 * Klasa reprezentuje ogólny term. Z niego dziedziczą rzeczywiste termy.
 * Zawiera pole "term name", które w zależności od realizacji określa nazwę danego termu
 * Oraz pole function_arg - używane tylko przez term typu {@link Function} (inne nie udostępniają konstruktorów do dodawania go)
 */
public abstract class Term {

    protected String term_name;

    protected Term() {
    }

    protected Term(Term other) {
        term_name = other.term_name;
    }

    protected Term(String name) {
        this.term_name = name;
    }


    public void setName(String new_name) {
        term_name = new_name;
    }

    public String getName() {
        return term_name;
    }

    //  public abstract boolean isRealizationOf(Term t);
//  public abstract boolean isInstanceOf(Term t);
    public abstract boolean isConstant();

    public abstract boolean isVariable();

    public abstract boolean isFunction();

    public boolean couldBeMarged(Term other) {
        throw new Error("moze usunac");
    }

    public abstract Term merge(Term other);//byc moze wystarczy przypisywac wartosc this a nie zwracac

    public String toString() {
        return term_name;
    }

    @Override
    public abstract Object clone();

    @Override
    public abstract boolean equals(Object other);

    public abstract boolean meansTheSame(Object other);

    public static Term getTermFromString(String string) {
        string = string.trim();
        if (string.contains("(")) {
            String args = string.substring(string.indexOf("(") + 1, string.lastIndexOf(")"));
            String funcName = string.substring(0, string.indexOf("("));
            return new Function(funcName, args);
        } else if (string.equals(string.toLowerCase())) {
            return new Variable(string);
        } else {
            return new Constant(string);
        }
    }
    public abstract Term unificate(Unificator unificator);
/**
 * return null if conflict
 * */
    public Term returnNarrowerTerm(final Term other){
        if (this.isConstant()){
            if(other.isConstant()) {
                if(this.equals(other)) return this;
                else return null;
            }
            if(other.isVariable()){
                return this;
            }
            if(other.isFunction()){
                return null;
            }
        }else if(this.isVariable()){
            return other;
        }else if(this.isFunction()) {
            if (other.isVariable()) {
                return this;
            }
            if (other.isConstant()) {
                return null;
            }
            if (other.isFunction()) {
                ((Function)this).returnNarrowerFunction((Function)other);
            }
        }
        return null;
    }
    public void changeTermName(String oldName, String newName) {
        if (isVariable() && getName().equals(oldName))
            setName(newName);
        if (isFunction()) {
            Function function = (Function) this;
            for (int j = 0; j < function.getArgumentCount(); ++j) {
                function.getArgument(j).changeTermName(oldName, newName);
            }
        }
    }

    public boolean isOrContainsVariableWithGiven( String name) {
        if (isVariable() && getName().equals(name))
            return true;
        if (isFunction()) {
            Function function = (Function) this;
            for (int j = 0; j < function.getArgumentCount(); ++j) {
                if (function.getArgument(j).isOrContainsVariableWithGiven(name))
                    return true;
            }
        }
        return false;
    }
}

