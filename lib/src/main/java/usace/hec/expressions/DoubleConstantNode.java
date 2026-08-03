package usace.hec.expressions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class DoubleConstantNode implements DoubleExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final double value;

    public DoubleConstantNode(double value) {
        this.value = value;
    }

    @Override
    public double evaluate() {
        return value; // Zero boxing: returns primitive double
    }

    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
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