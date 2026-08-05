package usace.hec.expressions.math;

import usace.hec.expressions.*;

import java.io.Serial;


public class DoubleDivideNode extends DoubleBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
    private EvaluationError ee = new EvaluationError();
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link DoubleExpressionNode}s), returning the division {@code /} of
     * the first child's value by the second child's value (e.g. {@code x/y})
     */
    public DoubleDivideNode(DoubleExpressionNode left, DoubleExpressionNode right) {
        this.left = left;
        this.right = right;        
    }
    @Override
    public double evaluate() {
        double r = right.evaluate();
        if (r == 0.0){
            ee.report(ErrorState.INVALID, this, "Division by 0.0");
            return 0.0; //exit early
        }
        return left.evaluate() / r;
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
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
