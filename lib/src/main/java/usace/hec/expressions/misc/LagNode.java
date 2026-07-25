package usace.hec.expressions.misc;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;

public class LagNode<T extends Serializable> extends BinaryExpressionNode<T, Integer, T> {
    @Serial
    private static final long serialVersionUID = 1L;
    private transient Queue<T> lagQueue;
    private transient Integer lag;
    public LagNode(ExpressionNode<T> left, ExpressionNode<Integer> right) {
        super(left, right);
    }

    @Override
    public String OpName() {
        return Operator().getPrefixName();
    }

    @Override
    public String InfixOpName() {
        return Operator().getInfixName();
    }

    @Override
    public ExpressionOperator Operator() {
        return ExpressionOperator.LAG;
    }

    @Override
    public String ExcelSyntax() {
        return PreFixSyntax(); //for excel syntax in this case prefix is the correct syntax
    }
    @Override
    public T evaluate() {
        //TODO: initialize Queue with values that come lag steps before simulation start
        if (lagQueue == null) {
            lag = rightnode.evaluate().intValue();
            lagQueue = new ArrayDeque<>();
            T dummyVal = leftnode.evaluate();
            for (int i = 0; i < lag; i++) {
                lagQueue.add(dummyVal);
            }
        }
        else {
            lagQueue.add(leftnode.evaluate());
        }
        return lagQueue.poll();
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;//placeholder
    }
}
