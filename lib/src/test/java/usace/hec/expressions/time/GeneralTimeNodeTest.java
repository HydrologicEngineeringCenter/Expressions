package usace.hec.expressions.time;

import java.time.LocalDateTime;
import org.junit.Test;
import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.DoubleConstantNode;
import usace.hec.expressions.IntegerConstantNode;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.logical.DoubleIfNode;
import usace.hec.expressions.math.DoubleAddNode;
import usace.hec.expressions.math.DoubleMultiplyNode;

import static org.junit.Assert.*;

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

    private static DateTimeExpressionNode date(int year, int month, int day) {
        return new DateNode(
                new IntegerConstantNode(year),
                new IntegerConstantNode(month),
                new IntegerConstantNode(day));
    }

    @Test
    public void testCalendarYearEvaluate() {
        System.out.print("CalendarYear Test\n");

        IntegerExpressionNode calYear = new CalendarYearNode(date(2026, 8, 6));
        assertEquals(2026, calYear.evaluate());

        IntegerExpressionNode calYearB = new CalendarYearNode(date(1999, 12, 31));
        assertEquals(1999, calYearB.evaluate());
    }

    @Test
    public void testDayOfMonthEvaluate() {
        System.out.print("DayOfMonth Test\n");

        IntegerExpressionNode dom = new DayOfMonthNode(date(2026, 8, 6));
        assertEquals(6, dom.evaluate());

        IntegerExpressionNode domEndOfMonth = new DayOfMonthNode(date(2026, 1, 31));
        assertEquals(31, domEndOfMonth.evaluate());
    }

    @Test
    public void testMonthEvaluate() {
        System.out.print("Month Test\n");

        IntegerExpressionNode month = new MonthNode(date(2026, 8, 6));
        assertEquals(8, month.evaluate());

        IntegerExpressionNode monthDec = new MonthNode(date(2026, 12, 1));
        assertEquals(12, monthDec.evaluate());
    }

    @Test
    public void testLeapYearEvaluate() {
        System.out.print("LeapYear Test\n");

        LeapYearNode leap2000 = new LeapYearNode(date(2000, 1, 1));
        assertTrue(leap2000.evaluate());

        LeapYearNode leap1900 = new LeapYearNode(date(1900, 1, 1));
        assertFalse(leap1900.evaluate());

        LeapYearNode notLeap2023 = new LeapYearNode(date(2023, 1, 1));
        assertFalse(notLeap2023.evaluate());

        LeapYearNode leap2024 = new LeapYearNode(date(2024, 6, 15));
        assertTrue(leap2024.evaluate());
    }

    @Test
    public void testDateNodeInvalidDateReportsError() {
        System.out.print("DateNode Invalid Date Test\n");

        // Month 13 does not exist.
        DateTimeExpressionNode invalidDate = date(2026, 13, 1);
        invalidDate.evaluate();
        assertTrue(invalidDate.hasError());

        // February 30th does not exist.
        DateTimeExpressionNode invalidDayForMonth = date(2026, 2, 30);
        invalidDayForMonth.evaluate();
        assertTrue(invalidDayForMonth.hasError());
    }

    @Test
    public void testWaterYearEvaluate() {
        System.out.print("WaterYear Test\n");

        // Aug 6, 2026 is before Oct 1, so water year == calendar year.
        WaterYearNode beforeOctober = new WaterYearNode(date(2026, 8, 6));
        assertEquals(2026, beforeOctober.evaluate());

        // Nov 15, 2026 is after Sept 30, so water year == calendar year + 1.
        WaterYearNode afterSeptember = new WaterYearNode(date(2026, 11, 15));
        assertEquals(2027, afterSeptember.evaluate());
    }

    @Test
    public void testWeekdayEvaluate() {
        System.out.print("Weekday test\n");
        DayOfWeekNode fridayNode = new DayOfWeekNode(date(2026,8,21));
        assertEquals("Friday", fridayNode.evaluate());
        DayOfWeekNode sundayNode = new DayOfWeekNode(date(2026, 8, 23));
        assertEquals("Sunday", sundayNode.evaluate());
        IsWeekdayNode isWeekdayNodeTrue = new IsWeekdayNode(date(2026,8,21));
        assertTrue(isWeekdayNodeTrue.evaluate());
        IsWeekdayNode isWeekdayNodeFalse = new IsWeekdayNode(date(2026,8,22));
        assertFalse(isWeekdayNodeFalse.evaluate());
    }
}