package usace.hec.expressions.time;

import java.util.Date;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;


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
        return "BEFORE";
    }
    @Override
    public String InfixOpName() {
        return "BEFORE";
    }
}