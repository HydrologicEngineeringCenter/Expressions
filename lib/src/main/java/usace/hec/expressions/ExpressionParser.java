package usace.hec.expressions;

import usace.hec.expressions.comparison.DoubleEqualToNode;
import usace.hec.expressions.comparison.DoubleGreaterThanNode;
import usace.hec.expressions.comparison.DoubleGreaterThanOrEqualNode;
import usace.hec.expressions.comparison.DoubleLessThanNode;
import usace.hec.expressions.comparison.DoubleLessThanOrEqualNode;
import usace.hec.expressions.comparison.IntegerEqualToNode;
import usace.hec.expressions.comparison.IntegerGreaterThanNode;
import usace.hec.expressions.comparison.IntegerGreaterThanOrEqualNode;
import usace.hec.expressions.comparison.IntegerLessThanNode;
import usace.hec.expressions.comparison.IntegerLessThanOrEqualNode;
import usace.hec.expressions.logical.*;
import usace.hec.expressions.math.DoubleAbsNode;
import usace.hec.expressions.math.DoubleAddNode;
import usace.hec.expressions.math.DoubleCeilingNode;
import usace.hec.expressions.math.DoubleDivideNode;
import usace.hec.expressions.math.DoubleExponentNode;
import usace.hec.expressions.math.DoubleFloorNode;
import usace.hec.expressions.math.DoubleMaxNode;
import usace.hec.expressions.math.DoubleMinNode;
import usace.hec.expressions.math.DoubleMinusNode;
import usace.hec.expressions.math.DoubleMultiplyNode;
import usace.hec.expressions.math.DoubleNegateNode;
import usace.hec.expressions.math.IntegerAbsNode;
import usace.hec.expressions.math.IntegerAddNode;
import usace.hec.expressions.math.IntegerCeilingNode;
import usace.hec.expressions.math.IntegerDivideNode;
import usace.hec.expressions.math.IntegerExponentNode;
import usace.hec.expressions.math.IntegerFloorNode;
import usace.hec.expressions.math.IntegerMaxNode;
import usace.hec.expressions.math.IntegerMinNode;
import usace.hec.expressions.math.IntegerMinusNode;
import usace.hec.expressions.math.IntegerMultiplyNode;
import usace.hec.expressions.math.IntegerNegateNode;
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
 * <h3>Type Safety & Coercion</h3>
 * <ul>
 *   <li>The parser enforces type compatibility at parse time.</li>
 *   <li>Widening coercion (e.g., int -> double) is applied automatically via {@link IntegerToDoubleCoerceNode}.</li>
 *   <li>Narrowing coercion is generally rejected to prevent silent precision loss, except where explicit (e.g., DATE args).</li>
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
            return ParseResult.error(state.errorPosition, state.errorMessage, state.errorRemaining);
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
                left = NodeFactory.buildBinaryNode(s, ExpressionOperator.OR, left, right);
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
                left = NodeFactory.buildBinaryNode(s, ExpressionOperator.XOR, left, right);
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
                left = NodeFactory.buildBinaryNode(s, ExpressionOperator.AND, left, right);
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
                    left = NodeFactory.buildBinaryNode(s, oe, left, right);
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
                if (op.op() == ExpressionOperator.PLUS || op.op() == ExpressionOperator.MINUS) {
                    s.advance();
                    ExpressionNode right = parseMultiplicative(s);
                    if (s.hasError) return null;
                    left = NodeFactory.buildBinaryNode(s, op.op(), left, right);
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
                if (op.op() == ExpressionOperator.MULTIPLY || op.op() == ExpressionOperator.DIVIDE) {
                    s.advance();
                    ExpressionNode right = parseExponent(s);
                    if (s.hasError) return null;
                    left = NodeFactory.buildBinaryNode(s, op.op(), left, right);
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
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op) {
                if (op.op() == ExpressionOperator.POW) {
                    s.advance();
                    ExpressionNode right = parseUnary(s);
                    if (s.hasError) return null;
                    left = NodeFactory.buildBinaryNode(s, op.op(), left, right);
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

    private ExpressionNode parseUnary(ParseState s) {
        Token t = peek(s);
        
        // Unary minus
        if (t instanceof Token.Operator op && op.op() == ExpressionOperator.MINUS) {
            s.advance();
            ExpressionNode child = parseUnary(s);
            if (s.hasError) return null;
            return NodeFactory.buildUnaryNode(s, ExpressionOperator.NEGATE, child);
        }

        // Prefix functions: ABS, FLOOR, CEILING
        if (t instanceof Token.Function fn) {
            if (fn.op() == ExpressionOperator.ABS || fn.op() == ExpressionOperator.FLOOR || fn.op() == ExpressionOperator.CEILING
            || fn.op() == ExpressionOperator.DOUBLECOERSION || fn.op() == ExpressionOperator.INTCOERSION) {
                s.advance();
                // Check for functional syntax ABS(x) or prefix syntax ABS x
                if (peek(s) instanceof Token.LeftParen) {
                    s.advance();
                    ExpressionNode child = parseExpression(s);
                    if (s.hasError) return null;
                    expect(s, Token.RightParen.class, "')'");
                    if (s.hasError) return null;
                    return NodeFactory.buildUnaryNode(s, fn.op(), child);
                } else {
                    ExpressionNode child = parseUnary(s);
                    if (s.hasError) return null;
                    return NodeFactory.buildUnaryNode(s, fn.op(), child);
                }
            }
        }
        
        // Handle operator syntax for ABS/FLOOR/CEILING if defined as operators
        if (t instanceof Token.Operator op2) {
            if (op2.op() == ExpressionOperator.ABS || op2.op() == ExpressionOperator.FLOOR || op2.op() == ExpressionOperator.CEILING) {
                s.advance();
                ExpressionNode child = parseUnary(s);
                if (s.hasError) return null;
                return NodeFactory.buildUnaryNode(s, op2.op(), child);
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

        s.advance();

        if (t instanceof Token.DoubleLiteral num) {
            return new DoubleConstantNode(num.value());
        }
        if (t instanceof Token.IntegerLiteral num) {
            return new IntegerConstantNode(num.value());
        }
        if (t instanceof Token.Variable var) {
            // TODO: In a full implementation, look up 'var.name()' in a SymbolTable
            // to determine the correct variable type (Int, Double, Boolean).
            // Defaulting to DoubleVariableNode for backward compatibility.
            return new DoubleVariableNode(var.name());
        }
        if (t instanceof Token.StringLiteral sl) {
            return new StringConstantNode(sl.value());
        }
        if (t instanceof Token.BooleanLiteral bl) {
            return new BooleanConstantNode(bl.value());
        }
        if (t instanceof Token.LeftParen) {
            ExpressionNode node = parseExpression(s);
            if (s.hasError) return null;
            expect(s, Token.RightParen.class, "')'");
            if (s.hasError) return null;
            return node;
        }
        if (t instanceof Token.Function fn) {
            return parseFunctionCall(s, fn.op());
        }

        setError(s, t.position(), "Unexpected token: " + t, remainingInput(s.input, t.position()));
        return null;
    }

    private ExpressionNode parseFunctionCall(ParseState s, ExpressionOperator fn) {
        expect(s, Token.LeftParen.class, "'(' after function name");
        if (s.hasError) return null;

        List<ExpressionNode> args = new ArrayList<>();

        if (peek(s) instanceof Token.RightParen) {
            s.advance();
            return NodeFactory.buildFunctionNode(s, fn, args);
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

        return NodeFactory.buildFunctionNode(s, fn, args);
    }

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------

    private Token peek(ParseState s) {
        return (s.pos < s.tokens.size()) ? s.tokens.get(s.pos) : null;
    }

    private int currentPos(ParseState s) {
        Token t = peek(s);
        return (t != null) ? t.position() : -1;
    }

    private void expect(ParseState s, Class<?> expectedClass, String description) {
        Token t = peek(s);
        if (t == null) {
            setError(s, currentPos(s), "Unexpected end of input, expected " + description, "");
            return;
        }
        if (!expectedClass.isInstance(t)) {
            setError(s, t.position(), "Expected " + description + " but found: " + t, remainingInput(s.input, t.position()));
            return;
        }
        s.advance();
    }

    private void setError(ParseState s, int position, String message, String remaining) {
        if (s != null) {
            s.hasError = true;
            s.errorPosition = position;
            s.errorMessage = message;
            s.errorRemaining = remaining;
        }
    }

    private static String remainingInput(String input, int pos) {
        if (pos < 0 || pos >= input.length()) return "";
        int end = Math.min(pos + 40, input.length());
        String s = input.substring(pos, end);
        return (end < input.length()) ? s + "..." : s;
    }

    // -----------------------------------------------------------------
    // Parse state
    // -----------------------------------------------------------------

    public static class ParseState {
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