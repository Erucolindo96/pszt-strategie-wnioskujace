package Inference;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
import Inference.strategy.Strategy;

/**
 * Klasa realizująca wnioskowanie.
 * Aby wnioskować należy dodać jej bazę wiedzy {@link KnowledgeBase} strategie {@link Strategy}, zgodnie z którą ma wnioskować, a następnie wywołać metodę "inference()"
 * Metoda ta zwraca informację o prawdziwości tezy w postaci enuma {@link InferenceProduct} - mówi on czy teza jest prawdą, czy nie udało się przeprowadzić wnioskowania
 */
public class InferenceMashine {

    private KnowledgeBase knowlege_base;
    private KnowledgeBase zb_uzasadnien;

    private Strategy strategy;

    public InferenceMashine()
    {
        strategy = null;
        knowlege_base = null;
        zb_uzasadnien = null;
    }
    public InferenceMashine(KnowledgeBase knowlege_base, KnowledgeBase zb_uzasadnien, Strategy strategy )
    {
        this.knowlege_base = knowlege_base;
        this.zb_uzasadnien = zb_uzasadnien;
        this.strategy = strategy;
    }

    public void addStrategy(Strategy s)
    {
        strategy = s;
    }
    public void addKnowlegeBase(KnowledgeBase base)
    {
        knowlege_base = base;
    }
    public void addZbUzasadnien(KnowledgeBase zb_uzas)
    {
        this.zb_uzasadnien = zb_uzas;
    }

    public InferenceProduct inference(Clause thesy)
    {
        throw new RuntimeException("TODO");
    }


}
