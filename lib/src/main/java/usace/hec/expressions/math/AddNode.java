package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;



import java.io.Serial;



public class AddNode extends NumericalBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link ExpressionNode}s), returning the summation {@code +} of the childs' values (e.g. {@code 2 + 2 == 4})
     */
    public AddNode(ExpressionNode<Number> left, ExpressionNode<Number> right) {
        super(left, right);
        
    }
    @Override
    public Double evaluate() {
        Double lv = ((Number)leftnode.evaluate()).doubleValue();
        Double rv = ((Number)rightnode.evaluate()).doubleValue();
        return lv+rv;
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
        return ExpressionOperator.PLUS;
    }
}
