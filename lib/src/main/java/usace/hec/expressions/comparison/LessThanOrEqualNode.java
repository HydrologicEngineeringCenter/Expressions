package usace.hec.expressions.comparison;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;

public class LessThanOrEqualNode<R extends Number,L extends Number> extends BinaryExpressionNode<Boolean,R, L> {
    public LessThanOrEqualNode(ExpressionNode<L> left, ExpressionNode<R> right) {
        super(left, right);
        
    }
    @Override
    public Boolean evaluate() {
        L left = leftnode.evaluate();
        R right = rightnode.evaluate();
        Boolean result = left.doubleValue() <= right.doubleValue();
        return result;
    }
    @Override
    public String OpName() {
        return "LTE";
    }
    @Override
    public String InfixOpName() {
        return "<=";
    }
}

