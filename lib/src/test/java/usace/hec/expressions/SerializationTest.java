package usace.hec.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import usace.hec.expressions.comparison.DoubleGreaterThanNode;
import usace.hec.expressions.logical.DoubleIfNode;
import usace.hec.expressions.math.DoubleAddNode;
import usace.hec.expressions.math.DoubleMultiplyNode;

public class SerializationTest {

    @SuppressWarnings("unchecked")
    private static <T extends ExpressionNode> T roundTrip(T obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(obj);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return (T) in.readObject();
        }
    }

    @Test
    public void constantNodeRoundTrips() throws IOException, ClassNotFoundException {
        DoubleConstantNode original = new DoubleConstantNode(3.4);
        DoubleConstantNode copy = roundTrip(original);
        assertEquals(original.evaluate(), copy.evaluate(), 0.0);
        assertEquals(original.PreFixSyntax(), copy.PreFixSyntax());
    }

    @Test
    public void expressionTreeRoundTrips() throws Exception {
        DoubleExpressionNode original = new DoubleAddNode(
                new DoubleConstantNode(2.0),
                new DoubleConstantNode(3.4));
        DoubleExpressionNode copy = roundTrip(original);
        assertEquals(original.evaluate(), copy.evaluate(), 0.0);
        assertEquals(original.PreFixSyntax(), copy.PreFixSyntax());
    }

    @Test
    public void ifNodeRoundTrips() throws Exception {
        DoubleExpressionNode original = new DoubleIfNode(
                new BooleanConstantNode(true),
                new DoubleConstantNode(1.0),
                new DoubleConstantNode(2.0));
        DoubleExpressionNode copy = roundTrip(original);
        assertEquals(original.evaluate(), copy.evaluate(), 0.0);
    }

    @Test
    public void variableNodeRoundTripsIncludingCurrentValue() throws Exception {
        DoubleVariableNode original = new DoubleVariableNode("Flow");
        original.onDataUpdate(new DataUpdate("Flow", 42.0));
        DoubleVariableNode copy = roundTrip(original);
        assertEquals(original.evaluate(), copy.evaluate(), 0.0);

        copy.onDataUpdate(new DataUpdate("Flow", 99.0));
        assertEquals(99.0, copy.evaluate(), 0.0);
    }

    @Test
    public void bigIfNodeRoundTrips() throws Exception {
        // IF([X] > [Y], [X] + [Y], [X] * [Y])
        DoubleVariableNode x = new DoubleVariableNode("X");
        DoubleVariableNode y = new DoubleVariableNode("Y");

        ArrayList<Double> values = new ArrayList<>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);

        ArrayDataUpdater adu = new ArrayDataUpdater(values);

        BooleanExpressionNode condition = new DoubleGreaterThanNode(x, y);
        DoubleExpressionNode add = new DoubleAddNode(x, y);
        DoubleExpressionNode multiply = new DoubleMultiplyNode(x, y);
        DoubleExpressionNode ifNode = new DoubleIfNode(condition, add, multiply);

        DoubleExpressionNode copy = roundTrip(ifNode);

        List<DataListener> list = ifNode.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }
        list = copy.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        adu.publishNext("X");
        adu.publishNext("Y");
        assertEquals(ifNode.evaluate(), copy.evaluate(), 0.0);
    }

    @Test
    public void variableNodeRoundTripsButDropsItsProvider() throws Exception {
        DataHub provider = new DataHub();
        provider.setValue("Flow", 42.0);

        DoubleVariableNode requester = new DoubleVariableNode("Flow");
        requester.setProvider(provider);
        assertEquals(42.0, requester.evaluate(), 0.0);

        DoubleVariableNode copy = roundTrip(requester);
        assertEquals("Flow", copy.getName());

        assertThrows(NullPointerException.class, copy::evaluate);

        copy.setProvider(provider);
        assertEquals(42.0, copy.evaluate(), 0.0);
    }
}