package usace.hec.expressions.logical;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;

import java.io.Serial;


public class AndNode extends BinaryExpressionNode<Boolean,Boolean,Boolean> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children (boolean {@link ExpressionNode}s), returning the AND of
     * the childs' values (e.g. {@code true} if both children are true, otherwise {@code false})
     */
    public AndNode(ExpressionNode<Boolean> left, ExpressionNode<Boolean> right) {
        super(left, right);
        
    }
    @Override
    public Boolean evaluate() {
        Boolean left = leftnode.evaluate();
        Boolean right = rightnode.evaluate();
        Boolean result = left && right;
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
        return ExpressionOperator.AND;
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
}

