/**
 * Core abstract syntax tree interfaces and leaf node types.
 *
 * <h2>Architecture</h2>
 * <p>
 * Expression trees are composed of {@link ExpressionNode} instances arranged
 * in a directed acyclic graph. Leaf nodes ({@link IntegerVariableNode},
 * {@link BooleanVariableNode}, {@link DoubleVariableNode}, {@link DateTimeVariableNode}) hold or reference data({@link UpdateableLeafNode}). Interior
 * nodes ({@link BinaryExpressionNode} and {@link UnaryExpressionNode}) apply
 * operators to their children.
 * </p>
 *
 * <h2>Usage Pattern</h2>
 * <pre>{@code
 * DoubleExpressionNode x = DoubleVariableNode(1.0);
 * DoubleExpresionNode y = DoubleVariableNode(2.0);
 * double result = DoubleIfNode(
 *     DoubleEaualToNode(x, y),
 *     DoubleAddNode(x, y),
 *     DoubleMultiplyNode(x, y)
 * );
 * // Optionally register listeners with a DataProvider when using UpdatableLeafNodes, then call result.evaluate()
 * }</pre>
 *
 * <h2>Thread Safety</h2>
 * <p>
 * Expression trees are immutable once constructed. {@code evaluate()} is
 * thread-safe as long as the underlying {@link DataProvider} is thread-safe.
 * Calling {@code setProvider()} on a tree that is already being evaluated
 * concurrently is not safe.
 * </p>
 *
 * <h2>Serialization</h2>
 * <p>
 * All node types implement {@link java.io.Serializable}. Use
 * {@code ObjectOutputStream} / {@code ObjectInputStream} to persist and
 * restore expression trees. See {@link SerializationTest} for examples.
 * </p>
 *
 * @see usace.hec.expressions.parsing
 * @see usace.hec.expressions.math
 * @see usace.hec.expressions.logical
 * @see usace.hec.expressions.comparison
 * @see usace.hec.expressions.strings
 * @see usace.hec.expressions.time
 */
package usace.hec.expressions;