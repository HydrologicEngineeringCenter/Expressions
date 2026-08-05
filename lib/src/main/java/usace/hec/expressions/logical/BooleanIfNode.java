package usace.hec.expressions.logical;


import usace.hec.expressions.BooleanExpressionNode;

public class BooleanIfNode extends IfNode implements BooleanExpressionNode {

    public BooleanIfNode(BooleanExpressionNode condition, BooleanExpressionNode thenn, BooleanExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public boolean evaluate() {
        BooleanExpressionNode thenBranch = (BooleanExpressionNode) thenNode;
        BooleanExpressionNode elseBranch = (BooleanExpressionNode) elseNode;

        boolean conditionVal = conditionNode.evaluate();
        boolean thenBranchVal = thenBranch.evaluate();
        boolean elseBranchVal = elseBranch.evaluate();
        checkErrors();
        return conditionVal ? thenBranchVal : elseBranchVal;
    }
}
