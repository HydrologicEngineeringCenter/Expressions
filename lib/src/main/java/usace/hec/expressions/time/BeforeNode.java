package usace.hec.expressions.time;

import java.time.LocalDate;
import java.util.Date;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ConstantLeafNode;
import usace.hec.expressions.ExpressionOperator;


public class BeforeNode extends BinaryExpressionNode<Boolean, LocalDate, LocalDate> {
    public BeforeNode(ConstantLeafNode<LocalDate> left, ConstantLeafNode<LocalDate> right){
        super(left, right);
    }
    @Override
    public Boolean evaluate() {
        return leftnode.evaluate().isBefore(rightnode.evaluate());
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
        return ExpressionOperator.BEFORE;
    }
}