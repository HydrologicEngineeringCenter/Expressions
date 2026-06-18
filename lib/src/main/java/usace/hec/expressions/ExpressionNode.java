package usace.hec.expressions;

import java.util.List;

public interface ExpressionNode<T> {
    T evaluate();
    String stringify();
    String stringifyInFix();
    List<DataListener<?>> fetchListeners();
    void prefixAppend(StringBuilder sb);
    void excelAppend(StringBuilder sb);
}
