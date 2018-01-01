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

    /**
     * Ta relacja nie musi być zwrotna
     */
    public boolean isUnificableWith(Predicate other) {
        throw new RuntimeException("TODO");
    }

    public boolean isInstationOf(Predicate other) {
        return name.equals(other.name) && terms.size() == other.terms.size();
    }

    /**
     * Ta relacja nie musi być zwrotna
     */
    public ArrayList<Term> getMaxCommonUnificator(Predicate other) {
        throw new RuntimeException("TODO");
    }

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
        //throw new RuntimeException("TODO");
    }

    /**
     * Tworzy poprawny zunifikowany predykat na podstawie unificatora
     */
    // TODO: 21.11.2017 obsluga bledu - brak znalezienia  termu
    public Predicate unificate(Unificator unificator) {
        Predicate unificatedPredicate = new Predicate(this.name);
        for (Term term : terms) {
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
        for (int i = 0; i < terms.size(); ++i) {
            if (!getTerm(i).equals(unificated.getTerm(i))) {
                if (unificator.termIsInUnificator(getTerm(i))) {
                    Term prevNewValue=unificator.getNewValue(getTerm(i));
                    if (prevNewValue.equals(other.getTerm(i))) {
                        continue;
                    }
                    else {
                        Term narrower=prevNewValue.returnNarrowerTerm(other.getTerm(i));
                        if( narrower == null){//there was a conflict
                            return null;
                        }
                        unificator.setNarrowerValue(getTerm(i), narrower);
                    }
                } else {
                    unificator.addPair(getTerm(i), unificated.getTerm(i));
                }
            }
        }
        return unificator;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Predicate) {
            if (name.equals(((Predicate) other).name) && terms.size() == ((Predicate) other).terms.size()) {
                boolean ret = true;
                for (int i = 0; i < terms.size(); ++i) {
                    if (!((Predicate) other).terms.get(i).equals(terms.get(i))) //jesli odpowiednie termy nie sa rowne zwroc falsz
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
            } else if (i == chars.length - 1 && count!=0){
                throw new IllegalArgumentException("Branches are illegal");
            }
        }
        return terms;
    }

}
