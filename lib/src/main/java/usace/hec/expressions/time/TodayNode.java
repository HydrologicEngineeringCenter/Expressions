package usace.hec.expressions.time;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;


public class TodayNode implements DateTimeExpressionNode, DisplayNode {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public LocalDateTime evaluate() {
        return LocalDateTime.now();
    }

    @Override
    public List<DataListener> fetchListeners() {
        return new ArrayList<>();
    }



    @Override
    public String PreFixSyntax(){
        return ExpressionOperator.TODAY.getInfixName() +"()";
    }
    @Override
    public String ExcelSyntax(){
        return ExpressionOperator.TODAY.getInfixName() +"()";
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.TODAY;
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DATE;
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
