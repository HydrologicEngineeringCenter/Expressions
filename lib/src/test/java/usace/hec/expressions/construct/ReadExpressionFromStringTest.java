package usace.hec.expressions.construct;

import static org.junit.Assert.*;

import org.junit.Test;
import usace.hec.expressions.*;

public class ReadExpressionFromStringTest {

    @Test
    public void testParseSimpleExpression() {
        ExpressionParser parser = new ExpressionParser();
        ParseResult result = parser.parse("1.0 + 2.0");

        assertFalse(result.hasError());
        DoubleExpressionNode node = (DoubleExpressionNode) result.getNode();
        assertEquals(3.0, node.evaluate(), 0.0);
    }

    @Test
    public void testParseIfExpression() {
        ExpressionParser parser = new ExpressionParser();
        ParseResult result = parser.parse("IF(5.0 > 3.0, 10.0, 20.0)");

        assertFalse(result.hasError());
        DoubleExpressionNode node = (DoubleExpressionNode) result.getNode();
        assertEquals(10.0, node.evaluate(), 0.0); // 5>3 is true, so 10
    }

    @Test
    public void testParseNestedExpression() {
        ExpressionParser parser = new ExpressionParser();
        ParseResult result = parser.parse("IF(5.0 > 3.0, 1.0 + 2.0, 3.0 * 4.0)");

        assertFalse(result.hasError());
        DoubleExpressionNode node = (DoubleExpressionNode) result.getNode();
        assertEquals(3.0, node.evaluate(), 0.0); // 5>3 is true, so 1+2=3
    }

    @Test
    public void testParseWithVariables() {
        ExpressionParser parser = new ExpressionParser();
        ParseResult result = parser.parse("[Flow] + [Stage]");

        assertFalse(result.hasError());
        DoubleExpressionNode node = (DoubleExpressionNode) result.getNode();

        // Set up data provider
        DataHub provider = new DataHub();
        provider.setValue("Flow", 100.0);
        provider.setValue("Stage", 50.0);
        node.setProvider(provider);

        assertEquals(150.0, node.evaluate(), 0.0);
    }

    @Test
    public void testParseError() {
        ExpressionParser parser = new ExpressionParser();
        ParseResult result = parser.parse("IF(1.0, 2.0)"); // Missing third argument

        assertTrue(result.hasError());
        assertTrue(result.getError().message().contains("IF requires exactly 3 arguments"));
    }

    @Test
    public void testParseSyntaxGeneration() {
        ExpressionParser parser = new ExpressionParser();
        ParseResult result = parser.parse("[X] > [Y]");

        assertFalse(result.hasError());
        ExpressionNode node = (ExpressionNode)result.getNode();

        String prefix = node.PreFixSyntax();
        String excel = node.ExcelSyntax();

        assertNotNull(prefix);
        assertNotNull(excel);
        System.out.println("Prefix: " + prefix);
        System.out.println("Excel: " + excel);
    }

    @Test
    public void testParseComplexIf() {
        ExpressionParser parser = new ExpressionParser();
        // IF(500 <= [X] AND [X] <= 1000, [X], IF([X] < 500, [X] + 500, 1000))
        ParseResult result = parser.parse("IF(500 <= [X] && [X] <= 1000, [X], IF([X] < 500, [X] + 500, 1000))");

        assertFalse(result.hasError());
        DoubleExpressionNode node = (DoubleExpressionNode) result.getNode();

        DataHub provider = new DataHub();
        node.setProvider(provider);

        provider.setValue("X", 200.0);
        assertEquals(700.0, node.evaluate(), 0.0); // 200<500 => 200+500

        provider.setValue("X", 670.0);
        assertEquals(670.0, node.evaluate(), 0.0); // 500<=670<=1000

        provider.setValue("X", 1200.0);
        assertEquals(1000.0, node.evaluate(), 0.0); // 1200>1000 => 1000
    }
}