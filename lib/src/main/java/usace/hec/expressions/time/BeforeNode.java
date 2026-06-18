package usace.hec.expressions.time;

import java.util.Date;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;


public class BeforeNode extends BinaryExpressionNode<Boolean, Date, Date> {
    public BeforeNode(ExpressionNode<Date> left, ExpressionNode<Date> right){
        super(left, right);
    }
    @Override
    public Boolean evaluate() {
        return leftnode.evaluate().before(rightnode.evaluate());
    }
    @Override
    public String OpName() {
        return Operator().getPrefixName();
    }
    @Override
    public String InfixOpName() {
        return Operator().getPrefixName();
    }
    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.BEFORE;
    }
}