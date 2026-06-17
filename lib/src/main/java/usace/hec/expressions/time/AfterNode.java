package usace.hec.expressions.time;

import java.util.Date;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;


public class AfterNode extends BinaryExpressionNode<Boolean, Date, Date> {
    public AfterNode(ExpressionNode<Date> left, ExpressionNode<Date> right){
        super(left, right);
    }
    @Override
    public Boolean evaluate() {
        return leftnode.evaluate().after(rightnode.evaluate());
    }
    @Override
    public String OpName() {
        return "AFTER";
    }
    @Override
    public String InfixOpName() {
        return "AFTER";
    }
}