package usace.hec.expressions.math;



import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;


public class DoubleCeilingNode extends DoubleUnaryExpressionNode {
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
        ee.clear();
        double childVal = child.evaluate();
        checkErrors();
        return Math.ceil(childVal);
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.CEILING;
    }
    @Override
    public String ExcelSyntax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operator().getPrefixName());
        sb.append('(');
        sb.append(child().ExcelSyntax());
        sb.append(')');
        return sb.toString(); //same as Prefix, but must propogate excelSyntax to children
    }

    @Override
    public ExpressionNode child() {
        return this.child;
    }
}
