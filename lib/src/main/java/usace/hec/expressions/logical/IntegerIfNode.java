package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.IntegerExpressionNode;

public class IntegerIfNode extends IfNode implements IntegerExpressionNode {

    public IntegerIfNode(BooleanExpressionNode condition, IntegerExpressionNode thenn, IntegerExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public int evaluate() {
        ee.clear();
        IntegerExpressionNode thenBranch = (IntegerExpressionNode) thenNode;
        IntegerExpressionNode elseBranch = (IntegerExpressionNode) elseNode;
        boolean conditionVal = conditionNode.evaluate();
        if (conditionVal && !conditionNode.hasError()) {
            int thenBranchVal = thenBranch.evaluate();
            ee = thenBranch.getEvaluationError();
            return thenBranchVal;
        } else if (!conditionNode.hasError()) {
            int elseBranchVal = elseBranch.evaluate();
            ee= elseBranch.getEvaluationError();
            return elseBranchVal;
        } else {
            ee = conditionNode.getEvaluationError();
        }
        return 0;
    }
}
