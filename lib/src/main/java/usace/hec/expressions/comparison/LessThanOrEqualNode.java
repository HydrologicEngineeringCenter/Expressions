package usace.hec.expressions.comparison;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

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
        return Operator().getPrefixName();
    }
    @Override
    public String InfixOpName() {
        return Operator().getInfixName();
    }
    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.LTE;
    }
}

