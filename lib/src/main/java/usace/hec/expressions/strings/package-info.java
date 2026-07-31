/**
 * String operator nodes for text expressions.
 *
 * <p>Nodes in this package implement {@link usace.hec.expressions.StringExpressionNode} or {@link usace.hec.expressions.BooleanExpressionNode} 
 * and evaluate to {@code String} values. Supported functions:</p>
 * <ul>
 *   <li>{@code CONCAT(s1, s2, ...)} — concatenate strings</li>
 *   <li>{@code UPPER(s)} — convert to uppercase</li>
 *   <li>{@code LOWER(s)} — convert to lowercase</li>
 *   <li>{@code TRIM(s)} — remove leading/trailing whitespace</li>
 *   <li>{@code SUBSTRING(s, start, length)} — extract substring</li>
 *   <li>{@code LENGTH(s)} — return string length</li>
 *   <li>{@code CONTAINS(s, substring)} — check if string contains substring return boolean</li>
 *   <li>{@code STARTSWITH(s, prefix)} — check if string starts with prefix return boolean</li>
 *   <li>{@code ENDSWITH(s, suffix)} — check if string ends with suffix return boolean</li>
 *   <li>{@code REPLACE(s, target, replacement)} — replace occurrences</li>
 * </ul>
 *
 * @see usace.hec.expressions.StringExpressionNode
 * @see usace.hec.expressions.ExpressionOperator
 */
package usace.hec.expressions.strings;