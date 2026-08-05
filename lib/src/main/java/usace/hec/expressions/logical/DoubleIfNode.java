package usace.hec.expressions.logical;


import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DoubleExpressionNode;

import java.time.LocalDateTime;

public class DoubleIfNode extends IfNode implements DoubleExpressionNode {
    public DoubleIfNode(BooleanExpressionNode condition, DoubleExpressionNode thenn, DoubleExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public double evaluate() {
        DoubleExpressionNode thenBranch = (DoubleExpressionNode) thenNode;
        DoubleExpressionNode elseBranch = (DoubleExpressionNode) elseNode;
        boolean conditionVal = conditionNode.evaluate();
        double thenBranchVal = thenBranch.evaluate();
        double elseBranchVal = elseBranch.evaluate();
        checkErrors();
        return conditionVal ? thenBranchVal : elseBranchVal;
    }
}
