package Inference.Predicate.Terms;

import java.util.ArrayList;

/**
 * @author erucolindo
 */
public class Function extends Term {
    ArrayList<Term> args;

    public Function() {
        super();
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
            return (o.term_name.equals(this.term_name)); //jesli nazwa fcji sie zgadza
//            {
//                return o.function_arg.equals(this.function_arg); //to sprawdzamy czy argument jest dobry
//            }

        }
        return false;
    }

    @Override
    public String toString() {
        String lebel = term_name + "( ";
        for (Term arg : args) {
            lebel+= arg.toString() + " ";
        }
        lebel+=")";
        return lebel;
    }


    @Override
    public Term merge(Term other) {//byc moze wystarczy przypisywac wartosc this  anie zwracac
        if (other.isVariable()) {
            return this;
        } else if (other.isConstant()) {
            if (args.size() != 1) return null;
            else {
                Term funcTerm = args.get(0);
                if (funcTerm.isConstant()) {
                    return null;
                }
                return (args.set(0, funcTerm.merge(funcTerm)));
            }
        } else if (other.isFunction()) {
            if (((Function) other).getArgs().size() != this.args.size()) {
                return null;
            }
            for (int i = 0; i < args.size(); ++i) {
                Term otherTerm = ((Function) other).getArgs().get(i);
                Term merged = this.args.get(i).merge(other);
                if (merged == null)
                    return null;
                else {
                    this.args.set(i, merged);
                }
            }
            return this;
        }
        throw new RuntimeException("Unknown term type");
    }
}
