package usace.hec.expressions;

import java.io.Serializable;

import java.time.Instant;
import java.util.List;

public interface ExpressionNode extends Serializable {
    String PreFixSyntax();
    String ExcelSyntax();
    List<DataListener> fetchListeners();
    default void setProvider(DataProvider dp){
        return;
    }
    ExpressionType resultType();
    ExpressionOperator Operator();
}
