package usace.hec.expressions;

public enum Arity {
    LEAF(0),
    UNARY(1),
    BINARY(2),
    TERNARY(3);

    private final int value;

    Arity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
