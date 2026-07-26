package usace.hec.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.math.AddNode;

public class ExpressionNodeTest {
    @Test
    public void testEvaluate() {
        ExpressionNode<Number> Add = new AddNode(new ConstantLeafNode<Number>(2.0), new ConstantLeafNode<Number>(3.4));
        Double result = ((Number)Add.evaluate()).doubleValue();
        assertEquals(5.4, result, 0.0);
        String expression = Add.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = Add.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
    }

    @Test
    public void testFetchListeners() {
        ExpressionNode<Number> Add = new AddNode(new ConstantLeafNode<Number>(2.0), new ConstantLeafNode<Number>(3.4));
        List<DataListener<?>> list = Add.fetchListeners();
        if (list.size()!=0){
            assertFalse(true);
        }
    }
}
