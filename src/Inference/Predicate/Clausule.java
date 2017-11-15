package Inference.Predicate;

import java.util.ArrayList;

public class Clausule {

    private ArrayList<Literal> literals;

    public Clausule()
    {
        literals = new ArrayList<>();
    }
    public Clausule(ArrayList<Literal> literals)
    {
        this.literals = literals;
    }

    public void addLiteral(Literal l)
    {
        literals.add(l);
    }
    public Literal getLiteralByOrder(int number)
    {
        return literals.get(number);
    }


}
