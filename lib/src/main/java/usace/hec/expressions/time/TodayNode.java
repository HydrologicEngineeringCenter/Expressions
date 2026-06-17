package usace.hec.expressions.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionNode;
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
    public String stringify(){
        return "TODAY()";
    }
        @Override
    public String stringifyInFix(){
        return "TODAY()";
    }
}
