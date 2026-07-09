package usace.hec.expressions.comparison;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;

public class EqualToNode<R extends Number,L extends Number> extends BinaryExpressionNode<Boolean,R, L> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children (numerical {@link ExpressionNode}s), returning whether the two values are equal
     * (e.g. {@code true} if both children are the same value, otherwise {@code false})
     */
    public EqualToNode(ExpressionNode<L> left, ExpressionNode<R> right) {
        super(left, right);
    }
    @Override
    public Boolean evaluate() {
        L left = leftnode.evaluate();
        R right = rightnode.evaluate();
        Boolean result = left.doubleValue() == right.doubleValue();
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
        return ExpressionOperator.EQ;
    }
}
