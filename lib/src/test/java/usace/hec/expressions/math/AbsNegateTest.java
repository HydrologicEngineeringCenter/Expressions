package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.DoubleVariableNode;

public class AbsNegateTest {

    @Test
    public void testEvaluate() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        BaseDataUpdater adu = new BaseDataUpdater();

        DoubleExpressionNode neg = new DoubleNegateNode(x);
        DoubleExpressionNode abs = new DoubleAbsNode(neg);

        List<DataListener> list = abs.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", 1.0);
        double result = neg.evaluate();
        assertEquals(-1.0, result, 0.0);
        result = abs.evaluate();
        assertEquals(1.0, result, 0.0);

        adu.publish("X", -2.0);
        result = neg.evaluate();
        assertEquals(2.0, result, 0.0);
        result = abs.evaluate();
        assertEquals(2.0, result, 0.0);

        adu.publish("X", 0.0);
        result = neg.evaluate();
        assertEquals(0.0, result, 0.0);
        result = abs.evaluate();
        assertEquals(0.0, result, 0.0);

        DoubleExpressionNode doubleNeg = new DoubleNegateNode(neg);
        adu.publish("X", 500.0);
        result = doubleNeg.evaluate();
        assertEquals(500.0, result, 0.0);

        adu.publish("X", -500.0);
        result = doubleNeg.evaluate();
        assertEquals(-500.0, result, 0.0);
    }

    @Test
    public void testSyntax() {
        DoubleVariableNode x = new DoubleVariableNode("X");

        DoubleExpressionNode neg = new DoubleNegateNode(x);
        DoubleExpressionNode abs = new DoubleAbsNode(neg);

        String expression = neg.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = neg.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        String expression2 = abs.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = abs.ExcelSyntax();
        System.out.print(expression2Infix + "\n");

        DoubleExpressionNode doubleNeg = new DoubleNegateNode(neg);
        String expression3 = doubleNeg.PreFixSyntax();
        System.out.print(expression3 + "\n");
        String expression3Infix = doubleNeg.ExcelSyntax();
        System.out.print(expression3Infix + "\n");
    }
}