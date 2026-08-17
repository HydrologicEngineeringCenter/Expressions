package usace.hec.expressions.math;

import org.junit.Test;
import usace.hec.expressions.*;

import static org.junit.Assert.*;

public class MultiDivTest {

    @Test
    public void testEvaluate() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        DataProvider dp = new DataHub();

        DoubleExpressionNode multi = new DoubleMultiplyNode(x, y);
        DoubleExpressionNode div = new DoubleDivideNode(x, y);

        multi.setProvider(dp);
        div.setProvider(dp);

        dp.setDouble("X", 1.0);
        dp.setDouble("Y", 1.0);

        double result = multi.evaluate();
        double result2 = div.evaluate();
        assertEquals(1.0, result, 0.0);
        assertEquals(1.0, result2, 0.0);

        dp.setDouble("Y", 2.0);
        result = multi.evaluate();
        result2 = div.evaluate();
        assertEquals(2.0, result, 0.0);
        assertEquals(0.5, result2, 0.0);

        dp.setDouble("X", 16.0);
        result = multi.evaluate();
        result2 = div.evaluate();
        assertEquals(32.0, result, 0.0);
        assertEquals(8.0, result2, 0.0);

        dp.setDouble("X", 3.0);
        result = multi.evaluate();
        assertEquals(6.0, result, 0.0);

        dp.setDouble("Y", 0.0);
        result = div.evaluate();
        assertEquals(0.0,result,0.0);
        assertTrue(div.hasError());
    }

    @Test
    public void testIntegerEvaluate() {
        IntegerVariableNode x = new IntegerVariableNode("X");
        IntegerVariableNode y = new IntegerVariableNode("Y");
        DataProvider dp = new DataHub();

        IntegerExpressionNode multi = new IntegerMultiplyNode(x, y);
        IntegerExpressionNode div = new IntegerDivideNode(x, y);

        multi.setProvider(dp);
        div.setProvider(dp);

        dp.setInt("X", 1);
        dp.setInt("Y", 1);

        double result = multi.evaluate();
        double result2 = div.evaluate();
        assertEquals(1, result, 0);
        assertEquals(1, result2, 0);

        dp.setInt("Y", 2);
        result = multi.evaluate();
        result2 = div.evaluate();
        assertEquals(2, result, 0);
        assertEquals(0, result2, 0);

        dp.setInt("X", 16);
        result = multi.evaluate();
        result2 = div.evaluate();
        assertEquals(32, result, 0);
        assertEquals(8, result2, 0);

        dp.setInt("X", 3);
        result = multi.evaluate();
        assertEquals(6, result, 0);

        dp.setInt("Y", 0);
        result = div.evaluate();
        assertEquals(0,result,0);
        assertTrue(div.hasError());
    }

    @Test
    public void testSyntax() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");

        DoubleExpressionNode multi = new DoubleMultiplyNode(x, y);
        DoubleExpressionNode div = new DoubleDivideNode(x, y);

        String expression = multi.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = multi.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        String expression2 = div.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = div.ExcelSyntax();
        System.out.print(expression2Infix + "\n");
    }
}