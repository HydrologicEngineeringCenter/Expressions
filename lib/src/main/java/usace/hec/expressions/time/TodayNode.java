package usace.hec.expressions.time;

import java.io.Serial;
import java.time.LocalDateTime;
import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;


public class TodayNode implements DateTimeExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public LocalDateTime evaluate() {
        return LocalDateTime.now();
    }

    @Override
    public String PreFixSyntax(){
        return ExpressionOperator.TODAY.getInfixName() +"()";
    }
    @Override
    public String ExcelSyntax(){
        return ExpressionOperator.TODAY.getInfixName() +"()";
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.TODAY;
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DATE;
    }

}
