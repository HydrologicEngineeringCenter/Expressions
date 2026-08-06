package usace.hec.expressions;

import java.io.Serial;

public class IntegerConstantNode implements IntegerExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final int value;

    public IntegerConstantNode(int value) {
        this.value = value;
    }

    @Override
    public int evaluate() {
        ee.clear();     ee.clear();
        return value; // Zero boxing: returns primitive Integer
    }

    @Override
    public ExpressionType resultType() {
        return ExpressionType.INTEGER;
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