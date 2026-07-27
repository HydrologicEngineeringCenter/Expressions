package usace.hec.expressions.time;

import java.io.Serial;
import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.comparison.BooleanBinaryExpressionNode;


public class AfterNode extends BooleanBinaryExpressionNode implements DisplayNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode left;
    private DateTimeExpressionNode right;
    public AfterNode(DateTimeExpressionNode left, DateTimeExpressionNode right){
        this.left = left;
        this.right = right;
    }
    @Override
    public boolean evaluate() {
        return left.evaluate().isAfter(right.evaluate());
    }
    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.AFTER;
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
    }
    @Override
    public ExpressionNode left() {
        return this.left;
    }
    @Override
    public ExpressionNode right() {
        return this.right;
    }
    @Override
    public String displayName(boolean infix) {
        if(infix){
            return Operator().getInfixName();
        }else{
            return Operator().getPrefixName();
        }
    }
    @Override
    public String category() {
        return "Time";
    }
    @Override
    public String defaultSyntax(boolean infix) {
        if (infix){
            return Operator().getInfixName();
        }else{
            return Operator().getPrefixName() + "(,)";
        }
    }
}