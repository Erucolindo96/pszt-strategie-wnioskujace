package Inference.Predicate;

import Inference.Predicate.Terms.Term;

import java.util.ArrayList;

public class Predicate {

    private String name;
    private ArrayList<Term> arguments;

    //konstruktory
    public Predicate()
    {
        name = "";
        arguments = new ArrayList<>();
    }
    public Predicate(String name, ArrayList<Term> terms)
    {
        this.name = name;
        this.arguments = terms;
    }

    // Gettery i settery
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }

    public ArrayList<Term> getArguments()
    {
        return new ArrayList<>(arguments);
    }

    public void setArguments(ArrayList<Term> terms)
    {
        arguments = terms;
    }

    public int getArgumentsCount()
    {
        return arguments.size();
    }

    //metody wlasciwe

    /**
     * Ta relacja nie musi być zwrotna
     *
     */
    public boolean isUnificableWith(Predicate other)
    {
        throw new RuntimeException("TODO");
    }
    public boolean isInstationOf(Predicate other)
    {
        return name.equals(other.name) && arguments.size() == other.arguments.size();
    }
    /**
     * Ta relacja nie musi być zwrotna
     *
     */
    public ArrayList<Term> getMaxCommonUnificator(Predicate other)
    {
        throw new RuntimeException("TODO");
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Predicate)
        {
            if(name.equals( ((Predicate) other).name) && arguments.size() == ((Predicate) other).arguments.size())
            {
                boolean ret = true;
                for (int i = 0; i< arguments.size(); ++i )
                {
                     if(!((Predicate) other).arguments.get(i).equals(arguments.get(i))) //jesli odpowiednie termy nie sa rowne zwroc falsz
                         ret = false;
                }
                return ret;
            }
        }
        return false;
    }

}
