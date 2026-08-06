package usace.hec.expressions.math;



import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ErrorState;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionNode;

import java.io.Serial;


public class DoubleDivideNode extends DoubleBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
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
        double leftVal = left.evaluate();
        double rightVal = right.evaluate();
        checkErrors();
        if (rightVal == 0.0){
            if (!right.hasError()) {
                ee.report(ErrorState.INVALID, this, "Division by 0.0");
            }
            return 0.0; //exit early
        }
        return leftVal / rightVal;
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
