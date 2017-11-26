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

    public void addLiteralsList(ArrayList<Literal> list) {
        literals.addAll(list);
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
        Unificator unificator = null;
        Unificator otherUnificator = null;
        if (getCount() != other.getCount()) {
            return null;
        }
        int i, j=0;
        outerLoop:
        for (i = 0; i < literals.size(); ++i) {
            for (j = 0; j < literals.size(); ++j) {
                if (getLiteral(i).canBeResolutatedWith(other.getLiteral(j))) {
                    unificator = getLiteral(i).createUnificator(other.getLiteral(j));
                    otherUnificator = other.getLiteral(j).createUnificator(getLiteral(i));
                    break outerLoop;
                }
            }
        }
        if (unificator == null) {
            return null;
        }
        Clause merged = new Clause(unificate(unificator));
        merged.addLiteralsList(other.unificate(otherUnificator));
        merged.deleteMergedPredicates(i,j+literals.size());//można to zrobić bardziej elegancko pracując na kopii klauzul i po prostu je usunac przed unifikajca
        return merged;
    }

    public ArrayList<Literal> unificate(Unificator unificator) {
        ArrayList<Literal> list = new ArrayList<>();
        for (Literal literal : literals) {
            list.add(literal.unificate(unificator));
        }
        return list;
    }

    public void parseString(String clause) {
        boolean negated;
        for (String predicateString : clause.split("v")) {//TODO przenies to do literalu bo brak logiki
            predicateString = predicateString.trim();
            if (predicateString.charAt(0) == '-') {
                predicateString = predicateString.substring(1);
                negated = true;
            } else {
                negated = false;
            }
            String predicateName = predicateString.substring(0, predicateString.indexOf("(")).trim();
            String terms = predicateString.substring(predicateString.indexOf("(") + 1, predicateString.lastIndexOf(")"));
            literals.add(new Literal(negated, predicateName, terms));
        }
    }
    private void deleteMergedPredicates(int first, int second){
        literals.remove(first);
        literals.remove(--second);
    }
}
