package usace.hec.expressions.math;

import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.DoubleExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;

public class DoubleFloorNode extends DoubleUnaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private DoubleExpressionNode child;
    /**
     * A numerical {@link UnaryExpressionNode} that evaluates a child (numerical {@link ExpressionNode}), returning the {@code Math.floor} of the child's value (e.g. {@code Math.floor(6.6) == 6})
     */
    public DoubleFloorNode(DoubleExpressionNode child) {
        this.child = child;
    }

    @Override
    public double evaluate() {
        return Math.floor(child.evaluate());
    }

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.FLOOR;
    }
    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
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
                return StaticOperator().getPrefixName() + "()";
            }
        }
    };
}
