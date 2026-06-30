package usace.hec.expressions.misc;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.*;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MinusNode;

public class LagNodeTest {
    @Test
    public void basicLagTest(){
        ExpressionNode<Double> X = new UpdateableLeafNode<>("X");
        ExpressionNode<Integer> lag = new ConstantLeafNode<>(4);
        ExpressionNode<Double> lagNode = new LagNode<>(X, lag);

        BaseDataUpdater adu = new BaseDataUpdater();

        List<DataListener<?, ?>> list = lagNode.fetchListeners();
        for(DataListener<?, ?> d : list) {
            adu.register(d);
        }
        adu.publish("X", 1.0);
        Double result = lagNode.evaluate();
        assertEquals(1.0, result, 0.0);
        adu.publish("X", 2.0);
        result = lagNode.evaluate();
        assertEquals(1.0, result, 0.0);
        adu.publish("X", 3.0);
        result = lagNode.evaluate();
        assertEquals(1.0, result, 0.0);
        adu.publish("X", 4.0);
        result = lagNode.evaluate();
        assertEquals(1.0, result, 0.0);
        adu.publish("X", 5.0);
        result = lagNode.evaluate();
        assertEquals(2.0, result, 0.0);

        String expression = lagNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = lagNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
    }

}
