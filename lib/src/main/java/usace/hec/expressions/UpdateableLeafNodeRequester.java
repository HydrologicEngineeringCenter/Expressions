package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class UpdateableLeafNodeRequester<T extends Serializable> implements ExpressionNode<T>, DataRequester {
    protected final String name;
    protected transient DataProvider dp = null;

    @Serial
    private static final long serialVersionUID = 1L;

    public UpdateableLeafNodeRequester(String name) {
        this.name = name;
    }

    @Override
    public T evaluate() {
        return dp.provideValueForCurrentTimestep(name);
    }

    @Override
    public String PreFixSyntax(){
        return "[" + this.name + "]";
    }
        @Override
    public String ExcelSyntax(){
        return "[" + this.name + "]";
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        return List.of();
    }


    @Override
    public String getName() {
        return name;
    }


    @Override
    public void setProvider(DataProvider dp){
        this.dp = dp;
    }

    @Override
    public ExpressionOperator Operator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Operator'");
    }
}