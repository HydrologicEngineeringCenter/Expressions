package usace.hec.expressions;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConstantLeafNode<T extends Serializable> implements ExpressionNode<T>, LeafNode<T>{
    private final T value;
    @Serial
    private static final long serialVersionUID = 1L;

    public ConstantLeafNode(T value) {
        this.value = value;
    }

    @Override
    public T evaluate() {
        return this.value;
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        return new ArrayList<>();
    }

    @Override
    public String PreFixSyntax(){
        return value.toString();
    }
        @Override
    public String ExcelSyntax(){
        return value.toString();
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.CONSTANT;
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;//placeholder
    }

}