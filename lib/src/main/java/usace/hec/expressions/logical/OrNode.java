package usace.hec.expressions.logical;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class OrNode extends BinaryExpressionNode<Boolean,Boolean,Boolean> {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children (boolean {@link ExpressionNode}s), returning the OR of
     * the childs' values (e.g. {@code true} if at least one child is true, otherwise {@code false})
     */
    public OrNode(ExpressionNode<Boolean> left, ExpressionNode<Boolean> right) {
        super(left, right);
        
    }
    @Override
    public Boolean evaluate() {
        if (leftnode.evaluate()){
            return true;
        } else {
            return rightnode.evaluate();
        }
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
        return ExpressionOperator.OR;
    }
}

