package usace.hec.expressions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class StringConstantNode implements StringExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final String value;

    public StringConstantNode(String value) {
        this.value = value;
    }

    @Override
    public String evaluate() {
        return value; // Zero boxing: returns primitive String
    }

    @Override
    public ExpressionType resultType() {
        return ExpressionType.STRING;
    }

    @Override
    public String PreFixSyntax() {
        return String.valueOf(value);
    }

    @Override
    public String ExcelSyntax() {
        return String.valueOf(value);
    }

    @Override
    public List<DataListener> fetchListeners() {
        return new ArrayList<>(); // Constants don't listen to data
    }

    @Override
    public void setProvider(DataProvider dp) {
        // No-op: constants never change
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.CONSTANT;
    }
}