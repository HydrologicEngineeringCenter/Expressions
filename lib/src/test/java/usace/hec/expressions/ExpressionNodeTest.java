package usace.hec.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.math.DoubleAddNode;

public class ExpressionNodeTest {

    @Test
    public void testEvaluate() {
        DoubleExpressionNode add = new DoubleAddNode(
                new DoubleConstantNode(2.0),
                new DoubleConstantNode(3.4));

        double result = add.evaluate();
        assertEquals(5.4, result, 0.0);

        String expression = add.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = add.ExcelSyntax();
        System.out.print(expressionInfix + "\n");
    }

    @Test
    public void testFetchListeners() {
        DoubleExpressionNode add = new DoubleAddNode(
                new DoubleConstantNode(2.0),
                new DoubleConstantNode(3.4));

        List<DataListener> list = add.fetchListeners();
        // Constants don't listen to data, so list should be empty
        assertFalse(list.size() != 0);
    }
}