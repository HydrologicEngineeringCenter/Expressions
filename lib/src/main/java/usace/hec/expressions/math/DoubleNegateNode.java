package usace.hec.expressions.math;

import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;


public class DoubleNegateNode extends DoubleUnaryExpressionNode implements DisplayNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode child;
    /**
     * A numerical {@link UnaryExpressionNode} that evaluates a child (numerical {@link DoubleExpressionNode}), returning the negation {@code -} of the child's value (e.g. {@code -x})
     */
    public DoubleNegateNode(DoubleExpressionNode child) {
        this.child = child;
    }

    @Override
    public double evaluate() {
        return -child.evaluate();
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.NEGATE;
    }

    @Override
    public ExpressionNode child() {
        return this.child;
    }
    @Override
    public String displayName(boolean infix) {
        if(infix){
            return Operator().getInfixName();
        }else{
            return Operator().getPrefixName();
        }
    }
    @Override
    public String category() {
        return "Math";
    }
    @Override
    public String defaultSyntax(boolean infix) {
        if (infix){
            return Operator().getInfixName();
        }else{
            return Operator().getPrefixName() + "()";
        }
    }
}
