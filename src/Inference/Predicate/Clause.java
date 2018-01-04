package Inference.Predicate;

import Inference.Predicate.Terms.Function;
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

    public Clause(Clause other) {
        literals = new ArrayList<>();
        for(Literal l: other.literals)
        {
            literals.add((Literal)l.clone());
        }
    }

    @Override
    public Object clone() {
        return new Clause(this);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Clause) {
            Clause c = (Clause) other;
            if (this.literals.equals(c.literals))
                return true;
        }
        return false;
    }

    public void addLiteral(Literal l) {
        literals.add(l);
    }

    private void addLiteralsList(ArrayList<Literal> list) {
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

    public Clause getResolution(final Clause other) {
        Unificator unificator = null, otherUnificator = null;
        int i, j = 0;
        outerLoop:
        for (i = 0; i < literals.size(); ++i) {
            for (j = 0; j < other.getCount(); ++j) {
                if (getLiteral(i).canBeResolutatedWith(other.getLiteral(j))) {
                    renameConflictsVariable(other, getLiteral(i));
                    unificator = getLiteral(i).createUnificator(other.getLiteral(j));
                    otherUnificator = other.getLiteral(j).createUnificator(getLiteral(i));
                    break outerLoop;
                }
            }
        }
        if (unificator == null || otherUnificator == null) {
            return null;
        }
        unificator.resolveUnificatorConflicts(otherUnificator);
        otherUnificator.resolveUnificatorConflicts(unificator);

        Clause merged = new Clause(getUnificatedPredicates(unificator));
        merged.addLiteralsList(other.getUnificatedPredicates(otherUnificator));
        merged.deleteMergedPredicates(i, j + literals.size() - 1);//można to zrobić bardziej elegancko pracując na kopii klauzul i po prostu je usunac przed unifikajcap
        return merged;
    }

    private ArrayList<Literal> getUnificatedPredicates(Unificator unificator) {
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
            validateString(predicateString);
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

    private void validateString(String predicateString) {
        if (!predicateString.contains("(") || !predicateString.contains(")")) {
            throw new IllegalArgumentException("Branches are illegal");
        }
        if (predicateString.indexOf("(") == 0) {
            throw new IllegalArgumentException("Predicate's name is missing");
        }
    }

    private void deleteMergedPredicates(int firstToDel, int secondToDel) {
        literals.remove(firstToDel);
        literals.remove(secondToDel);
    }

    public boolean isContradictory(final Clause other) {
        if (this.literals.size() > 1 || other.getCount() > 1) {
            return false;
        }
        return literals.get(0).isContradictory(other.getLiteral(0));
    }

    private void renameConflictsVariable(final Clause other, Literal merged) {
        for (Literal literal : literals) {
            for (int i = 0; i < literal.getTermsCount(); ++i) {
                int number = 0;
                String conflictedName=containsConflictedVariable(literal.getTerm(i), other);
                while (conflictedName!=null && !mergedContainsVar(merged, conflictedName)) {
                    String newName = conflictedName + number;
                    changeName(conflictedName, newName);
                    conflictedName=containsConflictedVariable(literal.getTerm(i), other);
                }
            }
        }
    }
/**
returns name of variable in term which exist in both clauses or null if variable is unique
*/
    private String containsConflictedVariable(final Term term, final Clause other) {
        if (term.isVariable()) {
            if( other.containsVariable(term.getName()))
                return term.getName();
            else
                return null;
        }
        if (term.isFunction()) {
            Function function = (Function) term;
            for (int j = 0; j < function.getArgumentCount(); ++j) {
                String varName=containsConflictedVariable(function.getArgument(j), other);
                if (varName != null)
                    return varName;
            }
        }
        return null;
    }

    public void changeName(String oldName, String newName) {
        for (Literal literal : literals) {
            for (int i = 0; i < literal.getTermsCount(); ++i) {
                changeTermName(literal.getTerm(i), oldName, newName);
            }
        }
    }

    private void changeTermName(Term term, String oldName, String newName) {
        if (term.isVariable() && term.getName().equals(oldName))
            term.setName(newName);
        if (term.isFunction()) {
            Function function = (Function) term;
            for (int j = 0; j < function.getArgumentCount(); ++j) {
                changeTermName(function.getArgument(j), oldName, newName);
            }
        }
    }
    public boolean mergedContainsVar(Literal merged, String name){
        for (int i = 0; i < merged.getTermsCount(); ++i) {
            if (isOrContainsVar(merged.getTerm(i), name))
                return true;
        }
        return false;
    }
    public boolean containsVariable(String searchedName) {
        for (Literal literal : literals) {
            for (int i = 0; i < literal.getTermsCount(); ++i) {
                if (isOrContainsVar(literal.getTerm(i), searchedName))
                    return true;
            }
        }
        return false;
    }

    private boolean isOrContainsVar(Term term, String name) {
        if (term.isVariable() && term.getName().equals(name))
            return true;
        if (term.isFunction()) {
            Function function = (Function) term;
            for (int j = 0; j < function.getArgumentCount(); ++j) {
                if (isOrContainsVar(function.getArgument(j), name))
                    return true;
            }
        }
        return false;
    }
}
