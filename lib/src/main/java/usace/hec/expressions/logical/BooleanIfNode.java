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
        boolean thenBranchVal = false;
        boolean elseBranchVal = false;
        if (conditionVal && !conditionNode.hasError()) {
            thenBranchVal = thenBranch.evaluate();
            ee = thenBranch.getEvaluationError();
        } else if (!conditionNode.hasError()) {
            elseBranchVal = elseBranch.evaluate();
            ee= elseBranch.getEvaluationError();
        } else {
            ee = conditionNode.getEvaluationError();
        }
        return conditionVal ? thenBranchVal : elseBranchVal;
    }
}
