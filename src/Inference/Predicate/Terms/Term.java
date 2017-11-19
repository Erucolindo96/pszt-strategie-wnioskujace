package Inference.Predicate.Terms;

import Inference.Strategy.Strategy;

import java.util.ArrayList;

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

    public abstract Term clone();

    //  public abstract boolean isRealizationOf(Term t);
//  public abstract boolean isInstanceOf(Term t);
    public abstract boolean isConstant();

    public abstract boolean isVariable();

    public abstract boolean isFunction();

    public boolean couldBeMarged(Term other) {
        if (isConstant() && other.isConstant()) {
            return false;
        } else if (isFunction()) {
            if (other.isFunction()) {
                return false;
            }
            ArrayList<Term> terms = ((Function) this).getArgs();
            for (Term term : terms) {
                if (!term.couldBeMarged(other))
                    return false;
            }
        } else if (isVariable()) {

        }
        throw new Error("moze usunac");
    }

    public abstract Term merge(Term other) ;//byc moze wystarczy przypisywac wartosc this a nie zwracac

    public String toString() {
        return term_name;
    }


}

