package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import usace.hec.expressions.ArrayDataUpdater;
import usace.hec.expressions.ConstantLeafNode;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UpdateableLeafNode;

public class AddNodeTest {
    @Test
    public void testEvaluate() {
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Double> Y = new UpdateableLeafNode<>("Y");
        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        ArrayDataUpdater adu = new ArrayDataUpdater(values);
        ExpressionNode<Double> Add = new AddNode(X, Y);
        List<DataListener<?>> list = Add.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publishNext("X");
        adu.publishNext("Y");
        adu.advance();
        Double result = Add.evaluate();
        assertEquals(2.0, result, 0.0);
        adu.publishNext("X");
        result = Add.evaluate();
        assertEquals(3.0, result, 0.0);
        adu.publishNext("Y");
        adu.advance();
        result = Add.evaluate();
        assertEquals(4.0, result, 0.0);
        adu.publishNext("X");
        adu.publishNext("Y");
        adu.advance();
        result = Add.evaluate();
        assertEquals(6.0, result, 0.0);
    }
}
