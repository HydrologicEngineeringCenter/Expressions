package usace.hec.expressions;

import org.apache.commons.math3.analysis.function.Constant;
import usace.hec.expressions.comparison.*;
import usace.hec.expressions.logical.*;
import usace.hec.expressions.math.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface ExpressionNode<T extends Serializable> extends Serializable {
    T evaluate();
    String PreFixSyntax();
    String ExcelSyntax();
    List<DataListener<?>> fetchListeners();
    default void setProvider(DataProvider dp){
        return;
    }
    void prefixAppend(StringBuilder sb);
    void excelAppend(StringBuilder sb);
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> ExpressionNode<T> fromPreFixSyntax(String PrefixSyntax, Class<T> type) {
        // Locate the first '(' — if there isn't one, the entire string is a literal (base case)
        int firstParen = PrefixSyntax.indexOf('(');
        if (firstParen == -1) {
            // Base case: leaf node.
            T value;
            //UpdatableLeafNode
            if (PrefixSyntax.indexOf('[') != -1) {
                return new UpdateableLeafNode<>(PrefixSyntax.substring(1, PrefixSyntax.length() - 1));
            }
            return new ConstantLeafNode<>(parseLeafValue(PrefixSyntax, type));
        }
        // Everything before the first '(' is the operator's name, e.g. "ADD".
        String operatorName = PrefixSyntax.substring(0, firstParen);

        // The very last char must be the ')' matching firstParen.
        // Stripping both parens leaves just the raw, comma-separated argument list.
        String argsBlock = PrefixSyntax.substring(firstParen + 1, PrefixSyntax.length() - 1);

        // Split argsBlock into top-level arguments by tracking paren depth,
        // so commas belonging to NESTED calls (like the comma inside MULT(3,2))
        // are not mistaken for top-level argument separators.
        List<String> argStrings = new ArrayList<>();
        int depth = 0;
        int segmentStart = 0;
        for (int i = 0; i < argsBlock.length(); i++) {
            char c = argsBlock.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            } else if (c == ',' && depth == 0) {
                argStrings.add(argsBlock.substring(segmentStart, i));
                segmentStart = i + 1;
            }
        }
        argStrings.add(argsBlock.substring(segmentStart)); // final argument

        // Build the correct node type based on the operator name.
        switch (operatorName) {
            case "PLUS":
                return (ExpressionNode<T>) new AddNode(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "MINUS":
                return (ExpressionNode<T>) new MinusNode(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "MULTIPLY":
                return (ExpressionNode<T>) new MultiplyNode(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "DIVIDE":
                return (ExpressionNode<T>) new DivideNode(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "POW":
                return (ExpressionNode<T>) new ExponentNode(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "MAX":
                return (ExpressionNode<T>) new MaxNode(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "MIN":
                return (ExpressionNode<T>) new MinNode(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "NEGATE":
                return (ExpressionNode<T>) new NegateNode(fromPreFixSyntax(argStrings.get(0), Double.class));
            case "AND":
                return (ExpressionNode<T>) new AndNode(fromPreFixSyntax(argStrings.get(0), Boolean.class), fromPreFixSyntax(argStrings.get(1), Boolean.class));
            case "OR":
                return (ExpressionNode<T>) new OrNode(fromPreFixSyntax(argStrings.get(0), Boolean.class), fromPreFixSyntax(argStrings.get(1), Boolean.class));
            case "XOR":
                return (ExpressionNode<T>) new XorNode(fromPreFixSyntax(argStrings.get(0), Boolean.class), fromPreFixSyntax(argStrings.get(1), Boolean.class));
            case "EQ":
                return (ExpressionNode<T>) new EqualToNode<>(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "GT":
                return (ExpressionNode<T>) new GreaterThanNode<>(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "GTE":
                return (ExpressionNode<T>) new GreaterThanOrEqualNode<>(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "LT":
                return (ExpressionNode<T>) new LessThanNode<>(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "LTE":
                return (ExpressionNode<T>) new LessThanOrEqualNode<>(fromPreFixSyntax(argStrings.get(0), Double.class), fromPreFixSyntax(argStrings.get(1), Double.class));
            case "ABS":
                return (ExpressionNode<T>) new AbsNode(fromPreFixSyntax(argStrings.get(0), Double.class));
            case "IF":
                return new IfNode<>(fromPreFixSyntax(argStrings.get(0), Boolean.class), fromPreFixSyntax(argStrings.get(1), type), fromPreFixSyntax(argStrings.get(2), type));
            default:
                throw new IllegalArgumentException("Unknown operator: " + operatorName);
        }
    }

    private static <T extends Serializable> T parseLeafValue(String prefixSyntax, Class<T> type) {
        if (type == Boolean.class) {
            return (T) Boolean.valueOf(prefixSyntax);
        }
        if (type == Integer.class) {
            return (T) Integer.valueOf(prefixSyntax);
        }
        if (type == Double.class) {
            return (T) Double.valueOf(prefixSyntax);
        }
        throw new IllegalArgumentException("Unsupported leaf type: " + type);
    }
}
