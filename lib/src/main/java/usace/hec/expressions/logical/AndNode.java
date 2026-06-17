package usace.hec.expressions.logical;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;

public class AndNode extends BinaryExpressionNode<Boolean,Boolean,Boolean> {
    public AndNode(ExpressionNode<Boolean> left, ExpressionNode<Boolean> right) {
        super(left, right);
        
    }
    @Override
    public Boolean evaluate() {
        Boolean left = leftnode.evaluate();
        Boolean right = rightnode.evaluate();
        Boolean result = left && right;
        return result;
    }
    @Override
    public String OpName() {
        return "AND";
    }
    @Override
    public String InfixOpName() {
        return "&&";
    }
}

