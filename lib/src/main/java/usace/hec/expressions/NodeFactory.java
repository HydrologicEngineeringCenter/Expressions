package usace.hec.expressions;

import usace.hec.expressions.comparison.*;
import usace.hec.expressions.logical.*;
import usace.hec.expressions.math.*;
import usace.hec.expressions.time.*;

import java.util.ArrayList;
import java.util.List;

public class NodeFactory {
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
        }

        setError(s, currentPos(s), "Unsupported binary operator type: " + commonType, "");
        return null;
    }

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
                default -> { setError(s, currentPos(s), "Unary " + op + " not implemented for DATE", ""); yield new IntegerConstantNode(0); }
            };
        }

        setError(s, currentPos(s), "Unary operator " + op + " not supported for " + type, "");
        return null;
    }

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

                if (resultType == ExpressionType.DOUBLE) {
                    return new DoubleIfNode((BooleanExpressionNode) cond, (DoubleExpressionNode) promotedThen, (DoubleExpressionNode) promotedElse);
                } else if (resultType == ExpressionType.INTEGER) {
                    return new IntegerIfNode((BooleanExpressionNode) cond, (IntegerExpressionNode) promotedThen, (IntegerExpressionNode) promotedElse);
                } else if (resultType == ExpressionType.DATE){
                    return new DateTimeIfNode((BooleanExpressionNode) cond, (DateTimeExpressionNode)  promotedThen, (DateTimeExpressionNode) promotedElse);
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
                    if (commonType == ExpressionType.INTEGER) {
                        result = (fn == ExpressionOperator.MAX)
                                ? new IntegerMaxNode((IntegerExpressionNode) result, (IntegerExpressionNode)promotedArgs.get(i))
                                : new IntegerMinNode((IntegerExpressionNode)result, (IntegerExpressionNode)promotedArgs.get(i));
                    }
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
        return false;
    }

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
            default -> null;
        };
    }
    private static ExpressionNode createDateBinaryNode(ExpressionOperator op, DateTimeExpressionNode left, DateTimeExpressionNode right) {
        return switch (op) {
            case AFTER -> new AfterNode(left, right);
            case BEFORE -> new BeforeNode(left, right);
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
