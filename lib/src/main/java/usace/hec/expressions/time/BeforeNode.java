package usace.hec.expressions.time;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Date;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ConstantLeafNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;


public class BeforeNode extends BinaryExpressionNode<Boolean, LocalDateTime, LocalDateTime> {
    @Serial
    private static final long serialVersionUID = 1L;
    public BeforeNode(ExpressionNode<LocalDateTime> left, ExpressionNode<LocalDateTime> right){
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
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
}