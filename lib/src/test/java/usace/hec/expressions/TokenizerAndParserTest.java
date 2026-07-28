package usace.hec.expressions;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TokenizerAndParserTest {

    private static final List<Map<String, Object>> TEST_CASES = new ArrayList<>();

    static {
        // VALID EXPRESSIONS
        TEST_CASES.add(Map.of("input", "IF(MIN(1.0,2.0) > MAX(2.0,1.0), 11.0, 12.0)", "error", false, "msg", "", "result", 12.0));
        TEST_CASES.add(Map.of("input", "IF(MIN(1.0,2.0) < MAX(2.0,1.0), 11.0, 12.0)", "error", false, "msg", "", "result", 11.0));
        TEST_CASES.add(Map.of("input", "3.0 > 11.0", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "2^2^3", "error", false, "msg", "", "result", 64));
        TEST_CASES.add(Map.of("input", "3.0 < 11.0", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "3.0 >= 11.0", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "3.0 <= 11.0", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "3.0 == 11.0", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "TRUE && FALSE", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "TRUE || FALSE", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "- - 5.0", "error", false, "msg", "", "result", 5.0));
        TEST_CASES.add(Map.of("input", "MAX(10.0, 20.0, 5.0)", "error", false, "msg", "", "result", 20.0));
        TEST_CASES.add(Map.of("input", "MIN(10.0, 20.0, 5.0)", "error", false, "msg", "", "result", 5.0));
        TEST_CASES.add(Map.of("input", "ABS(-5.0)", "error", false, "msg", "", "result", 5.0));
        TEST_CASES.add(Map.of("input", "FLOOR(3.7)", "error", false, "msg", "", "result", 3.0));
        TEST_CASES.add(Map.of("input", "CEILING(3.2)", "error", false, "msg", "", "result", 4.0));
        TEST_CASES.add(Map.of("input", "GT(5.0, 3.0)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "GTE(5.0, 5.0)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "LTE(3.0, 5.0)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "LT(3.0, 5.0)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "EQ(5.0, 5.0)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "AND(TRUE, FALSE)", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "XOR(TRUE, TRUE)", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "XOR(TRUE, FALSE)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "TODAY()", "error", false, "msg", "", "result", LocalDateTime.now()));
        TEST_CASES.add(Map.of("input", "Date(1983,12,25)", "error", false, "msg", "", "result", LocalDateTime.of(1983, 12, 25, 0, 0)));
        TEST_CASES.add(Map.of("input", "DOY(Date(1983,12,25))", "error", false, "msg", "", "result", 359));
        TEST_CASES.add(Map.of("input", "AFTER(Date(1983,12,25),Date(1982,12,25))", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "BEFORE(Date(1983,12,25),Date(1982,12,25))", "error", false, "msg", "", "result", false));

        // ERROR EXPRESSIONS
        TEST_CASES.add(Map.of("input", "", "error", true, "msg", "Empty expression"));
        TEST_CASES.add(Map.of("input", "1.2.3", "error", true, "msg", "Unexpected token after end of expression"));
        TEST_CASES.add(Map.of("input", "[X", "error", true, "msg", "Unclosed '[' for variable"));
        TEST_CASES.add(Map.of("input", "[]", "error", true, "msg", "Empty variable name"));
        TEST_CASES.add(Map.of("input", "PLU(1.0,2.0)", "error", true, "msg", "Unknown identifier: PLU"));
        TEST_CASES.add(Map.of("input", "1 @ 2", "error", true, "msg", "Unexpected character: '@'"));
        TEST_CASES.add(Map.of("input", "3.0=11.0", "error", true, "msg", "Unexpected character: '='"));
        TEST_CASES.add(Map.of("input", "1.0 != 1.0", "error", true, "msg", "Unexpected character: '!'"));
        TEST_CASES.add(Map.of("input", "contains(a,b)", "error", true, "msg", "Unknown identifier: contains"));
        TEST_CASES.add(Map.of("input", "IF(1.0<2.0, 3.0)", "error", true, "msg", "IF requires exactly 3 arguments"));
        TEST_CASES.add(Map.of("input", "MAX()", "error", true, "msg", "MAX requires at least 1 argument"));
        TEST_CASES.add(Map.of("input", "ABS(1.0, 2.0)", "error", true, "msg", "Expected ')' but found: Comma"));
        TEST_CASES.add(Map.of("input", "((1.0+2.0)", "error", true, "msg", "Unexpected end of input, expected ')'"));
        TEST_CASES.add(Map.of("input", "1.0+2.0)", "error", true, "msg", "Unexpected token after end of expression"));
    }

    @Test
    public void testAllCases() {
        ExpressionParser parser = new ExpressionParser();

        for (Map<String, Object> testCase : TEST_CASES) {
            String input = (String) testCase.get("input");
            boolean expectError = (boolean) testCase.get("error");
            String expectedMsg = (String) testCase.get("msg");

            System.out.println(testCase);
            ParseResult result = parser.parse(input);

            assertEquals("Input: \"" + input + "\"", expectError, result.hasError());

            if (expectError) {
                String actualMsg = result.getError().message();
                assertTrue("Input: \"" + input + "\"\nExpected: \"" + expectedMsg + "\"\nActual: \"" + actualMsg + "\"",
                        actualMsg.contains(expectedMsg));
            } else {
                Object expected = testCase.get("result");
                ExpressionNode node = (ExpressionNode)result.getNode();
                //Object actual = node.evaluate();

                if (expected instanceof Double) {
                    double actualDouble = ((DoubleExpressionNode) node).evaluate();
                    assertEquals("Input: \"" + input + "\"", (Double) expected, actualDouble, 1e-9);
                } else if (expected instanceof Boolean) {
                    boolean actualBool = ((BooleanExpressionNode) node).evaluate();
                    assertEquals("Input: \"" + input + "\"", (Boolean) expected, actualBool);
                } else if (expected instanceof LocalDateTime) {
                    LocalDateTime actualDate = ((DateTimeExpressionNode)node).evaluate();
                    assertEquals("Input: \"" + input + "\"",
                            ((LocalDateTime) expected).getYear(), actualDate.getYear());
                    assertEquals("Input: \"" + input + "\"",
                            ((LocalDateTime) expected).getMonthValue(), actualDate.getMonthValue());
                    assertEquals("Input: \"" + input + "\"",
                            ((LocalDateTime) expected).getDayOfMonth(), actualDate.getDayOfMonth());
                } else if (expected instanceof Integer) {
                    int actualInt = ((IntegerExpressionNode) node).evaluate();
                    assertEquals("Input: \"" + input + "\"", (int) expected, actualInt);
                } else {
                    assertEquals("Input: \"" + input + "\"", expected, "nothing worked");
                }
            }
        }
    }
}