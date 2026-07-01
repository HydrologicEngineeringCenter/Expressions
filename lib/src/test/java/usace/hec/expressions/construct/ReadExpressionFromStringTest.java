package usace.hec.expressions.construct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.*;
import usace.hec.expressions.math.*;


public class ReadExpressionFromStringTest {

    @Test
    public void simpleString() {
        ExpressionNode<Double> expected = new AddNode(new ConstantLeafNode<>(2.0), new ConstantLeafNode<>(3.4));
        String expression = expected.PreFixSyntax();
        System.out.println(expected.PreFixSyntax());
        ExpressionNode<Double> result = ExpressionNode.fromPreFixSyntax(expression);
        assertEquals(result.evaluate(), expected.evaluate());
    }
    @Test
    public void nestedString(){

        ExpressionNode<Double> multNode = new MultiplyNode(new ConstantLeafNode<>(2.0), new ConstantLeafNode<>(3.4));
        ExpressionNode<Double> expected = new AddNode(multNode, new ConstantLeafNode<>(3.4));
        String expression = expected.PreFixSyntax();
        System.out.println(expected.PreFixSyntax());
        ExpressionNode<Double> result = ExpressionNode.fromPreFixSyntax(expression);
        System.out.println(expected.PreFixSyntax());
        assertEquals(result.evaluate(), expected.evaluate());
    }
}
