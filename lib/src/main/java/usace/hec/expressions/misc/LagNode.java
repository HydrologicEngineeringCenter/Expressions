package usace.hec.expressions.misc;

import usace.hec.expressions.BinaryExpressionNode;
import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;

import java.util.ArrayDeque;
import java.util.Queue;

public class LagNode<T> extends BinaryExpressionNode<T, Integer, T> {
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
    public void excelAppend(StringBuilder sb){
        sb.append(InfixOpName());
        sb.append('(');
        leftnode.excelAppend(sb);
        sb.append(',');
        rightnode.excelAppend(sb);
        sb.append(')');
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
}
