package usace.hec.expressions.time;


import java.io.Serial;

import java.time.LocalDateTime;
import java.util.List;

import usace.hec.expressions.DataListener;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.ExpressionNode;

public class DateNode implements ExpressionNode<LocalDateTime> {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ExpressionNode<Integer> _dd;
    private final ExpressionNode<Integer> _mm;
    private final ExpressionNode<Integer> _yyyy;
 
    public DateNode(ExpressionNode<Integer> yyyy, ExpressionNode<Integer> mm, ExpressionNode<Integer>dd) {
        _dd = dd;
        _mm = mm;
        _yyyy = yyyy;
    }

    @Override
    public LocalDateTime evaluate() {
        return LocalDateTime.of(((Number)_yyyy.evaluate()).intValue(),((Number)_mm.evaluate()).intValue(),((Number)_dd.evaluate()).intValue(),0,0);

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
    public List<DataListener<?>> fetchListeners() {
        List<DataListener<?>> list = _yyyy.fetchListeners();
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
