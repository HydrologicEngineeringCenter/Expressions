package usace.hec.expressions.logical;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.function.Constant;
import org.junit.Test;

import usace.hec.expressions.*;
import usace.hec.expressions.comparison.GreaterThanNode;
import usace.hec.expressions.comparison.LessThanOrEqualNode;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MultiplyNode;

public class AndOrXorTest {
    @Test
    public void testAndEvaluate() {
        UpdateableLeafNode<Boolean> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Boolean> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Boolean> andNode = new AndNode(X,Y);

        String expression = andNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = andNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?, ?>> list = andNode.fetchListeners();
        for(DataListener<?, ?> d : list){
            adu.register(d);
        }
        adu.publish("X",true);
        adu.publish("Y",true);
        Boolean result = andNode.evaluate(); //true and true
        assertEquals(true, result);
        adu.publish("X",false);
        result = andNode.evaluate();//false and true
        assertEquals(false, result);
        adu.publish("Y", false);
        result = andNode.evaluate(); //false and false
        assertEquals(false, result);
        adu.publish("X",true);
        result = andNode.evaluate(); //true and false
        assertEquals(false, result);

    }
    @Test
    public void testOrEvaluate() {
        UpdateableLeafNode<Boolean> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Boolean> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Boolean> orNode = new OrNode(X,Y);

        String expression = orNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = orNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?, ?>> list = orNode.fetchListeners();
        for(DataListener<?, ?> d : list){
            adu.register(d);
        }
        adu.publish("X",true);
        adu.publish("Y",true);
        Boolean result = orNode.evaluate(); //true and true
        assertEquals(true, result);
        adu.publish("X",false);
        result = orNode.evaluate();//false and true
        assertEquals(true, result);
        adu.publish("Y", false);
        result = orNode.evaluate(); //false and false
        assertEquals(false, result);
        adu.publish("X",true);
        result = orNode.evaluate(); //true and false
        assertEquals(true, result);

    }
    @Test
    public void testXorEvaluate() {
        UpdateableLeafNode<Boolean> X = new UpdateableLeafNode<>("X");
        UpdateableLeafNode<Boolean> Y = new UpdateableLeafNode<>("Y");

        BaseDataUpdater adu = new BaseDataUpdater();
        ExpressionNode<Boolean> XorNode = new XorNode(X,Y);

        String expression = XorNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = XorNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
        List<DataListener<?, ?>> list = XorNode.fetchListeners();
        for(DataListener<?, ?> d : list){
            adu.register(d);
        }
        adu.publish("X",true);
        adu.publish("Y",true);
        Boolean result = XorNode.evaluate(); //true and true
        assertEquals(false, result);
        adu.publish("X",false);
        result = XorNode.evaluate();//false and true
        assertEquals(true, result);
        adu.publish("Y", false);
        result = XorNode.evaluate(); //false and false
        assertEquals(false, result);
        adu.publish("X",true);
        result = XorNode.evaluate(); //true and false
        assertEquals(true, result);
    }
}
