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
        TEST_CASES.add(Map.of("input", "(1 + 2) * (3 - 1)", "error", false, "msg", "", "result", 6));
        TEST_CASES.add(Map.of("input", "2 / 3", "error", false, "msg", "", "result", 2.0/3.0));
        TEST_CASES.add(Map.of("input", "2 // 3", "error", false, "msg", "", "result", 0));
        TEST_CASES.add(Map.of("input", "((2 + 3) * 4 - 5) / 3", "error", false, "msg", "", "result", 5.0));
        TEST_CASES.add(Map.of("input", "((2 + 3) * 4 - 5) // 3", "error", false, "msg", "", "result", 5));
        TEST_CASES.add(Map.of("input", "2 + 3 * 4 ^ 2", "error", false, "msg", "", "result", 50));
        TEST_CASES.add(Map.of("input", "(2.0 + 3.0) * (4.0 - 1.0)", "error", false, "msg", "", "result", 15.0));
        TEST_CASES.add(Map.of("input", "-(3 + 4)", "error", false, "msg", "", "result", -7));
        TEST_CASES.add(Map.of("input", "- - (5.0 + 3.0)", "error", false, "msg", "", "result", 8.0));
        TEST_CASES.add(Map.of("input", "((((1+2))))", "error", false, "msg", "", "result", 3));
        TEST_CASES.add(Map.of("input", "IF(MIN(1.0,2.0) > MAX(2.0,1.0), 11.0, 12.0)", "error", false, "msg", "", "result", 12.0));
        TEST_CASES.add(Map.of("input", "IF(MIN(1,2) > MAX(2,1), 11, 12)", "error", false, "msg", "", "result", 12));
        TEST_CASES.add(Map.of("input", "IF(MIN(1.0,2.0) < MAX(2.0,1.0), 11.0, 12.0)", "error", false, "msg", "", "result", 11.0));
        TEST_CASES.add(Map.of("input", "IF(1.0 > 0.0, IF(2.0 > 3.0, 10.0, 20.0), 30.0)", "error", false, "msg", "", "result", 20.0));
        TEST_CASES.add(Map.of("input", "IF(IF(TRUE, FALSE, TRUE), 1, 2)", "error", false, "msg", "", "result", 2));
        TEST_CASES.add(Map.of("input", "IF(MAX(5.0,2.0) > MIN(3.0,4.0), MAX(5.0,MIN(6.0,7.0)), 0.0)", "error", false, "msg", "", "result", 6.0));
        TEST_CASES.add(Map.of("input", "IF(TRUE, 1, 2.0)", "error", false, "msg", "", "result", 1.0));
        TEST_CASES.add(Map.of("input", "MAX(1, 2.0)", "error", false, "msg", "", "result", 2.0));
        TEST_CASES.add(Map.of("input", "MAX(TOINT(2.5), MIN(3, 10))", "error", false, "msg", "", "result", 3));
        TEST_CASES.add(Map.of("input", "MIN(MAX(1.0,2.0,3.0), MAX(0.0,1.0))", "error", false, "msg", "", "result", 1.0));
        TEST_CASES.add(Map.of("input", "ABS(ABS(-5.0))", "error", false, "msg", "", "result", 5.0));
        TEST_CASES.add(Map.of("input", "CEILING(FLOOR(3.7))", "error", false, "msg", "", "result", 3.0));
        TEST_CASES.add(Map.of("input", "TOINT( TODOUBLE( 5))", "error", false, "msg", "", "result", 5));
        TEST_CASES.add(Map.of("input", "3.0 > 11.0", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "2^2^3", "error", false, "msg", "", "result", 64));
        TEST_CASES.add(Map.of("input", "3.0 < 11.0", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "3.0 >= 11.0", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "3.0 <= 11.0", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "3.0 == 11.0", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "3.0 != 11.0", "error", false, "msg", "", "result", true));
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
        TEST_CASES.add(Map.of("input", "BETWEEN(1,2,3)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "AND(TRUE, FALSE)", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "XOR(TRUE, TRUE)", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "XOR(TRUE, FALSE)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "NOT(FALSE)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "(3.0 > 1.0) && (2.0 < 5.0)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "(3.0 > 1.0) || (2.0 > 5.0)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "NOT(3.0 > 5.0)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "(TRUE && FALSE) || (TRUE && TRUE)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "((1 > 0) && (2 > 1)) || FALSE", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "!!TRUE", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "!(TRUE && FALSE)", "error", false, "msg", "", "result", true));
        //Date Tests
        TEST_CASES.add(Map.of("input", "TODAY()", "error", false, "msg", "", "result", LocalDateTime.now()));
        TEST_CASES.add(Map.of("input", "Date(1983,12,25)", "error", false, "msg", "", "result", LocalDateTime.of(1983, 12, 25, 0, 0)));
        TEST_CASES.add(Map.of("input", "DOY(Date(1983,12,25))", "error", false, "msg", "", "result", 359));
        TEST_CASES.add(Map.of("input", "DOM(Date(1983,12,25))", "error", false, "msg", "", "result", 25));
        TEST_CASES.add(Map.of("input", "YEAR(Date(1983,12,25))", "error", false, "msg", "", "result", 1983));
        TEST_CASES.add(Map.of("input", "MONTH(Date(1983,12,25))", "error", false, "msg", "", "result", 12));
        TEST_CASES.add(Map.of("input", "WATERYEAR(Date(1983,12,25))", "error", false, "msg", "", "result", 1984));
        TEST_CASES.add(Map.of("input", "WATERYEAR(Date(1983,10,1))", "error", false, "msg", "", "result", 1984));
        TEST_CASES.add(Map.of("input", "WATERYEAR(Date(1983,9,30))", "error", false, "msg", "", "result", 1983));
        TEST_CASES.add(Map.of("input", "LEAPYEAR(Date(1983,12,25))", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "LEAPYEAR(Date(1984,12,25))", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "AFTER(Date(1983,12,25),Date(1982,12,25))", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "BEFORE(Date(1983,12,25),Date(1982,12,25))", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "Date(1983,12,25) != Date(1982,12,25)", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "IF(AFTER(TODAY(),Date(1982,12,25)), TODAY(), Date(1982,12,25))", "error", false, "msg", "", "result", LocalDateTime.now()));
        TEST_CASES.add(Map.of("input", "YEAR(IF(TRUE, Date(1999,1,1), Date(2000,1,1)))", "error", false, "msg", "", "result", 1999));
        TEST_CASES.add(Map.of("input", "DOY(IF(AFTER(Date(2000,1,1),Date(1999,1,1)), Date(2000,3,1), Date(1999,3,1)))", "error", false, "msg", "", "result", 61));
        TEST_CASES.add(Map.of("input", "WATERYEAR(IF(LEAPYEAR(Date(2000,1,1)), Date(1983,10,1), Date(1983,9,30)))", "error", false, "msg", "", "result", 1984));
        TEST_CASES.add(Map.of("input", "DOY(Date(1983,12,25)) + DOM(Date(1983,12,25))", "error", false, "msg", "", "result", 384));
        TEST_CASES.add(Map.of("input", "YEAR(Date(1983,12,25)) - MONTH(Date(1983,12,25))", "error", false, "msg", "", "result", 1971));
        //Coercion Tests
        TEST_CASES.add(Map.of("input", "TOINT(2^2^3)", "error", false, "msg", "", "result", 64));
        TEST_CASES.add(Map.of("input", "TODOUBLE(2^2^3)", "error", false, "msg", "", "result", 64.0));
        TEST_CASES.add(Map.of("input", "TOINT(2.0^2.0^3.0)", "error", false, "msg", "", "result", 64));
        TEST_CASES.add(Map.of("input", "TODOUBLE(2.0^2.0^3.0)", "error", false, "msg", "", "result", 64.0));
        TEST_CASES.add(Map.of("input", "TOINT(2.0 + 3.0 * 2.0)", "error", false, "msg", "", "result", 8));
        TEST_CASES.add(Map.of("input", "TODOUBLE(2 + 3 * 2)", "error", false, "msg", "", "result", 8.0));
        TEST_CASES.add(Map.of("input", "(TODOUBLE(2) + 3.0) * 2.0", "error", false, "msg", "", "result", 10.0));
        //String Tests
        TEST_CASES.add(Map.of("input", "CONCAT(\"Hello, \",\"World!\")", "error", false, "msg", "", "result", "Hello, World!"));
        TEST_CASES.add(Map.of("input", "UPPER(CONCAT(\"foo\",\"bar\"))", "error", false, "msg", "", "result", "FOOBAR"));
        TEST_CASES.add(Map.of("input", "CONTAINS(\"Hello World\",\"World\")", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "CONTAINS(\"Hello World\",\"xyz\")", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "STARTSWITH(\"Hello World\",\"Hello\")", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "STARTSWITH(\"Hello World\",\"World\")", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "ENDSWITH(\"Hello World\",\"World\")", "error", false, "msg", "", "result", true));
        TEST_CASES.add(Map.of("input", "ENDSWITH(\"Hello World\",\"Hello\")", "error", false, "msg", "", "result", false));
        TEST_CASES.add(Map.of("input", "REPLACE(\"Hello World\",\"World\",\"There\")", "error", false, "msg", "", "result", "Hello There"));
        TEST_CASES.add(Map.of("input", "SUBSTRING(\"Hello World\",0,5)", "error", false, "msg", "", "result", "Hello"));
        TEST_CASES.add(Map.of("input", "LENGTH(\"Hello\")", "error", false, "msg", "", "result", 5));
        TEST_CASES.add(Map.of("input", "LOWER(\"Hello World\")", "error", false, "msg", "", "result", "hello world"));
        TEST_CASES.add(Map.of("input", "UPPER(\"Hello World\")", "error", false, "msg", "", "result", "HELLO WORLD"));
        TEST_CASES.add(Map.of("input", "TRIM(\"  Hello  \")", "error", false, "msg", "", "result", "Hello"));
        TEST_CASES.add(Map.of("input", "UPPER(TRIM(\"  hello  \"))", "error", false, "msg", "", "result", "HELLO"));
        TEST_CASES.add(Map.of("input", "CONCAT(UPPER(\"foo\"), LOWER(\"BAR\"))", "error", false, "msg", "", "result", "FOObar"));
        TEST_CASES.add(Map.of("input", "REPLACE(CONCAT(\"Hello \", \"World\"), \"World\", \"There\")", "error", false, "msg", "", "result", "Hello There"));
        TEST_CASES.add(Map.of("input", "SUBSTRING(CONCAT(\"Hello\",\" World\"), 0, 5)", "error", false, "msg", "", "result", "Hello"));
        TEST_CASES.add(Map.of("input", "LENGTH(CONCAT(\"foo\",\"bar\"))", "error", false, "msg", "", "result", 6));
        TEST_CASES.add(Map.of("input", "CONTAINS(UPPER(\"hello world\"), \"WORLD\")", "error", false, "msg", "", "result", true));

        // ERROR EXPRESSIONS
        TEST_CASES.add(Map.of("input", "", "error", true, "msg", "Empty expression"));
        TEST_CASES.add(Map.of("input", "1.2.3", "error", true, "msg", "Unexpected token after end of expression"));
        TEST_CASES.add(Map.of("input", "[X", "error", true, "msg", "Unclosed '[' for variable"));
        TEST_CASES.add(Map.of("input", "[]", "error", true, "msg", "Empty variable name"));
        TEST_CASES.add(Map.of("input", "PLU(1.0,2.0)", "error", true, "msg", "Unknown identifier: PLU"));
        TEST_CASES.add(Map.of("input", "1 @ 2", "error", true, "msg", "Unexpected character: '@'"));
        TEST_CASES.add(Map.of("input", "3.0=11.0", "error", true, "msg", "Unexpected character: '='"));
        TEST_CASES.add(Map.of("input", "contains(a,b)", "error", true, "msg", "Unknown identifier: a"));
        TEST_CASES.add(Map.of("input", "IF(1.0<2.0, 3.0)", "error", true, "msg", "IF requires exactly 3 arguments"));
        TEST_CASES.add(Map.of("input", "MAX()", "error", true, "msg", "MAX requires at least 1 argument"));
        TEST_CASES.add(Map.of("input", "ABS(1.0, 2.0)", "error", true, "msg", "Expected ')' but found: Comma"));
        TEST_CASES.add(Map.of("input", "((1.0+2.0)", "error", true, "msg", "Unexpected end of input, expected ')'"));
        TEST_CASES.add(Map.of("input", "1.0+2.0)", "error", true, "msg", "Unexpected token after end of expression"));
        TEST_CASES.add(Map.of("input", "IF(1.0 > 0.0, IF(2.0 > 3.0, 10.0), 30.0)", "error", true, "msg", "IF requires exactly 3 arguments"));
        TEST_CASES.add(Map.of("input", "MAX(1.0, ABS(2.0,3.0))", "error", true, "msg", "Expected ')' but found: Comma"));
        TEST_CASES.add(Map.of("input", "IF(3.0 > 1.0, 1.0, TRUE)", "error", true, "msg", "IF branches must have compatible types"));
        TEST_CASES.add(Map.of("input", "1.0 + TRUE", "error", true, "msg", "Type mismatch for PLUS"));
        TEST_CASES.add(Map.of("input", "CONCAT(\"a\", 1.0)", "error", true, "msg", "Type mismatch for CONCAT"));
        TEST_CASES.add(Map.of("input", "MAX(1.0, (2.0 + 3.0)", "error", true, "msg", "Unexpected end of input, expected ')'"));
        TEST_CASES.add(Map.of("input", "MAX(1.0, FOO(2.0))", "error", true, "msg", "Unknown identifier: FOO"));
        TEST_CASES.add(Map.of("input", "SUBSTRING(CONCAT(\"a\",\"b\"), 0)", "error", true, "msg", "requires exactly 3 arguments"));
    }

    @Test
    public void testAllCases() {
        

        for (Map<String, Object> testCase : TEST_CASES) {
            String input = (String) testCase.get("input");
            boolean expectError = (boolean) testCase.get("error");
            String expectedMsg = (String) testCase.get("msg");

            System.out.println(testCase);

            ParseResult result = ExpressionParser.parse(input, null);

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
                } else if (expected instanceof String) {
                    String actualString = ((StringExpressionNode) node).evaluate();
                    assertEquals("Input: \"" + input + "\"", expected, actualString);
                } else {
                    assertEquals("Input: \"" + input + "\"", expected, "nothing worked");
                }
            }
        }
    }
}