package usace.hec.expressions.comparison;

import org.junit.Test;
import usace.hec.expressions.*;
import usace.hec.expressions.time.DateBetweenNode;
import usace.hec.expressions.time.DateNode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BetweenTest {
    @Test
    public void testDateBetween() {
        System.out.print("DateTime Between Test\n");

        DateTimeExpressionNode a = new DateNode(
                new IntegerConstantNode(2026), new IntegerConstantNode(8), new IntegerConstantNode(6));
        DateTimeExpressionNode afterA = new DateNode(
                new IntegerConstantNode(2026), new IntegerConstantNode(8), new IntegerConstantNode(7));
        DateTimeExpressionNode beforeA = new DateNode(
                new IntegerConstantNode(2026), new IntegerConstantNode(8), new IntegerConstantNode(5));

        BooleanExpressionNode between = new DateBetweenNode(beforeA, a, afterA);
        BooleanExpressionNode notBetween = new DateBetweenNode(beforeA, afterA, a);
        assertTrue(between.evaluate());
        assertFalse(notBetween.evaluate());
    }
    @Test
    public void testDoubleBetween() {
        System.out.print("Double Between Test\n");

        DoubleExpressionNode a = new DoubleConstantNode(2026.0);
        DoubleExpressionNode afterA = new DoubleConstantNode(2027.0);
        DoubleExpressionNode beforeA = new DoubleConstantNode(2025.0);

        BooleanExpressionNode between = new DoubleBetweenNode(beforeA, a, afterA);
        BooleanExpressionNode notBetween = new DoubleBetweenNode(beforeA, afterA, a);
        assertTrue(between.evaluate());
        assertFalse(notBetween.evaluate());
    }
    @Test
    public void testIntegerBetween() {
        System.out.print("Integer Between Test\n");

        IntegerExpressionNode a = new IntegerConstantNode(2026);
        IntegerExpressionNode afterA = new IntegerConstantNode(2027);
        IntegerExpressionNode beforeA = new IntegerConstantNode(2025);

        BooleanExpressionNode between = new IntegerBetweenNode(beforeA, a, afterA);
        BooleanExpressionNode notBetween = new IntegerBetweenNode(beforeA, afterA, a);
        assertTrue(between.evaluate());
        assertFalse(notBetween.evaluate());
    }
}
