package usace.hec.expressions.logical;

import usace.hec.expressions.*;

public class BooleanIfNode extends IfNode implements BooleanExpressionNode{

    public BooleanIfNode(BooleanExpressionNode condition, BooleanExpressionNode thenn, BooleanExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public boolean evaluate() {
        BooleanExpressionNode thenBranch = (BooleanExpressionNode) thenNode;
        BooleanExpressionNode elseBranch = (BooleanExpressionNode) elseNode;

        return conditionNode.evaluate() ? thenBranch.evaluate() : elseBranch.evaluate();
    }
}
