package usace.hec.expressions;

import usace.hec.expressions.comparison.EqualToNode;
import usace.hec.expressions.comparison.GreaterThanNode;
import usace.hec.expressions.comparison.GreaterThanOrEqualNode;
import usace.hec.expressions.comparison.LessThanNode;
import usace.hec.expressions.comparison.LessThanOrEqualNode;
import usace.hec.expressions.logical.AndNode;
import usace.hec.expressions.logical.IfNode;
import usace.hec.expressions.logical.OrNode;
import usace.hec.expressions.logical.XorNode;
import usace.hec.expressions.math.AbsNode;
import usace.hec.expressions.math.AddNode;
import usace.hec.expressions.math.CeilingNode;
import usace.hec.expressions.math.DivideNode;
import usace.hec.expressions.math.ExponentNode;
import usace.hec.expressions.math.FloorNode;
import usace.hec.expressions.math.MaxNode;
import usace.hec.expressions.math.MinNode;
import usace.hec.expressions.math.MinusNode;
import usace.hec.expressions.math.MultiplyNode;
import usace.hec.expressions.math.NegateNode;
import usace.hec.expressions.time.AfterNode;
import usace.hec.expressions.time.BeforeNode;
import usace.hec.expressions.time.DayOfYearNode;
import usace.hec.expressions.time.TodayNode;
import usace.hec.expressions.time.DateNode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Recursive-descent parser for Excel-like expression syntax.
 *
 * <h3>Supported grammar</h3>
 * <pre>
 * expression     -> logicalOr
 * logicalOr      -> logicalXor ( '||' logicalXor )*
 * logicalXor     -> logicalAnd ( '^^' logicalAnd )*
 * logicalAnd     -> comparison ( '&&' comparison )*
 * comparison     -> additive ( ( '>' | '>=' | '<' | '<=' | '==' ) additive )*
 * additive       -> multiplicative ( ( '+' | '-' ) multiplicative )*
 * multiplicative -> exponent ( ( '*' | '/' ) exponent )*
 * exponent       -> unary ( '^' unary )*          // right-associative
 * unary          -> ( '-' | 'ABS' | 'FLOOR' | 'CEILING' ) unary
 *                | primary
 * primary        -> NUMBER
 *                | VARIABLE                     // [name]
 *                | FUNCTION '(' arguments ')'   // IF, MAX, MIN, PLUS, MULTIPLY, ...
 *                | '(' expression ')'
 * arguments      -> expression ( ',' expression )*
 * </pre>
 *
 * <h3>Design notes</h3>
 * <ul>
 *   <li><b>Exception-free</b>: The parser never throws. All syntax errors,
 *       argument mismatches, and unexpected tokens are recorded in
 *       {@link ParseState} and surfaced as {@link ParseResult#error()}.</li>
 *   <li>Unary minus ({@code -}) is disambiguated in {@code parseUnary()} —
 *       the same token can be binary {@code MINUS} at the additive level
 *       or unary {@code NEGATE} at the prefix level.</li>
 *   <li>Prefix operator functions (e.g., {@code PLUS(a,b)}, {@code GT(x,y)})
 *       are handled in {@code buildFunctionNode()} and delegate to the
 *       standard node factories.</li>
 * </ul>
 */
public class ExpressionParser {

    // -----------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------

    /**
     * Parse an expression string into an {@link ExpressionNode} AST.
     *
     * @param input the expression text
     * @return success with the AST node, or an error with position info
     */
    public ParseResult<ExpressionNode> parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return ParseResult.error(0, "Empty expression", "");
        }

        List<Token> tokens = new Tokenizer().tokenize(input);
        for (Token t : tokens) {
            if (t.hasError()) {
                return ParseResult.error(t.position(), t.error(), t.toString());
            }
        }

        ParseState state = new ParseState(tokens, input);
        ExpressionNode node = parseExpression(state);

        if (state.hasError) {
            return ParseResult.error(state.errorPosition, state.errorMessage,
                    state.errorRemaining);
        }

        if (state.pos < tokens.size()) {
            Token next = tokens.get(state.pos);
            return ParseResult.error(next.position(),
                    "Unexpected token after end of expression: " + next,
                    remainingInput(input, next.position()));
        }

        if (node == null) {
            return ParseResult.error(0, "No expression produced", input);
        }

        return ParseResult.success(node);
    }

    // -----------------------------------------------------------------
    // Grammar rules
    // -----------------------------------------------------------------

    private ExpressionNode parseExpression(ParseState s) {
        return parseLogicalOr(s);
    }

    private ExpressionNode parseLogicalOr(ParseState s) {
        ExpressionNode left = parseLogicalXor(s);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op && op.op() == ExpressionOperator.OR) {
                s.advance();
                ExpressionNode right = parseLogicalXor(s);
                if (s.hasError) return null;
                left = makeBinaryNode(s, ExpressionOperator.OR, left, right);
                if (s.hasError) return null;
            } else {
                break;
            }
        }
        return left;
    }

    private ExpressionNode parseLogicalXor(ParseState s) {
        ExpressionNode left = parseLogicalAnd(s);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op && op.op() == ExpressionOperator.XOR) {
                s.advance();
                ExpressionNode right = parseLogicalAnd(s);
                if (s.hasError) return null;
                left = makeBinaryNode(s, ExpressionOperator.XOR, left, right);
                if (s.hasError) return null;
            } else {
                break;
            }
        }
        return left;
    }

    private ExpressionNode parseLogicalAnd(ParseState s) {
        ExpressionNode left = parseComparison(s);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op && op.op() == ExpressionOperator.AND) {
                s.advance();
                ExpressionNode right = parseComparison(s);
                if (s.hasError) return null;
                left = makeBinaryNode(s, ExpressionOperator.AND, left, right);
                if (s.hasError) return null;
            } else {
                break;
            }
        }
        return left;
    }

    private ExpressionNode parseComparison(ParseState s) {
        ExpressionNode left = parseAdditive(s);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op) {
                ExpressionOperator oe = op.op();
                if (oe == ExpressionOperator.GT || oe == ExpressionOperator.GTE ||
                        oe == ExpressionOperator.LT || oe == ExpressionOperator.LTE ||
                        oe == ExpressionOperator.EQ) {
                    s.advance();
                    ExpressionNode right = parseAdditive(s);
                    if (s.hasError) return null;
                    left = makeComparisonNode(s, oe, left, right);
                    if (s.hasError) return null;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return left;
    }

    private ExpressionNode parseAdditive(ParseState s) {
        ExpressionNode left = parseMultiplicative(s);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op) {
                if (op.op() == ExpressionOperator.PLUS ||
                        op.op() == ExpressionOperator.MINUS) {
                    s.advance();
                    ExpressionNode right = parseMultiplicative(s);
                    if (s.hasError) return null;
                    left = makeBinaryNode(s, op.op(), left, right);
                    if (s.hasError) return null;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return left;
    }

    private ExpressionNode parseMultiplicative(ParseState s) {
        ExpressionNode left = parseExponent(s);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op) {
                if (op.op() == ExpressionOperator.MULTIPLY ||
                        op.op() == ExpressionOperator.DIVIDE) {
                    s.advance();
                    ExpressionNode right = parseExponent(s);
                    if (s.hasError) return null;
                    left = makeBinaryNode(s, op.op(), left, right);
                    if (s.hasError) return null;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return left;
    }

    private ExpressionNode parseExponent(ParseState s) {
        ExpressionNode left = parseUnary(s);
        if (s.hasError) return null;
        Token t = peek(s);
        if (t instanceof Token.Operator op && op.op() == ExpressionOperator.POW) {
            s.advance();
            ExpressionNode right = parseExponent(s);
            if (s.hasError) return null;
            return makeBinaryNode(s, ExpressionOperator.POW, left, right);
        }
        return left;
    }

    private ExpressionNode parseUnary(ParseState s) {
        Token t = peek(s);
        if (t instanceof Token.Operator op && op.op() == ExpressionOperator.MINUS) {
            s.advance();
            ExpressionNode child = parseUnary(s);
            if (s.hasError) return null;
            return makeUnaryNode(s, ExpressionOperator.NEGATE, child);
        }

        if (t instanceof Token.Function fn) {
            if (fn.op() == ExpressionOperator.ABS ||
                    fn.op() == ExpressionOperator.FLOOR ||
                    fn.op() == ExpressionOperator.CEILING) {
                s.advance();
                if (peek(s) instanceof Token.LeftParen) {
                    s.advance();
                    ExpressionNode child = parseExpression(s);
                    if (s.hasError) return null;
                    expect(s, Token.RightParen.class, "')'");
                    if (s.hasError) return null;
                    return makeUnaryNode(s, fn.op(), child);
                } else {
                    ExpressionNode child = parseUnary(s);
                    if (s.hasError) return null;
                    return makeUnaryNode(s, fn.op(), child);
                }
            }
        }

        if (t instanceof Token.Operator op2) {
            if (op2.op() == ExpressionOperator.ABS ||
                    op2.op() == ExpressionOperator.FLOOR ||
                    op2.op() == ExpressionOperator.CEILING) {
                s.advance();
                ExpressionNode child = parseUnary(s);
                if (s.hasError) return null;
                return makeUnaryNode(s, op2.op(), child);
            }
        }

        return parsePrimary(s);
    }

    private ExpressionNode parsePrimary(ParseState s) {
        Token t = peek(s);
        if (t == null) {
            setError(s, currentPos(s), "Unexpected end of input", "");
            return null;
        }

        if (t instanceof Token.Number num) {
            s.advance();
            return new ConstantLeafNode<>(num.value());
        }

        if (t instanceof Token.Variable var) {
            s.advance();
            return new UpdateableLeafNode<>(var.name());
        }

        if (t instanceof Token.StringLiteral sl) {
            s.advance();
            boolean val = Boolean.parseBoolean(sl.value());
            return new ConstantLeafNode<>(val);
        }

        if (t instanceof Token.LeftParen) {
            s.advance();
            ExpressionNode node = parseExpression(s);
            if (s.hasError) return null;
            expect(s, Token.RightParen.class, "')'");
            if (s.hasError) return null;
            return node;
        }

        if (t instanceof Token.Function fn) {
            return parseFunctionCall(s, fn.op());
        }

        setError(s, t.position(), "Unexpected token: " + t,
                remainingInput(s.input, t.position()));
        return null;
    }

    private ExpressionNode parseFunctionCall(ParseState s, ExpressionOperator fn) {
        s.advance();
        expect(s, Token.LeftParen.class, "'(' after function name");
        if (s.hasError) return null;

        List<ExpressionNode> args = new ArrayList<>();

        if (peek(s) instanceof Token.RightParen) {
            s.advance();
            return buildFunctionNode(s, fn, args);
        }

        args.add(parseExpression(s));
        if (s.hasError) return null;

        while (!s.hasError && peek(s) instanceof Token.Comma) {
            s.advance();
            args.add(parseExpression(s));
            if (s.hasError) return null;
        }

        expect(s, Token.RightParen.class, "')' to close function call");
        if (s.hasError) return null;

        return buildFunctionNode(s, fn, args);
    }

    // -----------------------------------------------------------------
    // AST node factory helpers (exception-free)
    // -----------------------------------------------------------------

    private ExpressionNode makeBinaryNode(ParseState s, ExpressionOperator op,
            ExpressionNode left, ExpressionNode right) {
        switch (op) {
            case PLUS: return new AddNode(left, right);
            case MINUS: return new MinusNode(left, right);
            case MULTIPLY: return new MultiplyNode(left, right);
            case POW: return new ExponentNode(left, right);
            case DIVIDE: return new DivideNode(left, right);
            case MAX: return new MaxNode(left, right);
            case MIN: return new MinNode(left, right);
            case GT: return new GreaterThanNode<>(left, right);
            case GTE: return new GreaterThanOrEqualNode<>(left, right);
            case LT: return new LessThanNode<>(left, right);
            case LTE: return new LessThanOrEqualNode<>(left, right);
            case EQ: return new EqualToNode<>(left, right);
            case AND: return new AndNode(left, right);
            case OR: return new OrNode(left, right);
            case XOR: return new XorNode(left, right);
            default:
                setError(s, currentPos(s), "Unknown binary operator: " + op, "");
                return null;
        }
    }

    private ExpressionNode makeComparisonNode(ParseState s, ExpressionOperator op,
            ExpressionNode left, ExpressionNode right) {
        return makeBinaryNode(s, op, left, right);
    }

    private ExpressionNode makeUnaryNode(ParseState s, ExpressionOperator op,
            ExpressionNode child) {
        switch (op) {
            case NEGATE: return new NegateNode(child);
            case ABS: return new AbsNode(child);
            case FLOOR: return new FloorNode(child);
            case CEILING: return new CeilingNode(child);
            default:
                setError(s, currentPos(s), "Unknown unary operator: " + op, "");
                return null;
        }
    }

    private ExpressionNode buildFunctionNode(ParseState s, ExpressionOperator fn,
            List<ExpressionNode> args) {
        switch (fn) {
            case IF: {
                if (args.size() != 3) {
                    setError(s, currentPos(s), "IF requires exactly 3 arguments, got " + args.size(), "");
                    return null;
                }
                return new IfNode<>(args.get(0), args.get(1), args.get(2));
            }
            case MAX: {
                if (args.size() < 1) {
                    setError(s, currentPos(s), "MAX requires at least 1 argument, got " + args.size(), "");
                    return null;
                }
                ExpressionNode result = args.get(0);
                for (int i = 1; i < args.size(); i++) {
                    result = new MaxNode(result, args.get(i));
                }
                return result;
            }
            case MIN: {
                if (args.size() < 1) {
                    setError(s, currentPos(s), "MIN requires at least 1 argument, got " + args.size(), "");
                    return null;
                }
                ExpressionNode result = args.get(0);
                for (int i = 1; i < args.size(); i++) {
                    result = new MinNode(result, args.get(i));
                }
                return result;
            }
            case NEGATE:
            case ABS:
            case FLOOR:
            case CEILING: {
                if (args.size() != 1) {
                    setError(s, currentPos(s), fn.name() + " requires exactly 1 argument, got " + args.size(), "");
                    return null;
                }
                return makeUnaryNode(s, fn, args.get(0));
            }
            case PLUS:
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
            case POW:
            case GT:
            case GTE:
            case LT:
            case LTE:
            case EQ:
            case AND:
            case OR:
            case XOR: {
                if (args.size() != 2) {
                    setError(s, currentPos(s), fn.name() + " requires exactly 2 arguments, got " + args.size(), "");
                    return null;
                }
                return makeBinaryNode(s, fn, args.get(0), args.get(1));
            }
            /*case TODAY:
                // TODAY() -- zero arguments
                if (!(peek(s) instanceof Token.LeftParen)) {
                    setError(s, currentPos(s), "Expected '(' after TODAY", "");
                    return null;
                }
                s.advance();
                if (!(peek(s) instanceof Token.RightParen)) {
                    setError(s, currentPos(s), "TODAY() takes no arguments", "");
                    return null;
                }
                s.advance();
                return new TodayNode();

            case DOY:
                // DOY(date_expr) -- one argument
                if (!(peek(s) instanceof Token.LeftParen)) {
                    setError(s, currentPos(s), "Expected '(' after DOY", "");
                    return null;
                }
                s.advance();
                ExpressionNode doyArg = parseExpression(s);
                if (s.hasError) return null;
                if (!(peek(s) instanceof Token.RightParen)) {
                    setError(s, currentPos(s), "Expected ')' after DOY", "");
                    return null;
                }
                s.advance();
                return new DayOfYearNode(doyArg);

            case AFTER:
                // AFTER(date1, date2) -- two arguments
                if (!(peek(s) instanceof Token.LeftParen)) {
                    setError(s, currentPos(s), "Expected '(' after AFTER", "");
                    return null;
                }
                s.advance();
                ExpressionNode<LocalDateTime> afterLeft = parseExpression(s);
                if (s.hasError) return null;
                if (!(peek(s) instanceof Token.Comma)) {
                    setError(s, currentPos(s), "Expected ',' in AFTER(date1, date2)", "");
                    return null;
                }
                s.advance();
                ExpressionNode<LocalDateTime> afterRight = parseExpression(s);
                if (s.hasError) return null;
                if (!(peek(s) instanceof Token.RightParen)) {
                    setError(s, currentPos(s), "Expected ')' after AFTER", "");
                    return null;
                }
                s.advance();
                return new AfterNode(afterLeft, afterRight);

            case BEFORE:
                // BEFORE(date1, date2) -- two arguments
                if (!(peek(s) instanceof Token.LeftParen)) {
                    setError(s, currentPos(s), "Expected '(' after BEFORE", "");
                    return null;
                }
                s.advance();
                ConstantLeafNode<LocalDateTime> beforeLeft = parseExpression(s);
                if (s.hasError) return null;
                if (!(peek(s) instanceof Token.Comma)) {
                    setError(s, currentPos(s), "Expected ',' in BEFORE(date1, date2)", "");
                    return null;
                }
                s.advance();
                ExpressionNode beforeRight = parseExpression(s);
                if (s.hasError) return null;
                if (!(peek(s) instanceof Token.RightParen)) {
                    setError(s, currentPos(s), "Expected ')' after BEFORE arguments", "");
                    return null;
                }
                s.advance();
                return new BeforeNode(beforeLeft, beforeRight);

            case DATE:
                // DATE(year, month, day) -- three integer arguments
                if (!(peek(s) instanceof Token.LeftParen)) {
                    setError(s, currentPos(s), "Expected '(' after DATE", "");
                    return null;
                }
                s.advance();
                ExpressionNode year = parseExpression(s);
                if (s.hasError) return null;
                if (!(peek(s) instanceof Token.Comma)) {
                    setError(s, currentPos(s), "Expected ',' in DATE(year, month, day)","");
                    return null;
                }
                s.advance();
                ExpressionNode month = parseExpression(s);
                if (s.hasError) return null;
                if (!(peek(s) instanceof Token.Comma)) {
                    setError(s, currentPos(s), "Expected ',' in DATE(year, month, day)","");
                    return null;
                }
                s.advance();
                ExpressionNode day = parseExpression(s);
                if (s.hasError) return null;
                if (!(peek(s) instanceof Token.RightParen)) {
                    setError(s, currentPos(s), "Expected ')' after DATE arguments","");
                    return null;
                }
                s.advance();
                int y = ((Number) year.evaluate()).intValue();
                int m = ((Number) month.evaluate()).intValue();
                int d = ((Number) day.evaluate()).intValue();
                LocalDateTime ldt = LocalDateTime.of(y,m,d,0,0);
                return new DateNode(ldt);
                */
            default:
                setError(s, currentPos(s), "Unknown function: " + fn.name(), "");
                return null;
        }
    }

    // -----------------------------------------------------------------
    // Parsing utilities
    // -----------------------------------------------------------------

    private Token peek(ParseState s) {
        if (s.pos >= s.tokens.size()) return null;
        return s.tokens.get(s.pos);
    }

    private int currentPos(ParseState s) {
        Token t = peek(s);
        return t != null ? t.position() : -1;
    }

    private void expect(ParseState s, Class<?> expectedClass, String description) {
        Token t = peek(s);
        if (t == null) {
            setError(s, currentPos(s), "Unexpected end of input, expected " + description, "");
            return;
        }
        if (!expectedClass.isInstance(t)) {
            setError(s, t.position(),
                    "Expected " + description + " but found: " + t,
                    remainingInput(s.input, t.position()));
            return;
        }
        s.advance();
    }

    private void setError(ParseState s, int position, String message, String remaining) {
        s.hasError = true;
        s.errorPosition = position;
        s.errorMessage = message;
        s.errorRemaining = remaining;
    }

    private static String remainingInput(String input, int pos) {
        if (pos < 0 || pos >= input.length()) return "";
        int end = Math.min(pos + 40, input.length());
        String s = input.substring(pos, end);
        return end < input.length() ? s + "..." : s;
    }

    // -----------------------------------------------------------------
    // Parse state
    // -----------------------------------------------------------------

    private static class ParseState {
        final List<Token> tokens;
        final String input;
        int pos = 0;
        boolean hasError = false;
        int errorPosition = -1;
        String errorMessage = "";
        String errorRemaining = "";

        ParseState(List<Token> tokens, String input) {
            this.tokens = tokens;
            this.input = input;
        }

        void advance() {
            if (pos < tokens.size()) pos++;
        }
    }
}