package usace.hec.expressions.time;

import usace.hec.expressions.*;

import java.io.Serial;


public class CalendarYearNode implements UnaryExpressionNode, IntegerExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public CalendarYearNode(DateTimeExpressionNode child) {
        this.child = child;
    }
    @Override
    public int evaluate() {
        return child.evaluate().getYear();
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.YEAR;
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
    @Override
    public void setProvider(DataProvider dp) { child.setProvider(dp); }
}