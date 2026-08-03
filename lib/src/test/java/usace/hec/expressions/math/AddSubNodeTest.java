package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.DataHub;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.DoubleVariableNode;

import javax.xml.crypto.Data;

public class AddSubNodeTest {

    @Test
    public void testEvaluate() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");
        DataProvider dp = new DataHub();

        DoubleExpressionNode add = new DoubleAddNode(x, y);
        DoubleExpressionNode minus = new DoubleMinusNode(x, y);

        add.setProvider(dp);
        minus.setProvider(dp);
        
        dp.setDouble("X", 1.0);
        dp.setDouble("Y", 1.0);

        double result = add.evaluate();
        double result2 = minus.evaluate();
        assertEquals(2.0, result, 0.0);
        assertEquals(0.0, result2, 0.0);

        dp.setDouble("X", 2.0);
        result = add.evaluate();
        result2 = minus.evaluate();
        assertEquals(3.0, result, 0.0);
        assertEquals(1.0, result2, 0.0);

        dp.setDouble("Y", 2.0);
        result = add.evaluate();
        assertEquals(4.0, result, 0.0);

        dp.setDouble("X", 3.0);
        dp.setDouble("Y", 3.0);
        result = add.evaluate();
        assertEquals(6.0, result, 0.0);

        dp.setDouble("Y", 1000.0);
        result2 = minus.evaluate();
        assertEquals(-997.0, result2, 0.0);
    }

    @Test
    public void testSyntax() {
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");

        DoubleExpressionNode add = new DoubleAddNode(x, y);
        DoubleExpressionNode minus = new DoubleMinusNode(x, y);

        String expression = add.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = add.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        String expression2 = minus.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = minus.ExcelSyntax();
        System.out.print(expression2Infix + "\n");
    }
}