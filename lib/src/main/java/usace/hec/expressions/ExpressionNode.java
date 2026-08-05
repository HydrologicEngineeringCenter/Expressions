package usace.hec.expressions;

import java.io.Serializable;

public interface ExpressionNode extends Serializable, ErrorReportable {
    String PreFixSyntax();
    String ExcelSyntax();
    default void setProvider(DataProvider dp){
        return;
    }
    @Override
    default EvaluationError getEvaluationError(){
        return new EvaluationError();
    }
    ExpressionType resultType();
    ExpressionOperator Operator();
}
