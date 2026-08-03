package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DoubleExpressionNode;

import java.io.Serial;


public class DoubleIfNode extends IfNode implements DoubleExpressionNode{
    @Serial
    private static final long serialVersionUID = 1L;
    public DoubleIfNode(BooleanExpressionNode condition, DoubleExpressionNode thenn, DoubleExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public double evaluate() {
        DoubleExpressionNode thenBranch = (DoubleExpressionNode) thenNode;
        DoubleExpressionNode elseBranch = (DoubleExpressionNode) elseNode;
        
        return conditionNode.evaluate() ? thenBranch.evaluate() : elseBranch.evaluate();
    }


}
