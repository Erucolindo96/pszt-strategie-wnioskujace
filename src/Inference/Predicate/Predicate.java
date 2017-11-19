package Inference.Predicate;

import Inference.Predicate.Terms.Constant;
import Inference.Predicate.Terms.Term;
import Inference.Predicate.Terms.Variable;

import java.util.ArrayList;

public class Predicate {

    private String name;
    private ArrayList<Term> arguments;

    public Predicate(String name) {
        this.name = name;
        arguments = new ArrayList<>();
    }

    public Predicate(String name, ArrayList<Term> terms) {
        this.name = name;
        this.arguments = terms;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Term> getArguments() {
        return new ArrayList<>(arguments);
    }//moze zmienmy nazwe na get terms

    public void setArguments(ArrayList<Term> terms) {
        arguments = terms;
    }

    public int getArgumentsCount() {
        return arguments.size();
    }

    public void addArgument(Term term) {
        arguments.add(term);
    }

    /**
     * Ta relacja nie musi być zwrotna
     */
    public boolean isUnificableWith(Predicate other) {
        throw new RuntimeException("TODO");
    }

    public boolean isInstationOf(Predicate other) {
        return name.equals(other.name) && arguments.size() == other.arguments.size();
    }

    /**
     * Ta relacja nie musi być zwrotna
     */
    public ArrayList<Term> getMaxCommonUnificator(Predicate other) {
        throw new RuntimeException("TODO");
    }

    public Predicate getUnificator(Predicate other) {
        if (this.name.equals(other.getName()) || arguments.size() != other.getArgumentsCount())
            return null;
        Predicate predicate = new Predicate(name);
        for (int i = 0; i < arguments.size(); ++i) {
            Term term = arguments.get(i).merge(other.getArguments().get(i));
            if (term == null)
                return null;
            else {
                predicate.addArgument(term);
            }
        }
        return predicate;
        //throw new RuntimeException("TODO");
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Predicate) {
            if (name.equals(((Predicate) other).name) && arguments.size() == ((Predicate) other).arguments.size()) {
                boolean ret = true;
                for (int i = 0; i < arguments.size(); ++i) {
                    if (!((Predicate) other).arguments.get(i).equals(arguments.get(i))) //jesli odpowiednie termy nie sa rowne zwroc falsz
                        ret = false;
                }
                return ret;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String label = name + "( ";
        for (Term arg : arguments) {
            label += arg.toString() + " ";
        }
        return label + ")";
    }

}
