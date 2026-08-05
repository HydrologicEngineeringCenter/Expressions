package usace.hec.expressions.time;

import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;
import usace.hec.expressions.IntegerExpressionNode;

import java.io.Serial;


public class DayOfYearNode implements UnaryExpressionNode, IntegerExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public DayOfYearNode(DateTimeExpressionNode child) {
        this.child = child;
    }
    @Override
    public int evaluate() {
        return child.evaluate().getDayOfYear();
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.DOY;
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
    @Override
    public ExpressionNode child() {
        return this.child;
    }

}
