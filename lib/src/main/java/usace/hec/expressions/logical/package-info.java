/**
 * Logical operator nodes for boolean and logical expressions.
 *
 * <p>Nodes in this package implement {@link usace.hec.expressions.BooleanExpressionNode}, {@link usace.hec.expressions.DoubleExpressionNode}, {@link usace.hec.expressions.DateTimeExpressionNode}, or {@link usace.hec.expressions.IntegerExpressionNode}
 * and evaluate to {@code boolean} values. Supported operators:</p>
 * <ul>
 *   <li>{@code AND(a, b, ...)} — logical AND (infix: {@code &&})</li>
 *   <li>{@code OR(a, b, ...)} — logical OR (infix: {@code ||})</li>
 *   <li>{@code XOR(a, b, ...)} — logical XOR</li>
 *   <li>{@code IF(condition, then, else)} — ternary conditional that can produce double, integer, or dateTime</li>
 * </ul>
 *
 * @see usace.hec.expressions.BooleanExpressionNode
 * @see usace.hec.expressions.DoubleExpressionNode
 * @see usace.hec.expressions.IntegerExpressionNode
 * @see usace.hec.expressions.DateTimeExpressionNode
 * @see usace.hec.expressions.ExpressionOperator
 */
package usace.hec.expressions.logical;