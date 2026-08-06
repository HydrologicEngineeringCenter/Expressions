package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DoubleExpressionNode;

public class DoubleIfNode extends IfNode implements DoubleExpressionNode {

    public DoubleIfNode(BooleanExpressionNode condition, DoubleExpressionNode thenn, DoubleExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public double evaluate() {
        ee.clear();
        DoubleExpressionNode thenBranch = (DoubleExpressionNode) thenNode;
        DoubleExpressionNode elseBranch = (DoubleExpressionNode) elseNode;
        boolean conditionVal = conditionNode.evaluate();
        double thenBranchVal = 0.0;
        double elseBranchVal = 0.0;
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
