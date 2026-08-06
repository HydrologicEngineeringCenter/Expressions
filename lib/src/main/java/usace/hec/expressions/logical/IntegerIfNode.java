package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.IntegerExpressionNode;

public class IntegerIfNode extends IfNode implements IntegerExpressionNode {

    public IntegerIfNode(BooleanExpressionNode condition, IntegerExpressionNode thenn, IntegerExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public int evaluate() {
        IntegerExpressionNode thenBranch = (IntegerExpressionNode) thenNode;
        IntegerExpressionNode elseBranch = (IntegerExpressionNode) elseNode;
        boolean conditionVal = conditionNode.evaluate();
        int thenBranchVal = 0;
        int elseBranchVal = 0;
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
