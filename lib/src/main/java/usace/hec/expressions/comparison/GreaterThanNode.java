package usace.hec.expressions.comparison;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;

public class GreaterThanNode<R extends Number,L extends Number> extends BinaryExpressionNode<Boolean,R, L> {
    public GreaterThanNode(ExpressionNode<L> left, ExpressionNode<R> right) {
        super(left, right);
        
    }
    @Override
    public Boolean evaluate() {
        L left = leftnode.evaluate();
        R right = rightnode.evaluate();
        Boolean result = left.doubleValue() > right.doubleValue();
        return result;
    }
    @Override
    public String OpName() {
        return "GT";
    }
    @Override
    public String InfixOpName() {
        return ">=";
    }
}
