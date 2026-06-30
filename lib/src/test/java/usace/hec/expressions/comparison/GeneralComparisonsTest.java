package usace.hec.expressions.comparison;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.function.Constant;
import org.junit.Test;

import usace.hec.expressions.*;
import usace.hec.expressions.comparison.GreaterThanNode;
import usace.hec.expressions.comparison.LessThanOrEqualNode;
import usace.hec.expressions.logical.AndNode;
import usace.hec.expressions.logical.OrNode;
import usace.hec.expressions.logical.XorNode;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MultiplyNode;

public class GeneralComparisonsTest {
    @Test
    public void testLTEAndLTEvaluate() {
        System.out.print("LTE and LT Test" + "\n");

        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Boolean> ltNode = new LessThanNode<>(X,Y);
        ExpressionNode<Boolean> lteNode = new LessThanOrEqualNode<>(X,Y);

        String expression = ltNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = ltNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

        expression = lteNode.PreFixSyntax();
        System.out.print(expression + "\n");
        expressionInfix = lteNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

        List<DataListener<?>> list = ltNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publish("X",1.0);
        adu.publish("Y",1.0);
        Boolean result = ltNode.evaluate(); //1.0 < 1.0 false
        Boolean result2 = lteNode.evaluate();//1.0 <= 1.0 true
        assertEquals(false, result);
        assertEquals(true, result2);
        adu.publish("X",2.0);
        result = ltNode.evaluate();//2.0 < 1.0 false
        result2 = lteNode.evaluate();//2.0 <= 1.0 false
        assertEquals(false, result);
        assertEquals(false, result2);
        adu.publish("Y", 2.1);
        result = ltNode.evaluate();//2.0 < 2.1 true
        result2 = lteNode.evaluate();//2.0 <= 2.1 true
        assertEquals(true, result);
        assertEquals(true, result2);
    }
    @Test
    public void testGTEAndGTEvaluate() {
        System.out.print("GTE and GT Test" + "\n");

        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Boolean> gtNode = new GreaterThanNode<>(X,Y);
        ExpressionNode<Boolean> gteNode = new GreaterThanOrEqualNode<>(X,Y);

        String expression = gtNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = gtNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

        expression = gteNode.PreFixSyntax();
        System.out.print(expression + "\n");
        expressionInfix = gteNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?>> list = gtNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }

        adu.publish("X",1.0);
        adu.publish("Y",1.0);
        Boolean result = gtNode.evaluate(); //1.0 > 1.0 false
        Boolean result2 = gteNode.evaluate();//1.0 >= 1.0 true
        assertEquals(false, result);
        assertEquals(true, result2);
        adu.publish("X",2.0);
        result = gtNode.evaluate();//2.0 > 1.0 true
        result2 = gteNode.evaluate();//2.0 >= 1.0 true
        assertEquals(true, result);
        assertEquals(true, result2);
        adu.publish("Y", 2.1);
        result = gtNode.evaluate();//2.0 > 2.1 false
        result2 = gteNode.evaluate();//2.0 >= 2.1 false
        assertEquals(false, result);
        assertEquals(false, result2);
    }
    @Test
    public void testEqEvaluate() {
        System.out.print("EQ Test" + "\n");

        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Boolean> eqNode = new EqualToNode<>(X,Y);

        String expression = eqNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = eqNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?>> list = eqNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }

        adu.publish("X",1.0);
        adu.publish("Y",1.0);
        Boolean result = eqNode.evaluate(); //1.0 == 1.0 true
        assertEquals(true, result);
        adu.publish("X",2.0);
        result = eqNode.evaluate();//2.0 > 1.0 true
        assertEquals(false, result);
        adu.publish("Y", 2.1);
        result = eqNode.evaluate();//2.0 > 2.1 false
        assertEquals(false, result);
    }
}
