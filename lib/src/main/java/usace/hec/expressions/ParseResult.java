package usace.hec.expressions;

/**
 * Result of a parse operation. Contains either a successful result
 * or a parse error with position information.
 */
public final class ParseResult {

    private final ExpressionNode node;
    private final ParseError error;
    

    private ParseResult(ExpressionNode node, ParseError error) {
        this.node = node;
        this.error = error;
    }

    public static ParseResult success(ExpressionNode node) {
        return new ParseResult(node, null);
    }

    public static ParseResult error(int position, String message, String remaining) {
        return new ParseResult(null, new ParseError(position, message, remaining));
    }

    public boolean isSuccess() { return error == null; }
    public boolean hasError()  { return error != null; }
    public ExpressionType resultType(){
        if(node!=null){
            return node.resultType();
        }
        else return ExpressionType.VOID;
    }

    /**
     * @return the parsed value, or null if there was an error
     */
    public ExpressionNode getNode() { return node; }

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
