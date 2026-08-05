package usace.hec.expressions.time;


import java.io.Serial;

import java.time.DateTimeException;
import java.time.LocalDateTime;

import usace.hec.expressions.*;

public class DateNode extends TernaryExpressionNode implements DateTimeExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final IntegerExpressionNode _dd;
    private final IntegerExpressionNode _mm;
    private final IntegerExpressionNode _yyyy;
    private EvaluationError ee = new EvaluationError();
 
    public DateNode(IntegerExpressionNode yyyy, IntegerExpressionNode mm, IntegerExpressionNode dd) {
        _dd = dd;
        _mm = mm;
        _yyyy = yyyy;
    }

    @Override
    public LocalDateTime evaluate() {
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.of(_yyyy.evaluate(), _mm.evaluate(), _dd.evaluate(), 0, 0);
        } catch (DateTimeException e) {
            dateTime = LocalDateTime.now();
            ee.report(ErrorState.INVALID, this, "Invalid Date Entered");
        }
        return dateTime;

    }

    @Override
    public String PreFixSyntax() {
        return "DATE(" + _yyyy.PreFixSyntax() + ","
             + _mm.PreFixSyntax() + ","
             + _dd.PreFixSyntax() + ")";
    }

     @Override
    public String ExcelSyntax() {
        return "DATE(" + _yyyy.ExcelSyntax() + ","
             + _mm.ExcelSyntax() + ","
             + _dd.ExcelSyntax() + ")";
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.DATE;
    }

    @Override
    public ExpressionNode left() {
        return _dd;
    }

    @Override
    public ExpressionNode middle() {
        return _mm;
    }

    @Override
    public ExpressionNode right() {
        return _yyyy;
    }

}
