package Inference.Strategy;

import Inference.Predicate.KnowlegeBase;

public abstract class Strategy {

    public Strategy()
    {
        throw new RuntimeException("TODO");
    }

    public abstract KnowlegeBase resolution(KnowlegeBase knowlege_base, KnowlegeBase zb_uzasadnien);

}
