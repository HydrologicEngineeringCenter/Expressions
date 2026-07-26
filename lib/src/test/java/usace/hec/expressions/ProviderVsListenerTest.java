package usace.hec.expressions;

import org.junit.Test;
import usace.hec.expressions.comparison.GreaterThanOrEqualNode;
import usace.hec.expressions.comparison.LessThanNode;
import usace.hec.expressions.comparison.LessThanOrEqualNode;
import usace.hec.expressions.logical.AndNode;
import usace.hec.expressions.logical.IfNode;
import usace.hec.expressions.math.DoubleAddNode;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProviderVsListenerTest {


    @Test
    //Taken from IfNodeTest
    public void useMoreComparisons(){


        //IF(500 <= X AND X <= 1000, X, IF(X < 500, X + 500, 1000))

        UpdateableLeafNode<Number> X = new UpdateableLeafNode<>("X");

        BaseDataUpdater adu = new BaseDataUpdater();

        ExpressionNode<Number> const1 = new ConstantLeafNode<>(500.0);
        ExpressionNode<Number> const2 = new ConstantLeafNode<>(1000.0);

        ExpressionNode<Boolean> intermediateCondition1 = new LessThanOrEqualNode<>(const1, X);
        ExpressionNode<Boolean> intermediateCondition2 = new GreaterThanOrEqualNode<>(const2, X);

        ExpressionNode<Boolean> condition1 = new AndNode(intermediateCondition1, intermediateCondition2);

        ExpressionNode<Boolean> nextCondition = new LessThanNode<>(X,const1);
        ExpressionNode<Number> nextThenNode = new DoubleAddNode(X, const1);

        ExpressionNode<Number> nestedIfNode = new IfNode<>(nextCondition, nextThenNode, const2);
        ExpressionNode<Number> outerIfNode = new IfNode<>(condition1, X, nestedIfNode);

        List<DataListener<?>> list = outerIfNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i ++){
            adu.publish("X", 200.0);
        }
        double result = ((Number)outerIfNode.evaluate()).doubleValue();
        long t2 = System.currentTimeMillis();
        assertEquals(700.0,result,0.0);//(10 !<= 7) =>  10!>10 10*10=100
        adu.publish("X", 670.0);//advance x:(8 !<= 7) =>  10>8 10+8=18
        result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(670.0,result,0.0);
        adu.publish("X", 1200);//advance y 8!>8 => 8*8=64
        result = ((Number)outerIfNode.evaluate()).doubleValue();
        assertEquals(1000.0,result,0.0);
        System.out.println("DataListener execution took " + (t2 - t1) +" ms");


        //DataProviderTest

        //IF(500 <= X AND X <= 1000, X, IF(X < 500, X + 500, 1000))

        UpdateableLeafNodeRequester<Number> XX = new UpdateableLeafNodeRequester<>("XX");

        DataProvider dp = new DataHub();

        //Assigned earlier
        const1 = new ConstantLeafNode<>(500.0);
        const2 = new ConstantLeafNode<>(1000.0);

        ExpressionNode<Boolean> intermediateCondition3 = new LessThanOrEqualNode<>(const1, XX);
        ExpressionNode<Boolean> intermediateCondition4 = new GreaterThanOrEqualNode<>(const2, XX);

        ExpressionNode<Boolean> condition2 = new AndNode(intermediateCondition3, intermediateCondition4);

        ExpressionNode<Boolean> nextCondition2 = new LessThanNode<>(XX,const1);
        ExpressionNode<Number> nextThenNode2 = new DoubleAddNode(XX, const1);

        ExpressionNode<Number> nestedIfNode2 = new IfNode<>(nextCondition2, nextThenNode2, const2);
        ExpressionNode<Number> outerIfNode2 = new IfNode<>(condition2, XX, nestedIfNode2);

        outerIfNode2.setProvider(dp);

        t1 = System.currentTimeMillis();

        for (int i = 0; i < 100000000; i ++){
            dp.setValueForCurrentTimestep("XX",200.0);
        }
        double result2 = ((Number)outerIfNode2.evaluate()).doubleValue();
        t2 = System.currentTimeMillis();
        assertEquals(700.0,result2,0.0);//(10 !<= 7) =>  10!>10 10*10=100
        dp.setValueForCurrentTimestep("XX",670.0);;//advance x:(8 !<= 7) =>  10>8 10+8=18
        result2 = ((Number)outerIfNode2.evaluate()).doubleValue();
        assertEquals(670.0,result2,0.0);
        dp.setValueForCurrentTimestep("XX",1200.0);
        result2 = ((Number)outerIfNode2.evaluate()).doubleValue();
        assertEquals(1000.0,result2,0.0);
        System.out.println("DataProvider execution took " + (t2 - t1) +" ms");
    }

    @Test
    public void comparisonSimulator(){
        int dataListenerWins = 0;
        int dataProviderWins = 0;

        UpdateableLeafNode<Number> X = new UpdateableLeafNode<>("X");

        BaseDataUpdater adu = new BaseDataUpdater();

        ExpressionNode<Number> const1 = new ConstantLeafNode<>(500.0);
        ExpressionNode<Number> const2 = new ConstantLeafNode<>(1000.0);

        ExpressionNode<Boolean> intermediateCondition1 = new LessThanOrEqualNode<>(const1, X);
        ExpressionNode<Boolean> intermediateCondition2 = new GreaterThanOrEqualNode<>(const2, X);

        ExpressionNode<Boolean> condition1 = new AndNode(intermediateCondition1, intermediateCondition2);

        ExpressionNode<Boolean> nextCondition = new LessThanNode<>(X,const1);
        ExpressionNode<Number> nextThenNode = new DoubleAddNode(X, const1);

        ExpressionNode<Number> nestedIfNode = new IfNode<>(nextCondition, nextThenNode, const2);
        ExpressionNode<Number> outerIfNode = new IfNode<>(condition1, X, nestedIfNode);

        List<DataListener<?>> list = outerIfNode.fetchListeners();
        for(DataListener<?> d : list){
            adu.register(d);
        }



        UpdateableLeafNodeRequester<Number> XX = new UpdateableLeafNodeRequester<>("XX");

        DataProvider dp = new DataHub();

        //Assigned earlier
        const1 = new ConstantLeafNode<>(500.0);
        const2 = new ConstantLeafNode<>(1000.0);

        ExpressionNode<Boolean> intermediateCondition3 = new LessThanOrEqualNode<>(const1, XX);
        ExpressionNode<Boolean> intermediateCondition4 = new GreaterThanOrEqualNode<>(const2, XX);

        ExpressionNode<Boolean> condition2 = new AndNode(intermediateCondition3, intermediateCondition4);

        ExpressionNode<Boolean> nextCondition2 = new LessThanNode<>(XX,const1);
        ExpressionNode<Number> nextThenNode2 = new DoubleAddNode(XX, const1);

        ExpressionNode<Number> nestedIfNode2 = new IfNode<>(nextCondition2, nextThenNode2, const2);
        ExpressionNode<Number> outerIfNode2 = new IfNode<>(condition2, XX, nestedIfNode2);

        outerIfNode2.setProvider(dp);

        for (int i = 0; i < 1000; i ++) {
            long t1 = System.nanoTime();
            for (int j = 0; j < 1000000; j++) {
                adu.publish("X", 200.0);
            }
            double result = ((Number)outerIfNode.evaluate()).doubleValue();
            long t2 = System.nanoTime();
            long dataListenerExecutionTime = t2 - t1;

            t1 = System.nanoTime();

            for (int j = 0; j < 1000000; j++) {
                dp.setValueForCurrentTimestep("XX", 200.0);
            }
            double result2 = ((Number)outerIfNode2.evaluate()).doubleValue();
            t2 = System.nanoTime();
            long dataProviderExecutionTime = t2 - t1;
            if (dataListenerExecutionTime > dataProviderExecutionTime){
                dataListenerWins++;
            } else{
                dataProviderWins++;
            }
            if (i%5==0) {
                System.out.println("DataListener Wins: " + dataListenerWins);
                System.out.println("DataProvider Wins: " + dataProviderWins);
                System.out.println("Difference :" + (dataListenerExecutionTime - dataProviderExecutionTime));
            }
        }
    }
}