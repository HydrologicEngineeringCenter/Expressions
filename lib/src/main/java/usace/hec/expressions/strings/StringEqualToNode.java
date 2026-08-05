package usace.hec.expressions.strings;

import java.io.Serial;

import usace.hec.expressions.StringExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.comparison.BooleanBinaryExpressionNode;


public class StringEqualToNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private StringExpressionNode left;
    private StringExpressionNode right;
    public StringEqualToNode(StringExpressionNode left, StringExpressionNode right){
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        String leftVal = left.evaluate();
        String rightVal = right.evaluate();
        checkErrors();
        return leftVal.equals(rightVal);
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.EQ;
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
}