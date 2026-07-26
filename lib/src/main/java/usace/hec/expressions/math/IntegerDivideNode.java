package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class IntegerDivideNode extends IntegerBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode left;
    private IntegerExpressionNode right;
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link IntegerExpressionNode}s), returning the division {@code /} of
     * the first child's value by the second child's value (e.g. {@code x/y})
     */
    public IntegerDivideNode(IntegerExpressionNode left, IntegerExpressionNode right) {
        this.left = left;
        this.right = right;        
    }
    @Override
    public int evaluate() {
        if (right.evaluate() == 0.0){
            throw new ArithmeticException("Division by zero");
        }
        return left.evaluate() / right.evaluate();
    }
    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.DIVIDE;
    }
    @Override
    public ExpressionNode left() {
        return left;
    }
    @Override
    public ExpressionNode right() {
        return right;
    }
}
