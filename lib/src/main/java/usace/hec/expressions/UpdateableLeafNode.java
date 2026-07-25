package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateableLeafNode<T extends Serializable> implements ExpressionNode<T>, DataListener<T>, DataRequester{
    protected String name;
    protected T value;
    protected transient DataProvider dp = null;
    @Serial
    private static final long serialVersionUID = 1L;

    public UpdateableLeafNode(String name) {
        this.name = name;
        this.value = null;
    }

    @Override
    public T evaluate() {
        if(dp!=null){
            return dp.provideValueForCurrentTimestep(name);
        }
        return this.value;
    }

    // Listens directly to data changes without tree traversal
    @Override
    public void onDataUpdate(DataUpdate<T> update) {
        if (this.name.equals(update.variableName())) {
            this.value = update.newValue();
        }
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        List<DataListener<?>> list = new ArrayList<>();
        list.add(this);
        return list;
        
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
    public ExpressionNode<T> owner() {
        return this;
    }
  @Override
    public void setProvider(DataProvider dp){
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