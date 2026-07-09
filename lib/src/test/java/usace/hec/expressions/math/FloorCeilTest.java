package usace.hec.expressions.math;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import usace.hec.expressions.BaseDataUpdater;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.UpdateableLeafNode;

public class FloorCeilTest {
    @Test
    public void testEvaluate() {
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> ceil = new CeilingNode(X); // Math.ceil(-X)
        ExpressionNode<Double> floor = new FloorNode(X); // Math.floor(X)
        List<DataListener<?>> list = ceil.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }
        adu.publish("X",1.5);
        Double result = ceil.evaluate(); // 2
        assertEquals(2.0, result, 0.0);
        result = floor.evaluate(); // 1
        assertEquals(1.0, result, 0.0);
        adu.publish("X",-2.5);
        result = ceil.evaluate(); // math.ceil(-2.5)
        assertEquals(-2.0, result, 0.0);
        result = floor.evaluate(); //math.floor(-2.5)
        assertEquals(-3.0, result, 0.0);
        adu.publish("X",0.0);
        result = ceil.evaluate(); //math.floor(0.0)
        assertEquals(0.0, result, 0.0);
        result = floor.evaluate();//math.floor(0.0)
        assertEquals(0.0, result, 0.0);

    }

    @Test
    public void testSyntax(){ //You will have to examine the print statements, it will automatically return test passed.
        UpdateableLeafNode<Double> X = new UpdateableLeafNode<>("X");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Double> ceil = new CeilingNode(X); // Math.ceil(-X)
        ExpressionNode<Double> floor = new FloorNode(X); // Math.floor(X)

        String expression = ceil.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = ceil.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

        String expression2 = floor.PreFixSyntax();
        System.out.print(expression2 + "\n");
        String expression2Infix = floor.ExcelSyntax();
        System.out.print(expression2Infix+ "\n");
    }
}
