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

    public int getClauseCount() {
        return clauses.size();
    }

    public Clause getClause(int index) {
        return clauses.get(index);
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
    }
}
