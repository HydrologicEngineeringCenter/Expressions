package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DateTimeExpressionNode;
import java.time.LocalDateTime;

public class DateTimeIfNode extends IfNode implements DateTimeExpressionNode {

    public DateTimeIfNode(BooleanExpressionNode condition, DateTimeExpressionNode thenn, DateTimeExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public LocalDateTime evaluate() {
        ee.clear();
        DateTimeExpressionNode thenBranch = (DateTimeExpressionNode) thenNode;
        DateTimeExpressionNode elseBranch = (DateTimeExpressionNode) elseNode;

        boolean conditionVal = conditionNode.evaluate();
        if (conditionVal && !conditionNode.hasError()) {
            LocalDateTime thenBranchVal = thenBranch.evaluate();
            ee = thenBranch.getEvaluationError();
            return thenBranchVal;
        } else if (!conditionNode.hasError()) {
            LocalDateTime elseBranchVal = elseBranch.evaluate();
            ee= elseBranch.getEvaluationError();
            return elseBranchVal;
        } else {
            ee = conditionNode.getEvaluationError();
        }
        return LocalDateTime.MIN;
    }
}