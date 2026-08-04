package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import usace.hec.expressions.DataHub;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.DoubleVariableNode;

public class ExponentNodeTest {

    @Test
    public void testEvaluate() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        DataProvider dp = new DataHub();


        DoubleExpressionNode exp = new DoubleExponentNode(x, y);

        exp.setProvider(dp);


        dp.setDouble("X", 1.0);
        dp.setDouble("Y", 2.0);
        double result = exp.evaluate();
        assertEquals(1.0, result, 0.0);

        dp.setDouble("X", 2.0);
        result = exp.evaluate();
        assertEquals(4.0, result, 0.0);

        dp.setDouble("Y", 0.0);
        result = exp.evaluate();
        assertEquals(1.0, result, 0.0);

        dp.setDouble("X", 9.0);
        dp.setDouble("Y", 0.5);
        result = exp.evaluate();
        assertEquals(3.0, result, 0.0);

        dp.setDouble("X", -1000.0);
        result = exp.evaluate();
        assertEquals(result, Double.NaN,0.0);
    }

    @Test
    public void testSyntax() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");

        DoubleExpressionNode exp = new DoubleExponentNode(x, y);

        String expression = exp.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = exp.ExcelSyntax();
        System.out.print(expressionInfix + "\n");
    }
}