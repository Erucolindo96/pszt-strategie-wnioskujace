/**
 * A co z przypadkiem P(x, F(x)) + Q(y, N)
 * Wypadalo dodac by sprawdzenie equals dla funkcji
 */

package Interence.Predicate;

import Inference.Predicate.Clause;
import Inference.Predicate.Terms.Constant;
import Inference.Predicate.Terms.Function;
import Inference.Predicate.Terms.Term;
import Inference.Predicate.Terms.Variable;

import org.junit.Assert;
import org.junit.Test;

public class TermTest {

    @Test
    public void mergeConstWithVariable() {
        Variable var = new Variable("x");
        Constant constant = new Constant("X");
        Term merged = constant.merge(var);

        Assert.assertNotNull(merged);
        Assert.assertEquals(constant, merged);
    }

    @Test
    public void mergeVariableWithConst() {
        Variable var = new Variable("x");
        Constant constant = new Constant("X");
        Term merged = var.merge(constant);
        Assert.assertNotNull(merged);
        Assert.assertEquals(constant, merged);
    }

    @Test
    public void mergeVariableWithVariable() {
        Variable var = new Variable("x");
        Variable var1 = new Variable("y");

        Term merged = var.merge(var1);
        Assert.assertNotNull(merged);
        Assert.assertEquals("test1", var, merged);

        Term merged1 = var1.merge(var);
        Assert.assertNotNull(merged1);
        Assert.assertEquals("test2", var1, merged1);
    }

    @Test
    public void mergeConstWithConst() {
        Constant constant = new Constant("X");
        Constant constant1 = new Constant("Y");
        Term merged = constant.merge(constant1);

        Assert.assertNull(merged);
    }

    @Test
    public void mergeFunctionWithVariableAndConst() {
        Variable var = new Variable("x");
        Constant constant1 = new Constant("Y");
        Function function = new Function("F", var);
        Term merged = function.merge(constant1);

        Assert.assertNotNull(merged);
        Assert.assertTrue("New term is function", merged.isFunction());
        Assert.assertEquals("function argument is constant", constant1, ((Function) merged).getArgument(0));
        Assert.assertEquals("Term's name is correct", "F(Y)", merged.toString());

    }

    @Test
    public void mergeConstAndFunctionWithVariable() {
        Variable var = new Variable("x");
        Constant constant1 = new Constant("Y");
        Function function = new Function("F", var);
        Term merged = constant1.merge(function);

        Assert.assertNotNull(merged);
        Assert.assertEquals("New term is constant", constant1, merged);

    }

    @Test
    public void mergeVariableAndFunctionWithVariable() {
        Variable var = new Variable("x");
        Variable var1 = new Variable("y");
        Function function = new Function("F", var);
        Term merged = var.merge(function);

        Assert.assertNotNull(merged);
        Assert.assertTrue("New term is function", merged.isFunction());
        Assert.assertEquals("function argument wasn't changed", var, ((Function) merged).getArgument(0));
    }

    @Test
    public void mergeFunctionWithVariableAndVariable() {
        Variable var = new Variable("x");
        Variable var1 = new Variable("y");
        Function function = new Function("F", var);
        Term merged = function.merge(var);

        Assert.assertNotNull(merged);
        Assert.assertEquals("function wasn't changed", function, merged);
    }
}
