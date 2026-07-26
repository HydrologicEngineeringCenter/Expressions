package usace.hec.expressions.logical;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.BooleanVariableNode;
import usace.hec.expressions.DataListener;

public class AndOrXorTest {

    @Test
    public void testAndEvaluate() {
        BooleanVariableNode x = new BooleanVariableNode("X");
        BooleanVariableNode y = new BooleanVariableNode("Y");
        BaseDataUpdater adu = new BaseDataUpdater();

        BooleanExpressionNode andNode = new AndNode(x, y);

        String expression = andNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = andNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        List<DataListener> list = andNode.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", true);
        adu.publish("Y", true);
        boolean result = andNode.evaluate();
        assertEquals(true, result); // true && true

        adu.publish("X", false);
        result = andNode.evaluate();
        assertEquals(false, result); // false && true

        adu.publish("Y", false);
        result = andNode.evaluate();
        assertEquals(false, result); // false && false

        adu.publish("X", true);
        result = andNode.evaluate();
        assertEquals(false, result); // true && false
    }

    @Test
    public void testOrEvaluate() {
        BooleanVariableNode x = new BooleanVariableNode("X");
        BooleanVariableNode y = new BooleanVariableNode("Y");
        BaseDataUpdater adu = new BaseDataUpdater();

        BooleanExpressionNode orNode = new OrNode(x, y);

        String expression = orNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = orNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        List<DataListener> list = orNode.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", true);
        adu.publish("Y", true);
        boolean result = orNode.evaluate();
        assertEquals(true, result); // true || true

        adu.publish("X", false);
        result = orNode.evaluate();
        assertEquals(true, result); // false || true

        adu.publish("Y", false);
        result = orNode.evaluate();
        assertEquals(false, result); // false || false

        adu.publish("X", true);
        result = orNode.evaluate();
        assertEquals(true, result); // true || false
    }

    @Test
    public void testXorEvaluate() {
        BooleanVariableNode x = new BooleanVariableNode("X");
        BooleanVariableNode y = new BooleanVariableNode("Y");
        BaseDataUpdater adu = new BaseDataUpdater();

        BooleanExpressionNode xorNode = new XorNode(x, y);

        String expression = xorNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = xorNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        List<DataListener> list = xorNode.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publish("X", true);
        adu.publish("Y", true);
        boolean result = xorNode.evaluate();
        assertEquals(false, result); // true ^^ true

        adu.publish("X", false);
        result = xorNode.evaluate();
        assertEquals(true, result); // false ^^ true

        adu.publish("Y", false);
        result = xorNode.evaluate();
        assertEquals(false, result); // false ^^ false

        adu.publish("X", true);
        result = xorNode.evaluate();
        assertEquals(true, result); // true ^^ false
    }
}