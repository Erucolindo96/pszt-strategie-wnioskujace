package Inference;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
import Inference.strategy.LinearStrategy;
import Inference.strategy.Strategy;

import java.util.ArrayList;

import static Inference.InferenceProduct.FALSE;
import static Inference.InferenceProduct.TRUE;

/**
 * Klasa realizująca wnioskowanie.
 * Aby wnioskować należy dodać jej bazę wiedzy {@link KnowledgeBase} strategie {@link Strategy}, zgodnie z którą ma wnioskować, a następnie wywołać metodę "inference()"
 * Metoda ta zwraca informację o prawdziwości tezy w postaci enuma {@link InferenceProduct} - mówi on czy teza jest prawdą, czy nie udało się przeprowadzić wnioskowania
 */
public class InferenceMachine {

    private KnowledgeBase knowledgeBase;

    private Strategy strategy;

    public InferenceMachine() {
        strategy = null;
        knowledgeBase = null;
    }

    public InferenceMachine(KnowledgeBase knowledgeBase, Strategy strategy) {
        this.knowledgeBase = knowledgeBase;
        this.strategy = strategy;
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
        this.knowledgeBase = knowledgeBase;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public InferenceProduct inference(Clause thesis) {
        ArrayList<Clause> newClauses;
        int last;
        do {
            strategy = new LinearStrategy();
            newClauses = strategy.resolution(knowledgeBase);
            if (newClauses == null) {
                return FALSE;
            }
            last = knowledgeBase.getClauseCount() - 1;
            knowledgeBase.addClause(newClauses);
        } while (knowledgeBase.haveContradiction(last));
        return TRUE;
    }
}


