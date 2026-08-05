package usace.hec.expressions.math;

import usace.hec.expressions.*;

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
        int leftVal = left.evaluate();
        int rightVal = right.evaluate();
        checkErrors();
        if (rightVal == 0){
            if (!right.hasError()) {
                ee.report(ErrorState.INVALID, this, "Division by 0");
            }
            return 0; //exit early
        }
        return leftVal / rightVal;
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
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
