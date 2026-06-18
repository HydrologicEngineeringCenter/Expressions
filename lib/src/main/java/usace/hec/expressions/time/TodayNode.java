package usace.hec.expressions.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.LeafNode;

public class TodayNode implements ExpressionNode<Date>, LeafNode<Date> {

    @Override
    public Date evaluate() {
        Date t = new Date();
        return t;
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        return new ArrayList<>();
    }

    @Override
    public void prefixAppend(StringBuilder sb) {
        sb.append(PreFixSyntax());
    }

    @Override
    public void excelAppend(StringBuilder sb) {
        sb.append(PreFixSyntax());
    }

    @Override
    public String PreFixSyntax(){
        return ExpressionOperator.TODAY.getInfixName();
    }
    @Override
    public String ExcelSyntax(){
        return ExpressionOperator.TODAY.getInfixName();
    }
}
