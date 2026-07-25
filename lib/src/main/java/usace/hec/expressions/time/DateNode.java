package usace.hec.expressions.time;


import java.io.Serial;

import java.time.LocalDateTime;
import java.util.List;

import usace.hec.expressions.ConstantLeafNode;
import usace.hec.expressions.DataListener;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.ExpressionNode;

public class DateNode implements ExpressionNode<LocalDateTime> {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ExpressionNode _dd;
    private final ExpressionNode _mm;
    private final ExpressionNode _yyyy;
/*    private final ExpressionNode<Integer> _dd;
    private final ExpressionNode<Integer> _mm;
    private final ExpressionNode<Integer> _yyyy;
 
    public DateNode(ExpressionNode<Integer> yyyy, ExpressionNode<Integer> mm, ExpressionNode<Integer>dd) {
        _dd = dd;
        _mm = mm;
        _yyyy = yyyy;
    }*/
    public DateNode(ExpressionNode yyyy, ExpressionNode mm, ExpressionNode dd) {
        _dd = dd;
        _mm = mm;
        _yyyy = yyyy;
    }
    @Override
    public LocalDateTime evaluate() {

        //this should not be necessary the parser should evaluate the generic types and perform this right. the tokenizer should produce ints or doubles correctly
        var y = _yyyy.evaluate();
        var mm = _mm.evaluate();
        var dd = _dd.evaluate();
        if(y instanceof Double){
            if(mm instanceof Double){
                if(dd instanceof Double){
                    Double dy = (Double)y;
                    Double dmm = (Double)mm;
                    Double ddd = (Double)dd;
                    return LocalDateTime.of(dy.intValue(),dmm.intValue(),ddd.intValue(),0,0);
                }
            }
        }else if(y instanceof Integer){
            if(mm instanceof Integer){
                if(dd instanceof Integer){

                    return LocalDateTime.of((Integer)y,(Integer)mm,(Integer)dd,0,0);
                }
            }
        }
        return LocalDateTime.now();
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
