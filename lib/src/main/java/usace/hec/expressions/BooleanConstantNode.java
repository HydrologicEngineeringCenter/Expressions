package usace.hec.expressions;

import java.io.Serial;

public class BooleanConstantNode implements BooleanExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final boolean value;

    public BooleanConstantNode(boolean value) {
        this.value = value;
    }

    @Override
    public boolean evaluate() {
        ee.clear();
        return value; // Zero boxing: returns primitive Boolean
    }

    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
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
    public void setProvider(DataProvider dp) {
        // No-op: constants never change
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.CONSTANT;
    }
}