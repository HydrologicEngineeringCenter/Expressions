package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UpdateableLeafNode;

public class AddNodeTest {
    @Test
    public void testEvaluate() {
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> Add = new AddNode(X, Y);
        List<DataListener<?>> list = Add.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publish("X",1.0);
        adu.publish("Y",1.0);
        Double result = Add.evaluate();
        assertEquals(2.0, result, 0.0);
        adu.publish("X",2.0);
        result = Add.evaluate();
        assertEquals(3.0, result, 0.0);
        adu.publish("Y", 2.0);
        result = Add.evaluate();
        assertEquals(4.0, result, 0.0);
        adu.publish("X",3.0);
        adu.publish("Y",3.0);
        result = Add.evaluate();
        assertEquals(6.0, result, 0.0);
    }
}
