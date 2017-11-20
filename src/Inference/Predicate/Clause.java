package Inference.Predicate;

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

    @Override
    public String toString() {
        String label = "";
        for (Literal literal : literals) {
            label += literal + " v ";
        }
        return label.substring(0, label.length() - 3);
    }

    public void parseString(String clause) {
        boolean negated;
        for (String predicate : clause.split("v")) {
            predicate=predicate.trim();
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
