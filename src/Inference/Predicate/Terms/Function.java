package Inference.Predicate.Terms;

import Inference.Predicate.Unificator;

import java.util.ArrayList;
import java.util.List;

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

    public Function(String func_name, Term argument) {
        super(func_name);
        this.args = new ArrayList<>();
        args.add(argument);
    }

    public Function(String var_name, ArrayList<Term> args) { //TODO zakladam ze to przekazuje args, a nie robi ich gleboka kopie
        super(var_name);
        this.args = args;
    }

    public Function(Function other) {
        super(other);
        args = new ArrayList<>();
        for (Term t : other.args) {
            args.add((Term) t.clone());
        }
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
    public boolean meansTheSame(Object other) {
        Variable o;
        if (other instanceof Function && this.args.size()==((Function)other).getArgumentCount()) {
            int i=0;
            for (Term arg:args){
                if(!arg.meansTheSame(((Function) other).getArgument(i++))){
                    return false;
                }
            }
            return true;
        }
        else return false;
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
    public Term merge(Term other) {
        if (other.isVariable()) {
            return this;
        } else if (other.isConstant()) {
            return null;
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
            newOne.addArgument(term.unificate(unificator));
        }
        return newOne;
    }

    public void swapArguments(Term oldArg, Term newArg) {
        for (int i = 0; i < args.size(); ++i) {
            if (args.get(i).equals(oldArg)) {
                args.set(i, newArg);
            } else if (args.get(i).isFunction()) {
                ((Function) args.get(i)).swapArguments(oldArg, newArg);
            }
        }
    }

    public Function returnNarrowerFunction(final Function other) {
        ArrayList<Term> args = new ArrayList<>();
        for (Term term : args) {
            Term narrower = term.returnNarrowerTerm(other);
            if (narrower == null) return null;
            args.add(narrower);
        }
        return new Function(term_name, args);
    }

}
