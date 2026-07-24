package usace.hec.expressions;

import java.util.List;

import org.junit.Test;

public class TokenizerTest {
    @Test
    public void testTokenize() {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> result = tokenizer.tokenize("IF((MIN(1,[adf])<MAX(1,2),1+2,3-4)");//does the tokenizer find the extra parenthesis is an error?
        boolean tokenError = false;
        for(Token t : result){
            if(t.hasError()){
                System.out.println(t.error());
                tokenError = true;
            }
            System.out.println(t.toString());
        }
        if(tokenError){
            System.out.println("found token errors");//currently the tokenizer is not finding these errors.
        }
        InfixParser parser = new InfixParser();
        ParseResult<ExpressionNode<?>> resultnode = parser.parse("IF(MIN(1.0,2.0)<MAX(1.0,2.0),1+2,3-4)");
        if(resultnode.hasError()){
            System.out.print(resultnode.getError().message());
        }else{
            System.out.print(resultnode.getNode().evaluate());
        }

    }
}
