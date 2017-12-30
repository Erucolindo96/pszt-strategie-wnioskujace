package Inference.Predicate;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class KnowledgeBase {
    private ArrayList<Clause> clauses = new ArrayList<>();


    public void loadFromFile(String fileName) throws IOException {
        Path path = FileSystems.getDefault().getPath("ClausesFiles", fileName);
        Files.lines(path).forEach(line -> clauses.add(new Clause(line)));
    }
    public KnowledgeBase()
    {
        clauses = new ArrayList<>();
    }
    public KnowledgeBase(KnowledgeBase other)
    {
        clauses = new ArrayList<>(other.clauses);
    }

    /**
     * Zwraca roznice zbiorow
     * @param other Zbior odejmowany
     * @return Roznice zbiorow
     */
    public KnowledgeBase getDifference(KnowledgeBase other)
    {
        KnowledgeBase diff = new KnowledgeBase(this);
        for(Clause c: other.clauses)
        {
            while(diff.clauses.remove(c));//usuwaj klauzule dopoki znajduja sie jakies kopie w ArrayLiscie
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
        clauses.add(clause);
    }

    public void addClause(ArrayList<Clause> newClauses) {
        clauses.addAll(newClauses);
    }

    public boolean haveContradiction(int firstToCheck) {
/*        for (int i = firstToCheck; i < clauses.size(); ++i) {
            for (int j = 0; j < i; ++j) {
                if (clauses.get(i).isContradictory(clauses.get(j)))
                    return true;
            }
        }
*/
        if(firstToCheck < 0 || clauses.size() <= firstToCheck)
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
