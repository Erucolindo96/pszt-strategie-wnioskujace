package Inference;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
import Inference.strategy.LinearStrategy;
import Inference.strategy.Strategy;

import java.util.ArrayList;
import java.util.Observable;

import static Inference.InferenceProduct.DONT_KNOW;
import static Inference.InferenceProduct.FALSE;
import static Inference.InferenceProduct.TRUE;

/**
 * Klasa realizująca wnioskowanie.
 * Aby wnioskować należy dodać jej bazę wiedzy {@link KnowledgeBase} strategie {@link Strategy}, zgodnie z którą ma wnioskować, a następnie wywołać metodę "inference()"
 * Metoda ta zwraca informację o prawdziwości tezy w postaci enuma {@link InferenceProduct} - mówi on czy teza jest prawdą, czy nie udało się przeprowadzić wnioskowania
 *
 * Klasa jst obiektem obserwowanym - obserwator może pobrać np aktualna baze wiedzy oraz wywnioskowane klauzule
 *
 *
 */
public class InferenceMachine extends Observable{

    private KnowledgeBase knowledgeBase;
    private ArrayList<Clause> newClauses;
    private Strategy strategy;

    public InferenceMachine() {
        super();
        strategy = null;
        knowledgeBase = null;
        newClauses = null;
    }

    public InferenceMachine(KnowledgeBase knowledgeBase, Strategy strategy) {
        super();
        this.knowledgeBase = knowledgeBase;
        this.strategy = strategy;
        newClauses = null;
    }
    public InferenceMachine(InferenceMachine other)
    {
        super();
        strategy = (Strategy)other.strategy.clone();
        knowledgeBase = new KnowledgeBase(other.knowledgeBase);
        newClauses = null;
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

    public ArrayList<Clause> getActualNewClauses()
    {
        return newClauses; //TODO jak zrobione beda dobrze konstruktory kopiujace to zrobic kopiowanie glebokie
    }

    public InferenceProduct inference(Clause thesis) {
        //ArrayList<Clause> newClauses; wykasowalem bo jest teraz polem klasy
        int last;//TODO chyba nieuzywane nigdzie
        do {
            //strategy = new LinearStrategy();
            newClauses = strategy.resolution(knowledgeBase);
            if (newClauses == null) {
                return DONT_KNOW;
            }
            last = knowledgeBase.getClauseCount() - 1; //patrz TODO powyzej
            knowledgeBase.addClause(newClauses);
            notifyObservers(); //ze nastapil nowy krok rezolucyjny
        } while (knowledgeBase.haveContradiction(last));
        return TRUE;
    }
}


