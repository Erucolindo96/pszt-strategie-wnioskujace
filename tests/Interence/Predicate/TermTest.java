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

import java.util.ArrayList;

public class TermTest {


    @Test
    public void VariableCopyConstructorTest()
    {
        Term x = new Variable("x");
        Term cpy = new Variable((Variable)x);
        Assert.assertTrue(cpy.getName().equals(x.getName()));
    }

    @Test
    public void ConstantCopyConstructorTest()
    {
        Term x = new Constant("X");
        Term cpy = new Constant((Constant)x);
        Assert.assertTrue(cpy.getName().equals(x.getName()));

    }


    @Test
    public void FunctionCopyConstructorTest()
    {
        Term zmienna = new Variable("zmienna");
        Term ZMIENNA = new Constant("ZMIENNA");
        Term foo = new Function("foo", zmienna);//foo(zmienna)
        ArrayList<Term> args = new ArrayList<>();
        args.add(zmienna);
        args.add(ZMIENNA);
        args.add(foo);
        Function f = new Function("funkcja", args);//funkcja(zmienna, ZMIENNA, foo(zmienna) )
        Function cpy = new Function((Function)f);

        Assert.assertTrue(f.getName().equals(cpy.getName())); //nazwy te same
        //czy typy zgodne
        Assert.assertTrue(f.getArgument(0).isVariable());
        Assert.assertTrue(cpy.getArgument(0).isVariable());
        Assert.assertTrue(f.getArgument(1).isConstant());
        Assert.assertTrue(cpy.getArgument(1).isConstant());
        Assert.assertTrue(f.getArgument(2).isFunction());
        Assert.assertTrue(cpy.getArgument(2).isFunction());

        //czy nazwy zgodne
        Assert.assertTrue(f.getArgument(0).getName().equals(cpy.getArgument(0).getName()));
        Assert.assertTrue(cpy.getArgument(1).getName().equals(f.getArgument(1).getName()));
        Assert.assertTrue(f.getArgument(2).getName().equals(cpy.getArgument(2).getName()));

        //czy podfunkcja "foo" jest dobra:
        Term foo_cpy = cpy.getArgument(2);
        Assert.assertTrue(foo_cpy.isFunction()); //typ
        Assert.assertTrue(foo_cpy.getName().equals("foo")); //nazwa
        Function foo_casted = (Function)foo_cpy;
        Assert.assertTrue(foo_casted.getArgument(0).isVariable()); //czy typ argumentu dobry
        Assert.assertTrue(foo_casted.getArgument(0).getName().equals(zmienna.getName())); //czy nazwa argumentu dobra
    }

    @Test
    public void equalsIdentityTermsVariableAndConstant()
    {
        Term x = new Variable("x");
        Term cpy_x = new Variable((Variable)x);
        Assert.assertTrue(cpy_x.equals(x));

        Term X = new Constant("X");
        Term cpy_X = new Constant((Constant)X);
        Assert.assertTrue(cpy_X.equals(X));

        Assert.assertFalse(cpy_X.equals(cpy_x));
    }



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
