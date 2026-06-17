package usace.hec.expressions;

import java.util.List;

public abstract class UnaryExpressionNode<T> implements ExpressionNode<T>{
    protected ExpressionNode<T> child;
    public UnaryExpressionNode(ExpressionNode<T> child){
        this.child = child;
    }

    @Override
    public List<DataListener<?>> fetchListeners() {
        return child.fetchListeners();
    }

    @Override
    public String stringify() {
        return OpName() + "(" + child.stringify() + ")";
    }

    @Override
    public String stringifyInFix() {
        return InfixOpName() + "(" + child.stringify() + ")";
    }
    public abstract String OpName();
    public abstract String InfixOpName();

}
