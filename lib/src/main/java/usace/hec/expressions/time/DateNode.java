package usace.hec.expressions.time;


import java.io.Serial;

import java.time.LocalDateTime;
import java.util.List;

import usace.hec.expressions.ConstantLeafNode;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionNode;

public class DateNode implements ExpressionNode<LocalDateTime> {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ExpressionNode<Integer> _dd;
    private final ExpressionNode<Integer> _mm;
    private final ExpressionNode<Integer> _yyyy;

    public DateNode(ExpressionNode<Integer> dd, ExpressionNode<Integer> mm, ExpressionNode<Integer>yyyy) {
        _dd = dd;
        _mm = mm;
        _yyyy = yyyy;
    }
    @Override
    public LocalDateTime evaluate() {
        Integer y = _yyyy.evaluate();
        Integer mm = _mm.evaluate();
        Integer dd = _dd.evaluate();
        return LocalDateTime.of(y,mm,dd,0,0);
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
}
