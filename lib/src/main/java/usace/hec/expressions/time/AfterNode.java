package usace.hec.expressions.time;

import java.io.Serial;

import usace.hec.expressions.*;
import usace.hec.expressions.comparison.BooleanBinaryExpressionNode;


public class AfterNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode left;
    private DateTimeExpressionNode right;
    public AfterNode(DateTimeExpressionNode left, DateTimeExpressionNode right){
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        return left.evaluate().isAfter(right.evaluate());
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.AFTER;
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
    @Override
    public ExpressionNode left() {
        return this.left;
    }
    @Override
    public ExpressionNode right() {
        return this.right;
    }
    @Override
    public void setProvider(DataProvider dp) { left.setProvider(dp); right.setProvider(dp); }
}