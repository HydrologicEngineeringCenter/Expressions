package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DateTimeVariableNode implements DateTimeExpressionNode, DataRequester {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final String name;
    protected volatile LocalDateTime value;
    protected transient DataProvider dp = null;

    public DateTimeVariableNode(String name) {
        this.name = name;
        this.value = LocalDateTime.now();
    }

    @Override
    public LocalDateTime evaluate() {
        return dp.provideDate(name);
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
