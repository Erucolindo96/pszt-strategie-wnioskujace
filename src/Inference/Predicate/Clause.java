package Inference.Predicate;

import Inference.Predicate.Terms.Term;

import java.util.ArrayList;


public class Clause {

    private ArrayList<Literal> literals;

    public Clause() {
        literals = new ArrayList<>();
    }

    public Clause(String literals) {
        this.literals = new ArrayList<>();
        parseString(literals);
    }

    public Clause(ArrayList<Literal> literals) {
        this.literals = literals;
    }

    public void addLiteral(Literal l) {
        literals.add(l);
    }

    public Literal getLiteral(int number) {
        return literals.get(number);
    }

    public int getCount() {
        return literals.size();
    }

    @Override
    public String toString() {
        String label = "";
        for (Literal literal : literals) {
            label += literal + " v ";
        }
        return label.substring(0, label.length() - 3);
    }

    public Clause getResolution(Clause other) {
        if (getCount() != other.getCount()) {
            return null;
        }
        for (int i = 0; i < literals.size(); ++i) {
            if (getLiteral(i).canBeResolutatedWith(other.getLiteral(i))) {
                //generate unificator
                //break
            }
        }
//        nosupcio unifikuj
        return null;
    }
//todo pomin B()
    public Clause unificate(Unificator unificator){
        Clause newOne = new Clause();
        for (Literal literal: literals) {
               newOne.addLiteral(literal.unificate(unificator));
        }
        return newOne;
    }

    public void parseString(String clause) {
        boolean negated;
        for (String predicate : clause.split("v")) {
            predicate = predicate.trim();
            if (predicate.charAt(0) == '-') {
                negated = true;
            } else {
                negated = false;
            }
            String predicateName = predicate.substring(0, predicate.indexOf("(")).trim();
            String terms = predicate.substring(predicate.indexOf("(") + 1, predicate.lastIndexOf(")"));
            literals.add(new Literal(negated, predicateName, terms));
        }
    }
}
