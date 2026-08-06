package usace.hec.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import org.junit.Test;
import usace.hec.expressions.comparison.DoubleGreaterThanNode;
import usace.hec.expressions.logical.BooleanIfNode;
import usace.hec.expressions.logical.DateTimeIfNode;
import usace.hec.expressions.logical.DoubleIfNode;
import usace.hec.expressions.logical.IntegerIfNode;
import usace.hec.expressions.logical.StringIfNode;
import usace.hec.expressions.math.DoubleDivideNode;
import usace.hec.expressions.math.IntegerDivideNode;
import usace.hec.expressions.strings.SubstringNode;
import usace.hec.expressions.time.DateNode;

public class EvaluationErrorHandlingTest {

    // IfNode tests

    @Test
    public void testIfNodeConditionErrorSurfaces() {
        System.out.print("IfNode condition error propagation Test\n");

        BooleanExpressionNode erroringCondition = new DoubleGreaterThanNode(
                new DoubleDivideNode(new DoubleConstantNode(1.0), new DoubleConstantNode(0.0)),
                new DoubleConstantNode(0.0));

        DoubleExpressionNode ifNode = new DoubleIfNode(
                erroringCondition, new DoubleConstantNode(1.0), new DoubleConstantNode(2.0));

        ifNode.evaluate();
        assertTrue(ifNode.hasError());
    }


    @Test
    public void testDoubleIfNodeUnusedBranchError() {
        System.out.print("DoubleIfNode unused-branch error Test\n");

        BooleanExpressionNode conditionTrue = new BooleanConstantNode(true);
        DoubleExpressionNode thenBranch = new DoubleConstantNode(42.0);
        DoubleExpressionNode erroringElseBranch = new DoubleDivideNode(
                new DoubleConstantNode(1.0), new DoubleConstantNode(0.0));

        DoubleExpressionNode ifNode = new DoubleIfNode(conditionTrue, thenBranch, erroringElseBranch);

        double result = ifNode.evaluate();
        assertEquals(42.0, result, 0.0); // correct branch's value is still returned
        assertFalse(ifNode.hasError());   // discarded branch's error doesn't surfaces
    }

    @Test
    public void testIntegerIfNodeUnusedBranchError() {
        System.out.print("IntegerIfNode unused-branch error Test\n");

        BooleanExpressionNode conditionFalse = new BooleanConstantNode(false);
        IntegerExpressionNode erroringThenBranch = new IntegerDivideNode(
                new IntegerConstantNode(1), new IntegerConstantNode(0));
        IntegerExpressionNode elseBranch = new IntegerConstantNode(7);

        IntegerExpressionNode ifNode = new IntegerIfNode(conditionFalse, erroringThenBranch, elseBranch);

        int result = ifNode.evaluate();
        assertEquals(7, result);
        assertFalse(ifNode.hasError());
    }

    @Test
    public void testBooleanIfNodeUnusedBranchError() {
        System.out.print("BooleanIfNode unused-branch error Test\n");

        BooleanExpressionNode conditionFalse = new BooleanConstantNode(false);
        BooleanExpressionNode erroringThenBranch = new DoubleGreaterThanNode(
                new DoubleDivideNode(new DoubleConstantNode(1.0), new DoubleConstantNode(0.0)),
                new DoubleConstantNode(0.0));
        BooleanExpressionNode elseBranch = new BooleanConstantNode(true);

        BooleanExpressionNode ifNode = new BooleanIfNode(conditionFalse, erroringThenBranch, elseBranch);

        boolean result = ifNode.evaluate();
        assertTrue(result);
        assertFalse(ifNode.hasError());
    }

    @Test
    public void testStringIfNodeUnusedBranchError() {
        System.out.print("StringIfNode unused-branch error Test\n");

        BooleanExpressionNode conditionTrue = new BooleanConstantNode(true);
        StringExpressionNode thenBranch = new StringConstantNode("ok");
        StringExpressionNode erroringElseBranch = new SubstringNode(
                new StringConstantNode("abc"), new IntegerConstantNode(-1), new IntegerConstantNode(2));

        StringExpressionNode ifNode = new StringIfNode(conditionTrue, thenBranch, erroringElseBranch);

        String result = ifNode.evaluate();
        assertEquals("ok", result);
        assertFalse(ifNode.hasError());
    }

    @Test
    public void testDateTimeIfNodeUnusedBranchError() {
        System.out.print("DateTimeIfNode unused-branch error Test\n");

        BooleanExpressionNode conditionTrue = new BooleanConstantNode(true);
        DateTimeExpressionNode thenBranch = new DateNode(
                new IntegerConstantNode(2026), new IntegerConstantNode(8), new IntegerConstantNode(6));
        DateTimeExpressionNode erroringElseBranch = new DateNode(
                new IntegerConstantNode(2026), new IntegerConstantNode(13), new IntegerConstantNode(1));

        DateTimeExpressionNode ifNode = new DateTimeIfNode(conditionTrue, thenBranch, erroringElseBranch);

        LocalDateTime result = ifNode.evaluate();
        assertEquals(LocalDateTime.of(2026, 8, 6, 0, 0), result);
        assertFalse(ifNode.hasError());
    }

    // SubstringNode

    @Test
    public void testSubstringNodeReportsErrorForNegativeStartIndex() {
        System.out.print("SubstringNode negative start index Test\n");

        StringExpressionNode substr = new SubstringNode(
                new StringConstantNode("hello"), new IntegerConstantNode(-1), new IntegerConstantNode(3));

        String result = substr.evaluate();
        assertEquals("", result);
        assertTrue(substr.hasError());
        assertTrue(substr.getEvaluationError().isInvalid());
    }

    @Test
    public void testSubstringNodeReportsErrorForEndIndexPastLength() {
        System.out.print("SubstringNode end index out of bounds Test\n");

        StringExpressionNode substr = new SubstringNode(
                new StringConstantNode("hello"), new IntegerConstantNode(0), new IntegerConstantNode(50));

        String result = substr.evaluate();
        assertEquals("", result);
        assertTrue(substr.hasError());
    }

    @Test
    public void testSubstringNodeReportsErrorWhenStartAfterEnd() {
        System.out.print("SubstringNode start after end Test\n");

        StringExpressionNode substr = new SubstringNode(
                new StringConstantNode("hello"), new IntegerConstantNode(4), new IntegerConstantNode(1));

        String result = substr.evaluate();
        assertEquals("", result);
        assertTrue(substr.hasError());
    }

    @Test
    public void testSubstringNodeErrorStatePersistsAcrossReevaluation() {
        System.out.print("SubstringNode stale-error-state Test\n");

        //Current implementation clears errors at the start of every evaluate.1
        IntegerVariableNode begin = new IntegerVariableNode("BEGIN");
        IntegerVariableNode end = new IntegerVariableNode("END");
        StringExpressionNode substr = new SubstringNode(new StringConstantNode("hello"), begin, end);
        DataProvider dp = new DataHub();
        substr.setProvider(dp);

        dp.setInt("BEGIN", -1);
        dp.setInt("END", 3);
        substr.evaluate();
        assertTrue(substr.hasError());

        dp.setInt("BEGIN", 0);
        dp.setInt("END", 5);
        String result = substr.evaluate();
        assertEquals("hello", result); // returned value has no errors
        assertFalse(substr.hasError()); // hasError is back to False
    }

    // DateNode

    @Test
    public void testDateNodeReportsErrorForInvalidDate() {
        System.out.print("DateNode invalid date Test\n");

        DateTimeExpressionNode invalidDate = new DateNode(
                new IntegerConstantNode(2026), new IntegerConstantNode(2), new IntegerConstantNode(30)); // Feb 30

        invalidDate.evaluate();
        assertTrue(invalidDate.hasError());
        assertTrue(invalidDate.getEvaluationError().isInvalid());
    }

    @Test
    public void testDateNodePropagatesChildError() {
        System.out.print("DateNode child error propagation Test\n");

        IntegerExpressionNode erroringMonth = new IntegerDivideNode(
                new IntegerConstantNode(1), new IntegerConstantNode(0));
        DateTimeExpressionNode dateWithErroringChild = new DateNode(
                new IntegerConstantNode(2026), erroringMonth, new IntegerConstantNode(15));

        dateWithErroringChild.evaluate();
        assertTrue(dateWithErroringChild.hasError());
    }
}
