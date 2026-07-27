package usace.hec.expressions.logical;

import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.DoubleExpressionNode;


public class DoubleIfNode extends IfNode implements DoubleExpressionNode{

    public DoubleIfNode(BooleanExpressionNode condition, DoubleExpressionNode thenn, DoubleExpressionNode elsee) {
        super(condition, thenn, elsee);
    }

    @Override
    public double evaluate() {
        DoubleExpressionNode thenBranch = (DoubleExpressionNode) thenNode;
        DoubleExpressionNode elseBranch = (DoubleExpressionNode) elseNode;
        
        return conditionNode.evaluate() ? thenBranch.evaluate() : elseBranch.evaluate();
    }
    public static DisplayNode displayData(){
        return DisplayData;
    }
    public static final DisplayNode DisplayData = new DisplayNode() {
        @Override
        public String displayName(boolean infix) {
            return StaticOperator().getPrefixName();
        }
        @Override
        public String category() {
            return "Logical";
        }
        @Override
        public String defaultSyntax(boolean infix) {
            return StaticOperator().getPrefixName() + "(,,)";
        }
    };
}
