package usace.hec.expressions.logical;


import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.StringExpressionNode;


public class StringIfNode extends IfNode implements StringExpressionNode {

    public StringIfNode(BooleanExpressionNode condition, StringExpressionNode thenn, StringExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public String evaluate() {
        ee.clear();
        StringExpressionNode thenBranch = (StringExpressionNode) thenNode;
        StringExpressionNode elseBranch = (StringExpressionNode) elseNode;

        boolean conditionVal = conditionNode.evaluate();
        if (conditionVal && !conditionNode.hasError()) {
            String thenBranchVal = thenBranch.evaluate();
            ee = thenBranch.getEvaluationError();
            return thenBranchVal;
        } else if (!conditionNode.hasError()) {
            String elseBranchVal = elseBranch.evaluate();
            ee= elseBranch.getEvaluationError();
            return elseBranchVal;
        } else {
            ee = conditionNode.getEvaluationError();
        }
        return "";
    }
}
