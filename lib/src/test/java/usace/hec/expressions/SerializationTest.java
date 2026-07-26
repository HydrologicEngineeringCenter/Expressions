package usace.hec.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import usace.hec.expressions.comparison.GreaterThanNode;
import usace.hec.expressions.logical.IfNode;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MultiplyNode;
import usace.hec.expressions.misc.LagNode;

public class SerializationTest {

    // Generic round-trip helper: writes obj to an in-memory byte stream, then
    // reads it back. Any node that isn't actually serializable throws here.
    @SuppressWarnings("unchecked")
    private static <T extends Serializable> T roundTrip(T obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(obj);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return (T) in.readObject();
        }
    }

    @Test
    public void constantLeafNodeRoundTrips() throws IOException, ClassNotFoundException {
        ConstantLeafNode<Double> original = new ConstantLeafNode<>(3.4);

        ConstantLeafNode<Double> copy = roundTrip(original);

        assertEquals(original.evaluate(), copy.evaluate());
        assertEquals(original.PreFixSyntax(), copy.PreFixSyntax());
    }

    @Test
    public void expressionTreeRoundTrips() throws Exception {
        ExpressionNode<Number> original = new AddNode(new ConstantLeafNode<>(2.0), new ConstantLeafNode<>(3.4));

        ExpressionNode<Number> copy = roundTrip(original);

        assertEquals(original.evaluate(), copy.evaluate());
        assertEquals(original.PreFixSyntax(), copy.PreFixSyntax());
    }

    @Test
    public void ifNodeRoundTrips() throws Exception {
        ExpressionNode<Double> original = new IfNode<>(
                new ConstantLeafNode<>(true),
                new ConstantLeafNode<>(1.0),
                new ConstantLeafNode<>(2.0));

        ExpressionNode<Double> copy = roundTrip(original);

        assertEquals(original.evaluate(), copy.evaluate());
    }



    @Test
    public void updateableLeafNodeRoundTripsIncludingCurrentValue() throws Exception {
        UpdateableLeafNode<Double> original = new UpdateableLeafNode<>("Flow");
        original.onDataUpdate(new DataUpdate<>("Flow", 42.0));

        UpdateableLeafNode<Double> copy = roundTrip(original);

        assertEquals(original.evaluate(), copy.evaluate());
        // The listener logic survives too: the copy still recognizes updates for its name.
        copy.onDataUpdate(new DataUpdate<>("Flow", 99.0));
        assertEquals(Double.valueOf(99.0), copy.evaluate());
    }

    @Test
    public void bigIfNodeRoundTrips() throws Exception {

        //IF(([X]>[Y]),([X]+[Y]),([X]*[Y]))
        UpdateableLeafNode<Number> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Number> Y = new UpdateableLeafNode<>("Y");
        ArrayList<Number> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        ArrayDataUpdater adu = new ArrayDataUpdater(values);
        ExpressionNode<Boolean> condition = new GreaterThanNode<>(X,Y);
        ExpressionNode<Number> Add = new AddNode(X, Y);
        ExpressionNode<Number> Multiply = new MultiplyNode(X, Y);

        ExpressionNode<Number> ifNode = new IfNode<>(condition, Add, Multiply);

        ExpressionNode<Number> copy = roundTrip(ifNode);

        List<DataListener<?>> list = ifNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }

        list = copy.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publishNext("X");
        adu.publishNext("Y");

        assertEquals(ifNode.evaluate(), copy.evaluate());
    }
    @Test
    public void requesterRoundTripsButDropsItsProvider() throws Exception {
        DataHub provider = new DataHub();
        provider.setValueForCurrentTimestep("Flow", 42.0);
        UpdateableLeafNodeRequester<Double> requester = new UpdateableLeafNodeRequester<>("Flow");
        requester.setProvider(provider);
        assertEquals(Double.valueOf(42.0), requester.evaluate());

        // dp is transient, must have setProvider() called again before it can evaluate().
        UpdateableLeafNodeRequester<Double> copy = roundTrip(requester);

        assertEquals("Flow", copy.getName());
        assertThrows(NullPointerException.class, copy::evaluate);

        copy.setProvider(provider);
        assertEquals(Double.valueOf(42.0), copy.evaluate());
    }

}
