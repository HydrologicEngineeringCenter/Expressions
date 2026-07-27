package usace.hec.expressions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoercionNodeTest {

    @Test
    public void integerToDoubleCoerceNodeEvaluate() {
        IntegerConstantNode intNode = new IntegerConstantNode(42);
        DoubleExpressionNode coerceNode = new IntegerToDoubleCoerceNode(intNode);

        assertEquals(42.0, coerceNode.evaluate(), 0.0);
        assertEquals(ExpressionType.DOUBLE, coerceNode.resultType());
    }

    @Test
    public void integerToDoubleCoerceNodeSyntax() {
        IntegerConstantNode intNode = new IntegerConstantNode(42);
        DoubleExpressionNode coerceNode = new IntegerToDoubleCoerceNode(intNode);

        assertEquals("TODOUBLE(42)", coerceNode.PreFixSyntax());
        assertEquals("TODOUBLE(42)", coerceNode.ExcelSyntax());
    }

    @Test
    public void integerToDoubleCoerceNodePropagatesListeners() {
        IntegerVariableNode var = new IntegerVariableNode("X");
        IntegerToDoubleCoerceNode coerceNode = new IntegerToDoubleCoerceNode(var);

        java.util.List<DataListener> listeners = coerceNode.fetchListeners();
        assertEquals(1, listeners.size());
        assertEquals("X", ((IntegerVariableNode)listeners.get(0).owner()).getName());
    }

    @Test
    public void integerToDoubleCoerceNodePropagatesProvider() {
        IntegerVariableNode var = new IntegerVariableNode("X");
        IntegerToDoubleCoerceNode coerceNode = new IntegerToDoubleCoerceNode(var);

        DataHub provider = new DataHub();
        provider.setValue("X", 99);
        coerceNode.setProvider(provider);

        assertEquals(99.0, coerceNode.evaluate(), 0.0);
    }

    @Test
    public void doubleToIntegerCoerceNodeEvaluate() {
        DoubleConstantNode doubleNode = new DoubleConstantNode(42.7);
        IntegerExpressionNode coerceNode = new DoubleToIntegerCoerceNode(doubleNode);

        assertEquals(42, coerceNode.evaluate());
    }

    @Test
    public void doubleToIntegerCoerceNodeSyntax() {
        DoubleConstantNode doubleNode = new DoubleConstantNode(42.7);
        IntegerExpressionNode coerceNode = new DoubleToIntegerCoerceNode(doubleNode);

        assertEquals("TOINT(42.7)", coerceNode.PreFixSyntax());
        assertEquals("TOINT(42.7)", coerceNode.ExcelSyntax());
    }

    @Test
    public void doubleToIntegerCoerceNodePropagatesListeners() {
        DoubleVariableNode var = new DoubleVariableNode("Y");
        DoubleToIntegerCoerceNode coerceNode = new DoubleToIntegerCoerceNode(var);

        java.util.List<DataListener> listeners = coerceNode.fetchListeners();
        assertEquals(1, listeners.size());
        assertEquals("Y", ((DoubleVariableNode)listeners.get(0).owner()).name);
    }

    @Test
    public void doubleToIntegerCoerceNodePropagatesProvider() {
        DoubleVariableNode var = new DoubleVariableNode("Y");
        DoubleToIntegerCoerceNode coerceNode = new DoubleToIntegerCoerceNode(var);

        DataHub provider = new DataHub();
        provider.setValue("Y", 99.9);
        coerceNode.setProvider(provider);

        assertEquals(99, coerceNode.evaluate());
    }

    @Test
    public void integerToDoubleCoerceNodeWithNegative() {
        IntegerConstantNode intNode = new IntegerConstantNode(-17);
        DoubleExpressionNode coerceNode = new IntegerToDoubleCoerceNode(intNode);

        assertEquals(-17.0, coerceNode.evaluate(), 0.0);
    }

    @Test
    public void doubleToIntegerCoerceNodeTruncates() {
        DoubleConstantNode doubleNode = new DoubleConstantNode(-3.9);
        IntegerExpressionNode coerceNode = new DoubleToIntegerCoerceNode(doubleNode);

        assertEquals(-3, coerceNode.evaluate());
    }
}