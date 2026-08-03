package usace.hec.expressions.logical;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.BooleanVariableNode;
import usace.hec.expressions.DataHub;
import usace.hec.expressions.DataProvider;

public class AndOrXorTest {

    @Test
    public void testAndEvaluate() {
        BooleanVariableNode x = new BooleanVariableNode("X");
        BooleanVariableNode y = new BooleanVariableNode("Y");
        DataProvider dp = new DataHub();
        BooleanExpressionNode andNode = new AndNode(x, y);
        andNode.setProvider(dp);

        String expression = andNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = andNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        dp.setBoolean("X", true);
        dp.setBoolean("Y", true);
        boolean result = andNode.evaluate();
        assertEquals(true, result); // true && true

        dp.setBoolean("X", false);
        result = andNode.evaluate();
        assertEquals(false, result); // false && true

        dp.setBoolean("Y", false);
        result = andNode.evaluate();
        assertEquals(false, result); // false && false

        dp.setBoolean("X", true);
        result = andNode.evaluate();
        assertEquals(false, result); // true && false
    }

    @Test
    public void testOrEvaluate() {
        BooleanVariableNode x = new BooleanVariableNode("X");
        BooleanVariableNode y = new BooleanVariableNode("Y");
        DataProvider dp = new DataHub();


        BooleanExpressionNode orNode = new OrNode(x, y);
        orNode.setProvider(dp);

        String expression = orNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = orNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        dp.setBoolean("X", true);
        dp.setBoolean("Y", true);
        boolean result = orNode.evaluate();
        assertEquals(true, result); // true || true

        dp.setBoolean("X", false);
        result = orNode.evaluate();
        assertEquals(true, result); // false || true

        dp.setBoolean("Y", false);
        result = orNode.evaluate();
        assertEquals(false, result); // false || false

        dp.setBoolean("X", true);
        result = orNode.evaluate();
        assertEquals(true, result); // true || false
    }

    @Test
    public void testXorEvaluate() {
        BooleanVariableNode x = new BooleanVariableNode("X");
        BooleanVariableNode y = new BooleanVariableNode("Y");
        DataProvider dp = new DataHub();

        BooleanExpressionNode xorNode = new XorNode(x, y);
        xorNode.setProvider(dp);

        String expression = xorNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = xorNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        dp.setBoolean("X", true);
        dp.setBoolean("Y", true);
        boolean result = xorNode.evaluate();
        assertEquals(false, result); // true ^^ true

        dp.setBoolean("X", false);
        result = xorNode.evaluate();
        assertEquals(true, result); // false ^^ true

        dp.setBoolean("Y", false);
        result = xorNode.evaluate();
        assertEquals(false, result); // false ^^ false

        dp.setBoolean("X", true);
        result = xorNode.evaluate();
        assertEquals(true, result); // true ^^ false
    }
}