package Inference;

import Inference.Predicate.Clause;
import Inference.Predicate.KnowlegeBase;
import Inference.Strategy.Strategy;

/**
 * Klasa realizująca wnioskowanie.
 * Aby wnioskować należy dodać jej bazę wiedzy {@link KnowlegeBase} strategie {@link Strategy}, zgodnie z którą ma wnioskować, a następnie wywołać metodę "inference()"
 * Metoda ta zwraca informację o prawdziwości tezy w postaci enuma {@link InferenceProduct} - mówi on czy teza jest prawdą, czy nie udało się przeprowadzić wnioskowania
 */
public class InferenceMashine {

    private KnowlegeBase knowlege_base;
    private KnowlegeBase zb_uzasadnien;

    private Strategy strategy;

    public InferenceMashine()
    {
        strategy = null;
        knowlege_base = null;
        zb_uzasadnien = null;
    }
    public InferenceMashine(KnowlegeBase knowlege_base, KnowlegeBase zb_uzasadnien, Strategy strategy )
    {
        this.knowlege_base = knowlege_base;
        this.zb_uzasadnien = zb_uzasadnien;
        this.strategy = strategy;
    }

    public void addStrategy(Strategy s)
    {
        strategy = s;
    }
    public void addKnowlegeBase(KnowlegeBase base)
    {
        knowlege_base = base;
    }
    public void addZbUzasadnien(KnowlegeBase zb_uzas)
    {
        this.zb_uzasadnien = zb_uzas;
    }

    public InferenceProduct inference(Clause thesy)
    {
        throw new RuntimeException("TODO");
    }


}
