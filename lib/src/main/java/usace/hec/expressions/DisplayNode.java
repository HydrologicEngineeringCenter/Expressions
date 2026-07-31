package usace.hec.expressions;

import java.util.List;

public interface DisplayNode {
    String displayName(boolean infix);
    String category();
    String defaultSyntax(boolean infix);
    List<ExpressionType> getExpressionResultTypes();
}
