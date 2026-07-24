package usace.hec.expressions;

/**
 * Result of a parse operation. Contains either a successful result
 * or a parse error with position information.
 */
public final class ParseResult<T> {

    private final T value;
    private final ParseError error;

    private ParseResult(T value, ParseError error) {
        this.value = value;
        this.error = error;
    }

    public static <T> ParseResult<T> success(T value) {
        return new ParseResult<>(value, null);
    }

    public static <T> ParseResult<T> error(int position, String message, String remaining) {
        return new ParseResult<>(null, new ParseError(position, message, remaining));
    }

    public boolean isSuccess() { return error == null; }
    public boolean hasError()  { return error != null; }

    /**
     * @return the parsed value, or null if there was an error
     */
    public T getNode() { return value; }

    /**
     * @return the error details, or null if parsing succeeded
     */
    public ParseError getError() { return error; }

    @Override
    public String toString() {
        if (isSuccess()) return "ParseResult[success]";
        return "ParseResult[error: " + error + "]";
    }
}
