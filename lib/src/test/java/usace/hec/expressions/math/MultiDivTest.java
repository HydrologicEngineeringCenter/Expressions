package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UpdateableLeafNode;

public class MultiDivTest {
    @Test
    public void testEvaluate() {
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> Multi = new MultiplyNode(X, Y);
        ExpressionNode<Double> Div = new DivideNode(X, Y);
        List<DataListener<?, ?>> list = Multi.fetchListeners();
        for(DataListener<?, ?> d : list){
            adu.register(d);
        }
        adu.publish("X",1.0);
        adu.publish("Y",1.0);
        Double result = Multi.evaluate();
        Double result2 = Div.evaluate();
        assertEquals(1.0, result, 0.0);
        assertEquals(1.0, result2, 0.0);
        adu.publish("Y",2.0);
        result = Multi.evaluate();
        result2 = Div.evaluate();
        assertEquals(2.0, result, 0.0);
        assertEquals(0.5, result2, 0.0);
        adu.publish("X", 16.0);
        result = Multi.evaluate();
        result2 = Div.evaluate();
        assertEquals(32.0, result, 0.0);
        assertEquals(8.0, result2, 0.0);
        adu.publish("X",3.0);
        result = Multi.evaluate();
        assertEquals(6.0, result, 0.0);
        adu.publish("Y", 0.0); // 3/0 is undefined
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> Div.evaluate());
        assertEquals("Division by zero", ex.getMessage());
    }

    @Test
    public void testSyntax(){ //You will have to examine the print statements, it will automatically return test passed.
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> Multi = new MultiplyNode(X, Y);
        ExpressionNode<Double> Div = new DivideNode(X, Y);

        String expression = Multi.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = Multi.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

        String expression2 = Div.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = Div.ExcelSyntax();
        System.out.print(expression2Infix+ "\n");

    }
}
