package usace.hec.expressions.misc;

import usace.hec.expressions.ExpressionNode;
import usace.hec.expressions.ExpressionOperator;
import usace.hec.expressions.ExpressionType;
import usace.hec.expressions.UnaryExpressionNode;

import java.io.Serial;
import java.util.Random;
import java.util.random.RandomGenerator;

public class RandNode extends UnaryExpressionNode<Double, Integer> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int SEED;
    private final Random RAND;
    public RandNode(ExpressionNode<Integer> child) {
        super(child);
        SEED = child.evaluate(); //safe because it uses a constant leaf node, which should already have an initial value.
        RAND = new Random(SEED);
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
        return ExpressionOperator.RAND;
    }

    @Override
    public Double evaluate() {
        return RAND.nextDouble();
    }
    @Override
    public ExpressionType resultType() {
        return ExpressionType.DOUBLE;
    }
}
