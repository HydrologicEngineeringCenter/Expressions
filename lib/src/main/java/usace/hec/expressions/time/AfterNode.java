package usace.hec.expressions.time;

import java.util.Date;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;


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

    @Override
    public ExpressionOperator Operator() {
        return null;
    }
}