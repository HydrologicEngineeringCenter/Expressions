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

        return conditionNode.evaluate() ? thenBranch.evaluate() : elseBranch.evaluate();
    }
}
