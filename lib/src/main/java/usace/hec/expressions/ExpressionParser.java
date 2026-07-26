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
import usace.hec.expressions.logical.AndNode;
import usace.hec.expressions.logical.DoubleIfNode;
import usace.hec.expressions.logical.IntegerIfNode;
import usace.hec.expressions.logical.OrNode;
import usace.hec.expressions.logical.XorNode;
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
 *   <li>Widening coercion (e.g., int -> double) is applied automatically via {@link IntToDoubleCoerceNode}.</li>
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
                left = buildBinaryNode(s, ExpressionOperator.OR, left, right);
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
                left = buildBinaryNode(s, ExpressionOperator.XOR, left, right);
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
                left = buildBinaryNode(s, ExpressionOperator.AND, left, right);
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
                    left = buildBinaryNode(s, oe, left, right);
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
                    left = buildBinaryNode(s, op.op(), left, right);
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
                    left = buildBinaryNode(s, op.op(), left, right);
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
            return buildBinaryNode(s, ExpressionOperator.POW, left, right);
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
            return buildUnaryNode(s, ExpressionOperator.NEGATE, child);
        }

        // Prefix functions: ABS, FLOOR, CEILING
        if (t instanceof Token.Function fn) {
            if (fn.op() == ExpressionOperator.ABS || fn.op() == ExpressionOperator.FLOOR || fn.op() == ExpressionOperator.CEILING) {
                s.advance();
                // Check for functional syntax ABS(x) or prefix syntax ABS x
                if (peek(s) instanceof Token.LeftParen) {
                    s.advance();
                    ExpressionNode child = parseExpression(s);
                    if (s.hasError) return null;
                    expect(s, Token.RightParen.class, "')'");
                    if (s.hasError) return null;
                    return buildUnaryNode(s, fn.op(), child);
                } else {
                    ExpressionNode child = parseUnary(s);
                    if (s.hasError) return null;
                    return buildUnaryNode(s, fn.op(), child);
                }
            }
        }
        
        // Handle operator syntax for ABS/FLOOR/CEILING if defined as operators
        if (t instanceof Token.Operator op2) {
            if (op2.op() == ExpressionOperator.ABS || op2.op() == ExpressionOperator.FLOOR || op2.op() == ExpressionOperator.CEILING) {
                s.advance();
                ExpressionNode child = parseUnary(s);
                if (s.hasError) return null;
                return buildUnaryNode(s, op2.op(), child);
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
    // AST node factory helpers (Type-Safe)
    // -----------------------------------------------------------------

    private ExpressionNode buildBinaryNode(ParseState s, ExpressionOperator op, ExpressionNode left, ExpressionNode right) {
        // 1. Validate compatibility
        if (!isCompatible(op, left.resultType(), right.resultType())) {
            setError(s, currentPos(s), "Type mismatch for " + op + ": " + left.resultType() + " and " + right.resultType(), "");
            return null;
        }

        // 2. Promote types (Widening)
        ExpressionNode lNode = left;
        ExpressionNode rNode = right;
        
        if (left.resultType() != right.resultType()) {
            if (ExpressionType.canWiden(left.resultType(), right.resultType())) {
                lNode = new IntegerToDoubleCoerceNode((IntegerExpressionNode) left);
            } else if (ExpressionType.canWiden(right.resultType(), left.resultType())) {
                rNode = new IntegerToDoubleCoerceNode((IntegerExpressionNode) right);
            } else {
                // Narrowing not allowed automatically
                setError(s, currentPos(s), "Cannot mix " + left.resultType() + " and " + right.resultType() + " without explicit cast", "");
                return null;
            }
        }

        // 3. Instantiate specialized node
        ExpressionType commonType = lNode.resultType();
        
        if (commonType == ExpressionType.DOUBLE) {
            return createDoubleBinaryNode(op, (DoubleExpressionNode) lNode, (DoubleExpressionNode) rNode);
        } else if (commonType == ExpressionType.INTEGER) {
            return createIntegerBinaryNode(op, (IntegerExpressionNode) lNode, (IntegerExpressionNode) rNode);
        } else if (commonType == ExpressionType.BOOLEAN) {
            return createBooleanBinaryNode(op, (BooleanExpressionNode) lNode, (BooleanExpressionNode) rNode);
        } else if (commonType == ExpressionType.DATE){
            return createDateBinaryNode(op,(DateTimeExpressionNode)lNode,(DateTimeExpressionNode) rNode);
        }

        setError(s, currentPos(s), "Unsupported binary operator type: " + commonType, "");
        return null;
    }

    private ExpressionNode buildUnaryNode(ParseState s, ExpressionOperator op, ExpressionNode child) {
        ExpressionType type = child.resultType();
        
        if (type == ExpressionType.DOUBLE) {
            return switch (op) {
                case NEGATE -> new DoubleNegateNode((DoubleExpressionNode) child);
                case ABS -> new DoubleAbsNode((DoubleExpressionNode) child);
                case FLOOR -> new DoubleFloorNode((DoubleExpressionNode) child);
                case CEILING -> new DoubleCeilingNode((DoubleExpressionNode) child);
                default -> { setError(s, currentPos(s), "Unknown unary operator: " + op, ""); yield new DoubleConstantNode(0.0);}
            };
        } else if (type == ExpressionType.INTEGER) {
            return switch (op) {
                case NEGATE -> new IntegerNegateNode((IntegerExpressionNode) child);
                case ABS -> new IntegerAbsNode((IntegerExpressionNode) child);
                case FLOOR -> new IntegerFloorNode((IntegerExpressionNode) child);
                //case CEILING -> new IntegerCeilingNode((DoubleExpressionNode) child);
                default -> { setError(s, currentPos(s), "Unary " + op + " not implemented for Int", ""); yield new IntegerConstantNode(0); }
            };
        } else if (type == ExpressionType.DATE){
            return switch (op) {
                case DOY -> new DayOfYearNode((DateTimeExpressionNode)child);
                default -> { setError(s, currentPos(s), "Unary " + op + " not implemented for DATE", ""); yield new IntegerConstantNode(0); }
            };
        }

        setError(s, currentPos(s), "Unary operator " + op + " not supported for " + type, "");
        return null;
    }

    private ExpressionNode buildFunctionNode(ParseState s, ExpressionOperator fn, List<ExpressionNode> args) {
        switch (fn) {
            case IF: {
                if (args.size() != 3) {
                    setError(s, currentPos(s), "IF requires exactly 3 arguments, got " + args.size(), "");
                    return null;
                }
                ExpressionNode cond = args.get(0);
                ExpressionNode then = args.get(1);
                ExpressionNode elseExpr = args.get(2);

                if (cond.resultType() != ExpressionType.BOOLEAN) {
                    setError(s, currentPos(s), "IF condition must be Boolean", "");
                    return null;
                }

                // Promote branches to match
                ExpressionNode promotedThen = then;
                ExpressionNode promotedElse = elseExpr;
                ExpressionType resultType = then.resultType();

                if (resultType != elseExpr.resultType()) {
                    if (ExpressionType.canWiden(resultType, elseExpr.resultType())) {
                        resultType = elseExpr.resultType();
                        promotedThen = coerceTo(then, resultType);
                    } else if (ExpressionType.canWiden(elseExpr.resultType(), resultType)) {
                        promotedElse = coerceTo(elseExpr, resultType);
                    } else {
                        setError(s, currentPos(s), "IF branches must have compatible types: " + resultType + " vs " + elseExpr.resultType(), "");
                        return null;
                    }
                }

                if (promotedThen == null || promotedElse == null) return null; // Coercion error

                if (resultType == ExpressionType.DOUBLE) {
                    return new DoubleIfNode((BooleanExpressionNode) cond, (DoubleExpressionNode) promotedThen, (DoubleExpressionNode) promotedElse);
                } else if (resultType == ExpressionType.INTEGER) {
                    return new IntegerIfNode((BooleanExpressionNode) cond, (IntegerExpressionNode) promotedThen, (IntegerExpressionNode) promotedElse);
                }
                
                setError(s, currentPos(s), "IF branch type " + resultType + " not supported", "");
                return null;
            }

            case MAX:
            case MIN: {
                if (args.isEmpty()) {
                    setError(s, currentPos(s), fn.name() + " requires at least 1 argument", "");
                    return null;
                }
                
                // Promote all args to the widest type
                ExpressionType commonType = args.get(0).resultType();
                List<ExpressionNode> promotedArgs = new ArrayList<>();
                
                for (ExpressionNode arg : args) {
                    ExpressionType argType = arg.resultType();
                    if (ExpressionType.canWiden(commonType, argType)) {
                        commonType = argType; // Found wider type
                    }
                }
                
                for (ExpressionNode arg : args) {
                    ExpressionNode coerced = coerceTo(arg, commonType);
                    if (coerced == null) return null;
                    promotedArgs.add(coerced);
                }
                
                // Build tree
                ExpressionNode result = promotedArgs.get(0);
                for (int i = 1; i < promotedArgs.size(); i++) {
                    if (commonType == ExpressionType.DOUBLE) {
                        result = (fn == ExpressionOperator.MAX) 
                            ? new DoubleMaxNode((DoubleExpressionNode)result, (DoubleExpressionNode)promotedArgs.get(i))
                            : new DoubleMinNode((DoubleExpressionNode)result, (DoubleExpressionNode)promotedArgs.get(i));
                    }
                    // Add Int support if IntMaxNode/IntMinNode exist
                }
                return result;
            }

            case NEGATE: case ABS: case FLOOR: case CEILING: {
                if (args.size() != 1) {
                    setError(s, currentPos(s), fn.name() + " requires exactly 1 argument", "");
                    return null;
                }
                return buildUnaryNode(s, fn, args.get(0));
            }

            case TODAY:
                if (!args.isEmpty()) {
                    setError(s, currentPos(s), "TODAY() takes no arguments", "");
                    return null;
                }
                return new TodayNode();

            case DOY:
                if (args.size() != 1) {
                    setError(s, currentPos(s), "DOY expects exactly 1 argument", "");
                    return null;
                }
                return buildUnaryNode(s, fn, args.get(0));

            case AFTER:
            case BEFORE:
                if (args.size() != 2) {
                    setError(s, currentPos(s), fn.name() + " expects exactly 2 arguments", "");
                    return null;
                }
                return buildBinaryNode(s, fn, args.get(0), args.get(1));

            case DATE: {
                if (args.size() != 3) {
                    setError(s, currentPos(s), "DATE expects exactly 3 arguments", "");
                    return null;
                }
                // DATE requires Integers. Coerce if necessary.
                ExpressionNode y = coerceTo(args.get(0), ExpressionType.INTEGER);
                ExpressionNode m = coerceTo(args.get(1), ExpressionType.INTEGER);
                ExpressionNode d = coerceTo(args.get(2), ExpressionType.INTEGER);
                
                if (y == null || m == null || d == null) return null;
                
                return new DateNode((IntegerExpressionNode) y, (IntegerExpressionNode) m, (IntegerExpressionNode) d);
            }

            // Delegate standard binary operators
            case PLUS: case MINUS: case MULTIPLY: case DIVIDE: case POW:
            case GT: case GTE: case LT: case LTE: case EQ:
            case AND: case OR: case XOR:
                if (args.size() != 2) {
                    setError(s, currentPos(s), fn.name() + " requires exactly 2 arguments", "");
                    return null;
                }
                return buildBinaryNode(s, fn, args.get(0), args.get(1));

            default:
                setError(s, currentPos(s), "Unknown function: " + fn.name(), "");
                return null;
        }
    }

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------

    private ExpressionNode coerceTo(ExpressionNode node, ExpressionType targetType) {
        ExpressionType current = node.resultType();
        if (current == targetType) return node;
        
        if (current == ExpressionType.INTEGER && targetType == ExpressionType.DOUBLE) {
            return new IntegerToDoubleCoerceNode((IntegerExpressionNode) node);
        }
        if (current == ExpressionType.DOUBLE && targetType == ExpressionType.INTEGER) {
            // Narrowing coercion for specific use cases like DATE
            return new DoubleToIntegerCoerceNode((DoubleExpressionNode) node);
        }
        
        setError(null, -1, "Cannot coerce " + current + " to " + targetType, ""); // Use null state for helper errors
        return null;
    }

    private boolean isCompatible(ExpressionOperator op, ExpressionType l, ExpressionType r) {
        if (l == r) return true;
        // Numeric binary ops
        if ((op == ExpressionOperator.PLUS || op == ExpressionOperator.MINUS || 
             op == ExpressionOperator.MULTIPLY || op == ExpressionOperator.DIVIDE || 
             op == ExpressionOperator.POW || op == ExpressionOperator.MAX || op == ExpressionOperator.MIN) &&
            l.isNumeric() && r.isNumeric()) {
            return true;
        }
        // Comparisons
        if ((op == ExpressionOperator.EQ || op == ExpressionOperator.GT || 
             op == ExpressionOperator.GTE || op == ExpressionOperator.LT || op == ExpressionOperator.LTE) &&
            l.isNumeric() && r.isNumeric()) {
            return true;
        }
        // Logical
        if ((op == ExpressionOperator.AND || op == ExpressionOperator.OR || op == ExpressionOperator.XOR) &&
            l == ExpressionType.BOOLEAN && r == ExpressionType.BOOLEAN) {
            return true;
        }
        return false;
    }

    private ExpressionNode createDoubleBinaryNode(ExpressionOperator op, DoubleExpressionNode left, DoubleExpressionNode right) {
        return switch (op) {
            case PLUS -> new DoubleAddNode(left, right);
            case MINUS -> new DoubleMinusNode(left, right);
            case MULTIPLY -> new DoubleMultiplyNode(left, right);
            case DIVIDE -> new DoubleDivideNode(left, right);
            case POW -> new DoubleExponentNode(left, right);
            case MAX -> new DoubleMaxNode(left, right);
            case MIN -> new DoubleMinNode(left, right);
            case EQ -> new DoubleEqualToNode(left, right);
            case GT -> new DoubleGreaterThanNode(left, right);
            case GTE -> new DoubleGreaterThanOrEqualNode(left, right);
            case LT -> new DoubleLessThanNode(left, right);
            case LTE -> new DoubleLessThanOrEqualNode(left, right);
            default -> null;
        };
    }

    private ExpressionNode createIntegerBinaryNode(ExpressionOperator op, IntegerExpressionNode left, IntegerExpressionNode right) {
        return switch (op) {
            case PLUS -> new IntegerAddNode(left, right);
            case MINUS -> new IntegerMinusNode(left, right);
            case MULTIPLY -> new IntegerMultiplyNode(left, right);
            case DIVIDE -> new IntegerDivideNode(left, right);
            case POW -> new IntegerExponentNode(left, right);
            case MAX -> new IntegerMaxNode(left, right);
            case MIN -> new IntegerMinNode(left, right);
            case EQ -> new IntegerEqualToNode(left, right);
            case GT -> new IntegerGreaterThanNode(left, right);
            case GTE -> new IntegerGreaterThanOrEqualNode(left, right);
            case LT -> new IntegerLessThanNode(left, right);
            case LTE -> new IntegerLessThanOrEqualNode(left, right);
            default -> null;
        };
    }

    private ExpressionNode createBooleanBinaryNode(ExpressionOperator op, BooleanExpressionNode left, BooleanExpressionNode right) {
        return switch (op) {
            case AND -> new AndNode(left, right);
            case OR -> new OrNode(left, right);
            case XOR -> new XorNode(left, right);
            default -> null;
        };
    }
    private ExpressionNode createDateBinaryNode(ExpressionOperator op, DateTimeExpressionNode left, DateTimeExpressionNode right) {
        return switch (op) {
            case AFTER -> new AfterNode(left, right);
            case BEFORE -> new BeforeNode(left, right);
            default -> null;
        };
    }
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