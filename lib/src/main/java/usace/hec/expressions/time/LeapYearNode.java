package usace.hec.expressions.time;

import usace.hec.expressions.*;

import java.io.Serial;

public class LeapYearNode implements UnaryExpressionNode, BooleanExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public LeapYearNode(DateTimeExpressionNode child) {
        this.child = child;
    }
    @Override
    public boolean evaluate() {
        return child.evaluate().getYear() % 4 == 0;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.LEAPYEAR;
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
