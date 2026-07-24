package usace.hec.expressions;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TokenizerAndParserTest {

    /**
     * Data-driven test runner.
     * Keys: "input", "error" (boolean), "msg" (String), "result" (Object - Double or Boolean)
     */
    private static final List<Map<String, Object>> TEST_CASES = new ArrayList<>();

    static {
        // ----------------------------------------------------------------
        // VALID EXPRESSIONS (Expected Results)
        // ----------------------------------------------------------------
        TEST_CASES.add(Map.of("input", "IF(MIN(1.0,2.0)<MAX(1.0,2.0),1+2,3-4)", "error", false, "msg", "", "result", 3.0));
        TEST_CASES.add(Map.of("input", "PLUS(1, 2) + MULTIPLY(3, 4)", "error", false, "msg", "", "result", 15.0));
        TEST_CASES.add(Map.of("input", "-1+1", "error", false, "msg", "", "result", 0.0));
        TEST_CASES.add(Map.of("input", "- 3^2", "error", false, "msg", "", "result", 9.0));
        TEST_CASES.add(Map.of("input", "(3 + 3) * 2", "error", false, "msg", "", "result", 12.0));
        TEST_CASES.add(Map.of("input", "3 * (3 + 2)", "error", false, "msg", "", "result", 15.0));
        TEST_CASES.add(Map.of("input", "3 + 3 * 2", "error", false, "msg", "", "result", 9.0));
        TEST_CASES.add(Map.of("input", "3 * 3 + 2", "error", false, "msg", "", "result", 11.0));
        TEST_CASES.add(Map.of("input", "3 > 11", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "3 < 11", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "3 >= 11", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "3 <= 11", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "3 == 11", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "TRUE && FALSE", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "TRUE || FALSE", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "- - 5", "error", false, "msg", "", "result", 5.0));
        TEST_CASES.add(Map.of("input", "MAX(10, 20, 5)", "error", false, "msg", "", "result", 20.0));
        TEST_CASES.add(Map.of("input", "MIN(10, 20, 5)", "error", false, "msg", "", "result", 5.0));
        TEST_CASES.add(Map.of("input", "ABS(-5)", "error", false, "msg", "", "result", 5.0));
        TEST_CASES.add(Map.of("input", "FLOOR(3.7)", "error", false, "msg", "", "result", 3.0));
        TEST_CASES.add(Map.of("input", "CEILING(3.2)", "error", false, "msg", "", "result", 4.0));
        // Prefix comparison operators
        TEST_CASES.add(Map.of("input", "GT(5, 3)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "GTE(5, 5)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "LTE(3, 5)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "LT(3, 5)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "EQ(5, 5)", "error", false, "msg", "", "result", true));

        // Prefix logical operators
        TEST_CASES.add(Map.of("input", "AND(TRUE, FALSE)", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "XOR(TRUE, TRUE)", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "XOR(TRUE, FALSE)", "error", false, "msg", "", "result", true));

        // Time/Date functions ()
        //TEST_CASES.add(Map.of("input", "TODAY()", "error", false, "msg", "", "result", 0.0));
        //TEST_CASES.add(Map.of("input", "DOY()", "error", false, "msg", "", "result", 0.0));
        //TEST_CASES.add(Map.of("input", "AFTER()", "error", false, "msg", "", "result", 0.0));
        //TEST_CASES.add(Map.of("input", "BEFORE()", "error", false, "msg", "", "result", 0.0));

        // ----------------------------------------------------------------
        // ERROR EXPRESSIONS (Expected Messages)
        // ----------------------------------------------------------------
        TEST_CASES.add(Map.of("input", "", "error", true, "msg", "Empty expression"));
        TEST_CASES.add(Map.of("input", "1.2.3", "error", true, "msg", "Unexpected token after end of expression: Number[value=0.3, pos=3, error=]"));
        TEST_CASES.add(Map.of("input", "[X", "error", true, "msg", "Unclosed '[' for variable"));
        TEST_CASES.add(Map.of("input", "[]", "error", true, "msg", "Empty variable name"));
        TEST_CASES.add(Map.of("input", "PLU(1,2)", "error", true, "msg", "Unknown identifier: PLU"));
        TEST_CASES.add(Map.of("input", "1 @ 2", "error", true, "msg", "Unexpected character: '@'"));
        
        // Tokenizer errors for unsupported syntax
        TEST_CASES.add(Map.of("input", "3=11", "error", true, "msg", "Unexpected character: '='"));
        TEST_CASES.add(Map.of("input", "1 != 1", "error", true, "msg", "Unexpected character: '!'"));
        TEST_CASES.add(Map.of("input", "contains(a,b)", "error", true, "msg", "Unknown identifier: contains"));
        
        // Parser structural errors
        TEST_CASES.add(Map.of("input", "IF(1<2, 3)", "error", true, "msg", "IF requires exactly 3 arguments"));
        TEST_CASES.add(Map.of("input", "MAX()", "error", true, "msg", "MAX requires at least 1 argument"));
        TEST_CASES.add(Map.of("input", "ABS(1, 2)", "error", true, "msg", "Expected ')' but found: Comma[pos=5, error=]"));
        TEST_CASES.add(Map.of("input", "((1+2)", "error", true, "msg", "Unexpected end of input, expected ')'"));
        TEST_CASES.add(Map.of("input", "1+2)", "error", true, "msg", "Unexpected token after end of expression"));
    }

    @Test
    public void testAllCases() {
        ExpressionParser parser = new ExpressionParser();
        
        for (Map<String, Object> testCase : TEST_CASES) {
            String input = (String) testCase.get("input");
            boolean expectError = (boolean) testCase.get("error");
            String expectedMsg = (String) testCase.get("msg");
            
            ParseResult<ExpressionNode> result = parser.parse(input);
            
            // 1. Verify error state
            assertEquals("Input: \"" + input + "\"", expectError, result.hasError());
            
            if (expectError) {
                // 2. Verify error message
                String actualMsg = result.getError().message();
                assertTrue("Input: \"" + input + "\"\nExpected message to contain: \"" + expectedMsg + "\"\nActual: \"" + actualMsg + "\"",
                        actualMsg.contains(expectedMsg));
            } else {
                // 3. Verify evaluation result
                Object expected = testCase.get("result");
                Object actual = result.getNode().evaluate();
                
                if (expected instanceof Double) {
                    assertEquals("Input: \"" + input + "\"", (Double) expected, (Double) actual, 1e-9);
                } else if (expected instanceof Boolean) {
                    assertEquals("Input: \"" + input + "\"", (Boolean) expected, (Boolean) actual);
                } else {
                    assertEquals("Input: \"" + input + "\"", expected, actual);
                }
            }
        }
    }
}