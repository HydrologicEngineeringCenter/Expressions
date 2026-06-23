package usace.hec.expressions.logical;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.function.Add;
import org.apache.commons.math3.analysis.function.Constant;
import org.junit.Test;

import usace.hec.expressions.*;
import usace.hec.expressions.comparison.GreaterThanNode;
import usace.hec.expressions.comparison.GreaterThanOrEqualNode;
import usace.hec.expressions.comparison.LessThanNode;
import usace.hec.expressions.comparison.LessThanOrEqualNode;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MultiplyNode;

public class IfNodeTest {
    @Test
    public void testEvaluate() {

        System.out.print("Basic Test \n");

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

        System.out.print("Nested Test \n");

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

        //ifNode = IF(([X]>[Y]),([X]+[Y]),([X]*[Y]))
        ExpressionNode<Double> ifNode = new IfNode<>(condition, Add, Multiply);

        //IF((X <= Z), (ifNode),(222))),
        ConstantLeafNode<Double> Z = new ConstantLeafNode<>(7.0);
        ConstantLeafNode<Double> TTT = new ConstantLeafNode<>(222.0);
        ExpressionNode<Boolean> newCondition = new LessThanOrEqualNode<>(X,Z);
        ExpressionNode<Double> outerIfNode = new IfNode<>(newCondition, TTT, ifNode);


        String expression = outerIfNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = outerIfNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?>> list = outerIfNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publishNext("X");
        adu.publishNext("Y");
        adu.advance();
        double result = outerIfNode.evaluate();
        assertEquals(100.0,result,0.0);//(10 !<= 7) =>  10!>10 10*10=100
        adu.publishNext("X");//advance x:(8 !<= 7) =>  10>8 10+8=18
        result = outerIfNode.evaluate();
        assertEquals(18.0,result,0.0);
        adu.publishNext("Y");//advance y 8!>8 => 8*8=64
        result = outerIfNode.evaluate();
        assertEquals(64.0,result,0.0);
        adu.advance();
        adu.publishNext("Y");//advance y 6!>8 6*8=48
        result = outerIfNode.evaluate();
        assertEquals(48.0,result,0.0);
        adu.publishNext("X");//advance x (6 <= 7) => TTT
        result = outerIfNode.evaluate();
        assertEquals(222.0,result,0.0);
    }

    @Test
    public void useMoreComparisons(){

        System.out.print("Comparison Test \n");

        //IF(500 <= X AND X <= 1000, X, IF(X < 500, X + 500, 1000))
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");

        BaseDataUpdater adu = new BaseDataUpdater();


        ExpressionNode<Double> const1 = new ConstantLeafNode<>(500.0);
        ExpressionNode<Double> const2 = new ConstantLeafNode<>(1000.0);

        ExpressionNode<Boolean> intermediateCondition1 = new LessThanOrEqualNode<>(const1, X);
        ExpressionNode<Boolean> intermediateCondition2 = new GreaterThanOrEqualNode<>(const2, X);

        ExpressionNode<Boolean> condition1 = new AndNode(intermediateCondition1, intermediateCondition2);

        ExpressionNode<Boolean> nextCondition = new LessThanNode<>(X,const1);
        ExpressionNode<Double> nextThenNode = new AddNode(X, const1);

        ExpressionNode<Double> nestedIfNode = new IfNode<>(nextCondition, nextThenNode, const2);
        ExpressionNode<Double> outerIfNode = new IfNode<>(condition1, X, nestedIfNode);

        String expression = outerIfNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = outerIfNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?>> list = outerIfNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publish("X", 200.0);
        double result = outerIfNode.evaluate();
        assertEquals(700.0,result,0.0);//(10 !<= 7) =>  10!>10 10*10=100
        adu.publish("X", 670.0);//advance x:(8 !<= 7) =>  10>8 10+8=18
        result = outerIfNode.evaluate();
        assertEquals(670.0,result,0.0);
        adu.publish("X", 1200);//advance y 8!>8 => 8*8=64
        result = outerIfNode.evaluate();
        assertEquals(1000.0,result,0.0);
    }
}
