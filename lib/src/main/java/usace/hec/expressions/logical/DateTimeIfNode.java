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
        LocalDateTime thenBranchVal = LocalDateTime.MIN;
        LocalDateTime elseBranchVal = LocalDateTime.MIN;
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