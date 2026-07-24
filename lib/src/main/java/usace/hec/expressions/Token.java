package usace.hec.expressions;

/**
 * Sealed hierarchy of tokens produced by {@link Tokenizer}.
 */
public sealed interface Token permits
    Token.Number,
    Token.Variable,
    Token.Operator,
    Token.Function,
    Token.LeftParen,
    Token.RightParen,
    Token.Comma,
    Token.StringLiteral,
    Token.Unknown 
    
{
    int position();
    boolean hasError();
    String error();
    record Number(double value, int pos, String error) implements Token {
        public int position(){
            return pos;
        }
        public boolean hasError(){
            return !error.isBlank();
        }
        public String error(){
            return error;
        }
    }
    record Variable(String name, int pos, String error) implements Token {
        public int position(){
            return pos;
        }
        public boolean hasError(){
            return !error.isBlank();
        }
        public String error(){
            return error;
        }
    }
    record Operator(ExpressionOperator op, int pos, String error) implements Token {
        public int position(){
            return pos;
        }
        public boolean hasError(){
            return !error.isBlank();
        }
        public String error(){
            return error;
        }
    }
    record Function(ExpressionOperator op, int pos, String error) implements Token {
        public int position(){
            return pos;
        }
        public boolean hasError(){
            return !error.isBlank();
        }
        public String error(){
            return error;
        }
    }
    record LeftParen(int pos, String error) implements Token {
        public int position(){
            return pos;
        }
        public boolean hasError(){
            return !error.isBlank();
        }
        public String error(){
            return error;
        }
    }
    record RightParen(int pos, String error) implements Token {
        public int position(){
            return pos;
        }
        public boolean hasError(){
            return !error.isBlank();
        }
        public String error(){
            return error;
        }
    }
    record Comma(int pos, String error) implements Token {
        public int position(){
            return pos;
        }
        public boolean hasError(){
            return !error.isBlank();
        }
        public String error(){
            return error;
        }
    }
    record StringLiteral(String value, int pos, String error) implements Token {
        public int position(){
            return pos;
        }
        public boolean hasError(){
            return !error.isBlank();
        }
        public String error(){
            return error;
        }
    }
    record Unknown(String value, int pos, String error, String remaining) implements Token {
        public int position(){
            return pos;
        }
        public boolean hasError(){
            return !error.isBlank();
        }
        public String error(){
            return error;
        }
    }
}