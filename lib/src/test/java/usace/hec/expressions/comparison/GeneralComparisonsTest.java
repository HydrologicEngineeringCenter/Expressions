package usace.hec.expressions.comparison;

import org.junit.Test;
import usace.hec.expressions.*;
import usace.hec.expressions.strings.StringEqualToNode;
import usace.hec.expressions.strings.StringNotEqualToNode;
import usace.hec.expressions.time.DateNode;
import usace.hec.expressions.time.NotSameDateNode;
import usace.hec.expressions.time.SameDateNode;

import static org.junit.Assert.*;


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

    @Test
    public void testBooleanEqualToAndNotEqualTo() {
        System.out.print("Boolean EQ/NEQ Test\n");

        BooleanVariableNode x = new BooleanVariableNode("X");
        BooleanVariableNode y = new BooleanVariableNode("Y");
        DataProvider dp = new DataHub();

        BooleanExpressionNode eq = new BooleanEqualToNode(x, y);
        BooleanExpressionNode neq = new BooleanNotEqualToNode(x, y);
        eq.setProvider(dp);
        neq.setProvider(dp);

        dp.setBoolean("X", true);
        dp.setBoolean("Y", true);
        assertTrue(eq.evaluate());
        assertFalse(neq.evaluate());

        dp.setBoolean("Y", false);
        assertFalse(eq.evaluate());
        assertTrue(neq.evaluate());

        dp.setBoolean("X", false);
        assertTrue(eq.evaluate());
        assertFalse(neq.evaluate());
    }

    @Test
    public void testDoubleEqualToAndNotEqualTo() {
        System.out.print("Double EQ/NEQ Test\n");

        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        DataProvider dp = new DataHub();

        BooleanExpressionNode eq = new DoubleEqualToNode(x, y);
        BooleanExpressionNode neq = new DoubleNotEqualToNode(x, y);
        eq.setProvider(dp);
        neq.setProvider(dp);

        dp.setDouble("X", 3.5);
        dp.setDouble("Y", 3.5);
        assertTrue(eq.evaluate());
        assertFalse(neq.evaluate());

        dp.setDouble("Y", 3.50001);
        assertFalse(eq.evaluate());
        assertTrue(neq.evaluate());

        dp.setDouble("X", -1.0);
        dp.setDouble("Y", -1.0);
        assertTrue(eq.evaluate());
        assertFalse(neq.evaluate());
    }

    @Test
    public void testIntegerEqualToAndNotEqualTo() {
        System.out.print("Integer EQ/NEQ Test\n");

        IntegerVariableNode x = new IntegerVariableNode("X");
        IntegerVariableNode y = new IntegerVariableNode("Y");
        DataProvider dp = new DataHub();

        BooleanExpressionNode eq = new IntegerEqualToNode(x, y);
        BooleanExpressionNode neq = new IntegerNotEqualToNode(x, y);
        eq.setProvider(dp);
        neq.setProvider(dp);

        dp.setInt("X", 7);
        dp.setInt("Y", 7);
        assertTrue(eq.evaluate());
        assertFalse(neq.evaluate());

        dp.setInt("Y", 8);
        assertFalse(eq.evaluate());
        assertTrue(neq.evaluate());
    }

    @Test
    public void testStringEqualToAndNotEqualTo() {
        System.out.print("String EQ/NEQ Test\n");

        StringVariableNode x = new StringVariableNode("X");
        StringVariableNode y = new StringVariableNode("Y");
        DataProvider dp = new DataHub();

        BooleanExpressionNode eq = new StringEqualToNode(x, y);
        BooleanExpressionNode neq = new StringNotEqualToNode(x, y);
        eq.setProvider(dp);
        neq.setProvider(dp);

        dp.setString("X", "abc");
        dp.setString("Y", "abc");
        assertTrue(eq.evaluate());
        assertFalse(neq.evaluate());

        dp.setString("Y", "ABC");
        assertFalse(eq.evaluate()); // comparison is case-sensitive
        assertTrue(neq.evaluate());

        dp.setString("X", "");
        dp.setString("Y", "");
        assertTrue(eq.evaluate());
        assertFalse(neq.evaluate());
    }

    @Test
    public void testSameDateAndNotSameDateEvaluate() {
        System.out.print("DateTime EQ/NEQ (SameDate/NotSameDate) Test\n");

        DateTimeExpressionNode a = new DateNode(
                new IntegerConstantNode(2026), new IntegerConstantNode(8), new IntegerConstantNode(6));
        DateTimeExpressionNode sameAsA = new DateNode(
                new IntegerConstantNode(2026), new IntegerConstantNode(8), new IntegerConstantNode(6));
        DateTimeExpressionNode differentFromA = new DateNode(
                new IntegerConstantNode(2026), new IntegerConstantNode(8), new IntegerConstantNode(7));

        BooleanExpressionNode same = new SameDateNode(a, sameAsA);
        BooleanExpressionNode notSame = new SameDateNode(a, differentFromA);
        assertTrue(same.evaluate());
        assertFalse(notSame.evaluate());

        BooleanExpressionNode neqSameDates = new NotSameDateNode(a, sameAsA);
        BooleanExpressionNode neqDifferentDates = new NotSameDateNode(a, differentFromA);
        assertFalse(neqSameDates.evaluate());
        assertTrue(neqDifferentDates.evaluate());
    }
}
