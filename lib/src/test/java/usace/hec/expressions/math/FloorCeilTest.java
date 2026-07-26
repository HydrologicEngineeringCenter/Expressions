package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.DoubleVariableNode;

public class FloorCeilTest {

    @Test
    public void testEvaluate() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        BaseDataUpdater adu = new BaseDataUpdater();

        DoubleExpressionNode ceil = new DoubleCeilingNode(x);
        DoubleExpressionNode floor = new DoubleFloorNode(x);

        List<DataListener> list = ceil.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", 1.5);
        double result = ceil.evaluate();
        assertEquals(2.0, result, 0.0);
        result = floor.evaluate();
        assertEquals(1.0, result, 0.0);

        adu.publish("X", -2.5);
        result = ceil.evaluate();
        assertEquals(-2.0, result, 0.0);
        result = floor.evaluate();
        assertEquals(-3.0, result, 0.0);

        adu.publish("X", 0.0);
        result = ceil.evaluate();
        assertEquals(0.0, result, 0.0);
        result = floor.evaluate();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testSyntax() {
        DoubleVariableNode x = new DoubleVariableNode("X");

        DoubleExpressionNode ceil = new DoubleCeilingNode(x);
        DoubleExpressionNode floor = new DoubleFloorNode(x);

        String expression = ceil.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = ceil.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        String expression2 = floor.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = floor.ExcelSyntax();
        System.out.print(expression2Infix + "\n");
    }
}