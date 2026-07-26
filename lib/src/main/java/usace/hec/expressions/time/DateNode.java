package usace.hec.expressions.time;


import java.io.Serial;

import java.time.LocalDateTime;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.DateTimeExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.IntegerExpressionNode;

public class DateNode implements DateTimeExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private final IntegerExpressionNode _dd;
    private final IntegerExpressionNode _mm;
    private final IntegerExpressionNode _yyyy;
 
    public DateNode(IntegerExpressionNode yyyy, IntegerExpressionNode mm, IntegerExpressionNode dd) {
        _dd = dd;
        _mm = mm;
        _yyyy = yyyy;
    }

    @Override
    public LocalDateTime evaluate() {
        return LocalDateTime.of(_yyyy.evaluate(),_mm.evaluate(),_dd.evaluate(),0,0);

    }
    @Override
    public String PreFixSyntax() {
        return "DATE(" + _yyyy.PreFixSyntax() + ","
             + _mm.PreFixSyntax() + ","
             + _dd.PreFixSyntax() + ")";
    }

     @Override
    public String ExcelSyntax() {
        return "DATE(" + _yyyy.ExcelSyntax() + ","
             + _mm.ExcelSyntax() + ","
             + _dd.ExcelSyntax() + ")";
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.DATE;
    }
    @Override
    public List<DataListener> fetchListeners() {
        List<DataListener> list = _yyyy.fetchListeners();
       list.addAll(_mm.fetchListeners());
       list.addAll(_dd.fetchListeners());
       return list;  
    }
    @Override
    public void setProvider(DataProvider dp) {
        _yyyy.setProvider(dp);
        _mm.setProvider(dp);
        _dd.setProvider(dp);
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DATE;
    }
}
