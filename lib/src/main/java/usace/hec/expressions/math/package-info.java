/**
 * Math operator nodes for numeric expressions.
 *
 * <p>Nodes in this package implement {@link usace.hec.expressions.DoubleExpressionNode} or {@link usace.hec.expressions.IntegerExpressionNode}
 * and evaluate to {@code double} values. Supported operators:</p>
 * <ul>
 *   <li>Arithmetic: {@code +}, {@code -}, {@code *}, {@code /}, {@code ^} (power)</li>
 *   <li>Unary: {@code -x} (negation)</li>
 *   <li>Functions: {@code ABS(x)}, {@code FLOOR(x)}, {@code CEILING(x)},
 *       {@code MAX(a,b,...)}, {@code MIN(a,b,...)}</li>
 *   <li>Type coercion: {@code TOINT(x)}, {@code TODOUBLE(x)}</li>
 * </ul>
 *
 * <p>Operator precedence follows Excel conventions. Exponentiation is left-associative.</p>
 *
 * @see usace.hec.expressions.DoubleExpressionNode
 * @see usace.hec.expressions.IntegerExpressionNode
 * @see usace.hec.expressions.ExpressionOperator
 */
package usace.hec.expressions.math;