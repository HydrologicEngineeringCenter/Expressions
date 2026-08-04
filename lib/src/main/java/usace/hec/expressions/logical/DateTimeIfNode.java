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
        DateTimeExpressionNode thenBranch = (DateTimeExpressionNode) thenNode;
        DateTimeExpressionNode elseBranch = (DateTimeExpressionNode) elseNode;

        return conditionNode.evaluate() ? thenBranch.evaluate() : elseBranch.evaluate();
    }
}