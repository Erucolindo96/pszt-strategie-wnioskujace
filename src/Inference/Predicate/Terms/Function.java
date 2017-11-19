package Inference.Predicate.Terms;

import java.util.ArrayList;

/**
 * @author erucolindo
 */
public class Function extends Term {
    private ArrayList<Term> args;

    public Function() {
        super();
    }

    public Function(String name) {
        super(name);
        this.args = new ArrayList<>();
    }

    public Function(String func_name, Term arg) {
        super(func_name);
        this.args = new ArrayList<>();
        this.args.add(arg);
    }

    public Function(String var_name, ArrayList<Term> args) {
        super(var_name);
        this.args = args;
    }

    public Function(Function other) {
        super(other);
    }

    public ArrayList<Term> getArgs() {
        return args;
    }

    public void setArgs(ArrayList<Term> args) {
        this.args = args;
    }

    public boolean isConstant() {
        return false;
    }

    public boolean isVariable() {
        return false;
    }

    public boolean isFunction() {
        return true;
    }

    @Override
    public Term clone() {
        return new Function(this);
    }

    @Override
    public boolean equals(Object other) {
        Function o;
        if (other instanceof Function) {
            o = (Function) other;
            if (o.term_name.equals(this.term_name)) {
                return o.args.equals(this.args); //to sprawdzamy czy argument jest dobry
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String lebel = term_name + "( ";
        for (Term arg : args) {
            lebel += arg.toString() + " ";
        }
        lebel += ")";
        return lebel;
    }


    @Override
    public Term merge(Term other) {//byc moze wystarczy przypisywac wartosc this a nie zwracac
        if (other.isVariable()) {
            return this;
        } else if (other.isConstant()) {
            if (args.size() != 1) return null;
            else {
                if (args.get(0).isConstant()) return null;
                return new Function(this.term_name, args.get(0).merge(other));
            }
        } else if (other.isFunction()) {
            return mergeWithFunction(((Function) other));
        }
        throw new RuntimeException("Unknown term type");
    }

    private Term mergeWithFunction(Function other) {
        if (term_name.equals(other.term_name) || other.getArgs().size() != this.args.size()) {
            return null;
        }
        Function mergedFunct = new Function(this.term_name);
        for (int i = 0; i < args.size(); ++i) {
            Term otherTerm = other.getArgs().get(i);
            Term mergedTerm = this.args.get(i).merge(otherTerm);
            if (mergedTerm == null)
                return null;
            else {
                mergedFunct.args.add(mergedTerm);
            }
        }
        return mergedFunct;
    }
}
