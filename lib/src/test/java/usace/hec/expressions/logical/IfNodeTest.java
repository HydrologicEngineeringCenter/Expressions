package usace.hec.expressions.logical;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.function.Constant;
import org.junit.Test;

import usace.hec.expressions.*;
import usace.hec.expressions.comparison.GreaterThanNode;
import usace.hec.expressions.comparison.LessThanOrEqualNode;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MultiplyNode;

public class IfNodeTest {
    @Test
    public void testEvaluate() {
        //IF(([X]>[Y]),([X]+[Y]),([X]*[Y]))
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        ArrayDataUpdater adu = new ArrayDataUpdater(values);
        ExpressionNode<Boolean> condition = new GreaterThanNode<>(X,Y);
        ExpressionNode<Double> Add = new AddNode(X, Y);
        ExpressionNode<Double> Multiply = new MultiplyNode(X, Y);

        ExpressionNode<Double> ifNode = new IfNode<>(condition, Add, Multiply);

        String expression = ifNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = ifNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?>> list = ifNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publishNext("X");
        adu.publishNext("Y");
        adu.advance();
        double result = ifNode.evaluate();
        assertEquals(1.0,result,0.0);//1!>1 1*1=1
        adu.publishNext("X");//advance x 2>1 1+2=3
        result = ifNode.evaluate();
        assertEquals(3.0,result,0.0);
        adu.publishNext("Y");//advance y 2!>2 2*2=4
        result = ifNode.evaluate();
        assertEquals(4.0,result,0.0);
        adu.advance();
        adu.publishNext("Y");//advance y 2!>3 2*3=6
        result = ifNode.evaluate();
        assertEquals(6.0,result,0.0);
        adu.publishNext("X");//advance x 3!>3 3*3 = 9
        result = ifNode.evaluate();
        assertEquals(9.0,result,0.0);

        //looney test
        adu.publish("X", 100.0);
        result = ifNode.evaluate();//100>3 100+3 = 103
        assertEquals(103.0,result,0.0);
    }
    @Test
    public void testNestedEvaluate() {
        //Nested IF((Z > X), (IF(([Y]>[X]),([X]+[Y]),([X]*[Y]))), (222))

        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");
        ArrayList<Double> values = new ArrayList<>();
        values.add(10.0);
        values.add(8.0);
        values.add(6.0);
        ArrayDataUpdater adu = new ArrayDataUpdater(values);
        ExpressionNode<Boolean> condition = new GreaterThanNode<>(Y,X);
        ExpressionNode<Double> Add = new AddNode(X, Y);
        ExpressionNode<Double> Multiply = new MultiplyNode(X, Y);

        //IF(([X]>[Y]),([X]+[Y]),([X]*[Y]))
        ExpressionNode<Double> ifNode = new IfNode<>(condition, Add, Multiply);

        //IF((X <= Z), (ifNode),(222))),
        ConstantLeafNode<Double> Z = new ConstantLeafNode<>(7.0);
        ConstantLeafNode<Double> TTT = new ConstantLeafNode<>(222.0);
        ExpressionNode<Boolean> newCondition = new LessThanOrEqualNode<>(X,Z);
        ExpressionNode<Double> nestedIfNode = new IfNode<>(newCondition, TTT, ifNode);


        String expression = nestedIfNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = nestedIfNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?>> list = nestedIfNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publishNext("X");
        adu.publishNext("Y");
        adu.advance();
        double result = nestedIfNode.evaluate();
        assertEquals(100.0,result,0.0);//(10 !<= 7) =>  10!>10 10*10=100
        adu.publishNext("X");//advance x:(8 !<= 7) =>  10>8 10+8=18
        result = nestedIfNode.evaluate();
        assertEquals(18.0,result,0.0);
        adu.publishNext("Y");//advance y 8!>8 => 8*8=64
        result = nestedIfNode.evaluate();
        assertEquals(64.0,result,0.0);
        adu.advance();
        adu.publishNext("Y");//advance y 6!>8 6*8=48
        result = nestedIfNode.evaluate();
        assertEquals(48.0,result,0.0);
        adu.publishNext("X");//advance x (6 <= 7) => TTT
        result = nestedIfNode.evaluate();
        assertEquals(222.0,result,0.0);
    }
}
