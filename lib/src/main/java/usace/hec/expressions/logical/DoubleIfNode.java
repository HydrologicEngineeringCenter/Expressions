package usace.hec.expressions.logical;

import usace.hec.expressions.*;



public class DoubleIfNode extends IfNode implements DoubleExpressionNode{
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
