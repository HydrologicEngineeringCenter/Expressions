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
        if (conditionVal && !conditionNode.hasError()) {
            double thenBranchVal = thenBranch.evaluate();
            ee = thenBranch.getEvaluationError();
            return thenBranchVal;
        } else if (!conditionNode.hasError()) {
            double elseBranchVal = elseBranch.evaluate();
            ee= elseBranch.getEvaluationError();
            return elseBranchVal;
        } else {
            ee = conditionNode.getEvaluationError();
        }
        return 0.0;
    }
}
