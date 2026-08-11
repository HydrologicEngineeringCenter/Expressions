package usace.hec.expressions;

import java.util.List;

public interface DisplayNode {
    String displayName(boolean infix);
    String category();
    String defaultSyntax(boolean infix);
    List<ExpressionType> getExpressionResultTypes();
    ExpressionOperator getOperator();
    /**
     * The concrete {@link ExpressionNode} classes that implement this operator - one per
     * result type variant (e.g. {@code DoubleGreaterThanNode}, {@code IntegerGreaterThanNode}
     * both backing the same {@code GREATER_THAN} operator). May contain more than one class.
     */
    List<Class<?>> getNodeClasses();
}
