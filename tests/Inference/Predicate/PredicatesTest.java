package Inference.Predicate;

import Inference.Predicate.Terms.Constant;
import Inference.Predicate.Terms.Function;
import Inference.Predicate.Terms.Term;
import Inference.Predicate.Terms.Variable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


public class PredicatesTest {

    private Predicate predicate_X_y;
    private Predicate predicate_x_Y;
    private Predicate predicate_func_func;
    private Term constX;
    private Term constY;
    private Term varY;
    private Term varX;
    private Term func0;
    private Term func1;

    @Before
    public void initialize() {
        constX = new Constant("X");
        constY = new Constant("Y");
        varY = new Variable("y");
        varX = new Variable("x");
        ArrayList<Term> list0 = new ArrayList<>();
        list0.add(constX);
        list0.add(varY);
        ArrayList<Term> list1 = new ArrayList<>();
        list1.add(varX);
        list1.add(constY);

        func0 = new Function("F", varX);
        ArrayList<Term> list2 = new ArrayList<>();
        list2.add(func0);
        list2.add(func0);

        predicate_X_y = new Predicate("P", list0);
        predicate_x_Y = new Predicate("P", list1);
        predicate_func_func = new Predicate("P", list2);
    }

    @Test
    public void createPredicateWithConstants() {
        Assert.assertEquals(predicate_X_y.getArguments().get(0), constX);
        Assert.assertEquals(predicate_x_Y.getArguments().get(1), constY);
    }

    @Test
    public void createPredicateWithAndVariables() {
        Assert.assertEquals(predicate_X_y.getArguments().get(1), varY);
        Assert.assertEquals(predicate_x_Y.getArguments().get(0), varX);
    }

    @Test
    public void createPredicateWithFunction() {
        Assert.assertEquals(predicate_func_func.getArguments().get(0), func0);
        Assert.assertEquals(((Function) predicate_func_func.getArguments().get(0)).getArgs().get(0), varX);
    }

    @Test
    public void mergeWithPredicateHavingDifferentName() {
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(varX);
        terms.add(varX);
        Predicate other = new Predicate("B", terms);
        Assert.assertNull(other.getUnificator(predicate_x_Y));
    }

    @Test
    public void mergeWithPredicateHavingMoreOrLessArguments() {
        ArrayList<Term> terms = new ArrayList<>();
        terms.add(varX);
        Predicate other = new Predicate("P", terms);
        Assert.assertNull(other.getUnificator(predicate_x_Y));
        Assert.assertNull(predicate_x_Y.getUnificator(other));
    }

    @Test
    public void mergeConstAndVariable() {
        Predicate mergerd = predicate_X_y.getUnificator(predicate_x_Y);
        Assert.assertNotNull(mergerd);
        Assert.assertEquals("Varables should be replaced by constants", constX, mergerd.getArguments().get(0));
        Assert.assertEquals("Varables should be replaced by constants", constY, mergerd.getArguments().get(1));
    }

    @Test
    public void mergeFunctionWithConst() {
        Predicate mergerd = predicate_func_func.getUnificator(predicate_X_y);
        Assert.assertNotNull(mergerd);
        Assert.assertTrue("First term should be funtion", mergerd.getArguments().get(0).isFunction());
        Assert.assertEquals("New function should have the same name", func0.getName(), mergerd.getArguments().get(0).getName());
        Assert.assertEquals("Constants should be place inside function", constX, ((Function) mergerd.getArguments().get(0)).getArgs().get(0));
    }

    @Test
    public void mergeFunctionWithVariable() {
        Predicate mergerd = predicate_func_func.getUnificator(predicate_X_y);
        Assert.assertTrue("Second term should be function", mergerd.getArguments().get(1).isFunction());
        Assert.assertEquals("Function should not be chanege", func0, mergerd.getArguments().get(1));
    }

    @Test
    public void mergeFunctionWithFunction() {
        ArrayList<Term> terms = new ArrayList<>();
        Term funct_X = new Function("F", constX);
        terms.add(funct_X);
        Predicate predicate0 = new Predicate("P", terms);

        ArrayList<Term> terms1 = new ArrayList<>();
        Term funct_x = new Function("F", varX);
        terms1.add(funct_x);
        Predicate predicate1 = new Predicate("P", terms1);

        Predicate merged = predicate0.getUnificator(predicate1);
        Predicate merged1 = predicate1.getUnificator(predicate0);

        Assert.assertNotNull(merged);
        Assert.assertEquals("Const should not be replaced", constX, ((Function) merged.getArguments().get(0)).getArgs().get(0));

        Assert.assertNotNull(merged1);
        Assert.assertEquals("Variable should be replaced by const", constX, ((Function) merged1.getArguments().get(0)).getArgs().get(0));
    }
    @Test
    public void mergeFunctionWithVarAndFunctionWithFunction() {
        ArrayList<Term> terms = new ArrayList<>();
        Term funct_X = new Function("F", constX);
        Term funct = new Function("F", funct_X);
        terms.add(funct);
        Predicate predicate0 = new Predicate("P", terms);

        ArrayList<Term> terms1 = new ArrayList<>();
        Term funct_x = new Function("F", varX);
        terms1.add(funct_x);
        Predicate predicate1 = new Predicate("P", terms1);

        Predicate merged = predicate0.getUnificator(predicate1);
        Predicate merged1 = predicate1.getUnificator(predicate0);

        Assert.assertNotNull(merged);
        Assert.assertEquals("Variable should be replaced by function", funct_X, ((Function) merged.getArguments().get(0)).getArgs().get(0));

        Assert.assertNotNull(merged1);
        Assert.assertEquals("Variable should be replaced by function", constX, ((Function) merged1.getArguments().get(0)).getArgs().get(0));
    }

}
