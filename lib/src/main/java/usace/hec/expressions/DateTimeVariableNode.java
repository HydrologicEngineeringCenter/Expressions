package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DateTimeVariableNode implements DateTimeExpressionNode, DataListener, DataRequester, Serializable {
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
        if (dp != null) {
            return (LocalDateTime)dp.provideValue(name);
        }
        return this.value;
    }

    @Override
    public void onDataUpdate(DataUpdate newValue) {
        this.value = (LocalDateTime)newValue.newValue();
    }

    @Override
    public List<DataListener> fetchListeners() {
        List<DataListener> list = new ArrayList<>();
        list.add(this);
        return list;
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
    public ExpressionNode owner() {
        return this;
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
