package usace.hec.expressions.construct;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import usace.hec.expressions.*;

public class ReadExpressionFromStringTest {

    @Test
    public void testParseSimpleExpression() {
        
        ParseResult result = ExpressionParser.parse("1.0 + 2.0", null);

        assertFalse(result.hasError());
        DoubleExpressionNode node = (DoubleExpressionNode) result.getNode();
        assertEquals(3.0, node.evaluate(), 0.0);
    }

    @Test
    public void testParseIfExpression() {
        
        ParseResult result = ExpressionParser.parse("IF(5.0 > 3.0, 10.0, 20.0)",null);

        assertFalse(result.hasError());
        DoubleExpressionNode node = (DoubleExpressionNode) result.getNode();
        assertEquals(10.0, node.evaluate(), 0.0); // 5>3 is true, so 10
    }

    @Test
    public void testParseNestedExpression() {
        
        ParseResult result = ExpressionParser.parse("IF(5.0 > 3.0, 1.0 + 2.0, 3.0 * 4.0)", null);

        assertFalse(result.hasError());
        DoubleExpressionNode node = (DoubleExpressionNode) result.getNode();
        assertEquals(3.0, node.evaluate(), 0.0); // 5>3 is true, so 1+2=3
    }

    @Test
    public void testParseWithVariables() {
        Map<String,ExpressionType> symbolTable = Map.of(
            "Flow", ExpressionType.DOUBLE,
            "Stage",ExpressionType.DOUBLE
        );
        ParseResult result = ExpressionParser.parse("[Flow] + [Stage]", symbolTable);

        assertFalse(result.hasError());
        DoubleExpressionNode node = (DoubleExpressionNode) result.getNode();

        // Set up data provider
        DataHub provider = new DataHub();
        provider.setDouble("Flow", 100.0);
        provider.setDouble("Stage", 50.0);
        node.setProvider(provider);

        assertEquals(150.0, node.evaluate(), 0.0);
    }

    @Test
    public void testParseError() {
        
        ParseResult result = ExpressionParser.parse("IF(1.0, 2.0)", null); 

        assertTrue(result.hasError());
        assertTrue(result.getError().message().contains("IF requires exactly 3 arguments"));
    }

    @Test
    public void testParseSyntaxGeneration() {
        Map<String,ExpressionType> symbolTable = Map.of(
            "X", ExpressionType.DOUBLE,
            "Y",ExpressionType.DOUBLE
        );
        ParseResult result = ExpressionParser.parse("[X] > [Y]",symbolTable);

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
        
        // IF(500 <= [X] AND [X] <= 1000, [X], IF([X] < 500, [X] + 500, 1000))
        Map<String,ExpressionType> symbolTable = Map.of(
            "X", ExpressionType.INTEGER,
            "Y",ExpressionType.INTEGER
        );
        ParseResult result = ExpressionParser.parse("IF(500 <= [X] && [X] <= 1000, [X], IF([X] < 500, [X] + 500, 1000))",symbolTable);

        assertFalse(result.hasError());
        IntegerExpressionNode node = (IntegerExpressionNode) result.getNode();

        DataHub provider = new DataHub();
        node.setProvider(provider);

        provider.setInt("X", 200);
        assertEquals(700.0, node.evaluate(), 0.0); // 200<500 => 200+500

        provider.setInt("X", 670);
        assertEquals(670.0, node.evaluate(), 0.0); // 500<=670<=1000

        provider.setInt("X", 1200);
        assertEquals(1000, node.evaluate()); // 1200>1000 => 1000
    }
}