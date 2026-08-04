package usace.hec.expressions.time;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import org.junit.Test;
import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.DoubleConstantNode;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.logical.DoubleIfNode;
import usace.hec.expressions.math.DoubleAddNode;
import usace.hec.expressions.math.DoubleMultiplyNode;

public class GeneralTimeNodeTest {

    @Test
    public void testAfterBeforeEvaluate() {
        System.out.println("After and Before Test\n");

        // Create DateTimeExpressionNode constants via DateNode
        DateTimeExpressionNode a = new DateNode(
                new usace.hec.expressions.IntegerConstantNode(2026),
                new usace.hec.expressions.IntegerConstantNode(6),
                new usace.hec.expressions.IntegerConstantNode(16));

        DateTimeExpressionNode b = new DateNode(
                new usace.hec.expressions.IntegerConstantNode(2026),
                new usace.hec.expressions.IntegerConstantNode(6),
                new usace.hec.expressions.IntegerConstantNode(18));

        AfterNode after = new AfterNode(a, b);
        BeforeNode before = new BeforeNode(a, b);

        DoubleConstantNode one = new DoubleConstantNode(1.0);
        DoubleConstantNode two = new DoubleConstantNode(2.0);

        DoubleAddNode addResult = new DoubleAddNode(one, two);
        DoubleMultiplyNode multiplyResult = new DoubleMultiplyNode(one, two);

        DoubleIfNode ifNodeAfter = new DoubleIfNode(after, addResult, multiplyResult);
        DoubleIfNode ifNodeBefore = new DoubleIfNode(before, addResult, multiplyResult);

        String expression = ifNodeAfter.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = ifNodeAfter.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        expression = ifNodeBefore.PreFixSyntax();
        System.out.print(expression + "\n");
        expressionInfix = ifNodeBefore.ExcelSyntax();
        System.out.println(expressionInfix + "\n");

        // dayOne is NOT after dayTwo, so else branch (multiply: 1*2 = 2)
        double resultA = ifNodeAfter.evaluate();
        assertEquals(2.0, resultA, 0.0);

        // dayOne IS before dayTwo, so then branch (add: 1+2 = 3)
        double resultB = ifNodeBefore.evaluate();
        assertEquals(3.0, resultB, 0.0);
    }

    @Test
    public void testDayofTheYearEvaluate() {
        System.out.println("Day of The Year Test\n");

        // 2026-06-16
        DateTimeExpressionNode dayOne = new DateNode(
                new usace.hec.expressions.IntegerConstantNode(2026),
                new usace.hec.expressions.IntegerConstantNode(6),
                new usace.hec.expressions.IntegerConstantNode(16));

        // 2028-06-16 (leap year)
        DateTimeExpressionNode dayTwo = new DateNode(
                new usace.hec.expressions.IntegerConstantNode(2028),
                new usace.hec.expressions.IntegerConstantNode(6),
                new usace.hec.expressions.IntegerConstantNode(16));

        IntegerExpressionNode dayIn2026 = new DayOfYearNode(dayOne);
        IntegerExpressionNode dayIn2028 = new DayOfYearNode(dayTwo);

        String expression = dayIn2026.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = dayIn2026.ExcelSyntax();
        System.out.println(expressionInfix + "\n");

        int resultA = dayIn2026.evaluate();
        assertEquals(167, resultA);

        int resultB = dayIn2028.evaluate();
        assertEquals(168, resultB);
    }

    @Test
    public void testTodayNode() {
        TodayNode today = new TodayNode();
        LocalDateTime result = today.evaluate();

        LocalDateTime now = LocalDateTime.now();
        assertEquals(now.getYear(), result.getYear());
        assertEquals(now.getMonthValue(), result.getMonthValue());
        assertEquals(now.getDayOfMonth(), result.getDayOfMonth());
    }
}