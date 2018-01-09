package Inference.Predicate;

import Inference.Predicate.Terms.Function;
import Inference.Predicate.Terms.Term;

import java.util.ArrayList;


public class Clause {

    private ArrayList<Literal> literals;
    private Clause mather;
    private Clause father;

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
        for (Literal literal : other.literals) {
            literals.add(new Literal(literal));
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

    /**
     * Zwraca długość klauzuli, która mogłaby powstać po rezolucji klauzuli z klauzulą resoluted_with
     *
     * @param resoluted_with
     * @return Długośc klauzuli która powstałaby z rezolucji naszej klauzuli z resoluted_with, przy założeniu, że rezolucja byłaby możliwa
     */
    public int getCountAfterResolution(Clause resoluted_with) {
        return literals.size() + resoluted_with.literals.size() - 2;// -2 poniewaz przy rezolucji z kazdej klauzuli zniknie 1 literał
    }

    @Override
    public String toString() {
        String label = "";
        for (Literal literal : literals) {
            label += literal + " v ";
        }
        return label.substring(0, label.length() - 3);
    }

    //    public Clause getResolution(final Clause other) {//TODO można rozważyć rezolucje do konca a nie po 1 predykacie
//        Clause temp=null,merged;
//        do {
//            merged = getResolution1(other);
//            if(merged!=null){
//                temp=other.getResolution1(merged);
//                if(temp==null){
//                    temp=getResolution1(merged);
//                }
//            }
//        }while (temp!=null);
//        return merged;
//    }
    public Clause getResolution(final Clause other) {
        Unificator unificator = null, otherUnificator = null;
        int i, j = 0;
        outerLoop:
        for (i = 0; i < literals.size(); ++i) {
            for (j = 0; j < other.getCount(); ++j) {
                if (getLiteral(i).canBeResolutatedWith(other.getLiteral(j))) {
                    unificator = getLiteral(i).createUnificator(other.getLiteral(j));
                    otherUnificator = other.getLiteral(j).createUnificator(getLiteral(i));
                    if (unificator != null && otherUnificator != null) {
                        renameConflictsVariable(unificator, other, getLiteral(i));
                        break outerLoop;
                    }
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
        merged.deleteMergedPredicates(i, j + literals.size() - 1);
        merged.father = this;
        merged.mather = other;
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
        for (String predicateString : clause.split("v")) {
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

    /**
     * if there are variables in both resolute clauses with same names, change one of it name
     */
    private void renameConflictsVariable(Unificator unificator, final Clause other, Literal merged) {
        for (Literal literal : literals) {
            for (int i = 0; i < literal.getTermsCount(); ++i) {
                int number = 0;
                String conflictedName = containsConflictedVariable(literal.getTerm(i), other);
                while (conflictedName != null && !merged.containsVariableWithGivenName(conflictedName)) {
                    String newName = conflictedName + number;
                    changeVariablesNames(conflictedName, newName);
                    unificator.changeVariablesName(conflictedName, newName);
                    conflictedName = containsConflictedVariable(literal.getTerm(i), other);
                }
            }
        }
    }

    /**
     * returns name of variable in given term which exist in both clauses
     * or null if variable is unique
     */
    private String containsConflictedVariable(final Term term, final Clause other) {
        if (term.isVariable()) {
            if (other.containsVariable(term.getName()))
                return term.getName();
            else
                return null;
        }
        if (term.isFunction()) {
            Function function = (Function) term;
            for (int j = 0; j < function.getArgumentCount(); ++j) {
                String varName = containsConflictedVariable(function.getArgument(j), other);
                if (varName != null)
                    return varName;
            }
        }
        return null;
    }

    private void changeVariablesNames(String oldName, String newName) {
        for (Literal literal : literals) {
            literal.changeTermsName(oldName, newName);
        }
    }

    public boolean containsVariable(String searchedName) {
        for (Literal literal : literals) {
            if (literal.containsVariableWithGivenName(searchedName)) {
                return true;
            }
        }
        return false;
    }
//    tez ma problem ze zmiennymi

    public boolean isTheSameOrWiderClauseThan(Clause other) {
        boolean theSameOrWieder=false;
        if(this.getCount()!=other.getCount()){
            return false;
        }
        for (Literal literal : literals) {
            theSameOrWieder=true;
            for (int j = 0; j < literals.size(); ++j) {
                Literal otherLiteral = other.getLiteral(j);
                if (literal.isNegated() != otherLiteral.isNegated() || literal.getName() != otherLiteral.getName()) {
                    theSameOrWieder= false;
                    continue;
                }
                for (int i = 0; i < literal.getTermsCount(); ++i) {
                    Term otherTerm = otherLiteral.getTerm(i);
                    if (!otherTerm.meansTheSame(otherTerm.returnNarrowerTerm(literal.getTerm(i)))) {
                        theSameOrWieder=false;
                        break;
                    }
                }
                if(!theSameOrWieder)
                    return false;
            }
        }
        return theSameOrWieder;
    }
}
