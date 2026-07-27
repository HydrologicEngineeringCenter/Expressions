package usace.hec.expressions.time;

import java.io.Serial;

import java.time.LocalDateTime;

import usace.hec.expressions.DisplayNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UpdateableLeafNode;

public class CurrentTimestepNode extends UpdateableLeafNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime date;

    public CurrentTimestepNode() {
        super("CurrentTimeStep");
    }
    @Override
    public LocalDateTime evaluate() {
        return this.date;
    }
  

    @Override
    public ExpressionOperator Operator() {
        return StaticOperator();
    }
    public static ExpressionOperator StaticOperator(){
        return ExpressionOperator.CURRENTTIMESTEP;
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
            return "Time";
        }
        @Override
        public String defaultSyntax(boolean infix) {
            return "[CurrentTimeStep]";
        }
    };
}