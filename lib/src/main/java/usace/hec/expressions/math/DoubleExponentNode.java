package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class DoubleExponentNode extends DoubleBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
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
        if (left.evaluate() < 0 && right.evaluate() < 1.0){
            throw new UnsupportedOperationException("Imaginary number unsupported");
        }
        return Math.pow(left.evaluate(), right.evaluate());
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
    public static DisplayNode displayData(){
        return DisplayData;
    }
    public static final DisplayNode DisplayData = new DisplayNode() {
        @Override
        public String displayName(boolean infix) {
            if(infix){
                return StaticOperator().getInfixName();
            }else{
                return StaticOperator().getPrefixName();
            }
        }
        @Override
        public String category() {
            return "Math";
        }
        @Override
        public String defaultSyntax(boolean infix) {
            if (infix){
                return StaticOperator().getInfixName();
            }else{
                return StaticOperator().getPrefixName() + "(,)";
            }
        }
    };
}
