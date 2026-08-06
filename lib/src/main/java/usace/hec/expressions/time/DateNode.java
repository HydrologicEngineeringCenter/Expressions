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
        int year = _yyyy.evaluate();
        int month = _mm.evaluate();
        int day = _dd.evaluate();
        checkErrors();
        if (!_yyyy.hasError() || !_mm.hasError()|| !_dd.hasError()) {
            try {
                dateTime = LocalDateTime.of(year, month, day, 0, 0);
            } catch (DateTimeException e) {
                dateTime = LocalDateTime.now();
                ee.report(ErrorState.INVALID, this, "Invalid Date Entered");
            }
            return dateTime;
        }
        return LocalDateTime.now();

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
