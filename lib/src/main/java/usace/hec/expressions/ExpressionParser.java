package usace.hec.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Recursive-descent parser for Excel-compatible expression syntax.
 *
 * Converts a string expression into a typed Abstract Syntax Tree (AST) of 
 * {@link ExpressionNode} objects. The parser tokenizes input using {@link Tokenizer},
 * validates token sequences, and constructs the tree using {@link NodeFactory}.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * ExpressionParser parser = new ExpressionParser();
 * ParseResult<ExpressionNode> result = parser.parse("IF([a] > 5, 10.0, 20.0)");
 * 
 * if (result.isSuccess()) {
 *     ExpressionNode tree = result.getNode();
 *     // evaluate or traverse tree...
 * } else {
 *     ParseError err = result.getError();
 *     System.err.println(err.message());
 * }
 * }</pre>
 *
 * <h2>Grammar & Operator Precedence</h2>
 * <p>The parser implements precedence through layered recursive-descent methods.
 * Operators at the same precedence level are evaluated left-to-right. Precedence 
 * from lowest to highest:</p>
 * <ol>
 *   <li>Logical OR ({@code ||})</li>
 *   <li>Logical XOR ({@code ^^})</li>
 *   <li>Logical AND ({@code &&})</li>
 *   <li>Comparisons ({@code >, >=, <, <=, ==})</li>
 *   <li>Additive ({@code +, -})</li>
 *   <li>Multiplicative ({@code *, /})</li>
 *   <li>Exponentiation ({@code ^})</li>
 *   <li>Unary prefix ({@code -, ABS, FLOOR, CEILING, TOINT, TODOUBLE, NOT})</li>
 *   <li>Primary: literals, variables, parenthesized expressions, function calls</li>
 * </ol>
 *
 * <h2>Supported Syntax</h2>
 * <ul>
 *   <li><b>Numeric Literals</b>: {@code 42} (Integer), {@code 3.14} (Double)</li>
 *   <li><b>Boolean Literals</b>: {@code TRUE}, {@code FALSE}</li>
 *   <li><b>String Literals</b>: {@code "text"}</li>
 *   <li><b>Variables</b>: {@code [variableName]} (parsed as {@link DoubleVariableNode})</li>
 *   <li><b>Infix Operators</b>: {@code + - * / ^ == > < >= <= && || ^^}</li>
 *   <li><b>Function Calls</b>: {@code IF(cond, then, else)}, {@code MAX(a,b)}, {@code TODAY()}, etc.</li>
 *   <li><b>Unary Prefix Functions</b>: Support both functional {@code ABS(x)} and prefix {@code ABS x} syntax.</li>
 * </ul>
 *
 * <h2>Type Resolution & Coercion</h2>
 * <p>The parser delegates node construction to {@link NodeFactory}, which resolves 
 * operand types at parse time. Binary and unary operators are constructed via 
 * {@link NodeFactory#buildBinaryNode} and {@link NodeFactory#buildUnaryNode}, 
 * which automatically insert widening coercion nodes (e.g., {@link IntegerToDoubleCoerceNode}) 
 * when mixing {@code int} and {@code double} operands. Explicit coercion functions 
 * {@code TOINT()} and {@code TODOUBLE()} are available for narrowing or explicit typing.</p>
 *
 * <h2>Error Handling</h2>
 * <p>The parser halts on the first unrecoverable syntax error. Errors are returned 
 * via {@link ParseResult} containing a {@link ParseError} with:</p>
 * <ul>
 *   <li>Character position index in the original input string</li>
 *   <li>Descriptive error message</li>
 *   <li>Remaining input snippet (up to 40 characters) for context</li>
 * </ul>
 *
 * <h2>Internal Structure</h2>
 * <p>Parsing state is tracked in {@link ParseState}, which maintains the token list, 
 * current position, input string, and error flags. Helper methods {@code peek()}, 
 * {@code expect()}, and {@code setError()} manage lookahead and validation. 
 * The {@code parseFunctionCall()} method handles arbitrary-arity functions by 
 * recursively parsing comma-separated arguments until a closing parenthesis is found.</p>
 *
 * @see Tokenizer
 * @see NodeFactory
 * @see ParseResult
 * @see ParseState
 * @see ExpressionNode
 * @see ExpressionOperator
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
    public static ParseResult parse(String input, Map<String,ExpressionType> symbolTable) {
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
        ExpressionNode node = parseExpression(state, symbolTable);

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

    private static ExpressionNode parseExpression(ParseState s, Map<String,ExpressionType> symbolTable) {
        return parseLogicalOr(s, symbolTable);
    }

    private static ExpressionNode parseLogicalOr(ParseState s, Map<String,ExpressionType> symbolTable) {
        ExpressionNode left = parseLogicalXor(s, symbolTable);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op && op.op() == ExpressionOperator.OR) {
                s.advance();
                ExpressionNode right = parseLogicalXor(s, symbolTable);
                if (s.hasError) return null;
                left = NodeFactory.buildBinaryNode(s, ExpressionOperator.OR, left, right);
                if (s.hasError) return null;
            } else {
                break;
            }
        }
        return left;
    }

    private static ExpressionNode parseLogicalXor(ParseState s, Map<String,ExpressionType> symbolTable) {
        ExpressionNode left = parseLogicalAnd(s, symbolTable);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op && op.op() == ExpressionOperator.XOR) {
                s.advance();
                ExpressionNode right = parseLogicalAnd(s, symbolTable);
                if (s.hasError) return null;
                left = NodeFactory.buildBinaryNode(s, ExpressionOperator.XOR, left, right);
                if (s.hasError) return null;
            } else {
                break;
            }
        }
        return left;
    }

    private static ExpressionNode parseLogicalAnd(ParseState s, Map<String,ExpressionType> symbolTable) {
        ExpressionNode left = parseComparison(s, symbolTable);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op && op.op() == ExpressionOperator.AND) {
                s.advance();
                ExpressionNode right = parseComparison(s, symbolTable);
                if (s.hasError) return null;
                left = NodeFactory.buildBinaryNode(s, ExpressionOperator.AND, left, right);
                if (s.hasError) return null;
            } else {
                break;
            }
        }
        return left;
    }

    private static ExpressionNode parseComparison(ParseState s, Map<String,ExpressionType> symbolTable) {
        ExpressionNode left = parseAdditive(s, symbolTable);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op) {
                ExpressionOperator oe = op.op();
                if (oe == ExpressionOperator.GT || oe == ExpressionOperator.GTE ||
                        oe == ExpressionOperator.LT || oe == ExpressionOperator.LTE ||
                        oe == ExpressionOperator.EQ || oe == ExpressionOperator.NEQ) {
                    s.advance();
                    ExpressionNode right = parseAdditive(s, symbolTable);
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

    private static ExpressionNode parseAdditive(ParseState s, Map<String,ExpressionType> symbolTable) {
        ExpressionNode left = parseMultiplicative(s, symbolTable);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op) {
                if (op.op() == ExpressionOperator.PLUS || op.op() == ExpressionOperator.MINUS) {
                    s.advance();
                    ExpressionNode right = parseMultiplicative(s, symbolTable);
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

    private static ExpressionNode parseMultiplicative(ParseState s, Map<String,ExpressionType> symbolTable) {
        ExpressionNode left = parseExponent(s, symbolTable);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op) {
                if (op.op() == ExpressionOperator.MULTIPLY || op.op() == ExpressionOperator.DIVIDE) {
                    s.advance();
                    ExpressionNode right = parseExponent(s, symbolTable);
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

    private static ExpressionNode parseExponent(ParseState s, Map<String,ExpressionType> symbolTable) {
        ExpressionNode left = parseUnary(s, symbolTable);
        if (s.hasError) return null;
        while (!s.hasError) {
            Token t = peek(s);
            if (t instanceof Token.Operator op) {
                if (op.op() == ExpressionOperator.POW) {
                    s.advance();
                    ExpressionNode right = parseUnary(s, symbolTable);
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

    private static ExpressionNode parseUnary(ParseState s, Map<String,ExpressionType> symbolTable) {
        Token t = peek(s);

        // Unary minus, Tokenizer can't tell difference between ExpressionOperator.MINUS and ExpressionOperator.NEGATE, repeated code necessary
        if (t instanceof Token.Operator op && (op.op() == ExpressionOperator.MINUS)) {
            s.advance();
            ExpressionNode child = parseUnary(s, symbolTable);
            if (s.hasError) return null;
            return NodeFactory.buildUnaryNode(s, ExpressionOperator.NEGATE, child);
        }
        if (t instanceof Token.Operator op && op.op() == ExpressionOperator.NOT) {
            s.advance();
            ExpressionNode child = parseUnary(s, symbolTable);
            if (s.hasError) return null;
            return NodeFactory.buildUnaryNode(s, op.op(), child);
        }


        // Prefix functions: ABS, FLOOR, CEILING
        if (t instanceof Token.Function fn) {
            if (fn.op() == ExpressionOperator.ABS || fn.op() == ExpressionOperator.FLOOR || fn.op() == ExpressionOperator.CEILING
            || fn.op() == ExpressionOperator.DOUBLECOERSION || fn.op() == ExpressionOperator.INTCOERSION) {
                s.advance();
                // Check for functional syntax ABS(x) or prefix syntax ABS x
                if (peek(s) instanceof Token.LeftParen) {
                    s.advance();
                    ExpressionNode child = parseExpression(s, symbolTable);
                    if (s.hasError) return null;
                    expect(s, Token.RightParen.class, "')'");
                    if (s.hasError) return null;
                    return NodeFactory.buildUnaryNode(s, fn.op(), child);
                } else {
                    ExpressionNode child = parseUnary(s, symbolTable);
                    if (s.hasError) return null;
                    return NodeFactory.buildUnaryNode(s, fn.op(), child);
                }
            }
        }
        
        // Handle operator syntax for ABS/FLOOR/CEILING if defined as operators
        if (t instanceof Token.Operator op2) {
            if (op2.op() == ExpressionOperator.ABS || op2.op() == ExpressionOperator.FLOOR || op2.op() == ExpressionOperator.CEILING) {
                s.advance();
                ExpressionNode child = parseUnary(s, symbolTable);
                if (s.hasError) return null;
                return NodeFactory.buildUnaryNode(s, op2.op(), child);
            }
        }

        return parsePrimary(s, symbolTable);
    }

    private static ExpressionNode parsePrimary(ParseState s, Map<String,ExpressionType> symbolTable) {
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
            if (symbolTable.containsKey(var.name())){
                ExpressionType et = symbolTable.get(var.name());
                if(et==ExpressionType.DOUBLE){
                    return new DoubleVariableNode(var.name());
                }else if(et==ExpressionType.INTEGER){
                    return new IntegerVariableNode(var.name());
                }else if(et==ExpressionType.DATE){
                    return new DateTimeVariableNode(var.name());
                }else if(et==ExpressionType.BOOLEAN){
                    return new BooleanVariableNode(var.name());
                }else if(et==ExpressionType.STRING){
                    return new StringVariableNode(var.name());
                }else{
                    setError(s, currentPos(s), "variable name " + var.name() + " type not recognized", "");
                    return null;
                }
                
            }else{
                setError(s, currentPos(s), "variable name " + var.name() + " not found", "");
                return null;
            }

        }
        if (t instanceof Token.StringLiteral sl) {
            return new StringConstantNode(sl.value());
        }
        if (t instanceof Token.BooleanLiteral bl) {
            return new BooleanConstantNode(bl.value());
        }
        if (t instanceof Token.LeftParen) {
            ExpressionNode node = parseExpression(s, symbolTable);
            if (s.hasError) return null;
            expect(s, Token.RightParen.class, "')'");
            if (s.hasError) return null;
            return node;
        }
        if (t instanceof Token.Function fn) {
            return parseFunctionCall(s, fn.op(), symbolTable);
        }

        setError(s, t.position(), "Unexpected token: " + t, remainingInput(s.input, t.position()));
        return null;
    }

    private static ExpressionNode parseFunctionCall(ParseState s, ExpressionOperator fn, Map<String,ExpressionType> symbolTable) {
        expect(s, Token.LeftParen.class, "'(' after function name");
        if (s.hasError) return null;

        List<ExpressionNode> args = new ArrayList<>();

        if (peek(s) instanceof Token.RightParen) {
            s.advance();
            return NodeFactory.buildFunctionNode(s, fn, args);
        }

        args.add(parseExpression(s, symbolTable));
        if (s.hasError) return null;

        while (!s.hasError && peek(s) instanceof Token.Comma) {
            s.advance();
            args.add(parseExpression(s, symbolTable));
            if (s.hasError) return null;
        }

        expect(s, Token.RightParen.class, "')' to close function call");
        if (s.hasError) return null;

        return NodeFactory.buildFunctionNode(s, fn, args);
    }

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------

    private static Token peek(ParseState s) {
        return (s.pos < s.tokens.size()) ? s.tokens.get(s.pos) : null;
    }

    private static int currentPos(ParseState s) {
        Token t = peek(s);
        return (t != null) ? t.position() : -1;
    }

    private static void expect(ParseState s, Class<?> expectedClass, String description) {
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

    private static void setError(ParseState s, int position, String message, String remaining) {
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