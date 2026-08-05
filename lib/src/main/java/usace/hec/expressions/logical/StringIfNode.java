package usace.hec.expressions.logical;


import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.StringExpressionNode;

import java.time.LocalDateTime;

public class StringIfNode extends IfNode implements StringExpressionNode {

    public StringIfNode(BooleanExpressionNode condition, StringExpressionNode thenn, StringExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public String evaluate() {
        StringExpressionNode thenBranch = (StringExpressionNode) thenNode;
        StringExpressionNode elseBranch = (StringExpressionNode) elseNode;

        boolean conditionVal = conditionNode.evaluate();
        String thenBranchVal = thenBranch.evaluate();
        String elseBranchVal = elseBranch.evaluate();
        checkErrors();
        return conditionVal ? thenBranchVal : elseBranchVal;
    }
}
