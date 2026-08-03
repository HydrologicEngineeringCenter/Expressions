package usace.hec.expressions.logical;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import usace.hec.expressions.*;
import usace.hec.expressions.comparison.DoubleGreaterThanNode;
import usace.hec.expressions.comparison.DoubleGreaterThanOrEqualNode;
import usace.hec.expressions.comparison.DoubleLessThanNode;
import usace.hec.expressions.comparison.DoubleLessThanOrEqualNode;
import usace.hec.expressions.math.DoubleAddNode;
import usace.hec.expressions.math.DoubleMultiplyNode;

public class IfNodeTest {

    @Test
    public void testEvaluate() {
        System.out.print("Basic Test\n");
        // IF([X] > [Y], [X] + [Y], [X] * [Y])

        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");

        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        int index = 0;

        DataProvider dp = new DataHub();


        BooleanExpressionNode condition = new DoubleGreaterThanNode(x, y);
        DoubleExpressionNode add = new DoubleAddNode(x, y);
        DoubleExpressionNode multiply = new DoubleMultiplyNode(x, y);
        DoubleExpressionNode ifNode = new DoubleIfNode(condition, add, multiply);

        ifNode.setProvider(dp);

        String expression = ifNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = ifNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");


        dp.setDouble("X", values.get(index));
        dp.setDouble("Y", values.get(index));
        index+=1;

        double result = ifNode.evaluate();
        assertEquals(1.0, result, 0.0); // 1!>1 => 1*1=1

        dp.setDouble("X", values.get(index)); // advance x: 2>1 => 1+2=3
        result = ifNode.evaluate();
        assertEquals(3.0, result, 0.0);

        dp.setDouble("Y", values.get(index)); // advance y: 2!>2 => 2*2=4
        result = ifNode.evaluate();
        assertEquals(4.0, result, 0.0);

        index+=1;
        dp.setDouble("Y", values.get(index)); // advance y: 2!>3 => 2*3=6
        result = ifNode.evaluate();
        assertEquals(6.0, result, 0.0);

        dp.setDouble("X", values.get(index)); // advance x: 3!>3 => 3*3=9
        result = ifNode.evaluate();
        assertEquals(9.0, result, 0.0);

        // Looney test
        dp.setDouble("X", 100.0);
        result = ifNode.evaluate(); // 100>3 => 100+3=103
        assertEquals(103.0, result, 0.0);
    }

    @Test
    public void testNestedEvaluate() {
        System.out.print("Nested Test\n");
        // IF(Z > X, IF([Y] > [X], [X] + [Y], [X] * [Y]), 222)

        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");

        ArrayList<Double> values = new ArrayList<>();
        values.add(10.0);
        values.add(8.0);
        values.add(6.0);
        DataProvider dp = new DataHub();
        int index = 0;

        BooleanExpressionNode innerCondition = new DoubleGreaterThanNode(y, x);
        DoubleExpressionNode add = new DoubleAddNode(x, y);
        DoubleExpressionNode multiply = new DoubleMultiplyNode(x, y);
        DoubleExpressionNode innerIf = new DoubleIfNode(innerCondition, add, multiply);

        DoubleConstantNode z = new DoubleConstantNode(7.0);
        DoubleConstantNode ttt = new DoubleConstantNode(222.0);

        BooleanExpressionNode outerCondition = new DoubleLessThanOrEqualNode(x, z);
        DoubleExpressionNode outerIf = new DoubleIfNode(outerCondition, ttt, innerIf);

        outerIf.setProvider(dp);

        String expression = outerIf.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = outerIf.ExcelSyntax();
        System.out.print(expressionInfix + "\n");


        dp.setDouble("X", values.get(index));
        dp.setDouble("Y", values.get(index));
        index+=1;

        double result = outerIf.evaluate();
        assertEquals(100.0, result, 0.0); // 10!<=7 => 10!>10 => 10*10=100

        dp.setDouble("X", values.get(index)); // 8!<=7 => 10>8 => 10+8=18
        result = outerIf.evaluate();
        assertEquals(18.0, result, 0.0);

        dp.setDouble("Y", values.get(index)); // 8!>8 => 8*8=64
        result = outerIf.evaluate();
        assertEquals(64.0, result, 0.0);

        index+=1;
        dp.setDouble("Y", values.get(index)); // 6!>8 => 6*8=48
        result = outerIf.evaluate();
        assertEquals(48.0, result, 0.0);

        dp.setDouble("X", values.get(index)); // 6<=7 => 222
        result = outerIf.evaluate();
        assertEquals(222.0, result, 0.0);
    }

    @Test
    public void useMoreComparisons() {
        System.out.print("Comparison Test\n");
        // IF(500 <= X AND X <= 1000, X, IF(X < 500, X + 500, 1000))

        DoubleVariableNode x = new DoubleVariableNode("X");
        DataHub dp = new DataHub();

        DoubleConstantNode const1 = new DoubleConstantNode(500.0);
        DoubleConstantNode const2 = new DoubleConstantNode(1000.0);

        BooleanExpressionNode intermediateCondition1 = new DoubleGreaterThanOrEqualNode(x, const1);
        BooleanExpressionNode intermediateCondition2 = new DoubleLessThanOrEqualNode(x, const2);
        BooleanExpressionNode condition1 = new AndNode(intermediateCondition1, intermediateCondition2);

        BooleanExpressionNode nextCondition = new DoubleLessThanNode(x, const1);
        DoubleExpressionNode nextThenNode = new DoubleAddNode(x, const1);
        DoubleExpressionNode nestedIfNode = new DoubleIfNode(nextCondition, nextThenNode, const2);
        DoubleExpressionNode outerIfNode = new DoubleIfNode(condition1, x, nestedIfNode);

        outerIfNode.setProvider(dp);

        String expression = outerIfNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = outerIfNode.ExcelSyntax();
        System.out.print(expressionInfix + "\n");
        
        dp.setDouble("X", 200.0);
        double result = outerIfNode.evaluate();
        assertEquals(700.0, result, 0.0); // 200<500 => 200+500=700

        dp.setDouble("X", 670.0);
        result = outerIfNode.evaluate();
        assertEquals(670.0, result, 0.0); // 500<=670<=1000 => 670

        dp.setDouble("X", 1200.0);
        result = outerIfNode.evaluate();
        assertEquals(1000.0, result, 0.0); // 1200>1000 => 1000
    }
}