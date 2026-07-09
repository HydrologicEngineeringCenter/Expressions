package usace.hec.expressions;

import java.io.Serializable;

public interface DataListener<T extends Serializable> {
    ExpressionNode<T> owner();
    void onDataUpdate(DataUpdate<T> update);
}
