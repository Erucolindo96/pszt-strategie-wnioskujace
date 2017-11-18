package Inference.Predicate;

public class Literal {

    private Predicate predicate;
    private boolean is_negation;

    public Literal()
    {
        predicate = new Predicate();
        is_negation = false;
    }
    public Literal(Predicate p, boolean negation)
    {
        predicate = p;
        is_negation = negation;
    }
    public Literal getUnificator(Predicate other) {
        throw new RuntimeException("TODO");
    }



}
