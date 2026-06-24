package usace.hec.expressions.misc;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import usace.hec.expressions.*;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MinusNode;

public class RandNodeTest {
    @Test
    public void basicRandTest(){
        ExpressionNode<Integer> seed = new ConstantLeafNode<>(4);
        ExpressionNode<Double> randNode = new RandNode(seed);

        BaseDataUpdater adu = new BaseDataUpdater();

        List<DataListener<?>> list = randNode.fetchListeners();
        for(DataListener<?> d : list) {
            adu.register(d);
        }

        Random check = new Random(4);

        for (int i = 0; i < 33550336; i++) {
            Double result = randNode.evaluate();
            assertEquals(check.nextDouble(), result, 0.0);
        }

        String expression = randNode.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = randNode.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");
    }

}
