package usace.hec.expressions.math;

import usace.hec.expressions.*;

import java.io.Serial;


public class IntegerExponentNode extends IntegerBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private IntegerExpressionNode left;
    private IntegerExpressionNode right;
    private EvaluationError ee = new EvaluationError();
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link IntegerExpressionNode}s) with exponentiation ({@code ^}) returning the value of the
     * first child's value to the power of the second child's value (e.g. {@code x^y})
     */
    public IntegerExponentNode(IntegerExpressionNode left, IntegerExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public int evaluate() {
        int l = left.evaluate();
        int r = right.evaluate();
        checkErrors();
        if (r < 0){
            if (!right.hasError()) {
                ee.report(ErrorState.INVALID, this, "Imaginary numbers unsupported");
            }
            return 0; //exit early
        }
        return (int) Math.pow(l, r);
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.POW;
    }
    @Override
    public ExpressionNode left() {
        return this.left;
    }
    @Override
    public ExpressionNode right() {
        return this.right;
    }

}
