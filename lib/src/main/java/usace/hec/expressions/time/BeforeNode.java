package usace.hec.expressions.time;

import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.comparison.BooleanBinaryExpressionNode;


public class BeforeNode extends BooleanBinaryExpressionNode {
    private DateTimeExpressionNode left;
    private DateTimeExpressionNode right;
    public BeforeNode(DateTimeExpressionNode left, DateTimeExpressionNode right){
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        return left.evaluate().isBefore(right.evaluate());
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.BEFORE;
    }
    @Override
    public ExpressionNode left() {
        return this.left;
    }
    @Override
    public ExpressionNode right() {
        return this.right;
    }

}