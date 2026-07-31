package usace.hec.expressions.time;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.DisplayNode;
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
    public List<DataListener> fetchListeners() {
        return new ArrayList<>();
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
