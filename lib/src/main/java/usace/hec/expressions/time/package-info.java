/**
 * Time and date operator nodes.
 *
 * <p>Nodes in this package implement {@link usace.hec.expressions.DateTimeExpressionNode}, {@link usace.hec.expressions.IntegerExpressionNode} or {@link usace.hec.expressions.BooleanExpressionNode}
 * and evaluate to {@link java.time.LocalDateTime} or {@code boolean} values. Supported functions:</p>
 * <ul>
 *   <li>{@code TODAY()} — return the current date and time</li>
 *   <li>{@code Date(year, month, day)} — construct a date from components</li>
 *   <li>{@code DOY(date)} — return day of year (1-366) as integer</li>
 *   <li>{@code AFTER(date1, date2)} — check if date1 is after date2 return boolean</li>
 *   <li>{@code BEFORE(date1, date2)} — check if date1 is before date2 return boolean</li>
 * </ul>
 *
 * @see usace.hec.expressions.DateTimeExpressionNode
 * @see usace.hec.expressions.ExpressionOperator
 */
package usace.hec.expressions.time;