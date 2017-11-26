package Inference.Predicate;

import Inference.Predicate.Terms.Function;
import Inference.Predicate.Terms.Term;

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

    public Predicate(String name, String terms) {
        this.name = name;
        this.arguments = parseString(terms);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Term getTerm(int index) {
        return arguments.get(index);
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

    /**
     * Masz chyba racje wygodniej byloby zwracac arrayList.
     * Nie wyłpuję np. że P(y, F(y)) + Q(x, x) = null
     */
    public Predicate getUnificated(Predicate other) {
        if (!this.name.equals(other.getName()) || arguments.size() != other.getArgumentsCount())
            return null;
        Predicate predicate = new Predicate(name);
        for (int i = 0; i < arguments.size(); ++i) {
            Term term = getTerm(i).merge(other.getTerm(i));
            if (term == null)
                return null;
            else {
                predicate.addArgument(term);
            }
        }
        return predicate;
        //throw new RuntimeException("TODO");
    }

    // TODO: 21.11.2017 obsluga bledu - brak znalezienia  termu
    public Predicate unificate(Unificator unificator) {
        Predicate unificatedPredicate = new Predicate(this.name);
        for (Term term : arguments) {
            if (term.isFunction()) {
                unificatedPredicate.addArgument(((Function) term).unificate(unificator));
            } else {
                unificatedPredicate.addArgument(unificator.getNewValue(term));
            }
        }
        return unificatedPredicate;
    }

    public Unificator createUnificator(Predicate other) {
        Unificator unificator = new Unificator();
        Predicate unificated = getUnificated(other);
        if (unificated == null) {
            return null;
        }
        for (int i = 0; i < arguments.size(); ++i) {
            if (!getTerm(i).equals(unificated.getTerm(i))) {
                if (unificator.termIsInUnificator(getTerm(i))) {
                    if (unificator.getNewValue(getTerm(i)).equals(other.getTerm(i))) {
                        continue;
                    }
                    throw new RuntimeException("hmmm dziwny przypadek moze sie da, ale nie wiem czy na pewno");
                }
                unificator.addPair(getTerm(i), unificated.getTerm(i));
            }
        }
        return unificator;
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
        String label = name + "(";
        for (Term arg : arguments) {
            label += arg.toString() + ",";
        }
        return label.substring(0, label.length() - 1) + ")";
    }

    /**
     * Version without syntax checking and possibility to place function in function or function with many arguments
     **/
    private ArrayList<Term> parseString(String string) {
        ArrayList<Term> terms = new ArrayList<>();
        for (String term : string.split(",")) {
            term = term.trim();
            terms.add(Term.getTermFromString(term));
        }
        return terms;
    }
}
