package usace.hec.expressions;

import java.io.Serializable;

public interface ExpressionNode extends Serializable {
    String PreFixSyntax();
    String ExcelSyntax();
    default void setProvider(DataProvider dp){
        return;
    }
    ExpressionType resultType();
    ExpressionOperator Operator();
}
