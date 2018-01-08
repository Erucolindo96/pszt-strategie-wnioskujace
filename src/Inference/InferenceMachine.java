package Inference;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
import Inference.strategy.JustificationSetStrategy;
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
    private KnowledgeBase justification_set;
    private ArrayList<Clause> newClauses;
    private Strategy strategy;

    public InferenceMachine() {
        super();
        strategy = null;
        knowledgeBase = null;
        justification_set = null;
        newClauses = null;
    }

    public InferenceMachine(KnowledgeBase knowledgeBase, Strategy strategy) {
        super();
        this.knowledgeBase = knowledgeBase;
        this.strategy = strategy;
        newClauses = null;
        justification_set = null;
    }
    public InferenceMachine(InferenceMachine other)
    {
        super();
        strategy = (Strategy)other.strategy.clone();
        knowledgeBase = new KnowledgeBase(other.knowledgeBase);
        justification_set = null;
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
    public int getInferenceStep()
    {
        return strategy.getStep();
    }

    private void addAntithesis()
    {
        ArrayList<Clause> antithesises=knowledgeBase.getThesis();
        if(strategy instanceof JustificationSetStrategy)
        {//utworz zb uzasadnien
            justification_set = new KnowledgeBase();
            justification_set.addClause(antithesises);
        }
        else
        {//po prostu dodaj antyteze do bazy wiedzy
            knowledgeBase.addClause(antithesises);
        }

    }

    public InferenceProduct inference() {
        addAntithesis();
        int last;
        do {
            newClauses = strategy.resolution(knowledgeBase, justification_set);
            if (newClauses == null)  //nie mozna wytworzyc nowych klauzul
            {
                return DONT_KNOW;
            }
            last = knowledgeBase.getClauseCount() - 1;

            knowledgeBase.addClause(newClauses);
            if(justification_set != null)
                justification_set.addClause(newClauses);

            setChanged();
            notifyObservers(); //ze nastapil nowy krok rezolucyjny
        } while (!knowledgeBase.haveContradiction(last));
        return TRUE;
    }
}


