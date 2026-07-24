package usace.hec.expressions;

/**
 * Sealed hierarchy of tokens produced by {@link Tokenizer}.
 */
public sealed interface Token permits
    Token.Number,
    Token.Variable,
    Token.Operator,
    Token.Function,
    Token.LeftParen,
    Token.RightParen,
    Token.Comma,
    Token.StringLiteral,
    Token.Unknown
{
    /** Zero-based position in the original source string. */
    int pos();

    /** Error message; blank when the token is valid. */
    String error();

    /** Convenience: same as {@link #pos()}. */
    default int position() {
        return pos();
    }

    /** Convenience: true when {@link #error()} is non-blank. */
    default boolean hasError() {
        return !error().isBlank();
    }

    record Number(double value, int pos, String error) implements Token {}
    record Variable(String name, int pos, String error) implements Token {}
    record Operator(ExpressionOperator op, int pos, String error) implements Token {}
    record Function(ExpressionOperator op, int pos, String error) implements Token {}
    record LeftParen(int pos, String error) implements Token {}
    record RightParen(int pos, String error) implements Token {}
    record Comma(int pos, String error) implements Token {}
    record StringLiteral(String value, int pos, String error) implements Token {}
    record Unknown(String value, int pos, String error, String remaining) implements Token {}
}