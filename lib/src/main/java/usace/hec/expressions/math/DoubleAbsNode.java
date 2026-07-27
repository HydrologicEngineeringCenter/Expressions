package usace.hec.expressions.math;

import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.io.Serial;


public class DoubleAbsNode extends DoubleUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode child;
    /**
     * A numerical {@link DoubleUnaryExpressionNode} that evaluates a child (numerical {@link DoubleExpressionNode}), returning the absolute value {@code Math.abs} of the child's value (e.g. {@code |-2| == 2})
     */
    public DoubleAbsNode(DoubleExpressionNode child) {
        this.child = child;
    }

    @Override
    public double evaluate() {
        return Math.abs(child.evaluate());

    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.ABS;
    }

    @Override
    public String ExcelSyntax() {
        return Operator().getInfixName() +  child.ExcelSyntax() + Operator().getInfixName();
    }

    @Override
    public ExpressionNode child() {
        return this.child;
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
                return StaticOperator().getPrefixName() + " " + StaticOperator().getPrefixName();
            }
        }
    };
}
