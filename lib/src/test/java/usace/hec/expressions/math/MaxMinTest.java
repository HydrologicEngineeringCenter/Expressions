package usace.hec.expressions.math;

import org.apache.commons.math3.analysis.function.Max;
import org.junit.Test;
import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UpdateableLeafNode;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MaxMinTest {
    @Test
    public void testEvaluate() {
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> Max = new MaxNode(X, Y);
        ExpressionNode<Double> Min = new MinNode(X, Y);



        List<DataListener<?, ?>> list = Max.fetchListeners();
        for(DataListener<?, ?> d : list){
            adu.register(d);
        }
        adu.publish("X",1.0);
        adu.publish("Y",1.0);
        Double result = Max.evaluate();
        Double result2 = Min.evaluate();
        assertEquals(1.0, result, 0.0);
        assertEquals(1.0, result2, 0.0);
        adu.publish("X",2.0);
        result = Max.evaluate();
        result2 = Min.evaluate();
        assertEquals(2.0, result, 0.0);
        assertEquals(1.0, result2, 0.0);
        adu.publish("Y", 10.0);
        result = Max.evaluate();
        result2 = Min.evaluate();
        assertEquals(10.0, result, 0.0);
        assertEquals(2.0, result2, 0.0);
        adu.publish("X",-13.0);
        adu.publish("Y",3.0);
        result = Max.evaluate();
        result2 = Min.evaluate();
        assertEquals(3.0, result, 0.0);
        assertEquals(-13.0, result2, 0.0);
        adu.publish("Y", -1000.0);
        result = Max.evaluate();
        result2 = Min.evaluate();
        assertEquals(-13.0, result, 0.0);
        assertEquals(-1000.0, result2, 0.0);
    }
    @Test
    public void testSyntax(){ //You will have to examine the print statements, it will automatically return test passed.
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        ExpressionNode<Double> Max = new MaxNode(X, Y);
        ExpressionNode<Double> Min = new MinNode(X, Y);

        String expression = Max.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = Max.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

        String expression2 = Min.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = Min.ExcelSyntax();
        System.out.print(expression2Infix+ "\n");

    }
}
