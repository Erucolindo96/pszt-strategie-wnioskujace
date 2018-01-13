package Inference.Predicate;

import Inference.Predicate.Terms.Function;
import Inference.Predicate.Terms.Term;

import java.util.ArrayList;

public class Predicate {

    private String name;
    private ArrayList<Term> terms;

    public Predicate(String name) {
        this.name = name;
        terms = new ArrayList<>();
    }

    public Predicate(String name, ArrayList<Term> terms) {
        this.name = name;
        this.terms = terms;
    }

    public Predicate(String name, String terms) {
        this.name = name;
        this.terms = parseString(terms);
    }

    public Predicate(Predicate other) {

        name = new String(other.name);
        terms = new ArrayList<>();
        for (Term t : other.terms) {
            terms.add((Term) t.clone());
        }
    }

    @Override
    public Object clone() {
        return new Predicate(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Term getTerm(int index) {
        return terms.get(index);
    }

    public int getTermsCount() {
        return terms.size();
    }

    public void addArgument(Term term) {
        terms.add(term);
    }

    public boolean isInstationOf(Predicate other) {
        return name.equals(other.name) && terms.size() == other.terms.size();
    }

    /**
     * Ta relacja nie musi być zwrotna
     */
//    public Unificator createMaxCommonUnificator(final Predicate other) {
//        if (!this.name.equals(other.getName()) || terms.size() != other.getTermsCount())
//            return null;
//        Unificator unificator = new Unificator();
//        for (int i = 0; i < terms.size(); ++i) {
//            if (!getTerm(i).createUnificationPair(unificator, other.getTerm(i)))
//                ; //there is an conflict can not crate unificator
//            return null;
//        }
//
//    }

    /**
     * Zwraca predykat na którego podstawie createUnificator() tworzy unificator
     * mogłaby być prywatna, ale są testy.
     */
    public Predicate getUnificated(Predicate other) {
        if (!this.name.equals(other.getName()) || terms.size() != other.getTermsCount())
            return null;
        Predicate predicate = new Predicate(name);
        for (int i = 0; i < terms.size(); ++i) {
            Term term = getTerm(i).merge(other.getTerm(i));
            if (term == null)
                return null;
            else {
                predicate.addArgument(term);
            }
        }
        return predicate;
    }

    /**
     * Unifikuje predykat na podstawie unificatora
     */
    // TODO: 21.11.2017 obsluga bledu - brak znalezienia  termu
    public Predicate unificate(Unificator unificator) {
        Predicate unificatedPredicate = new Predicate(this.name);
        for (Term term : terms) {
            unificatedPredicate.addArgument(term.unificate(unificator));
        }
        return unificatedPredicate;
    }

    public Unificator createUnificator(Predicate other) {
        Unificator unificator = new Unificator();
        Predicate unificated = getUnificated(other);
        if (unificated == null) {
            return null;
        }
        for (int i = 0; i < terms.size(); ++i) {
            if (!addedPair(unificator, getTerm(i), unificated.getTerm(i)))
                return null;
        }
        return unificator;
    }

    private static boolean addedPair(Unificator unificator, final Term myTerm, final Term unificatedTerm) {
        if (!myTerm.equals(unificatedTerm)) {
            if (myTerm.isFunction()) {
                Function function = (Function) myTerm;
                for (int i = 0; i < function.getArgumentCount(); ++i) {
                    if (!addedPair(unificator, function.getArgument(i), ((Function) unificatedTerm).getArgument(i)))
                        return false;
                }
            }
            if (unificator.termIsInUnificator(myTerm)) {
                Term prevNewValue = unificator.getNewValue(myTerm);
                if (prevNewValue.equals(unificatedTerm)) {
                    return true;
                } else {
                    Term narrower = prevNewValue.returnNarrowerTerm(unificatedTerm);
                    if (narrower == null) {//there was a conflict
                        return false;
                    }
                    unificator.setNarrowerValue(myTerm, narrower);
                }
            } else {
                unificator.addPair(myTerm, unificatedTerm);
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object other) { //TODO przerobić tak jak w rozmowie z Martyna na privie -juz chyba jest dobrze, zmiany trzeba wprowadzic z Klauzulach
        if (other instanceof Predicate) {
            if (name.equals(((Predicate) other).name) && terms.size() == ((Predicate) other).terms.size()) {
                // TODO UWAGA tutaj nie sprawdza się równości termów, bo to można wykonać tylko na poziomie klauzul (musimy porównać termy kilku literaów z danej klauzuli)
                //należy o tym pamiętać!
                for (int i = 0; i < terms.size(); ++i) {
                    if (!((Predicate) other).terms.get(i).meansTheSame(terms.get(i))) {
                        return false;
                    }
                }
                for (int i = 0; i < terms.size(); ++i) {
                    for (int j = 0; j < terms.size(); ++j) {
                        if (((Predicate) other).terms.get(i).equals(((Predicate) other).terms.get(j)) != terms.get(i).equals(terms.get(j))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String label = name + "(";
        for (Term arg : terms) {
            label += arg.toString() + ",";
        }
        return label.substring(0, label.length() - 1) + ")";
    }

    private ArrayList<Term> parseString(String string) {
        ArrayList<Term> terms = new ArrayList<>();
        int count = 0;
        char[] chars = string.toCharArray();
        String term = new String();
        for (int i = 0; i < chars.length; ++i) {
            term += chars[i];
            if (chars[i] == ('(')) {
                count += 1;
            }
            if (chars[i] == (')')) {
                count -= 1;
            }
            if (count == 0 && chars[i] == ',') {
                term = term.substring(0, term.length() - 1); //sub last ','
                terms.add(Term.getTermFromString(term));
                term = new String();
            } else if (count == 0 && i == chars.length - 1) {
                terms.add(Term.getTermFromString(term));
            } else if (i == chars.length - 1 && count != 0) {
                throw new IllegalArgumentException("Branches are illegal");
            }
        }
        return terms;
    }

}
