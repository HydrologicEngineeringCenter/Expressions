package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.DataHub;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.DoubleVariableNode;

public class MaxMinTest {

    @Test
    public void testEvaluate() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        DataProvider dp = new DataHub();

        DoubleExpressionNode max = new DoubleMaxNode(x, y);
        DoubleExpressionNode min = new DoubleMinNode(x, y);

        max.setProvider(dp);
        min.setProvider(dp);


        dp.setDouble("X", 1.0);
        dp.setDouble("Y", 1.0);

        double result = max.evaluate();
        double result2 = min.evaluate();
        assertEquals(1.0, result, 0.0);
        assertEquals(1.0, result2, 0.0);

        dp.setDouble("X", 2.0);
        result = max.evaluate();
        result2 = min.evaluate();
        assertEquals(2.0, result, 0.0);
        assertEquals(1.0, result2, 0.0);

        dp.setDouble("Y", 10.0);
        result = max.evaluate();
        result2 = min.evaluate();
        assertEquals(10.0, result, 0.0);
        assertEquals(2.0, result2, 0.0);

        dp.setDouble("X", -13.0);
        dp.setDouble("Y", 3.0);
        result = max.evaluate();
        result2 = min.evaluate();
        assertEquals(3.0, result, 0.0);
        assertEquals(-13.0, result2, 0.0);

        dp.setDouble("Y", -1000.0);
        result = max.evaluate();
        result2 = min.evaluate();
        assertEquals(-13.0, result, 0.0);
        assertEquals(-1000.0, result2, 0.0);
    }

    @Test
    public void testSyntax() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");

        DoubleExpressionNode max = new DoubleMaxNode(x, y);
        DoubleExpressionNode min = new DoubleMinNode(x, y);

        String expression = max.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = max.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        String expression2 = min.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = min.ExcelSyntax();
        System.out.print(expression2Infix + "\n");
    }
}