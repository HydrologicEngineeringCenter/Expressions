package usace.hec.expressions;

import org.apache.commons.math3.analysis.function.Constant;
import usace.hec.expressions.comparison.*;
import usace.hec.expressions.logical.*;
import usace.hec.expressions.math.*;

import java.util.ArrayList;
import java.util.List;

public interface ExpressionNode<T> {
    T evaluate();
    String PreFixSyntax();
    String ExcelSyntax();
    List<DataListener<?>> fetchListeners();
    void prefixAppend(StringBuilder sb);
    void excelAppend(StringBuilder sb);
    @SuppressWarnings("unchecked")
    public static <T> ExpressionNode<T> fromPreFixSyntax(String PrefixSyntax){

        // Locate the first '(' — if there isn't one, the entire string is a literal (base case)
        int firstParen = PrefixSyntax.indexOf('(');
        if (firstParen == -1) {
            // Base case: leaf node.
            T value;
            //UpdatableLeafNode
            if (PrefixSyntax.indexOf('[') != -1){
                return new UpdateableLeafNode<>(PrefixSyntax.substring(1, PrefixSyntax.length() - 1));
            }
            //Constant Boolean leaf node.
            else if (PrefixSyntax.equals("true") || PrefixSyntax.equals("false")){
                value = (T) (Object) Boolean.parseBoolean(PrefixSyntax);
            }
            //ConstantDouble leaf node.
            else {
                value = (T) (Object) Double.parseDouble(PrefixSyntax);
            }
            return new ConstantLeafNode<>(value);
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

        // Recursively parse each top-level argument. Each call gets its own
        // self-contained substring, so no shared position/cursor state is needed
        // across recursive calls — this IS the recursive step.
        List<ExpressionNode<?>> args = new ArrayList<>();
        for (String argString : argStrings) {
            args.add(fromPreFixSyntax(argString));
        }

        // Build the correct node type based on the operator name.
        switch (operatorName) {
            case "PLUS":
                return (ExpressionNode<T>) new AddNode((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>) args.get(1));
            case "MINUS":
                return (ExpressionNode<T>) new MinusNode((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>) args.get(1));
            case "MULTIPLY":
                return (ExpressionNode<T>) new MultiplyNode((ExpressionNode<Double>)args.get(0), (ExpressionNode<Double>)args.get(1));
            case "DIVIDE":
                return (ExpressionNode<T>) new DivideNode((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>)args.get(1));
            case "POW":
                return (ExpressionNode<T>) new ExponentNode((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>)args.get(1));
            case "MAX":
                return (ExpressionNode<T>) new MaxNode((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>)args.get(1));
            case "MIN":
                return (ExpressionNode<T>) new MinNode((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>)args.get(1));
            case "NEGATE":
                return (ExpressionNode<T>) new NegateNode((ExpressionNode<Double>)args.get(0));
            case "AND":
                return (ExpressionNode<T>) new AndNode((ExpressionNode<Boolean>) args.get(0), (ExpressionNode<Boolean>)args.get(1));
            case "OR":
                return (ExpressionNode<T>) new OrNode((ExpressionNode<Boolean>) args.get(0), (ExpressionNode<Boolean>)args.get(1));
            case "XOR":
                return (ExpressionNode<T>) new XorNode((ExpressionNode<Boolean>) args.get(0), (ExpressionNode<Boolean>)args.get(1));
            case "EQ":
                return (ExpressionNode<T>) new EqualToNode<>((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>)args.get(1));
            case "GT":
                return (ExpressionNode<T>) new GreaterThanNode<>((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>)args.get(1));
            case "GTE":
                return (ExpressionNode<T>) new GreaterThanOrEqualNode<>((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>)args.get(1));
            case "LT":
                return (ExpressionNode<T>) new LessThanNode<>((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>)args.get(1));
            case "LTE":
                return (ExpressionNode<T>) new LessThanOrEqualNode<>((ExpressionNode<Double>) args.get(0), (ExpressionNode<Double>)args.get(1));
            case "ABS":
                return (ExpressionNode<T>) new AbsNode((ExpressionNode<Double>)args.get(0));
            case "IF":
                return (ExpressionNode<T>) new IfNode<>((ExpressionNode<Boolean>)args.get(0),(ExpressionNode<Double>)args.get(1), (ExpressionNode<Double>)args.get(2) );
            default:
                throw new IllegalArgumentException("Unknown operator: " + operatorName);
        }
    }
}
