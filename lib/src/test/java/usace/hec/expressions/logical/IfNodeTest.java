package usace.hec.expressions.logical;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import usace.hec.expressions.ArrayDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UpdateableLeafNode;
import usace.hec.expressions.comparison.GreaterThanNode;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MultiplyNode;

public class IfNodeTest {
    @Test
    public void testEvaluate() {
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

        String expression = ifNode.stringify();

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
}
