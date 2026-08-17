package usace.hec.expressions;

public enum ExpressionOperator {

    // --- Arithmetic (Math) ---
    PLUS("Math", "+", "PLUS", "+", "PLUS(,)", Arity.BINARY, "Adds two numeric values."),
    MINUS("Math", "-", "MINUS", "-", "MINUS(,)",Arity.BINARY, "Subtracts the second numeric value from the first."),
    MULTIPLY("Math", "*", "MULTIPLY", "*", "MULTIPLY(,)", Arity.BINARY, "Multiplies two numeric values."),
    DIVIDE("Math", "/", "DIVIDE",  "/", "DIVIDE(,)",Arity.BINARY, "Divides the first numeric value by the second."),
    INT_DIVIDE("Math", "//", "INT_DIVIDE",  "//", "INT_DIVIDE(,)",Arity.BINARY, "Divides the first numeric value by the second, then floor into integer."),
    POW("Math", "^", "POW", "^", "POW(,)", Arity.BINARY, "Raises the first numeric value to the power of the second."),

    // --- Aggregate (Math) ---
    MAX("Math", "MAX", "MAX","MAX(,)", "MAX(,)", Arity.BINARY, "Returns the greater of two numeric values."),
    MIN("Math", "MIN", "MIN", "MIN(,)", "MIN(,)",Arity.BINARY, "Returns the lesser of two numeric values."),

    // --- Unary Math (Math) ---
    NEGATE("Math", "-", "NEGATE", "-", "NEGATE()",Arity.UNARY, "Negates a numeric value."),
    ABS("Math", "|", "ABS", "|0.0|", "ABS()",Arity.UNARY, "Returns the absolute value of a numeric value."),
    FLOOR("Math", "FLOOR", "FLOOR","FLOOR()", "FLOOR()", Arity.UNARY, "Rounds a numeric value down to the nearest integer."),
    CEILING("Math", "CEILING", "CEILING","CEILING()", "CEILING()", Arity.UNARY, "Rounds a numeric value up to the nearest integer."),

    // --- Logical ---
    NOT("Logical", "!", "NOT", "!", "NOT()", Arity.UNARY, "Returns the logical negation of a boolean value."),
    AND("Logical", "&&", "AND","&&", "AND(,)", Arity.BINARY, "Returns true if both boolean values are true."),
    OR("Logical", "||", "OR", "||", "OR(,)", Arity.BINARY, "Returns true if either boolean value is true."),
    XOR("Logical", "^^", "XOR","^^", "XOR(,)", Arity.BINARY, "Returns true if exactly one of the two boolean values is true."),

    // --- Comparison ---
    EQ("Comparison", "==", "EQ","==", "EQ(,)", Arity.BINARY, "Returns true if the two values are equal."),
    NEQ("Comparison", "!=", "NEQ","!=", "NEQ(,)", Arity.BINARY, "Returns true if the two values are not equal."),
    GT("Comparison", ">", "GT",">", "GT(,)", Arity.BINARY, "Returns true if the first value is greater than the second."),
    GTE("Comparison", ">=", "GTE",">=", "GTE(,)", Arity.BINARY, "Returns true if the first value is greater than or equal to the second."),
    LT("Comparison", "<", "LT","<", "LT(,)", Arity.BINARY, "Returns true if the first value is less than the second."),
    LTE("Comparison", "<=", "LTE","<=", "LTE(,)", Arity.BINARY, "Returns true if the first value is less than or equal to the second."),
    BETWEEN("Comparison", "BETWEEN", "BETWEEN","BETWEEN(,,)" ,"BETWEEN(,,)" ,Arity.TERNARY, "Returns true if the second value is strictly between the first and third values."),

    // --- Date / time ---
    AFTER("Time", "AFTER", "AFTER","AFTER(,)", "AFTER(,)", Arity.BINARY, "Returns true if the first date/time is after the second."),
    BEFORE("Time", "BEFORE", "BEFORE","BEFORE(,)", "BEFORE(,)", Arity.BINARY, "Returns true if the first date/time is before the second."),
    TODAY("Time", "TODAY", "TODAY","TODAY()", "TODAY()", Arity.LEAF, "Returns the current date."),
    DATE("Time", "DATE", "DATE","DATE(,,)", "DATE(,,)", Arity.TERNARY, "Constructs a date from year, month, and day values."),
    DOY("Time", "DOY", "DOY", "DOY()", "DOY()", Arity.UNARY, "Returns the day of the year for a date."),
    DOM("Time", "DOM", "DOM", "DOM()", "DOM()", Arity.UNARY, "Returns the day of the month for a date."),
    YEAR("Time","YEAR", "YEAR", "YEAR()", "YEAR()", Arity.UNARY, "Returns the calendar year for a date."),
    WATERYEAR("Time","WATERYEAR", "WATERYEAR", "WATERYEAR()", "WATERYEAR()", Arity.UNARY, "Returns the water year for a date."),
    LEAPYEAR("Time","LEAPYEAR", "LEAPYEAR", "LEAPYEAR()", "LEAPYEAR()", Arity.UNARY, "Returns true if the year of a date is a leap year."),
    MONTH("Time","MONTH", "MONTH", "MONTH()", "MONTH()", Arity.UNARY, "Returns the month for a date."),

    // --- Statisticalal / random ---
    LAG("Statistical", "LAG", "LAG", "LAG(,)", "LAG(,)", Arity.BINARY, "Returns the value of a variable from a prior time step, offset by a given amount."),
    RAND("Statistical", "RAND", "RAND","RAND()", "RAND()", Arity.UNARY, "Returns a random number."),

    // --- Type conversion ---
    INTCOERSION("Conversion", "TOINT", "TOINT","TOINT()", "TOINT()", Arity.UNARY, "Converts a value to an integer."),
    DOUBLECOERSION("Conversion", "TODOUBLE", "TODOUBLE","TODOUBLE()", "TODOUBLE()", Arity.UNARY, "Converts a value to a double."),

    // --- Logical ---
    IF("Logical", "IF", "IF","IF(,,)", "IF(,,)", Arity.TERNARY, "Returns the second value if the first (boolean) value is true, otherwise returns the third value."),

    // --- String ---
    CONCAT("String", "CONCAT", "CONCAT","CONCAT(,)", "CONCAT(,)", Arity.BINARY, "Concatenates two strings."),
    SUBSTRING("String", "SUBSTRING", "SUBSTRING","SUBSTRING(,,)", "SUBSTRING(,,)", Arity.TERNARY, "Returns a substring of a string, given a start index and length."),
    LENGTH("String", "LENGTH", "LENGTH","LENGTH()", "LENGTH()", Arity.UNARY, "Returns the length of a string."),
    LOWER("String", "LOWER", "LOWER","LOWER()", "LOWER()", Arity.UNARY, "Converts a string to lowercase."),
    UPPER("String", "UPPER", "UPPER", "UPPER()", "UPPER()",Arity.UNARY, "Converts a string to uppercase."),
    TRIM("String", "TRIM", "TRIM","TRIM()", "TRIM()", Arity.UNARY, "Removes leading and trailing whitespace from a string."),
    REPLACE("String", "REPLACE", "REPLACE","REPLACE(,,)", "REPLACE(,,)", Arity.TERNARY, "Replaces all occurrences of a substring within a string with another string."),
    CONTAINS("String", "CONTAINS", "CONTAINS","CONTAINS(,)", "CONTAINS(,)", Arity.BINARY, "Returns true if a string contains a given substring."),
    STARTSWITH("String", "STARTSWITH", "STARTSWITH","STARTSWITH(,)", "STARTSWITH(,)", Arity.BINARY, "Returns true if a string starts with a given substring."),
    ENDSWITH("String", "ENDSWITH", "ENDSWITH","ENDSWITH(,)", "ENDSWITH(,)", Arity.BINARY, "Returns true if a string ends with a given substring."),
    //SPLIT("String", "SPLIT", "SPLIT", Arity.BINARY),
    //JOIN("String", "JOIN", "JOIN", Arity.BINARY),

    // --- Leaf nodes ---
    CONSTANT("LEAF", "", "","", "", Arity.LEAF, "A literal constant value."),
    VARIABLE("LEAF", "[", "[","[\"a\"]", "[\"a\"]", Arity.UNARY, "A reference to a named variable.");
    private final String category;
    private final String infix;
    private final String prefix;
    private final String infixSyntax;
    private final String prefixSyntax;
    private final Arity arity;
    private final String description;

    ExpressionOperator(String category, String infix, String prefix, String infixSyntax, String prefixSyntax, Arity arity, String description) {
        this.category = category;
        this.infix = infix;
        this.prefix = prefix;
        this.infixSyntax = infixSyntax;
        this.prefixSyntax = prefixSyntax;
        this.arity = arity;
        this.description = description;
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

    public String getDescription(){
        return description;
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