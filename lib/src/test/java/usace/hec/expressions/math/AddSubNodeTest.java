package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UpdateableLeafNode;

public class AddSubNodeTest {
    @Test
    public void testEvaluate() {
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> Add = new AddNode(X, Y);
        ExpressionNode<Double> Minus = new MinusNode(X, Y);
        List<DataListener<?>> list = Add.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publish("X",1.0);
        adu.publish("Y",1.0);
        Double result = Add.evaluate();
        Double result2 = Minus.evaluate();
        assertEquals(2.0, result, 0.0);
        assertEquals(0.0, result2, 0.0);
        adu.publish("X",2.0);
        result = Add.evaluate();
        result2 = Minus.evaluate();
        assertEquals(3.0, result, 0.0);
        assertEquals(1.0, result2, 0.0);
        adu.publish("Y", 2.0);
        result = Add.evaluate();
        assertEquals(4.0, result, 0.0);
        adu.publish("X",3.0);
        adu.publish("Y",3.0);
        result = Add.evaluate();
        assertEquals(6.0, result, 0.0);
        adu.publish("Y", 1000.0);
        result2 = Minus.evaluate();
        assertEquals(-997.0, result2, 0.0);
    }

    @Test
    public void testSyntax(){ //You will have to examine the print statements, it will automatically return test passed.
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> Add = new AddNode(X, Y);
        ExpressionNode<Double> Minus = new MinusNode(X, Y);

        String expression = Add.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = Add.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

        String expression2 = Minus.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = Minus.ExcelSyntax();
        System.out.print(expression2Infix+ "\n");

    }
}
