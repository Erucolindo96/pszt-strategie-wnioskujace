package Inference.Predicate;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class KnowledgeBase {
    private ArrayList<Clause> clauses = new ArrayList<>();
    private ArrayList<Clause> antithesis;
    private ArrayList<Clause> contradict_clauses;

    /**
     * clauses in file after empty line are antithesis;
     */
    public void loadFromFile(String pathString) throws IOException {
        Path path = FileSystems.getDefault().getPath(pathString);
        ArrayList<String> clausesStrings=new ArrayList<>();
        Files.lines(path).forEach(line ->clausesStrings.add(line));
        loadFromStringArray(clausesStrings);
    }
    private void loadFromStringArray(ArrayList<String> list){
        boolean wasEnter=false;
        for (String string:list) {
            if (string.isEmpty()){
                wasEnter=true;
            }else if(wasEnter){
                antithesis.add(new Clause(string));
            }else{
                clauses.add(new Clause(string));
            }
        }
        if (!wasEnter) {
            throw new IllegalArgumentException();
        }
    }

    private void addContradictClause(Clause one, Clause two)
    {
        contradict_clauses = new ArrayList<>();
        contradict_clauses.add((Clause)one.clone());
        contradict_clauses.add((Clause)two.clone());
    }

    public KnowledgeBase() {
        clauses = new ArrayList<>();
        antithesis =new ArrayList<>();
    }

    public KnowledgeBase(KnowledgeBase other) {
        clauses = new ArrayList<>();
        for(Clause c: other.clauses)
        {
            clauses.add((Clause)c.clone());
        }

        antithesis = new ArrayList<>();
        for(Clause c: other.antithesis)
        {
            this.antithesis.add((Clause)c.clone());
        }
    }

    public ArrayList<Clause> getAntithesis() {
        return antithesis;
    }
        /**
         * Zwraca informację, czy dana klauzula jest juz zawarta w bazie wiedzy
         * @param other Klauzula o która pytamy, czy znajduje sie już w zbiorze
         * @return True jeśli klauzula other juz jest w zbiorze, false w przeciwnym razie
         */
    public boolean haveThisClause(Clause other)
    {
        return clauses.contains(other);
    }
//    tez ma problem ze zmiennymi
    public boolean haveThisOrWiderClause(Clause other)
    {
        if(clauses.contains(other))
            return true;
        for(Clause clause: clauses){
            if(clause.isTheSameOrWiderClauseThan(other))
                return true;
        }
        return false;
    }
    /**
     * Zwraca roznice zbiorow
     *
     * @param other Zbior odejmowany
     * @return Roznice zbiorow
     */
    public KnowledgeBase getDifference(KnowledgeBase other) {
        KnowledgeBase diff = new KnowledgeBase(this);
        for (Clause c : other.clauses) {
            while (diff.clauses.remove(c)) ;//usuwaj klauzule dopoki znajduja sie jakies kopie w ArrayLiscie
        }
        return diff;
    }

    public int getClauseCount() {
        return clauses.size();
    }

    public Clause getClause(int index) {
        return clauses.get(index);
    }

    public void addClause(Clause clause) {
        clauses.add(new Clause((clause)));
    }

    public void addClause(ArrayList<Clause> newClauses) {
        for(Clause c: newClauses)
        {
            clauses.add((Clause)c.clone());
        }
    }

    public int getMaxLengthOfClause() {
        int max_lenght = 0;
        for (Clause c : clauses) {
            if (c.getCount() > max_lenght)
                max_lenght = c.getCount();
        }
        return max_lenght;
    }


    public boolean haveContradiction(int firstToCheck) {
        if (firstToCheck < 0 || clauses.size() <= firstToCheck)
            throw new RuntimeException("Zla wartosc firstToCheck - mniejsza od 0 lub wieksza niz ilosc klauzul");

        for (int i = firstToCheck; i < clauses.size(); ++i) {
            for (int j = 0; j < i; ++j) {
                if (clauses.get(i).isContradictory(clauses.get(j)))
                {
                    addContradictClause(clauses.get(i), clauses.get(j));
                    return true;
                }

            }
        }//przechodzi po klauzulach od firstTocheck w gore
        for (int i = 0; i < firstToCheck; ++i) {
            for (int j = 0; j < i; ++j) {
                if (clauses.get(i).isContradictory(clauses.get(j)))
                {
                    addContradictClause(clauses.get(i), clauses.get(j));
                    return true;
                }
            }
        }//a tu od 0 do firstToCheck
        return false;
    }

    public ArrayList<Clause> getContradictClauses()
    {
        return contradict_clauses;
    }

}

