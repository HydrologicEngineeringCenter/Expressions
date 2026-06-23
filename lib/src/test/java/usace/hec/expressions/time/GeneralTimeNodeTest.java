package usace.hec.expressions.time;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import org.junit.Test;

import usace.hec.expressions.ConstantLeafNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.logical.IfNode;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.MultiplyNode;

public class GeneralTimeNodeTest {
    @Test
    public void testAfterBeforeEvaluate() {
        System.out.println("After and Before Test \n");
        LocalDate dayOne = LocalDate.of(2026, 6, 16);
        ConstantLeafNode<LocalDate> a = new ConstantLeafNode<>(dayOne);
        LocalDate dayTwo = LocalDate.of(2026,6 , 18);
        ConstantLeafNode<LocalDate> b = new ConstantLeafNode<>(dayTwo);
        AfterNode after = new AfterNode(a, b);
        BeforeNode before = new BeforeNode(a, b);
        ConstantLeafNode<Double> one = new ConstantLeafNode<>(1.0);
        ConstantLeafNode<Double> two = new ConstantLeafNode<>(2.0);

        AddNode Add = new AddNode(one, two);
        MultiplyNode Multiply = new MultiplyNode(one, two);

        IfNode<Double> ifNodeAfter = new IfNode<>(after, Add, Multiply);
        IfNode<Double> ifNodeBefore = new IfNode<>(before, Add, Multiply);

        String expression = ifNodeAfter.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = ifNodeAfter.ExcelSyntax();
        System.out.print(expressionInfix+ "\n");

        expression = ifNodeBefore.PreFixSyntax();
        System.out.print(expression + "\n");
        expressionInfix = ifNodeBefore.ExcelSyntax();
        System.out.println(expressionInfix+ "\n");

        Double resultA = ifNodeAfter.evaluate();
        assertEquals(2.0,resultA,0.0);
        Double resultB = ifNodeBefore.evaluate();
        assertEquals(3.0,resultB,0.0);
    }
    @Test
    public void testDayofTheYearEvaluate() {
        System.out.println("Day of The Year Test \n");
        LocalDate dayOne = LocalDate.of(2026, 6, 16);
        ConstantLeafNode<LocalDate> a = new ConstantLeafNode<>(dayOne);
        LocalDate dayTwo = LocalDate.of(2028,6 , 16);
        ConstantLeafNode<LocalDate> b = new ConstantLeafNode<>(dayTwo);
        ExpressionNode<Integer> dayIn2026 = new DayOfYearNode(a);
        ExpressionNode<Integer> dayIn2028 = new DayOfYearNode(b);

        String expression = dayIn2026.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = dayIn2026.ExcelSyntax();
        System.out.println(expressionInfix+ "\n");


        Integer resultA = dayIn2026.evaluate();
        assertEquals(167,resultA,0.0);
        Integer resultB = dayIn2028.evaluate();
        assertEquals(168,resultB,0.0);
    }
}
