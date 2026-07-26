package usace.hec.expressions.comparison;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.DoubleVariableNode;


public class GeneralComparisonsTest {

    @Test
    public void testLTEAndLTEvaluate() {
        System.out.print("LTE and LT Test\n");

        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        BaseDataUpdater adu = new BaseDataUpdater();

        BooleanExpressionNode ltNode = new DoubleLessThanNode(x, y);
        BooleanExpressionNode lteNode = new DoubleLessThanOrEqualNode(x, y);

        String expression = ltNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = ltNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        expression = lteNode.PreFixSyntax();
        System.out.print(expression + "\n");
        expressionInfix = lteNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        List<DataListener> list = ltNode.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", 1.0);
        adu.publish("Y", 1.0);
        boolean result = ltNode.evaluate(); // 1.0 < 1.0
        boolean result2 = lteNode.evaluate(); // 1.0 <= 1.0
        assertEquals(false, result);
        assertEquals(true, result2);

        adu.publish("X", 2.0);
        result = ltNode.evaluate(); // 2.0 < 1.0
        result2 = lteNode.evaluate(); // 2.0 <= 1.0
        assertEquals(false, result);
        assertEquals(false, result2);

        adu.publish("Y", 2.1);
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
        BaseDataUpdater adu = new BaseDataUpdater();

        BooleanExpressionNode gtNode = new DoubleGreaterThanNode(x, y);
        BooleanExpressionNode gteNode = new DoubleGreaterThanOrEqualNode(x, y);

        String expression = gtNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = gtNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        expression = gteNode.PreFixSyntax();
        System.out.print(expression + "\n");
        expressionInfix = gteNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        List<DataListener> list = gtNode.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", 1.0);
        adu.publish("Y", 1.0);
        boolean result = gtNode.evaluate(); // 1.0 > 1.0
        boolean result2 = gteNode.evaluate(); // 1.0 >= 1.0
        assertEquals(false, result);
        assertEquals(true, result2);

        adu.publish("X", 2.0);
        result = gtNode.evaluate(); // 2.0 > 1.0
        result2 = gteNode.evaluate(); // 2.0 >= 1.0
        assertEquals(true, result);
        assertEquals(true, result2);

        adu.publish("Y", 2.1);
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
        BaseDataUpdater adu = new BaseDataUpdater();

        BooleanExpressionNode eqNode = new DoubleEqualToNode(x, y);

        String expression = eqNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = eqNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        List<DataListener> list = eqNode.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", 1.0);
        adu.publish("Y", 1.0);
        boolean result = eqNode.evaluate(); // 1.0 == 1.0
        assertEquals(true, result);

        adu.publish("X", 2.0);
        result = eqNode.evaluate(); // 2.0 == 1.0
        assertEquals(false, result);

        adu.publish("Y", 2.1);
        result = eqNode.evaluate(); // 2.0 == 2.1
        assertEquals(false, result);
    }

    @Test
    public void testIntegerComparisons() {
        System.out.print("Integer Comparison Test\n");

        usace.hec.expressions.IntegerVariableNode x = new usace.hec.expressions.IntegerVariableNode("X");
        usace.hec.expressions.IntegerVariableNode y = new usace.hec.expressions.IntegerVariableNode("Y");
        BaseDataUpdater adu = new BaseDataUpdater();

        BooleanExpressionNode gtNode = new IntegerGreaterThanNode(x, y);
        BooleanExpressionNode lteNode = new IntegerLessThanOrEqualNode(x, y);
        BooleanExpressionNode eqNode = new IntegerEqualToNode(x, y);

        List<DataListener> list = gtNode.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", 5);
        adu.publish("Y", 3);

        assertEquals(true, gtNode.evaluate()); // 5 > 3
        assertEquals(false, lteNode.evaluate()); // 5 <= 3
        assertEquals(false, eqNode.evaluate()); // 5 == 3

        adu.publish("X", 3);
        assertEquals(false, gtNode.evaluate()); // 3 > 3
        assertEquals(true, lteNode.evaluate()); // 3 <= 3
        assertEquals(true, eqNode.evaluate()); // 3 == 3
    }
}
