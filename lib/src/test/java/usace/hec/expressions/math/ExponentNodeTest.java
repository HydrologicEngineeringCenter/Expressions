package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.DoubleVariableNode;

public class ExponentNodeTest {

    @Test
    public void testEvaluate() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        BaseDataUpdater adu = new BaseDataUpdater();

        DoubleExpressionNode exp = new DoubleExponentNode(x, y);

        List<DataListener> list = exp.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", 1.0);
        adu.publish("Y", 2.0);
        double result = exp.evaluate();
        assertEquals(1.0, result, 0.0);

        adu.publish("X", 2.0);
        result = exp.evaluate();
        assertEquals(4.0, result, 0.0);

        adu.publish("Y", 0.0);
        result = exp.evaluate();
        assertEquals(1.0, result, 0.0);

        adu.publish("X", 9.0);
        adu.publish("Y", 0.5);
        result = exp.evaluate();
        assertEquals(3.0, result, 0.0);

        adu.publish("X", -1000.0);
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, () -> exp.evaluate());
        assertEquals("Imaginary number unsupported", ex.getMessage());
    }

    @Test
    public void testSyntax() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");

        DoubleExpressionNode exp = new DoubleExponentNode(x, y);

        String expression = exp.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = exp.ExcelSyntax();
        System.out.print(expressionInfix + "\n");
    }
}