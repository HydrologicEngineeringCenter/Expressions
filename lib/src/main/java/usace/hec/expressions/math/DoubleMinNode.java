package usace.hec.expressions.math;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class DoubleMinNode extends DoubleBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode left;
    private DoubleExpressionNode right;
    /**
     * A numerical {@link BinaryExpressionNode} that evaluates two children (numerical {@link DoubleExpressionNode}s) returning the minimum value ({@code Math.min}) between
     * the first child's value and the second child's value (e.g. {@code min(8,16) == 8})
     */
    public DoubleMinNode(DoubleExpressionNode left, DoubleExpressionNode right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public double evaluate() {
        return Math.min(left.evaluate(),right.evaluate());
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.MIN;
    }
    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
    @Override
    public ExpressionNode left() {
        return this.left;
    }
    @Override
    public ExpressionNode right() {
        return this.right;
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
