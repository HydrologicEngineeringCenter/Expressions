package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.IntegerExpressionNode;

public class IntegerIfNode extends IfNode implements IntegerExpressionNode{

    public IntegerIfNode(BooleanExpressionNode condition, IntegerExpressionNode thenn, IntegerExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public int evaluate() {
        IntegerExpressionNode thenBranch = (IntegerExpressionNode) thenNode;
        IntegerExpressionNode elseBranch = (IntegerExpressionNode) elseNode;
        
        return conditionNode.evaluate() ? thenBranch.evaluate() : elseBranch.evaluate();
    }
    
}
