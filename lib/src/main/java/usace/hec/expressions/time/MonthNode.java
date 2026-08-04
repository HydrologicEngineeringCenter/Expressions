package usace.hec.expressions.time;

import usace.hec.expressions.*;

import java.io.Serial;

public class MonthNode implements UnaryExpressionNode, IntegerExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public MonthNode(DateTimeExpressionNode child) {
        this.child = child;
    }
    @Override
    public int evaluate() {
        return child.evaluate().getMonthValue();
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.MONTH;
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
    @Override
    public ExpressionNode child() {
        return this.child;
    }
}