package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UpdateableLeafNode;

public class ExponentNodeTest {
    @Test
    public void testEvaluate() {
        UpdateableLeafNode<Number> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Number> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Number> Exp = new DoubleExponentNode(X, Y);
        List<DataListener<?>> list = Exp.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publish("X",1.0);
        adu.publish("Y",2.0);
        Double result = ((Number)Exp.evaluate()).doubleValue(); // 1^(2) = 1
        assertEquals(1.0, result, 0.0);
        adu.publish("X",2.0);
        result = ((Number)Exp.evaluate()).doubleValue(); // 2^(2) = 4
        assertEquals(4.0, result, 0.0);
        adu.publish("Y", 0.0);
        result = ((Number)Exp.evaluate()).doubleValue(); // 2^(0) = 1
        assertEquals(1.0, result, 0.0);
        adu.publish("X",9.0);
        adu.publish("Y",0.5);
        result = ((Number)Exp.evaluate()).doubleValue(); //sqrt(9) = 3
        assertEquals(3.0, result, 0.0);
        adu.publish("X",-1000.0); //sqrt(-1000) = 1000i, but not supported I believe.

        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, () -> Exp.evaluate());
        assertEquals("Imaginary number unsupported", ex.getMessage());
    }

    @Test
    public void testSyntax(){ //You will have to examine the print statements, it will automatically return test passed.
        UpdateableLeafNode<Number> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Number> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Number> Exp = new DoubleExponentNode(X, Y);

        String expression = Exp.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = Exp.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

    }
}
