package Inference.Predicate;

import Inference.Predicate.Terms.Constant;
import Inference.Predicate.Terms.Term;
import Inference.Predicate.Terms.Variable;

import java.util.ArrayList;

public class Predicate {

    private String name;
    private ArrayList<Term> arguments;

    public Predicate() {
        name = "";
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
    }

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
        if (!this.name.equals(other.getName()) || arguments.size() != other.getArgumentsCount())
            return null;
        Predicate predicate = new Predicate();
        for (int i = 0; i < arguments.size(); ++i) {
            Term term = arguments.get(i).merge(other.getArguments().get(i));
            if (term.equals(null))
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
        String label= name + "( " ;
        for (Term arg :arguments){
            label+= arg.toString()+" ";
        }
        return label +")";
    }

    public static void main(String[] args) {
        Term t1 = new Constant("X");
        Term t2 = new Constant("Y");
        Term t3 = new Variable("y");
        Term t4 = new Variable("x");
        ArrayList<Term>list=new ArrayList<Term>();
        list.add(t1);
        list.add(t2);
        ArrayList<Term>list1=new ArrayList<Term>();
        list1.add(t3);
        list1.add(t4);
        Predicate P = new Predicate("P", list);
        Predicate P1 = new Predicate("P", list1);
        System.out.println(P);
    }

}
