package usace.hec.expressions;

public enum ExpressionOperator {

    // --- Arithmetic (Math) ---
    PLUS("Math", "+", "PLUS", Arity.BINARY),
    MINUS("Math", "-", "MINUS", Arity.BINARY),
    MULTIPLY("Math", "*", "MULTIPLY", Arity.BINARY),
    DIVIDE("Math", "/", "DIVIDE", Arity.BINARY),
    POW("Math", "^", "POW", Arity.BINARY),

    // --- Aggregate (Math) ---
    MAX("Math", "MAX", "MAX", Arity.BINARY),
    MIN("Math", "MIN", "MIN", Arity.BINARY),

    // --- Unary Math (Math) ---
    NEGATE("Math", "-", "NEGATE", Arity.UNARY),
    ABS("Math", "|", "ABS", Arity.UNARY),
    FLOOR("Math", "FLOOR", "FLOOR", Arity.UNARY),
    CEILING("Math", "CEILING", "CEILING", Arity.UNARY),

    // --- Logical ---
    AND("Logical", "&&", "AND", Arity.BINARY),
    OR("Logical", "||", "OR", Arity.BINARY),
    XOR("Logical", "^^", "XOR", Arity.BINARY),

    // --- Comparison ---
    EQ("Comparison", "==", "EQ", Arity.BINARY),
    GT("Comparison", ">", "GT", Arity.BINARY),
    GTE("Comparison", ">=", "GTE", Arity.BINARY),
    LT("Comparison", "<", "LT", Arity.BINARY),
    LTE("Comparison", "<=", "LTE", Arity.BINARY),

    // --- Date / time ---
    AFTER("Time", "AFTER", "AFTER", Arity.BINARY),
    BEFORE("Time", "BEFORE", "BEFORE", Arity.BINARY),
    TODAY("Time", "TODAY", "TODAY", Arity.UNARY),
    DATE("Time", "DATE", "DATE", Arity.UNARY),
    CURRENTTIMESTEP("Time", "CURRENTTIMESTEP", "CURRENTTIMESTEP", Arity.UNARY),
    DOY("Time", "DOY", "DOY", Arity.UNARY),

    // --- Statisticalal / random ---
    LAG("Statistical", "LAG", "LAG", Arity.UNARY),
    RAND("Statistical", "RAND", "RAND", Arity.UNARY),

    // --- Type conversion ---
    INTCOERSION("Conversion", "TOINT", "TOINT", Arity.UNARY),
    DOUBLECOERSION("Conversion", "TODOUBLE", "TODOUBLE", Arity.UNARY),

    // --- Logical ---
    IF("Logical", "IF", "IF", Arity.TERNARY),

    // --- String ---
    CONCAT("String", "CONCAT", "CONCAT", Arity.BINARY),
    SUBSTRING("String", "SUBSTRING", "SUBSTRING", Arity.TERNARY),
    LENGTH("String", "LENGTH", "LENGTH", Arity.UNARY),
    LOWER("String", "LOWER", "LOWER", Arity.UNARY),
    UPPER("String", "UPPER", "UPPER", Arity.UNARY),
    TRIM("String", "TRIM", "TRIM", Arity.UNARY),
    REPLACE("String", "REPLACE", "REPLACE", Arity.BINARY),
    CONTAINS("String", "CONTAINS", "CONTAINS", Arity.BINARY),
    STARTSWITH("String", "STARTSWITH", "STARTSWITH", Arity.BINARY),
    ENDSWITH("String", "ENDSWITH", "ENDSWITH", Arity.BINARY),
    SPLIT("String", "SPLIT", "SPLIT", Arity.BINARY),
    JOIN("String", "JOIN", "JOIN", Arity.BINARY),

    // --- Leaf nodes ---
    CONSTANT("LEAF", "", "", Arity.LEAF),
    VARIABLE("LEAF", "[", "[", Arity.LEAF);

    private final String category;
    private final String infix;
    private final String prefix;
    private final Arity arity;

    ExpressionOperator(String category, String infix, String prefix, Arity arity) {
        this.category = category;
        this.infix = infix;
        this.prefix = prefix;
        this.arity = arity;
    }

    public String getCategory() {
        return category;
    }

    public String getInfixName() {
        return infix;
    }

    public String getPrefixName() {
        return prefix;
    }

    public Arity getArity() {
        return arity;
    }

    public boolean isUnary() {
        return arity == Arity.UNARY;
    }

    public boolean isBinary() {
        return arity == Arity.BINARY;
    }

    public boolean isTernary() {
        return arity == Arity.TERNARY;
    }

    public boolean isLeaf() {
        return arity == Arity.LEAF;
    }
}