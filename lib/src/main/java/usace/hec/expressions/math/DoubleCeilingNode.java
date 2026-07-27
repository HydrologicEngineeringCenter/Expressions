package usace.hec.expressions.math;

import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;


public class DoubleCeilingNode extends DoubleUnaryExpressionNode implements DisplayNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode child;
    /**
     * A numerical {@link UnaryExpressionNode} that evaluates a child (numerical {@link DoubleExpressionNode}), returning the {@code Math.ceil} of the child's value (e.g. {@code Math.ceil(6.6) == 7})
     */
    public DoubleCeilingNode(DoubleExpressionNode child) {
        this.child = child;
    }

    @Override
    public double evaluate() {
        return Math.ceil(child.evaluate());
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.CEILING;
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
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
