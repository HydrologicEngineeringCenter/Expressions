package usace.hec.expressions.strings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import usace.hec.expressions.BooleanExpressionNode;
import usace.hec.expressions.DataHub;
import usace.hec.expressions.DataProvider;
import usace.hec.expressions.IntegerExpressionNode;
import usace.hec.expressions.IntegerVariableNode;
import usace.hec.expressions.StringConstantNode;
import usace.hec.expressions.StringExpressionNode;
import usace.hec.expressions.StringVariableNode;

public class StringsTest {

    @Test
    public void testConcatenateEvaluate() {
        System.out.print("Concatenate Test\n");

        StringVariableNode x = new StringVariableNode("X");
        StringVariableNode y = new StringVariableNode("Y");
        DataProvider dp = new DataHub();

        StringExpressionNode concat = new ConcatenateNode(x, y);
        concat.setProvider(dp);

        String expression = concat.PreFixSyntax();
        System.out.print(expression + "\n");
        String expressionInfix = concat.ExcelSyntax();
        System.out.print(expressionInfix + "\n");

        dp.setString("X", "Hello, ");
        dp.setString("Y", "World!");
        assertEquals("Hello, World!", concat.evaluate());

        dp.setString("X", "");
        assertEquals("World!", concat.evaluate());
    }

    @Test
    public void testContainsEvaluate() {
        System.out.print("Contains Test\n");

        StringVariableNode source = new StringVariableNode("SOURCE");
        StringVariableNode search = new StringVariableNode("SEARCH");
        DataProvider dp = new DataHub();

        BooleanExpressionNode contains = new ContainsNode(source, search);
        contains.setProvider(dp);

        dp.setString("SOURCE", "the quick brown fox");
        dp.setString("SEARCH", "quick");
        assertTrue(contains.evaluate());

        dp.setString("SEARCH", "slow");
        assertFalse(contains.evaluate());

        dp.setString("SEARCH", "");
        assertTrue(contains.evaluate()); // every string contains the empty string
    }

    @Test
    public void testStartsWithAndEndsWithEvaluate() {
        System.out.print("StartsWith and EndsWith Test\n");

        StringVariableNode source = new StringVariableNode("SOURCE");
        StringVariableNode search = new StringVariableNode("SEARCH");
        DataProvider dp = new DataHub();

        BooleanExpressionNode startsWith = new StartsWithNode(source, search);
        BooleanExpressionNode endsWith = new EndsWithNode(source, search);
        startsWith.setProvider(dp);
        endsWith.setProvider(dp);

        dp.setString("SOURCE", "expression");
        dp.setString("SEARCH", "expr");
        assertTrue(startsWith.evaluate());
        assertFalse(endsWith.evaluate());

        dp.setString("SEARCH", "sion");
        assertFalse(startsWith.evaluate());
        assertTrue(endsWith.evaluate());
    }

    @Test
    public void testReplaceEvaluate() {
        System.out.print("Replace Test\n");

        StringVariableNode source = new StringVariableNode("SOURCE");
        StringConstantNode target = new StringConstantNode("cat");
        StringConstantNode replacement = new StringConstantNode("dog");
        DataProvider dp = new DataHub();

        StringExpressionNode replace = new ReplaceNode(source, target, replacement);
        replace.setProvider(dp);

        dp.setString("SOURCE", "the cat sat on the cat mat");
        assertEquals("the dog sat on the dog mat", replace.evaluate());

        dp.setString("SOURCE", "no matches here");
        assertEquals("no matches here", replace.evaluate());
    }

    @Test
    public void testStringLengthEvaluate() {
        System.out.print("StringLength Test\n");

        StringVariableNode source = new StringVariableNode("SOURCE");
        DataProvider dp = new DataHub();

        IntegerExpressionNode length = new StringLengthNode(source);
        length.setProvider(dp);

        dp.setString("SOURCE", "hello");
        assertEquals(5, length.evaluate());

        dp.setString("SOURCE", "");
        assertEquals(0, length.evaluate());
    }

    @Test
    public void testToLowerAndToUpperEvaluate() {
        System.out.print("ToLower and ToUpper Test\n");

        StringVariableNode source = new StringVariableNode("SOURCE");
        DataProvider dp = new DataHub();

        StringExpressionNode lower = new ToLowerNode(source);
        StringExpressionNode upper = new ToUpperNode(source);
        lower.setProvider(dp);
        upper.setProvider(dp);

        dp.setString("SOURCE", "MiXeD CaSe");
        assertEquals("mixed case", lower.evaluate());
        assertEquals("MIXED CASE", upper.evaluate());
    }

    @Test
    public void testTrimEvaluate() {
        System.out.print("Trim Test\n");

        StringVariableNode source = new StringVariableNode("SOURCE");
        DataProvider dp = new DataHub();

        StringExpressionNode trim = new TrimNode(source);
        trim.setProvider(dp);

        dp.setString("SOURCE", "   padded string   ");
        assertEquals("padded string", trim.evaluate());

        dp.setString("SOURCE", "no padding");
        assertEquals("no padding", trim.evaluate());
    }

    @Test
    public void testSubstringEvaluate() {
        System.out.print("Substring Test\n");

        StringVariableNode source = new StringVariableNode("SOURCE");
        IntegerVariableNode begin = new IntegerVariableNode("BEGIN");
        IntegerVariableNode end = new IntegerVariableNode("END");
        DataProvider dp = new DataHub();

        StringExpressionNode substr = new SubstringNode(source, begin, end);
        substr.setProvider(dp);

        dp.setString("SOURCE", "expressions");
        dp.setInt("BEGIN", 0);
        dp.setInt("END", 4);
        assertEquals("expr", substr.evaluate());
        assertFalse(substr.hasError());

        dp.setInt("BEGIN", 4);
        dp.setInt("END", 11);
        assertEquals("essions", substr.evaluate());
        assertFalse(substr.hasError());

        // zero-length substring at the boundary is valid
        dp.setInt("BEGIN", 11);
        dp.setInt("END", 11);
        assertEquals("", substr.evaluate());
        assertFalse(substr.hasError());

        //Error testing
        dp.setInt("BEGIN", 20);
        dp.setInt("END", 11);
        assertEquals("", substr.evaluate());
        assertTrue(substr.hasError());

        dp.setInt("BEGIN", 2);
        dp.setInt("END", 30);
        assertEquals("", substr.evaluate());
        assertTrue(substr.hasError());

        dp.setInt("BEGIN", 8);
        dp.setInt("END", 2);
        assertEquals("", substr.evaluate());
        assertTrue(substr.hasError());
    }
}
