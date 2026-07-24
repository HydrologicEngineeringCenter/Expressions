package usace.hec.expressions;

/**
 * Describes a parse error with position information for UI highlighting.
 */
public record ParseError(
    int position,       // character index in the input where the error was detected
    String message,     // human-readable description of the problem
    String remaining    // the unparsed remainder of input, for context display
) {
    @Override
    public String toString() {
        return "ParseError[pos=" + position + ", msg=\"" + message + "\"]";
    }
}