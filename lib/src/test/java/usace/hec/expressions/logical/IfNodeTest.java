package usace.hec.expressions.logical;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import usace.hec.expressions.*;
import usace.hec.expressions.comparison.DoubleGreaterThanNode;
import usace.hec.expressions.comparison.GreaterThanOrEqualNode;
import usace.hec.expressions.comparison.LessThanNode;
import usace.hec.expressions.comparison.LessThanOrEqualNode;
import usace.hec.expressions.math.DoubleAddNode;
import usace.hec.expressions.math.DoubleMultiplyNode;

public class IfNodeTest {
    @Test
    public void testEvaluate() {

        System.out.print("Basic Test \n");

        //IF(([X]>[Y]),([X]+[Y]),([X]*[Y]))
        UpdateableLeafNode<Number> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Number> Y = new UpdateableLeafNode<>("Y");
        ArrayList<Number> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        ArrayDataUpdater adu = new ArrayDataUpdater(values);
        ExpressionNode<Boolean> condition = new DoubleGreaterThanNode<>(X,Y);
        ExpressionNode<Number> Add = new DoubleAddNode(X, Y);
        ExpressionNode<Number> Multiply = new DoubleMultiplyNode(X, Y);

        ExpressionNode<Number> ifNode = new IfNode<>(condition, Add, Multiply);

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
        double result = ((Number)ifNode.evaluate()).doubleValue();
        assertEquals(1.0,result,0.0);//1!>1 1*1=1
        adu.publishNext("X");//advance x 2>1 1+2=3
        result = ((Number)ifNode.evaluate()).doubleValue();
        assertEquals(3.0,result,0.0);
        adu.publishNext("Y");//advance y 2!>2 2*2=4
        result = ((Number)ifNode.evaluate()).doubleValue();
        assertEquals(4.0,result,0.0);
        adu.advance();
        adu.publishNext("Y");//advance y 2!>3 2*3=6
        result = ((Number)ifNode.evaluate()).doubleValue();
        assertEquals(6.0,result,0.0);
        adu.publishNext("X");//advance x 3!>3 3*3 = 9
        result = ((Number)ifNode.evaluate()).doubleValue();
        assertEquals(9.0,result,0.0);

        //looney test
        adu.publish("X", 100.0);
        result = ((Number)ifNode.evaluate()).doubleValue();//100>3 100+3 = 103
        assertEquals(103.0,result,0.0);
    }
    @Test
    public void testNestedEvaluate() {

        System.out.print("Nested Test \n");

        //Nested IF((Z > X), (IF(([Y]>[X]),([X]+[Y]),([X]*[Y]))), (222))

        UpdateableLeafNode<Number> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Number> Y = new UpdateableLeafNode<>("Y");
        ArrayList<Number> values = new ArrayList<>();
        values.add(10.0);
        values.add(8.0);
        values.add(6.0);
        ArrayDataUpdater adu = new ArrayDataUpdater(values);
        ExpressionNode<Boolean> condition = new DoubleGreaterThanNode<>(Y,X);
        ExpressionNode<Number> Add = new DoubleAddNode(X, Y);
        ExpressionNode<Number> Multiply = new DoubleMultiplyNode(X, Y);

        //ifNode = IF(([X]>[Y]),([X]+[Y]),([X]*[Y]))
        ExpressionNode<Number> ifNode = new IfNode<>(condition, Add, Multiply);

        //IF((X <= Z), (ifNode),(222))),
        ConstantLeafNode<Number> Z = new ConstantLeafNode<>(7.0);
        ConstantLeafNode<Number> TTT = new ConstantLeafNode<>(222.0);
        ExpressionNode<Boolean> newCondition = new LessThanOrEqualNode<>(X,Z);
        ExpressionNode<Number> outerIfNode = new IfNode<>(newCondition, TTT, ifNode);


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
        double result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(100.0,result,0.0);//(10 !<= 7) =>  10!>10 10*10=100
        adu.publishNext("X");//advance x:(8 !<= 7) =>  10>8 10+8=18
        result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(18.0,result,0.0);
        adu.publishNext("Y");//advance y 8!>8 => 8*8=64
        result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(64.0,result,0.0);
        adu.advance();
        adu.publishNext("Y");//advance y 6!>8 6*8=48
        result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(48.0,result,0.0);
        adu.publishNext("X");//advance x (6 <= 7) => TTT
        result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(222.0,result,0.0);
    }

    @Test
    public void useMoreComparisons(){

        System.out.print("Comparison Test \n");

        //IF(500 <= X AND X <= 1000, X, IF(X < 500, X + 500, 1000))
        UpdateableLeafNode<Number> X = new UpdateableLeafNode<>("X");

        BaseDataUpdater adu = new BaseDataUpdater();


        ExpressionNode<Number> const1 = new ConstantLeafNode<>(500.0);
        ExpressionNode<Number> const2 = new ConstantLeafNode<>(1000.0);

        ExpressionNode<Boolean> intermediateCondition1 = new LessThanOrEqualNode<>(const1, X);
        ExpressionNode<Boolean> intermediateCondition2 = new GreaterThanOrEqualNode<>(const2, X);

        ExpressionNode<Boolean> condition1 = new AndNode(intermediateCondition1, intermediateCondition2);

        ExpressionNode<Boolean> nextCondition = new LessThanNode<>(X,const1);
        ExpressionNode<Number> nextThenNode = new DoubleAddNode(X, const1);

        ExpressionNode<Number> nestedIfNode = new IfNode<>(nextCondition, nextThenNode, const2);
        ExpressionNode<Number> outerIfNode = new IfNode<>(condition1, X, nestedIfNode);

        String expression = outerIfNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = outerIfNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?>> list = outerIfNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publish("X", 200.0);
        double result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(700.0,result,0.0);//(10 !<= 7) =>  10!>10 10*10=100
        adu.publish("X", 670.0);//advance x:(8 !<= 7) =>  10>8 10+8=18
        result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(670.0,result,0.0);
        adu.publish("X", 1200);//advance y 8!>8 => 8*8=64
        result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(1000.0,result,0.0);
    }
}
