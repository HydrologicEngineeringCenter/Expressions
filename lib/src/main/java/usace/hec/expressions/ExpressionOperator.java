package usace.hec.expressions;

public enum ExpressionOperator {

    // --- Arithmetic (Math) ---
    PLUS("Math", "+", "PLUS", "+", "PLUS(,)", Arity.BINARY),
    MINUS("Math", "-", "MINUS", "-", "MINUS(,)",Arity.BINARY),
    MULTIPLY("Math", "*", "MULTIPLY", "*", "MULTIPLY(,)", Arity.BINARY),
    DIVIDE("Math", "/", "DIVIDE",  "/", "DIVIDE(,)",Arity.BINARY),
    POW("Math", "^", "POW", "^", "POW(,)", Arity.BINARY),

    // --- Aggregate (Math) ---
    MAX("Math", "MAX", "MAX","MAX(,)", "MAX(,)", Arity.BINARY),
    MIN("Math", "MIN", "MIN", "MIN(,)", "MIN(,)",Arity.BINARY),

    // --- Unary Math (Math) ---
    NEGATE("Math", "-", "NEGATE", "-", "NEGATE()",Arity.UNARY),
    ABS("Math", "|", "ABS", "|0.0|", "ABS()",Arity.UNARY),
    FLOOR("Math", "FLOOR", "FLOOR","FLOOR()", "FLOOR()", Arity.UNARY),
    CEILING("Math", "CEILING", "CEILING","CEILING()", "CEILING()", Arity.UNARY),

    // --- Logical ---
    AND("Logical", "&&", "AND","&&", "AND(,)", Arity.BINARY),
    OR("Logical", "||", "OR", "||", "OR(,)", Arity.BINARY),
    XOR("Logical", "^^", "XOR","^^", "XOR(,)", Arity.BINARY),

    // --- Comparison ---
    EQ("Comparison", "==", "EQ","==", "EQ(,)", Arity.BINARY),
    GT("Comparison", ">", "GT",">", "GT(,)", Arity.BINARY),
    GTE("Comparison", ">=", "GTE",">=", "GTE(,)", Arity.BINARY),
    LT("Comparison", "<", "LT","<", "LT(,)", Arity.BINARY),
    LTE("Comparison", "<=", "LTE","<=", "LTE(,)", Arity.BINARY),

    // --- Date / time ---
    AFTER("Time", "AFTER", "AFTER","AFTER(,)", "AFTER(,)", Arity.BINARY),
    BEFORE("Time", "BEFORE", "BEFORE","BEFORE(,)", "BEFORE(,)", Arity.BINARY),
    TODAY("Time", "TODAY", "TODAY","TODAY()", "TODAY()", Arity.LEAF),
    DATE("Time", "DATE", "DATE","DATE(,,)", "DATE(,,)", Arity.TERNARY),
    DOY("Time", "DOY", "DOY", "DOY()", "DOY()", Arity.UNARY),
    DOM("Time", "DOM", "DOM", "DOM()", "DOM()", Arity.UNARY),
    YEAR("Time","YEAR", "YEAR", "YEAR()", "YEAR()", Arity.UNARY),
    WATERYEAR("Time","WATERYEAR", "WATERYEAR", "WATERYEAR()", "WATERYEAR()", Arity.UNARY),
    LEAPYEAR("Time","LEAPYEAR", "LEAPYEAR", "LEAPYEAR()", "LEAPYEAR()", Arity.UNARY),
    MONTH("Time","MONTH", "MONTH", "MONTH()", "MONTH()", Arity.UNARY),

    // --- Statisticalal / random ---
    LAG("Statistical", "LAG", "LAG", "LAG(,)", "LAG(,)", Arity.BINARY),
    RAND("Statistical", "RAND", "RAND","RAND()", "RAND()", Arity.UNARY),

    // --- Type conversion ---
    INTCOERSION("Conversion", "TOINT", "TOINT","TOINT()", "TOINT()", Arity.UNARY),
    DOUBLECOERSION("Conversion", "TODOUBLE", "TODOUBLE","TODOUBLE()", "TODOUBLE()", Arity.UNARY),

    // --- Logical ---
    IF("Logical", "IF", "IF","IF(,,)", "IF(,,)", Arity.TERNARY),

    // --- String ---
    CONCAT("String", "CONCAT", "CONCAT","CONCAT(,)", "CONCAT(,)", Arity.BINARY),
    SUBSTRING("String", "SUBSTRING", "SUBSTRING","SUBSTRING(,,)", "SUBSTRING(,,)", Arity.TERNARY),
    LENGTH("String", "LENGTH", "LENGTH","LENGTH()", "LENGTH()", Arity.UNARY),
    LOWER("String", "LOWER", "LOWER","LOWER()", "LOWER()", Arity.UNARY),
    UPPER("String", "UPPER", "UPPER", "UPPER()", "UPPER()",Arity.UNARY),
    TRIM("String", "TRIM", "TRIM","TRIM()", "TRIM()", Arity.UNARY),
    REPLACE("String", "REPLACE", "REPLACE","REPLACE(,,)", "REPLACE(,,)", Arity.TERNARY),
    CONTAINS("String", "CONTAINS", "CONTAINS","CONTAINS(,)", "CONTAINS(,)", Arity.BINARY),
    STARTSWITH("String", "STARTSWITH", "STARTSWITH","STARTSWITH(,)", "STARTSWITH(,)", Arity.BINARY),
    ENDSWITH("String", "ENDSWITH", "ENDSWITH","ENDSWITH(,)", "ENDSWITH(,)", Arity.BINARY),
    //SPLIT("String", "SPLIT", "SPLIT", Arity.BINARY),
    //JOIN("String", "JOIN", "JOIN", Arity.BINARY),

    // --- Leaf nodes ---
    CONSTANT("LEAF", "", "","", "", Arity.LEAF),
    VARIABLE("LEAF", "[", "[","[\"a\"]", "[\"a\"]", Arity.UNARY);

    private final String category;
    private final String infix;
    private final String prefix;
    private final String infixSyntax;
    private final String prefixSyntax;
    private final Arity arity;

    ExpressionOperator(String category, String infix, String prefix,String infixSyntax, String prefixSyntax, Arity arity) {
        this.category = category;
        this.infix = infix;
        this.prefix = prefix;
        this.infixSyntax = infixSyntax;
        this.prefixSyntax = prefixSyntax;
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
    public String getInfixSyntax() {
        return infixSyntax;
    }

    public String getPrefixSyntax() {
        return prefixSyntax;
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