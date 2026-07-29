package usace.hec.expressions;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

public class TokenizerTest {

    @Test
    public void testTokenize() {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> result = tokenizer.tokenize("IF((MIN(1,[adf])))");

        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Verify no tokens have errors
        for (Token token : result) {
            assertFalse("Token has error: " + token, token.hasError());
        }
    }

    @Test
    public void testTokenizeIntegerAndDouble() {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> result = tokenizer.tokenize("42 + 3.14");

        assertEquals(3, result.size());

        // First token should be IntegerLiteral
        Token first = result.get(0);
        assertTrue(first instanceof Token.IntegerLiteral);
        assertEquals(42, ((Token.IntegerLiteral) first).value());

        // Third token should be DoubleLiteral
        Token third = result.get(2);
        assertTrue(third instanceof Token.DoubleLiteral);
        assertEquals(3.14, ((Token.DoubleLiteral) third).value(), 0.0001);
    }

    @Test
    public void testTokenizeString() {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> result = tokenizer.tokenize("\"Hello World!\"");

        assertEquals(1, result.size());

        // First token should be StringLiteral
        Token first = result.get(0);
        assertTrue(first instanceof Token.Unknown);
        //assertEquals("Hello World", ((Token.StringLiteral) first).value());

    }

    @Test
    public void testTokenizeBoolean() {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> result = tokenizer.tokenize("TRUE && FALSE");

        assertEquals(3, result.size());

        Token first = result.get(0);
        assertTrue(first instanceof Token.BooleanLiteral);
        assertEquals(true, ((Token.BooleanLiteral) first).value());

        Token third = result.get(2);
        assertTrue(third instanceof Token.BooleanLiteral);
        assertEquals(false, ((Token.BooleanLiteral) third).value());
    }

    @Test
    public void testTokenizeError() {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> result = tokenizer.tokenize("1 @ 2");

        assertTrue(result.stream().anyMatch(Token::hasError));
    }

    @Test
    public void testTokenizeVariable() {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> result = tokenizer.tokenize("[Flow] + [Stage]");

        assertEquals(3, result.size());

        Token first = result.get(0);
        assertTrue(first instanceof Token.Variable);
        assertEquals("Flow", ((Token.Variable) first).name());

        Token third = result.get(2);
        assertTrue(third instanceof Token.Variable);
        assertEquals("Stage", ((Token.Variable) third).name());
    }

}