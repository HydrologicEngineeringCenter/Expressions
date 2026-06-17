package usace.hec.expressions.time;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import usace.hec.expressions.ConstantLeafNode;
import usace.hec.expressions.logical.IfNode;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MultiplyNode;

public class AfterNodeTest {
    @Test
    public void testEvaluate() {
        Date dayOne = new Date(2026,6 , 16);
        ConstantLeafNode<Date> a = new ConstantLeafNode<Date>(dayOne);
        Date dayTwo = new Date(2026,6 , 18);
        ConstantLeafNode<Date> b = new ConstantLeafNode<Date>(dayTwo);
        AfterNode after = new AfterNode(a, b);
        BeforeNode before = new BeforeNode(a, b);
        ConstantLeafNode<Double> one = new ConstantLeafNode<Double>(1.0);
        ConstantLeafNode<Double> two = new ConstantLeafNode<Double>(2.0);

        AddNode Add = new AddNode(one, two);
        MultiplyNode Multiply = new MultiplyNode(one, two);

        IfNode<Double> ifNodeAfter = new IfNode<>(after, Add, Multiply);
        IfNode<Double> ifNodeBefore = new IfNode<>(before, Add, Multiply);

        Double resultA = ifNodeAfter.evaluate();
        assertEquals(2.0,resultA,0.0);
        Double resultB = ifNodeBefore.evaluate();
        assertEquals(3.0,resultB,0.0);

    }
}
