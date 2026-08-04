package usace.hec.expressions;

import usace.hec.expressions.comparison.*;
import usace.hec.expressions.logical.*;
import usace.hec.expressions.math.*;
import usace.hec.expressions.strings.*;
import usace.hec.expressions.time.*;

import java.util.ArrayList;
import java.util.List;

import static usace.hec.expressions.ExpressionOperator.NOT;

/**
 * Static factory for constructing typed {@link ExpressionNode} instances during parsing.
 *
 * <p>Translates abstract {@link ExpressionOperator} enums and raw operand nodes into 
 * concrete, type-safe expression tree nodes. The factory enforces type compatibility, 
 * applies automatic widening coercion, and delegates to specialized node classes in the 
 * {@code math}, {@code logical}, {@code comparison}, {@code strings}, and {@code time} 
 * sub-packages.</p>
 *
 * <h3>Type Resolution Strategy</h3>
 * <ul>
 *   <li><b>Binary Operators:</b> Validates operand compatibility via {@link #isCompatible}. 
 *       If types differ but widening is possible, wraps the narrower operand in 
 *       {@link IntegerToDoubleCoerceNode}. Narrowing is rejected.</li>
 *   <li><b>Unary Operators:</b> Dispatches based on the child node's {@link ExpressionType}. 
 *       Supports numeric, boolean, date, and string unary operations.</li>
 *   <li><b>IF Function:</b> Requires a boolean condition. Promotes the {@code then} and 
 *       {@code else} branches to a common compatible type before construction.</li>
 *   <li><b>MAX/MIN Functions:</b> Scans all arguments to find the widest type, coerces 
 *       every argument to that type, and builds a left-associative operator chain.</li>
 *   <li><b>DATE Function:</b> Explicitly coerces all three arguments to {@code int}.</li>
 *   <li><b>SUBSTRING Function:</b> Requires a string source and coerces index arguments 
 *       to {@code int} if they are {@code double}.</li>
 * </ul>
 *
 * <h3>Error Handling</h3>
 * <p>All factory methods accept an {@link ExpressionParser.ParseState} to record type 
 * mismatches, invalid argument counts, or unsupported operations. On error, the factory 
 * sets the state flags and returns {@code null} (or a placeholder constant node for 
 * unary switch fallbacks).</p>
 *
 * @see ExpressionParser
 * @see ExpressionOperator
 * @see ExpressionType
 * @see IntegerToDoubleCoerceNode
 */
public class NodeFactory {
    /**
     * Constructs a ternary expression node based on the child's type and the operator.
     *
     * <p>Uses if statements to map operator + type combinations to concrete node
     * classes. Supports if operations ({@code IF}, date operations
     * ({@code DATE}), and string operations ({@code SUBSTRING}, {@code REPLACE})
     * Unknown combinations record an error and return null.</p>
     *
     * @param s     the parse state for error tracking
     * @param op    the ternary operator or function
     * @param left the left node, usually a condition or source string
     * @param middle , usually a then
     * @param right , usually an else
     * @return a typed unary node, or {@code null} if types are incompatible
     */
        public static ExpressionNode buildTernaryNode(ExpressionParser.ParseState s, ExpressionOperator op, ExpressionNode left, ExpressionNode middle, ExpressionNode right) {
            //All arguments have been validated beforehand

            ExpressionNode lNode = left;
            ExpressionNode mNode = middle;
            ExpressionNode rNode = right;

            //Instantiate specialized node
            ExpressionType commonType = mNode.resultType();
            if (op == ExpressionOperator.IF) {
                if (commonType == ExpressionType.DOUBLE) {
                    return new DoubleIfNode((BooleanExpressionNode) lNode, (DoubleExpressionNode) mNode, (DoubleExpressionNode) rNode);
                } else if (commonType == ExpressionType.INTEGER) {
                    return new IntegerIfNode((BooleanExpressionNode) lNode,(IntegerExpressionNode) mNode, (IntegerExpressionNode) rNode);
                } else if (commonType == ExpressionType.BOOLEAN) {
                    return new BooleanIfNode((BooleanExpressionNode) lNode, (BooleanExpressionNode) mNode, (BooleanExpressionNode) rNode);
                } else if (commonType == ExpressionType.DATE) {
                    return new DateTimeIfNode((BooleanExpressionNode) lNode, (DateTimeExpressionNode) mNode, (DateTimeExpressionNode) rNode);
                } else if (commonType == ExpressionType.STRING) {
                    return new StringIfNode((BooleanExpressionNode) lNode, (StringExpressionNode) mNode, (StringExpressionNode) rNode);
                }
                setError(s, currentPos(s), "Ternary " + op + " not implemented for " + commonType, "");
                return null;
            }
            else if (op == ExpressionOperator.SUBSTRING){

                return new SubstringNode((StringExpressionNode) lNode, (IntegerExpressionNode) mNode, (IntegerExpressionNode) rNode);
            }
            else if (op == ExpressionOperator.REPLACE){
                return new ReplaceNode((StringExpressionNode) lNode, (StringExpressionNode) mNode, (StringExpressionNode) rNode);
            }
            else if (op == ExpressionOperator.DATE){
                return new DateNode((IntegerExpressionNode) lNode, (IntegerExpressionNode) mNode, (IntegerExpressionNode) rNode);
            }

            setError(s, currentPos(s), "Unsupported ternary operator type: " + commonType, "");
            return null;
        }
    /**
     * Constructs a binary expression node with type validation and coercion.
     *
     * <p>Checks that both operands are compatible with the operator. If types differ but
     * widening is supported (e.g., {@code int} and {@code double}), wraps the narrower
     * operand in a coercion node. Dispatches to the appropriate typed factory method
     * based on the resolved common type.</p>
     *
     * @param s      the parse state for error tracking
     * @param op     the binary operator
     * @param left   the left operand node
     * @param right  the right operand node
     * @return a typed binary node, or {@code null} if types are incompatible
     * @see #createDoubleBinaryNode
     * @see #createIntegerBinaryNode
     * @see #createBooleanBinaryNode
     * @see #createDateBinaryNode
     * @see #createStringBinaryNode
     */
    public static ExpressionNode buildBinaryNode(ExpressionParser.ParseState s, ExpressionOperator op, ExpressionNode left, ExpressionNode right) {
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
        } else if (commonType == ExpressionType.STRING){
            return createStringBinaryNode(op, (StringExpressionNode) lNode, (StringExpressionNode) rNode);
        }

        setError(s, currentPos(s), "Unsupported binary operator type: " + commonType, "");
        return null;
    }
    /**
     * Constructs a unary expression node based on the child's type and the operator.
     *
     * <p>Uses switch expressions to map operator + type combinations to concrete node 
     * classes. Supports numeric operations ({@code NEGATE}, {@code ABS}, {@code FLOOR}, 
     * {@code CEILING}), type coercion ({@code TOINT}, {@code TODOUBLE}), date operations 
     * ({@code DOY}), and string operations ({@code LENGTH}, {@code LOWER}, {@code UPPER}, 
     * {@code TRIM}). Unknown combinations record an error and return a typed placeholder.</p>
     *
     * @param s     the parse state for error tracking
     * @param op    the unary operator or function
     * @param child the operand node
     * @return a typed unary node, or a constant placeholder on error
     */
    public static ExpressionNode buildUnaryNode(ExpressionParser.ParseState s, ExpressionOperator op, ExpressionNode child) {
        ExpressionType type = child.resultType();

        if (type == ExpressionType.DOUBLE) {
            return switch (op) {
                case NEGATE -> new DoubleNegateNode((DoubleExpressionNode) child);
                case ABS -> new DoubleAbsNode((DoubleExpressionNode) child);
                case FLOOR -> new DoubleFloorNode((DoubleExpressionNode) child);
                case CEILING -> new DoubleCeilingNode((DoubleExpressionNode) child);
                case INTCOERSION -> new DoubleToIntegerCoerceNode((DoubleExpressionNode) child);
                case DOUBLECOERSION -> child;
                default -> { setError(s, currentPos(s), "Unknown unary operator: " + op, ""); yield new DoubleConstantNode(0.0);}
            };
        } else if (type == ExpressionType.INTEGER) {
            return switch (op) {
                case NEGATE -> new IntegerNegateNode((IntegerExpressionNode) child);
                case ABS -> new IntegerAbsNode((IntegerExpressionNode) child);
                case FLOOR -> new IntegerFloorNode((IntegerExpressionNode) child);
                case CEILING -> new IntegerCeilingNode((IntegerExpressionNode) child);
                case DOUBLECOERSION -> new IntegerToDoubleCoerceNode((IntegerExpressionNode) child);
                case INTCOERSION -> child;
                default -> { setError(s, currentPos(s), "Unary " + op + " not implemented for Int", ""); yield new IntegerConstantNode(0); }
            };
        } else if (type == ExpressionType.DATE){
            return switch (op) {
                case DOY -> new DayOfYearNode((DateTimeExpressionNode)child);
                case DOM -> new DayOfMonthNode((DateTimeExpressionNode)child);
                case YEAR -> new CalendarYearNode((DateTimeExpressionNode)child);
                case WATERYEAR -> new WaterYearNode((DateTimeExpressionNode)child);
                case LEAPYEAR -> new LeapYearNode((DateTimeExpressionNode)child);
                case MONTH -> new MonthNode((DateTimeExpressionNode)child);
                default -> { setError(s, currentPos(s), "Unary " + op + " not implemented for DATE", ""); yield new IntegerConstantNode(0); }
            };
        } else if (type == ExpressionType.STRING){
            return switch (op) {
              case LENGTH -> new StringLengthNode((StringExpressionNode) child);
              case LOWER -> new ToLowerNode((StringExpressionNode) child);
              case UPPER -> new ToUpperNode((StringExpressionNode) child);
              case TRIM -> new TrimNode((StringExpressionNode) child);
              default-> {setError(s, currentPos(s), "Unary " + op + " not implemented for STRING", ""); yield new StringConstantNode(""); }
            };
        } else if (type == ExpressionType.BOOLEAN) {
            if (op == NOT){
                return new NotNode((BooleanExpressionNode) child);
            }
            setError(s, currentPos(s), "Unary " + op + " not implemented for BOOLEAN", "");
            return null;
        }

        setError(s, currentPos(s), "Unary operator " + op + " not supported for " + type, "");
        return null;
    }
    /**
     * Constructs a function call node with argument validation and type promotion.
     *
     * <p>Handles all built-in functions by dispatching on the {@link ExpressionOperator}. 
     * Validates argument counts, checks operand types, and applies coercion where required. 
     * Special handling includes:</p>
     * <ul>
     *   <li>{@code IF}: Exactly 3 args. Condition must be boolean. Branches are promoted 
     *       to a common type.</li>
     *   <li>{@code MAX}/{@code MIN}: Variable arity. All args promoted to the widest type. 
     *       Builds a left-associative chain of binary nodes.</li>
     *   <li>{@code DATE}: Exactly 3 args. All coerced to {@code int}.</li>
     *   <li>{@code SUBSTRING}: Exactly 3 args. Source must be string. Indices coerced to {@code int}.</li>
     *   <li>{@code REPLACE}, {@code CONCAT}, {@code CONTAINS}, {@code STARTSWITH}, {@code ENDSWITH}: 
     *       Require string arguments.</li>
     *   <li>{@code TODAY}, {@code DOY}, {@code NEGATE}, {@code ABS}, {@code FLOOR}, {@code CEILING}: 
     *       Delegated to {@link #buildUnaryNode} or handled directly.</li>
     *   <li>Standard binary operators ({@code PLUS}, {@code GT}, {@code AND}, etc.): 
     *       Delegated to {@link #buildBinaryNode}.</li>
     * </ul>
     *
     * @param s    the parse state for error tracking
     * @param fn   the function operator
     * @param args the parsed argument nodes
     * @return the constructed function node, or {@code null} on validation failure
     */
    public static ExpressionNode buildFunctionNode(ExpressionParser.ParseState s, ExpressionOperator fn, List<ExpressionNode> args) {
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

                return buildTernaryNode(s, fn, cond, promotedThen, promotedElse);
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
                                ? new DoubleMaxNode((DoubleExpressionNode) result, (DoubleExpressionNode) promotedArgs.get(i))
                                : new DoubleMinNode((DoubleExpressionNode) result, (DoubleExpressionNode) promotedArgs.get(i));
                    }
                    // Add Int support if IntMaxNode/IntMinNode exist
                    if (commonType == ExpressionType.INTEGER) {
                        result = (fn == ExpressionOperator.MAX)
                                ? new IntegerMaxNode((IntegerExpressionNode) result, (IntegerExpressionNode) promotedArgs.get(i))
                                : new IntegerMinNode((IntegerExpressionNode) result, (IntegerExpressionNode) promotedArgs.get(i));
                    }
                }
                return result;
            }
            case NOT:
            case NEGATE:
            case ABS:
            case FLOOR:
            case CEILING: {
                if (args.size() != 1) {
                    setError(s, currentPos(s), fn.name() + " requires exactly 1 argument", "");
                    return null;
                }
                return buildUnaryNode(s, fn, args.get(0));
            }
            //Time Operators
            case TODAY:
                if (!args.isEmpty()) {
                    setError(s, currentPos(s), "TODAY() takes no arguments", "");
                    return null;
                }
                return new TodayNode();

            case DOY:
            case DOM:
            case YEAR:
            case WATERYEAR:
            case LEAPYEAR:
            case MONTH:
                if (args.size() != 1) {
                    setError(s, currentPos(s), fn.name() + "expects exactly 1 argument", "");
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

                if (y == null || m == null || d == null) {
                    setError(s, currentPos(s), "All arguments must be numeric", "");
                    return null;
                }

                return buildTernaryNode(s, fn, y, m, d);
            }

            // Delegate standard binary operators
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
            case NEQ:
            case AND:
            case OR:
            case XOR:
                if (args.size() != 2) {
                    setError(s, currentPos(s), fn.name() + " requires exactly 2 arguments", "");
                    return null;
                }
                return buildBinaryNode(s, fn, args.get(0), args.get(1));
            //Delegate string operators
            case SUBSTRING: {
                if (args.size() != 3) {
                    setError(s, currentPos(s), fn.name() + "requires exactly 3 arguments", "");
                    return null;
                }
                if (args.get(0).resultType() != ExpressionType.STRING){
                    setError(s, currentPos(s), fn.name() + "first argument must be string", "");
                    return null;
                }
                //SUBSTRING requires integers, coerce if necessary
                ExpressionNode start = coerceTo(args.get(1), ExpressionType.INTEGER);
                ExpressionNode end = coerceTo(args.get(2), ExpressionType.INTEGER);
                if (start == null || end == null) {
                    setError(s, currentPos(s), "startIndex and endIndex should be numeric", "");
                    return null;
                }

                return buildTernaryNode(s,fn, args.get(0), start, end);
            }
            case REPLACE: {
                if (args.size() != 3) {
                    setError(s, currentPos(s), fn.name() + "requires exactly 3 arguments", "");
                    return null;
                }
                ExpressionNode sourceStr = args.get(0);
                ExpressionNode target = args.get(1);
                ExpressionNode replacement = args.get(2);

                if (sourceStr.resultType() != ExpressionType.STRING || target.resultType() != ExpressionType.STRING || replacement.resultType() != ExpressionType.STRING) {
                    setError(s, currentPos(s), "All args must be String for REPLACE", "");
                    return null;
                }
                return buildTernaryNode(s, fn, sourceStr, target, replacement);
            }

            case CONCAT: case CONTAINS: case STARTSWITH: case ENDSWITH: {
                if (args.size() != 2) {
                    setError(s, currentPos(s), fn.name() + "requires exactly 2 arguments", "");
                    return null;
                }
                return buildBinaryNode(s, fn, args.get(0), args.get(1));
            }
            case LENGTH: case LOWER: case UPPER: case TRIM: {
                if (args.size() != 1) {
                    setError(s, currentPos(s), fn.name() + "requires exactly 1 arguments", "");
                    return null;
                }
                return buildUnaryNode(s, fn, args.get(0));
            }




            default:
                setError(s, currentPos(s), "Unknown function: " + fn.name(), "");
                return null;
        }
    }

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------
    /**
     * Coerces a node to a target type when supported.
     * Handles {@code int} → {@code double} (widening) and {@code double} → {@code int} 
     * (narrowing, used for {@code DATE} and {@code SUBSTRING} indices).
     */
    private static ExpressionNode coerceTo(ExpressionNode node, ExpressionType targetType) {
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
    /**
     * Checks whether an operator supports the given operand types.
     * Returns true if types match, or if the operator supports mixed numeric/boolean/date types.
     */
    private static boolean isCompatible(ExpressionOperator op, ExpressionType l, ExpressionType r) {
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
        // Strings
        if ((op == ExpressionOperator.SUBSTRING) &&
                l.isNumeric() && r.isNumeric()) {
            return true;
        }
        return false;
    }

    /**
     * Type-specific dispatch methods that map operators to concrete node implementations.
     * Each returns a switch expression yielding the appropriate typed node or {@code null}.
     */
    private static ExpressionNode createDoubleBinaryNode(ExpressionOperator op, DoubleExpressionNode left, DoubleExpressionNode right) {
        return switch (op) {
            case PLUS -> new DoubleAddNode(left, right);
            case MINUS -> new DoubleMinusNode(left, right);
            case MULTIPLY -> new DoubleMultiplyNode(left, right);
            case DIVIDE -> new DoubleDivideNode(left, right);
            case POW -> new DoubleExponentNode(left, right);
            case MAX -> new DoubleMaxNode(left, right);
            case MIN -> new DoubleMinNode(left, right);
            case EQ -> new DoubleEqualToNode(left, right);
            case NEQ -> new DoubleNotEqualToNode(left,right);
            case GT -> new DoubleGreaterThanNode(left, right);
            case GTE -> new DoubleGreaterThanOrEqualNode(left, right);
            case LT -> new DoubleLessThanNode(left, right);
            case LTE -> new DoubleLessThanOrEqualNode(left, right);
            default -> null;
        };
    }

    private static ExpressionNode createIntegerBinaryNode(ExpressionOperator op, IntegerExpressionNode left, IntegerExpressionNode right) {
        return switch (op) {
            case PLUS -> new IntegerAddNode(left, right);
            case MINUS -> new IntegerMinusNode(left, right);
            case MULTIPLY -> new IntegerMultiplyNode(left, right);
            case DIVIDE -> new IntegerDivideNode(left, right);
            case POW -> new IntegerExponentNode(left, right);
            case MAX -> new IntegerMaxNode(left, right);
            case MIN -> new IntegerMinNode(left, right);
            case EQ -> new IntegerEqualToNode(left, right);
            case NEQ -> new IntegerNotEqualToNode(left, right);
            case GT -> new IntegerGreaterThanNode(left, right);
            case GTE -> new IntegerGreaterThanOrEqualNode(left, right);
            case LT -> new IntegerLessThanNode(left, right);
            case LTE -> new IntegerLessThanOrEqualNode(left, right);
            default -> null;
        };
    }

    private static ExpressionNode createBooleanBinaryNode(ExpressionOperator op, BooleanExpressionNode left, BooleanExpressionNode right) {
        return switch (op) {
            case AND -> new AndNode(left, right);
            case OR -> new OrNode(left, right);
            case XOR -> new XorNode(left, right);
            case EQ -> new BooleanEqualToNode(left,right);
            case NEQ -> new BooleanNotEqualToNode(left,right);
            default -> null;
        };
    }
    private static ExpressionNode createDateBinaryNode(ExpressionOperator op, DateTimeExpressionNode left, DateTimeExpressionNode right) {
        return switch (op) {
            case EQ -> new SameDateNode(left, right);
            case NEQ -> new NotSameDateNode(left, right);
            case AFTER -> new AfterNode(left, right);
            case BEFORE -> new BeforeNode(left, right);
            default -> null;
        };
    }

    private static ExpressionNode createStringBinaryNode(ExpressionOperator op, StringExpressionNode left, StringExpressionNode right) {
        return switch (op) {
            case EQ -> new StringEqualToNode(left, right);
            case NEQ -> new StringNotEqualToNode(left, right);
            case CONCAT -> new ConcatenateNode(left, right);
            case CONTAINS -> new ContainsNode(left, right);
            case STARTSWITH -> new StartsWithNode(left, right);
            case ENDSWITH -> new EndsWithNode(left,right);
            default -> null;
        };
    }

    private static Token peek(ExpressionParser.ParseState s) {
        return (s.pos < s.tokens.size()) ? s.tokens.get(s.pos) : null;
    }

    private static int currentPos(ExpressionParser.ParseState s) {
        Token t = peek(s);
        return (t != null) ? t.position() : -1;
    }

    private static void setError(ExpressionParser.ParseState s, int position, String message, String remaining) {
        if (s != null) {
            s.hasError = true;
            s.errorPosition = position;
            s.errorMessage = message;
            s.errorRemaining = remaining;
        }
    }
}
