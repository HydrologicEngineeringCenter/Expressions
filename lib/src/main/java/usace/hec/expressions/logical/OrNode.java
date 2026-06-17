package usace.hec.expressions.logical;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

public class OrNode extends BinaryExpressionNode<Boolean,Boolean,Boolean> {
    public OrNode(ExpressionNode<Boolean> left, ExpressionNode<Boolean> right) {
        super(left, right);
        
    }
    @Override
    public Boolean evaluate() {
        Boolean left = leftnode.evaluate();
        Boolean right = rightnode.evaluate();
        Boolean result = left || right;
        return result;
    }
    @Override
    public String OpName() {
        return Operator().getPrefixName();
    }
    @Override
    public String InfixOpName() {
        return Operator().getInfixName();
    }
    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.OR;
    }
}

