package usace.hec.expressions;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;
import usace.hec.expressions.comparison.DoubleLessThanOrEqualNode;
import usace.hec.expressions.comparison.DoubleGreaterThanOrEqualNode;
import usace.hec.expressions.comparison.DoubleLessThanNode;
import usace.hec.expressions.logical.AndNode;
import usace.hec.expressions.logical.DoubleIfNode;
import usace.hec.expressions.math.DoubleAddNode;

public class ProviderVsListenerTest {

    @Test
    public void useMoreComparisons() {
        // IF(500 <= X AND X <= 1000, X, IF(X < 500, X + 500, 1000))

        // --- DataListener path ---
        DoubleVariableNode x = new DoubleVariableNode("X");
        BaseDataUpdater adu = new BaseDataUpdater();

        DoubleConstantNode const1 = new DoubleConstantNode(500.0);
        DoubleConstantNode const2 = new DoubleConstantNode(1000.0);

        BooleanExpressionNode intermediateCondition1 = new DoubleGreaterThanOrEqualNode(x, const1);
        BooleanExpressionNode intermediateCondition2 = new DoubleLessThanOrEqualNode(x, const2);
        BooleanExpressionNode condition1 = new AndNode(intermediateCondition1, intermediateCondition2);

        BooleanExpressionNode nextCondition = new DoubleLessThanNode(x, const1);
        DoubleExpressionNode nextThenNode = new DoubleAddNode(x, const1);
        DoubleExpressionNode nestedIf = new DoubleIfNode(nextCondition, nextThenNode, const2);
        DoubleExpressionNode outerIf = new DoubleIfNode(condition1, x, nestedIf);

        List<DataListener> list = outerIf.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 100_000_000; i++) {
            adu.publish("X", 200.0);
        }
        double result = outerIf.evaluate();
        long t2 = System.currentTimeMillis();
        assertEquals(700.0, result, 0.0);

        adu.publish("X", 670.0);
        result = outerIf.evaluate();
        assertEquals(670.0, result, 0.0);

        adu.publish("X", 1200.0);
        result = outerIf.evaluate();
        assertEquals(1000.0, result, 0.0);

        System.out.println("DataListener execution took " + (t2 - t1) + " ms");

        // --- DataProvider path ---
        DoubleVariableNode xx = new DoubleVariableNode("XX");
        DataProvider dp = new DataHub();

        DoubleConstantNode const1b = new DoubleConstantNode(500.0);
        DoubleConstantNode const2b = new DoubleConstantNode(1000.0);

        BooleanExpressionNode intermediateCondition3 = new DoubleGreaterThanOrEqualNode(xx, const1b);
        BooleanExpressionNode intermediateCondition4 = new DoubleLessThanOrEqualNode(xx, const2b);
        BooleanExpressionNode condition2 = new AndNode(intermediateCondition3, intermediateCondition4);

        BooleanExpressionNode nextCondition2 = new DoubleLessThanNode(xx, const1b);
        DoubleExpressionNode nextThenNode2 = new DoubleAddNode(xx, const1b);
        DoubleExpressionNode nestedIf2 = new DoubleIfNode(nextCondition2, nextThenNode2, const2b);
        DoubleExpressionNode outerIf2 = new DoubleIfNode(condition2, xx, nestedIf2);

        outerIf2.setProvider(dp);

        t1 = System.currentTimeMillis();
        for (int i = 0; i < 100_000_000; i++) {
            dp.setValue("XX", 200.0);
        }
        double result2 = outerIf2.evaluate();
        t2 = System.currentTimeMillis();
        assertEquals(700.0, result2, 0.0);

        dp.setValue("XX", 670.0);
        result2 = outerIf2.evaluate();
        assertEquals(670.0, result2, 0.0);

        dp.setValue("XX", 1200.0);
        result2 = outerIf2.evaluate();
        assertEquals(1000.0, result2, 0.0);

        System.out.println("DataProvider execution took " + (t2 - t1) + " ms");
    }

    @Test
    public void comparisonSimulator() {
        int dataListenerWins = 0;
        int dataProviderWins = 0;

        // DataListener path
        DoubleVariableNode x = new DoubleVariableNode("X");
        BaseDataUpdater adu = new BaseDataUpdater();

        DoubleConstantNode c1 = new DoubleConstantNode(500.0);
        DoubleConstantNode c2 = new DoubleConstantNode(1000.0);

        BooleanExpressionNode ic1 = new DoubleGreaterThanOrEqualNode(x, c1);
        BooleanExpressionNode ic2 = new DoubleLessThanOrEqualNode(x, c2);
        BooleanExpressionNode cond1 = new AndNode(ic1, ic2);
        BooleanExpressionNode nc1 = new DoubleLessThanNode(x, c1);
        DoubleExpressionNode ntn1 = new DoubleAddNode(x, c1);
        DoubleExpressionNode nested1 = new DoubleIfNode(nc1, ntn1, c2);
        DoubleExpressionNode outer1 = new DoubleIfNode(cond1, x, nested1);

        List<DataListener> list = outer1.fetchListeners();
        for (DataListener d : list) {
            adu.register(d);
        }

        // DataProvider path
        DoubleVariableNode xx = new DoubleVariableNode("XX");
        DataProvider dp = new DataHub();

        DoubleConstantNode c1b = new DoubleConstantNode(500.0);
        DoubleConstantNode c2b = new DoubleConstantNode(1000.0);

        BooleanExpressionNode ic3 = new DoubleGreaterThanOrEqualNode(xx, c1b);
        BooleanExpressionNode ic4 = new DoubleLessThanOrEqualNode(xx, c2b);
        BooleanExpressionNode cond2 = new AndNode(ic3, ic4);
        BooleanExpressionNode nc2 = new DoubleLessThanNode(xx, c1b);
        DoubleExpressionNode ntn2 = new DoubleAddNode(xx, c1b);
        DoubleExpressionNode nested2 = new DoubleIfNode(nc2, ntn2, c2b);
        DoubleExpressionNode outer2 = new DoubleIfNode(cond2, xx, nested2);

        outer2.setProvider(dp);

        for (int i = 0; i < 1000; i++) {
            long t1 = System.nanoTime();
            for (int j = 0; j < 1_000_000; j++) {
                adu.publish("X", 200.0);
            }
            outer1.evaluate();
            long t2 = System.nanoTime();
            long dataListenerTime = t2 - t1;

            t1 = System.nanoTime();
            for (int j = 0; j < 1_000_000; j++) {
                dp.setValue("XX", 200.0);
            }
            outer2.evaluate();
            t2 = System.nanoTime();
            long dataProviderTime = t2 - t1;

            if (dataListenerTime > dataProviderTime) {
                dataListenerWins++;
            } else {
                dataProviderWins++;
            }

            if (i % 5 == 0) {
                System.out.println("DataListener Wins: " + dataListenerWins);
                System.out.println("DataProvider Wins: " + dataProviderWins);
                System.out.println("Difference: " + (dataListenerTime - dataProviderTime));
            }
        }
    }
}