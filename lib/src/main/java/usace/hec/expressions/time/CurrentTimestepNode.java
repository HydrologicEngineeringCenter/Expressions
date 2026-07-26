package usace.hec.expressions.time;

import java.io.Serial;

import java.time.LocalDateTime;

import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.UpdateableLeafNode;

public class CurrentTimestepNode extends UpdateableLeafNode {
    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime date;

    public CurrentTimestepNode() {
        super("CurrentTimestep");
    }
    @Override
    public LocalDateTime evaluate() {
        return this.date;
    }
  

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.CURRENTTIMESTEP;
    }
}