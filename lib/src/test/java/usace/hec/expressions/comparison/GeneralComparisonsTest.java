package usace.hec.expressions.comparison;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DataHub;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.DoubleVariableNode;


public class GeneralComparisonsTest {

    @Test
    public void testLTEAndLTEvaluate() {
        System.out.print("LTE and LT Test\n");

        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        DataProvider dp = new DataHub();
        BooleanExpressionNode ltNode = new DoubleLessThanNode(x, y);
        BooleanExpressionNode lteNode = new DoubleLessThanOrEqualNode(x, y);

        ltNode.setProvider(dp);
        lteNode.setProvider(dp);

        String expression = ltNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = ltNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        expression = lteNode.PreFixSyntax();
        System.out.print(expression + "\n");
        expressionInfix = lteNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        dp.setDouble("X", 1.0);
        dp.setDouble("Y", 1.0);
        boolean result = ltNode.evaluate(); // 1.0 < 1.0
        boolean result2 = lteNode.evaluate(); // 1.0 <= 1.0
        assertEquals(false, result);
        assertEquals(true, result2);

        dp.setDouble("X", 2.0);
        result = ltNode.evaluate(); // 2.0 < 1.0
        result2 = lteNode.evaluate(); // 2.0 <= 1.0
        assertEquals(false, result);
        assertEquals(false, result2);

        dp.setDouble("Y", 2.1);
        result = ltNode.evaluate(); // 2.0 < 2.1
        result2 = lteNode.evaluate(); // 2.0 <= 2.1
        assertEquals(true, result);
        assertEquals(true, result2);
    }

    @Test
    public void testGTEAndGTEvaluate() {
        System.out.print("GTE and GT Test\n");

        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        DataProvider dp = new DataHub();


        BooleanExpressionNode gtNode = new DoubleGreaterThanNode(x, y);
        BooleanExpressionNode gteNode = new DoubleGreaterThanOrEqualNode(x, y);

        gtNode.setProvider(dp);
        gteNode.setProvider(dp);

        String expression = gtNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = gtNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        expression = gteNode.PreFixSyntax();
        System.out.print(expression + "\n");
        expressionInfix = gteNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");


        dp.setDouble("X", 1.0);
        dp.setDouble("Y", 1.0);
        boolean result = gtNode.evaluate(); // 1.0 > 1.0
        boolean result2 = gteNode.evaluate(); // 1.0 >= 1.0
        assertEquals(false, result);
        assertEquals(true, result2);

        dp.setDouble("X", 2.0);
        result = gtNode.evaluate(); // 2.0 > 1.0
        result2 = gteNode.evaluate(); // 2.0 >= 1.0
        assertEquals(true, result);
        assertEquals(true, result2);

        dp.setDouble("Y", 2.1);
        result = gtNode.evaluate(); // 2.0 > 2.1
        result2 = gteNode.evaluate(); // 2.0 >= 2.1
        assertEquals(false, result);
        assertEquals(false, result2);
    }

    @Test
    public void testEqEvaluate() {
        System.out.print("EQ Test\n");

        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        DataProvider dp = new DataHub();

        BooleanExpressionNode eqNode = new DoubleEqualToNode(x, y);
        eqNode.setProvider(dp);

        String expression = eqNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = eqNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");


        dp.setDouble("X", 1.0);
        dp.setDouble("Y", 1.0);
        boolean result = eqNode.evaluate(); // 1.0 == 1.0
        assertEquals(true, result);

        dp.setDouble("X", 2.0);
        result = eqNode.evaluate(); // 2.0 == 1.0
        assertEquals(false, result);

        dp.setDouble("Y", 2.1);
        result = eqNode.evaluate(); // 2.0 == 2.1
        assertEquals(false, result);
    }

    @Test
    public void testIntegerComparisons() {
        System.out.print("Integer Comparison Test\n");

        usace.hec.expressions.IntegerVariableNode x = new usace.hec.expressions.IntegerVariableNode("X");
        usace.hec.expressions.IntegerVariableNode y = new usace.hec.expressions.IntegerVariableNode("Y");
        DataProvider dp = new DataHub();


        BooleanExpressionNode gtNode = new IntegerGreaterThanNode(x, y);
        BooleanExpressionNode lteNode = new IntegerLessThanOrEqualNode(x, y);
        BooleanExpressionNode eqNode = new IntegerEqualToNode(x, y);

        gtNode.setProvider(dp);

        dp.setInt("X", 5);
        dp.setInt("Y", 3);

        assertEquals(true, gtNode.evaluate()); // 5 > 3
        assertEquals(false, lteNode.evaluate()); // 5 <= 3
        assertEquals(false, eqNode.evaluate()); // 5 == 3

        dp.setInt("X", 3);
        assertEquals(false, gtNode.evaluate()); // 3 > 3
        assertEquals(true, lteNode.evaluate()); // 3 <= 3
        assertEquals(true, eqNode.evaluate()); // 3 == 3
    }
}
