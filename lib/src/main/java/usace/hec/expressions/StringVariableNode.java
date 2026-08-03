package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StringVariableNode implements StringExpressionNode, DataRequester {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final String name;
    protected volatile String value;
    protected transient DataProvider dp = null;

    public StringVariableNode(String name) {
        this.name = name;
        this.value = "";
    }

    @Override
    public String evaluate() {
        return dp.provideString(name);
    }


    @Override
    public String PreFixSyntax() {
        return "[" + this.name + "]";
    }

    @Override
    public String ExcelSyntax() {
        return "[" + this.name + "]";
    }

    @Override
    public void setProvider(DataProvider dp) {
        this.dp = dp;
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.VARIABLE;
    }

    @Override
    public String getName() {
        return this.name;
    }
}