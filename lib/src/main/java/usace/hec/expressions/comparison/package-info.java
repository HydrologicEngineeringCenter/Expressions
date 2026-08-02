/**
 * Comparison operator nodes for boolean expressions.
 *
 * <p>Nodes in this package implement {@link usace.hec.expressions.BooleanExpressionNode}
 * and evaluate to {@code boolean} values. Supported operators:</p>
 * <ul>
 *   <li>Equality: {@code ==} (infix), {@code EQ(a, b)}</li>
 *   <li>Greater than: {@code >} (infix), {@code GT(a, b)}</li>
 *   <li>Less than: {@code <} (infix), {@code LT(a, b)}</li>
 *   <li>Greater than or equal: {@code >=} (infix), {@code GTE(a, b)}</li>
 *   <li>Less than or equal: {@code <=} (infix), {@code LTE(a, b)}</li>
 * </ul>
 *
 * <p>Comparisons support numeric types ({@code int}, {@code double}) and
 * {@link java.time.LocalDateTime} values, within any operator the left and right node must agree in resultType.</p>
 *
 * @see usace.hec.expressions.BooleanExpressionNode
 * @see usace.hec.expressions.ExpressionOperator
 */
package usace.hec.expressions.comparison;