package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UpdateableLeafNode;

public class AbsNegateTest {
    @Test
    public void testEvaluate() {
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> neg = new NegateNode(X); // -X
        ExpressionNode<Double> abs = new AbsNode(neg); // |-X|
        List<DataListener<?>> list = abs.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publish("X",1.0);
        Double result = neg.evaluate(); // -1
        assertEquals(-1.0, result, 0.0);
        result = abs.evaluate(); // |-1|
        assertEquals(1.0, result, 0.0);
        adu.publish("X",-2.0);
        result = neg.evaluate(); // -(-2)
        assertEquals(2.0, result, 0.0);
        result = abs.evaluate(); // |-2|
        assertEquals(2.0, result, 0.0);
        adu.publish("X",0.0);
        result = neg.evaluate(); //-0 = 0
        assertEquals(0.0, result, 0.0);
        result = abs.evaluate();//|0| = 0
        assertEquals(0.0, result, 0.0);
        ExpressionNode<Double> doubleNeg = new NegateNode(neg); // -(-X)
        adu.publish("X",500.0);
        result = doubleNeg.evaluate(); //-(-500)
        assertEquals(500.0, result, 0.0);
        adu.publish("X",-500.0);
        result = doubleNeg.evaluate();//-(-(-500))
        assertEquals(-500.0, result, 0.0);

    }

    @Test
    public void testSyntax(){ //You will have to examine the print statements, it will automatically return test passed.
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> neg = new NegateNode(X);
        ExpressionNode<Double> abs = new AbsNode(neg);

        String expression = neg.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = neg.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

        String expression2 = abs.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = abs.ExcelSyntax();
        System.out.print(expression2Infix+ "\n");

        ExpressionNode<Double> doubleNeg = new NegateNode(neg);

        String expression3 = doubleNeg.PreFixSyntax();
        System.out.print(expression3 + "\n");
        String expression3Infix = doubleNeg.ExcelSyntax();
        System.out.print(expression3Infix+ "\n");
    }
}
