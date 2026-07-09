package usace.hec.expressions.comparison;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class GreaterThanOrEqualNode<R extends Number,L extends Number> extends BinaryExpressionNode<Boolean,R, L> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children (numerical {@link ExpressionNode}s), returning whether the left child's value is greater than or equal to the right child's value
     * (e.g. {@code true} if left value >= right value, otherwise {@code false})
     */
    public GreaterThanOrEqualNode(ExpressionNode<L> left, ExpressionNode<R> right) {
        super(left, right);

    }

    @Override
    public Boolean evaluate() {
        L left = leftnode.evaluate();
        R right = rightnode.evaluate();
        Boolean result = left.doubleValue() >= right.doubleValue();
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
        return ExpressionOperator.GTE;
    }
}

