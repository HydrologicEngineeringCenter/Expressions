package usace.hec.expressions.math;

import usace.hec.expressions.*;

import java.io.Serial;


public class DoubleExponentNode extends DoubleBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
    private EvaluationError ee = new EvaluationError();
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link DoubleExpressionNode}s) with exponentiation ({@code ^}) returning the value of the
     * first child's value to the power of the second child's value (e.g. {@code x^y})
     */
    public DoubleExponentNode(DoubleExpressionNode left, DoubleExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public double evaluate() {
        double l=left.evaluate();
        double r = right.evaluate();
        if ( r < 0.0){
            ee.report(ErrorState.INVALID, this, "Imaginary numbers unsupported");
            return 0.0; //exit early
        }
        return Math.pow(l, r);
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
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
    @Override
    public EvaluationError ownError(){
        return this.ee;
    }
}
