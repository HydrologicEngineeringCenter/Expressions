package usace.hec.expressions;

import java.util.List;

public interface DisplayNodeProvider {
    List<DisplayNode> getDisplayNodes();
    List<DisplayNode> getDisplayNodesByCategory(String category);
    List<DisplayNode> getDisplayNodesByOutputType(ExpressionType type);
}
