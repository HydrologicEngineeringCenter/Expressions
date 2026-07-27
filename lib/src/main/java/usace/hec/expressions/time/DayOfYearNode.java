package usace.hec.expressions.time;

import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;


public class DayOfYearNode implements UnaryExpressionNode, IntegerExpressionNode, DisplayNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DateTimeExpressionNode child;
    public DayOfYearNode(DateTimeExpressionNode child) {
        this.child = child;
    }
    @Override
    public int evaluate() {
        return child.evaluate().getDayOfYear();
    }
    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.DOY;
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
    }
    @Override
    public ExpressionNode child() {
        return this.child;
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
            return Operator().getInfixName()+ "()";
        }else{
            return Operator().getPrefixName() + "()";
        }
    }
}
