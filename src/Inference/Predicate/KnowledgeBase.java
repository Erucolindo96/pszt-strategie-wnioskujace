package Inference.Predicate;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

    public class KnowledgeBase {
        private ArrayList<Clause> clauses = new ArrayList<>();
        private Clause thesis;

        /**
         * last clause in file is thesis;
         */
    public void loadFromFile(String pathString) throws IOException {
        Path path = FileSystems.getDefault().getPath(pathString);
        Files.lines(path).forEach(line -> clauses.add(new Clause(line)));
//        TODO zapytać sie jakiej postaci moze byc teza, obacnie zakladamy ze jest postaci klauzuli prostej.
// A negacja tezy (p1 v p2 v p3) to (-p1 v -p2 v -p3) xd sry takie mamy testy xD, mogę ro zamienić, ale najpiewr spójrz ze wnioskowanie dla clausesTrue.txt działa, tylko się kroki nie wyświetlają
        thesis = clauses.remove(clauses.size() - 1);
    }

    public KnowledgeBase() {
        clauses = new ArrayList<>();
    }

    public KnowledgeBase(KnowledgeBase other) {
        clauses = new ArrayList<>();
        for(Clause c: other.clauses)
        {
            clauses.add((Clause)c.clone());
        }

        thesis = new Clause(other.thesis);
    }

    public Clause getThesis() {
        return thesis;
    }
    public Clause getAntithesis(){
        Clause antithesis= new Clause(thesis);
        for (int i=0; i<antithesis.getCount(); ++i){
            antithesis.getLiteral(i).negate();//TODO to chyba nie tak dziala - trzeb utworzyc tyle klauzul, ile jest literałów w tezie
        }
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
/*        for (int i = firstToCheck; i < clauses.size(); ++i) {
            for (int j = 0; j < i; ++j) {
                if (clauses.get(i).isContradictory(clauses.get(j)))
                    return true;
            }
        }
*/
        if (firstToCheck < 0 || clauses.size() <= firstToCheck)
            throw new RuntimeException("Zla wartosc firstToCheck - mniejsza od 0 lub wieksza niz ilosc klauzul");

        for (int i = firstToCheck; i < clauses.size(); ++i) {
            for (int j = 0; j < i; ++j) {
                if (clauses.get(i).isContradictory(clauses.get(j)))
                    return true;
            }
        }//przechodzi po klauzulach od firstTocheck w gore
        for (int i = 0; i < firstToCheck; ++i) {
            for (int j = 0; j < i; ++j) {
                if (clauses.get(i).isContradictory(clauses.get(j)))
                    return true;
            }
        }//a tu od 0 do firstToCheck

        return false;
    }
}
