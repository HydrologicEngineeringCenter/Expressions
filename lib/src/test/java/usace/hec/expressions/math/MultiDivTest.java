package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.DoubleVariableNode;

public class MultiDivTest {

    @Test
    public void testEvaluate() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        BaseDataUpdater adu = new BaseDataUpdater();

        DoubleExpressionNode multi = new DoubleMultiplyNode(x, y);
        DoubleExpressionNode div = new DoubleDivideNode(x, y);

        List<DataListener> list = multi.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", 1.0);
        adu.publish("Y", 1.0);

        double result = multi.evaluate();
        double result2 = div.evaluate();
        assertEquals(1.0, result, 0.0);
        assertEquals(1.0, result2, 0.0);

        adu.publish("Y", 2.0);
        result = multi.evaluate();
        result2 = div.evaluate();
        assertEquals(2.0, result, 0.0);
        assertEquals(0.5, result2, 0.0);

        adu.publish("X", 16.0);
        result = multi.evaluate();
        result2 = div.evaluate();
        assertEquals(32.0, result, 0.0);
        assertEquals(8.0, result2, 0.0);

        adu.publish("X", 3.0);
        result = multi.evaluate();
        assertEquals(6.0, result, 0.0);

        adu.publish("Y", 0.0);
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> div.evaluate());
        assertEquals("Division by zero", ex.getMessage());
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