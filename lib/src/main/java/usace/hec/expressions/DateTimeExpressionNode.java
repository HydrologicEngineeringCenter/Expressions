package usace.hec.expressions;

import java.time.LocalDateTime;

public interface DateTimeExpressionNode extends ExpressionNode<LocalDateTime> {
    LocalDateTime evaluate();
    @Override
    default ExpressionType resultType() {
        return ExpressionType.DATE;
    }
}
