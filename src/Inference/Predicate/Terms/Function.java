package Inference.Predicate.Terms;

import Inference.Predicate.Unificator;

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

    public Function(String name, String arguments) {
        super(name);
        this.args = parseString(arguments);
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
        args = other.args;
    }

    public Term getArgument(int index) {
        return args.get(index);
    }

    public void addArgument(Term term) {
        args.add(term);
    }

    public void setArgument(int index, Term term) {
        args.set(index, term);
    }

    public int getArgumentCount() {
        return args.size();
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
    public Object clone() {
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
        String label = term_name + "(";
        for (Term arg : args) {
            label += arg.toString() + ",";
        }
        return label.substring(0, label.length() - 1) + ")";
    }

    private ArrayList<Term> parseString(String string) {
        ArrayList<Term> arguments = new ArrayList<>();
        int count = 0;
        char[] chars = string.toCharArray();
        String arg = new String();
        for (int i = 0; i < chars.length; ++i) {
            arg += chars[i];
            if (chars[i] == ('(')) {
                count += 1;
            }
            if (chars[i] == (')')) {
                count -= 1;
            }
            if (count == 0 && chars[i] == ',') {
                arg = arg.substring(0, arg.length() - 1); //sub last ','
                arguments.add(Term.getTermFromString(arg));
                arg = new String();
            } else if (count == 0 && i == chars.length - 1) {
                arguments.add(Term.getTermFromString(arg));
            }
        }
        return arguments;
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
        if (!term_name.equals(other.term_name) || other.getArgumentCount() != this.args.size()) {
            return null;
        }
        Function mergedFunct = new Function(this.term_name);
        for (int i = 0; i < args.size(); ++i) {
            Term otherTerm = other.getArgument(i);
            Term mergedTerm = this.args.get(i).merge(otherTerm);
            if (mergedTerm == null)
                return null;
            else {
                mergedFunct.args.add(mergedTerm);
            }
        }
        return mergedFunct;
    }

    public Term unificate(Unificator unificator) {
        Function newOne = new Function(term_name);
        for (Term term : args) {
            if (term.isFunction()) {
                newOne.addArgument(((Function) term).unificate(unificator));
            } else {
                newOne.addArgument(unificator.getNewValue(term));
            }
        }
        return newOne;
    }
}
