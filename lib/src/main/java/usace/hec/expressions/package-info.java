/**
 * Core expression tree interfaces and node implementations for the HEC expression library.
 *
 * <p>The expression library parses strings into typed expression trees that evaluate to
 * primitive types: {@code boolean}, {@code int}, {@code double}, {@code String}, or
 * {@link java.time.LocalDateTime}. Each node type implements a typed expression interface
 * that declares {@code evaluate()} returning the appropriate type.</p>
 *
 * <h2>Core Interfaces</h2>
 * <ul>
 *   <li>{@link ExpressionNode} — base interface for all expression nodes. Declares
 *       {@link ExpressionNode#PreFixSyntax()}, {@link ExpressionNode#ExcelSyntax()},
 *       {@link ExpressionNode#fetchListeners()}, {@link ExpressionNode#resultType()},
 *       and {@link ExpressionNode#Operator()}.</li>
 *   <li>{@link BooleanExpressionNode} — evaluates to {@code boolean}. Used for logical
 *       operators ({@code AND}, {@code OR}, {@code XOR}), comparisons ({@code ==},
 *       {@code >}, {@code <}, {@code >=}, {@code <=}), and boolean constants.</li>
 *   <li>{@link IntegerExpressionNode} — evaluates to {@code int}. Used for integer
 *       constants and type coercion via {@code TOINT()}.</li>
 *   <li>{@link DoubleExpressionNode} — evaluates to {@code double}. Used for numeric
 *       constants, arithmetic operators ({@code +}, {@code -}, {@code *}, {@code /},
 *       {@code ^}), and math functions ({@code ABS}, {@code FLOOR}, {@code CEILING},
 *       {@code MAX}, {@code MIN}).</li>
 *   <li>{@link StringExpressionNode} — evaluates to {@code String}. Used for string
 *       constants and string functions ({@code CONCAT}, {@code UPPER}, {@code LOWER},
 *       {@code TRIM}, {@code SUBSTRING}, {@code LENGTH}, {@code CONTAINS},
 *       {@code STARTSWITH}, {@code ENDSWITH}, {@code REPLACE}).</li>
 *   <li>{@link DateTimeExpressionNode} — evaluates to {@link java.time.LocalDateTime}.
 *       Used for {@code TODAY()}, {@code Date(year,month,day)}, and date comparisons
 *       ({@code AFTER}, {@code BEFORE}).</li>
 * </ul>
 *
 * <h2>Constant Nodes</h2>
 * <ul>
 *   <li>{@link BooleanConstantNode} — holds a {@code boolean} literal (TRUE/FALSE)</li>
 *   <li>{@link IntegerConstantNode} — holds an {@code int} literal</li>
 *   <li>{@link DoubleConstantNode} — holds a {@code double} literal</li>
 *   <li>{@link StringConstantNode} — holds a {@code String} literal</li>
 * </ul>
 *
 * <h2>Variable Nodes</h2>
 * <p>Variable nodes reference external data by name using bracket syntax: {@code [variableName]}.
 *
 *
 * <h2>Data Flow</h2>
 * <p>Variable nodes can receive values from a {@link DataProvider}. Attach a provider
 * using {@link ExpressionNode#setProvider(DataProvider)}.
 *
 * <h2>Parsing</h2>
 * <p>Use {@link ExpressionParser} to parse expression strings into {@link ExpressionNode}
 * trees. The parser returns a {@link ParseResult} containing either the root node or
 * an error. Expression syntax supports:</p>
 * <ul>
 *   <li>Infix operators: {@code +}, {@code -}, {@code *}, {@code /}, {@code ^},
 *       {@code ==}, {@code >}, {@code <}, {@code >=}, {@code <=}, {@code &&}, {@code ||}</li>
 *   <li>Function calls: {@code IF(condition, then, else)}, {@code MAX(a,b)},
 *       {@code ABS(x)}, {@code CONCAT(s1, s2)}, {@code TODAY()}, etc.</li>
 *   <li>Variables: {@code [variableName]}</li>
 *   <li>Literals: numeric ({@code 1}, {@code 3.14}), boolean ({@code TRUE}, {@code FALSE}),
 *       string ({@code "text"})</li>
 * </ul>
 *
 * <h2>Operator Precedence</h2>
 * <p>Operator precedence follows Excel conventions. Exponentiation ({@code ^}) is
 * left-associative: {@code 2^2^3} evaluates as {@code (2^2)^3 = 64}.</p>
 *
 * <h2>Serialization</h2>
 * <p>All expression nodes implement {@link java.io.Serializable}. Trees can be
 * serialized and deserialized for persistence or network transfer.</p>
 *
 * @see ExpressionParser
 * @see ParseResult
 * @see ExpressionType
 * @see ExpressionOperator
 * @see usace.hec.expressions.math
 * @see usace.hec.expressions.logical
 * @see usace.hec.expressions.comparison
 * @see usace.hec.expressions.strings
 * @see usace.hec.expressions.time
 */
package usace.hec.expressions;