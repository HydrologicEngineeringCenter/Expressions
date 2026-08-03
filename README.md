# Expressions
A Java 21 library that parses Excel-compatible expression strings into typed Abstract Syntax Trees (ASTs). The ASTs evaluate to primitives, support serialization, and update external variables through a data observer pattern.

# Quick Start
```java
ExpressionParser parser = new ExpressionParser();

// Parse an expression string
ParseResult<ExpressionNode> result = parser.parse("IF([a] > 5.0, [a] * 2.0, [b] + 10.0)");

if (!result.hasError()) {
    ExpressionNode tree = result.getNode();
    
    // Attach a DataProvider to resolve variable names
    DataProvider provider = name -> {
        if (name.equals("a")) return 10.0;
        if (name.equals("b")) return 3.0;
        return null;
    };
    tree.setProvider(provider);
    
    // Cast to the typed interface and evaluate
    DoubleExpressionNode typedTree = (DoubleExpressionNode) tree;
    double value = typedTree.evaluate(); // 20.0
} else {
    ParseError err = result.getError();
    System.err.println("Parse error at " + err.position() + ": " + err.message());
}
```
# Supported Syntax
## Literals
|Type|Examples|
|----|------|
|Integer | 42, 0, -17 |
|Double | 3.14, 0.0, 1e10 |
|Boolean | TRUE, FALSE |
|String | "Hello, World!" |

## Variables
Bracket syntax references external data:

`[X], [flowRate], [temperature]`

A DataProvider can provide data at execution time for each variable to update the value reperesented and produce a different result from the overall AST.


Infix Operators

|Precedence|	Operators|	Example|
|-|-|-|
|Lowest |	`||` (OR) |	`a || b`|
||`^^` (XOR)|	`a ^^ b`|
||`&&`|	`a && b`|
||`==`, `>`, `<`, `>=`, `<=`|	`a == b`|
||`+`, `-`	|`a + b`|
||`*`, `/`	|`a * b`|
|Highest|	`^`|	`2 ^ 2 ^ 3`|

Note: Operator precedence follows Excel conventions. Exponentiation is left-associative: `2^2^3` evaluates as `(2^2)^3 = 64`.

# Functions
|Function|	Arguments	|Returns	|Description|
|-|-|-|-|
|IF(cond, then, else)|	3	|double/int/date|	Conditional branching|
|MAX(a, b)|	2	|double/int	|Maximum of arguments|
|MIN(a, b)|	2	|double/int	|Minimum of arguments|
|ABS(x)|	1	|double/int	|Absolute value|
|FLOOR(x)|	1	|double	|Floor value|
|CEILING(x)|	1	|double	Ceiling value|
|TOINT(x)|	1	|int	Coerce to integer|
|TODOUBLE(x)|	1	|double|	Coerce to double
|TODAY()|	0	|date	|Current date/time|
|Date(y, m, d)|	3	|date	|Construct date|
|DOY(date)	|1	|int	|Day of year (1-366)|
|AFTER(d1, d2)|	2|	boolean	Date comparison|
|BEFORE(d1, d2)|	2|	boolean	Date comparison|
|CONCAT(s1, s2)|	2|	string	Concatenate strings|
|UPPER(s)	|1	|string	Uppercase|
|LOWER(s)|	1|	string	|Lowercase|
|TRIM(s)|	1	|string	|Remove whitespace|
|SUBSTRING(s, start, end)|	3	|string	Extract substring|
|LENGTH(s)	|1	|int|	String |length|
|CONTAINS(s, sub)|	2	|boolean|	Check substring|
|STARTSWITH(s, prefix)|	2|	boolean|	Check prefix|
|ENDSWITH(s, suffix)|	2|	boolean|	Check suffix|
|REPLACE(s, target, rep)|	3|	string|	Replace text|
#  Examples
```css
IF([flow] > 100.0, [flow] * 0.8, [flow])
MAX([a], [b], [c])
ABS([temp] - 32.0)
CONCAT([firstName], " ", [lastName])
DOY(Date(2024, 12, 25))
```

# Type System
The library uses five typed expression interfaces, each declaring a typed evaluate() method:

|Interface|	Return Type|	Example Nodes|
|-|-|-|
|DoubleExpressionNode	|double	|DoubleConstantNode, DoubleAddNode, DoubleIfNode|
|IntegerExpressionNode|	int	|IntegerConstantNode, IntegerAddNode, IntegerIfNode|
|BooleanExpressionNode|	boolean	|BooleanConstantNode, AndNode, DoubleGreaterThanNode|
|StringExpressionNode|	String	|StringConstantNode, ConcatenateNode, ToUpperNode|
|DateTimeExpressionNode	|LocalDateTime	|TodayNode, DateNode, DateTimeIfNode|

#Automatic Coercion

The parser applies widening coercion automatically:

`int + double` → int promoted to double, produces DoubleAddNode
`MAX(1, 2.5)` → int promoted to double, produces DoubleMaxNode chain
Narrowing requires explicit functions: TOINT(3.7) or DATE(y, m, d) (args coerced to int).

# Variable Nodes & Data Flow
Variable nodes reference external data by name and update through the observer pattern:

```java
// Parse expression with variables
ExpressionParser parser = new ExpressionParser();
ExpressionNode tree = parser.parse("[inflow] - [outflow]").getNode();

// Attach a DataProvider that resolves variable names
DataProvider provider = name -> {
    if (name.equals("inflow")) return 100.0;
    if (name.equals("outflow")) return 30.0;
    return null;
};
tree.setProvider(provider);

// Evaluate
DoubleExpressionNode typedTree = (DoubleExpressionNode) tree;
double result = typedTree.evaluate(); // 70.0
```
# Listener Collection
Collect all variable listeners from a tree to register with a DataUpdater:

```java
List<DataListener> listeners = tree.fetchListeners();
for (DataListener listener : listeners) {
    dataUpdater.register(listener);
}
// When data changes, DataUpdater calls onDataUpdate() on each listener
```
# UpdateableLeafNode
For manual tree construction (without parsing), UpdateableLeafNode is the base class for variable nodes:

```java
UpdateableLeafNode var = new UpdateableLeafNode("flowRate");
var.setProvider(provider);
Object value = var.evaluate(); // delegates to provider.provideValue("flowRate")
```
# Error Handling
ParseResult<T> returns either a success node or a typed error:

```java
ParseResult<ExpressionNode> result = parser.parse("IF(1.0 < 2.0)");

if (result.hasError()) {
    ParseError err = result.getError();
    System.err.println(err.message());     // "IF requires exactly 3 arguments"
    System.err.println(err.position());    // character index in input
    System.err.println(err.remaining());   // remaining input snippet
}
```
Common errors:

Empty expression
Unclosed brackets [X
Unknown identifiers PLU(1,2)
Invalid characters 1 @ 2
Wrong argument count IF(a, b) (missing third arg)
Type mismatch TRUE + 5
# Serialization
All expression nodes implement java.io.Serializable. Trees can be persisted and restored:

```java
// Serialize
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tree.ser"))) {
    oos.writeObject(tree);
}

// Deserialize
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tree.ser"))) {
    ExpressionNode restored = (ExpressionNode) ois.readObject();
}
```

# Syntax Generation
Each node can render its tree as a string in two formats:

```java
ExpressionNode tree = parser.parse("ABS([x] + 1.0)").getNode();

System.out.println(tree.PreFixSyntax());  // + [x] 1.0 (operator-first)
System.out.println(tree.ExcelSyntax());   // ABS([x]+1.0) (Excel-style)
```
# Build
bash
./gradlew build
Requires Java 21. The build produces a JAR with all node classes, parser, and type system.

# Project Structure
|Package|	Contents|
|-|-|
|usace.hec.expressions|	Core interfaces, constant nodes, variable nodes, parser, factory|
|usace.hec.expressions.math|	Arithmetic and math operator nodes (Add, Subtract, Multiply, Divide, Exponent, Abs, Floor, Ceiling, Max, Min)|
|usace.hec.expressions.logical|	Logical operator nodes (And, Or, Xor, If)|
|usace.hec.expressions.comparison|	Comparison operator nodes (EqualTo, GreaterThan, LessThan, etc.)|
|usace.hec.expressions.strings	|String operator nodes (Concatenate, Upper, Lower, Trim, Substring, Length, Contains, Replace)|
|usace.hec.expressions.time	|Time/date operator nodes (Today, Date, DayOfYear, After, Before)|
