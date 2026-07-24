package usace.hec.expressions;

import usace.hec.expressions.comparison.*;
import usace.hec.expressions.logical.*;
import usace.hec.expressions.math.*;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class InfixParser {

    public ParseResult<ExpressionNode<?>> parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return ParseResult.error(0, "Empty expression", "");
        }

        List<Token> tokenResult = new Tokenizer().tokenize(input);
        for(Token t: tokenResult){
            if (t.hasError()) {
                return ParseResult.error(t.position(), t.error(), t.toString());
            }         
        }

        return buildAstRecursiveDescent(tokenResult);
    }

    private boolean isUnaryContext(Deque<Token> stack, List<Token> output) {
        if (output.isEmpty() && stack.isEmpty()) return true;
        if (stack.isEmpty()) {
            Token last = output.get(output.size() - 1);
            return last instanceof Token.Operator || last instanceof Token.LeftParen || last instanceof Token.Comma;
        }
        Token top = stack.peek();
        return top instanceof Token.Operator || top instanceof Token.LeftParen || 
               top instanceof Token.Comma || top instanceof Token.Function;
    }

    private boolean isOperatorOrFunction(Token t) {
        return t instanceof Token.Operator || t instanceof Token.Function;
    }

    private boolean shouldPop(Token stackTop, Token currentToken) {
        if (stackTop instanceof Token.Function) return false;
        if (stackTop instanceof Token.Operator && currentToken instanceof Token.Operator) {
            ExpressionOperator topOp = ((Token.Operator) stackTop).op();
            ExpressionOperator currOp = ((Token.Operator) currentToken).op();
            int topPrec = OperatorPrecedence.getPrecedence(topOp);
            int currPrec = OperatorPrecedence.getPrecedence(currOp);
            if (topPrec > currPrec) return true;
            if (topPrec == currPrec && OperatorPrecedence.isLeftAssociative(currOp)) return true;
        }
        return false;
    }
    private ParseResult<ExpressionNode<?>> buildAstRecursiveDescent(List<Token> tokens){
        ExpressionNode<?> ast;
        ParseResult<ExpressionNode<?>> result = null;
    
        for (Token t: tokens){
            //errors have already been handled
            
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> ExpressionNode<T> makeIfNode(ExpressionNode<?> cond, ExpressionNode<?> thenB, ExpressionNode<?> elseB) {
        return (ExpressionNode<T>) new IfNode(cond, thenB, elseB);
    }

    @SuppressWarnings("unchecked")
    private ExpressionNode<?> makeBinaryNode(ExpressionOperator op, ExpressionNode<?> left, ExpressionNode<?> right) {
        try {
            switch (op) {
                case PLUS: return new AddNode((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case MINUS: return new MinusNode((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case MULTIPLY: return new MultiplyNode((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case DIVIDE: return new DivideNode((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case POW: return new ExponentNode((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case MAX: return new MaxNode((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case MIN: return new MinNode((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case GT: return new GreaterThanNode<>((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case GTE: return new GreaterThanOrEqualNode<>((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case LT: return new LessThanNode<>((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case LTE: return new LessThanOrEqualNode<>((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case EQ: return new EqualToNode<>((ExpressionNode<Double>) left, (ExpressionNode<Double>) right);
                case AND: return new AndNode((ExpressionNode<Boolean>) left, (ExpressionNode<Boolean>) right);
                case OR: return new OrNode((ExpressionNode<Boolean>) left, (ExpressionNode<Boolean>) right);
                case XOR: return new XorNode((ExpressionNode<Boolean>) left, (ExpressionNode<Boolean>) right);
                default: throw new IllegalArgumentException("Unknown binary operator: " + op);
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Type mismatch for operator: " + op, e);
        }
    }

    @SuppressWarnings("unchecked")
    private ExpressionNode<?> makeUnaryNode(ExpressionOperator op, ExpressionNode<?> child) {
        try {
            switch (op) {
                case NEGATE: return new NegateNode((ExpressionNode<Double>) child);
                case ABS: return new AbsNode((ExpressionNode<Double>) child);
                case FLOOR: return new FloorNode((ExpressionNode<Double>) child);
                case CEILING: return new CeilingNode((ExpressionNode<Double>) child);
                default: throw new IllegalArgumentException("Unknown unary operator: " + op);
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Type mismatch for operator: " + op, e);
        }
    }

    private boolean createNodeForOperator(Deque<ExpressionNode<?>> stack, ExpressionOperator op) {
        try {
            switch (op) {
                case NEGATE:
                case ABS:
                case FLOOR:
                case CEILING:
                    if (stack.size() < 1) return false;
                    stack.push(makeUnaryNode(op, stack.pop()));
                    break;
                case IF:
                    if (stack.size() < 3) return false;
                    ExpressionNode<?> elseBranch = stack.pop();
                    ExpressionNode<?> thenBranch = stack.pop();
                    ExpressionNode<?> condition = stack.pop();
                    stack.push(makeIfNode(condition, thenBranch, elseBranch));
                    break;
                default:
                    if (stack.size() < 2) return false;
                    ExpressionNode<?> right = stack.pop();
                    ExpressionNode<?> left = stack.pop();
                    stack.push(makeBinaryNode(op, left, right));
                    break;
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}