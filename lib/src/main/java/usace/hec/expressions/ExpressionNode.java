package usace.hec.expressions;

import java.io.Serializable;

public interface ExpressionNode extends Serializable, ErrorReportable {
    String PreFixSyntax();
    String ExcelSyntax();
    default void setProvider(DataProvider dp){
        return;
    }
    @Override
    default void setErrorChannel(ErrorChannel channel){
        return;
    }
    @Override
    default EvaluationError getEvaluationError(){
        return null;
    }
    ExpressionType resultType();
    ExpressionOperator Operator();
}
