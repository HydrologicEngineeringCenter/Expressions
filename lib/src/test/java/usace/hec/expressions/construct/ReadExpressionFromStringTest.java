package usace.hec.expressions.construct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.*;
import usace.hec.expressions.comparison.GreaterThanOrEqualNode;
import usace.hec.expressions.comparison.LessThanNode;
import usace.hec.expressions.comparison.LessThanOrEqualNode;
import usace.hec.expressions.logical.AndNode;
import usace.hec.expressions.logical.IfNode;
import usace.hec.expressions.math.*;


public class ReadExpressionFromStringTest {
    @Test
    public void simpleString() {
        //ADD(2.0,3.4)
        ExpressionNode<Double> expected = new AddNode(new ConstantLeafNode<>(2.0), new ConstantLeafNode<>(3.4));
        String expression = expected.PreFixSyntax();
        System.out.println(expected.PreFixSyntax());
        ExpressionNode<Double> result = ExpressionNode.fromPreFixSyntax(expression);
        assertEquals(result.evaluate(), expected.evaluate());
    }
    @Test
    public void nestedString(){
        //ADD(MULT(2.0,3.4), 3.4)
        ExpressionNode<Double> multNode = new MultiplyNode(new ConstantLeafNode<>(2.0), new ConstantLeafNode<>(3.4));
        ExpressionNode<Double> expected = new AddNode(multNode, new ConstantLeafNode<>(3.4));
        String expression = expected.PreFixSyntax();
        System.out.println(expected.PreFixSyntax());
        ExpressionNode<Double> result = ExpressionNode.fromPreFixSyntax(expression);
        System.out.println(expected.PreFixSyntax());
        assertEquals(result.evaluate(), expected.evaluate());
    }

    @Test
    public void AddSubWithUpdatablesString(){
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> Add = new AddNode(X, Y);
        ExpressionNode<Double> Minus = new MinusNode(X, Y);
        List<DataListener<?>> list = Add.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        String expressionAdd = Add.PreFixSyntax();
        System.out.println(Add.PreFixSyntax());
        ExpressionNode<Double> expectedAdd = ExpressionNode.fromPreFixSyntax(expressionAdd);
        String expressionMinus = Minus.PreFixSyntax();
        System.out.println(Minus.PreFixSyntax());
        ExpressionNode<Double> expectedMinus = ExpressionNode.fromPreFixSyntax(expressionMinus);
        BaseDataUpdater adu1 = new BaseDataUpdater();

        //POTENTIAL PROBLEM: the string parser creates new UpdatableLeafNodes, so nodes like Add and Minus who share the same
        //updatable leaf node will not share them once reserialized. Might have to change owner to use the string name instead of the object
        //itself.

        List<DataListener<?>> list1 = expectedAdd.fetchListeners();
        for(DataListener<?> d : list1){
            adu1.register(d);
        }
        List<DataListener<?>> list2 = expectedMinus.fetchListeners();
        for(DataListener<?> d : list2){
            adu1.register(d);
        }

        adu.publish("X",1.0);
        adu.publish("Y",1.0);
        adu1.publish("X",1.0);
        adu1.publish("Y",1.0);

        Double result = Add.evaluate();
        Double result2 = Minus.evaluate();
        Double expected = expectedAdd.evaluate();
        Double expected2 = expectedMinus.evaluate();
        assertEquals(result, expected, 0.0);
        assertEquals(result2, expected2, 0.0);
    }

    @Test
    public void bigIfTest(){

        //Begin Copy from IfNodeTest

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

        //End Copy from IfNodeTest


        List<DataListener<?>> list = outerIfNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        ExpressionNode<Double> expected = ExpressionNode.fromPreFixSyntax(expression);
        List<DataListener<?>> list1 = expected.fetchListeners();
        for(DataListener<?> d : list1){
            adu.register(d);
        }
        adu.publish("X", 200.0);
        double result = outerIfNode.evaluate();
        assertEquals(result, expected.evaluate(), 0.0);
    }
}
