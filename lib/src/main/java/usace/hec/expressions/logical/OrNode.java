package usace.hec.expressions.logical;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.comparison.BooleanBinaryExpressionNode;

import java.io.Serial;


public class OrNode extends BooleanBinaryExpressionNode {
    @Serial
    private static final long serialVersionUID = 1L;
    private BooleanExpressionNode left;
    private BooleanExpressionNode right;
    /**
     * A boolean {@link BinaryExpressionNode} that evaluates two children (boolean {@link ExpressionNode}s), returning the OR of
     * the childs' values (e.g. {@code true} if at least one child is true, otherwise {@code false})
     */
    public OrNode(BooleanExpressionNode left, BooleanExpressionNode right) {
        this.left = left;
        this.right = right;
        
    }
    @Override
    public boolean evaluate() {
        if (left.evaluate()){
            return true;
        } else {
            return right.evaluate();
        }
    }
    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator() {
        return ExpressionOperator.OR;
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.BOOLEAN;
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
            return "Logical";
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

