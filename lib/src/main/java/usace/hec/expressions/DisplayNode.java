package usace.hec.expressions;

public interface DisplayNode {
    String displayName(boolean infix);
    String category();
    String defaultSyntax(boolean infix);
}
