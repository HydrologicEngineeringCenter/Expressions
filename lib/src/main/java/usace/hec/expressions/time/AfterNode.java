package usace.hec.expressions.time;

import java.io.Serial;
import java.time.LocalDateTime;


import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ConstantLeafNode;
import usace.hec.expressions.ExpressionOperator;


public class AfterNode extends BinaryExpressionNode<Boolean, LocalDateTime, LocalDateTime> {
    @Serial
    private static final long serialVersionUID = 1L;
    public AfterNode(ConstantLeafNode<LocalDateTime> left, ConstantLeafNode<LocalDateTime> right){
        super(left, right);
    }
    @Override
    public Boolean evaluate() {
        return leftnode.evaluate().isAfter(rightnode.evaluate());
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
        return ExpressionOperator.AFTER;
    }
}