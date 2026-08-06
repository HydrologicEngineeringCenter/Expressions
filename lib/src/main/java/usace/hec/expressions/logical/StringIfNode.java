package usace.hec.expressions.logical;


import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.StringExpressionNode;


public class StringIfNode extends IfNode implements StringExpressionNode {

    public StringIfNode(BooleanExpressionNode condition, StringExpressionNode thenn, StringExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public String evaluate() {
        StringExpressionNode thenBranch = (StringExpressionNode) thenNode;
        StringExpressionNode elseBranch = (StringExpressionNode) elseNode;

        boolean conditionVal = conditionNode.evaluate();
        String thenBranchVal = "";
        String elseBranchVal = "";
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
