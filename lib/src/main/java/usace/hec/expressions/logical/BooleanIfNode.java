package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;

public class BooleanIfNode extends IfNode implements BooleanExpressionNode {

    public BooleanIfNode(BooleanExpressionNode condition, BooleanExpressionNode thenn, BooleanExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public boolean evaluate() {
        ee.clear();
        BooleanExpressionNode thenBranch = (BooleanExpressionNode) thenNode;
        BooleanExpressionNode elseBranch = (BooleanExpressionNode) elseNode;

        boolean conditionVal = conditionNode.evaluate();
        if (conditionVal && !conditionNode.hasError()) {
            boolean thenBranchVal = thenBranch.evaluate();
            ee = thenBranch.getEvaluationError();
            return thenBranchVal;
        } else if (!conditionNode.hasError()) {
            boolean elseBranchVal = elseBranch.evaluate();
            ee= elseBranch.getEvaluationError();
            return elseBranchVal;
        } else {
            ee = conditionNode.getEvaluationError();
        }
        return false;
    }
}
