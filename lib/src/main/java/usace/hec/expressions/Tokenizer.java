package usace.hec.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tokenizer {

    private static final Map<String, ExpressionOperator> INFIX_MAP = new HashMap<>();
    private static final Map<String, ExpressionOperator> FUNCTION_MAP = new HashMap<>();
    private static final List<String> MULTI_CHAR_OPS = new ArrayList<>();

    static {
        for (ExpressionOperator op : ExpressionOperator.values()) {
            FUNCTION_MAP.put(op.name(), op);
            String infix = op.getInfixName();
            
            // Only register true infix operators.
            // Conditions:
            // 1. Non-empty, doesn't contain special chars
            // 2. Not a purely alphabetic word (those are functions/prefix)
            // 3. Has a defined precedence > 0 (excludes unary-only ops like NEGATE)
            if (infix != null && !infix.isEmpty()
                    && !infix.contains("?") && !infix.contains("(")
                    && !infix.contains(",") && !infix.contains(" ")
                    && !infix.equals("[]")
                    && !infix.matches("[A-Za-z]+")
                    && op!=ExpressionOperator.NEGATE) {
                INFIX_MAP.put(infix, op);
            }
        }
        MULTI_CHAR_OPS.addAll(INFIX_MAP.keySet().stream()
                .filter(k -> k.length() > 1)
                .sorted((a, b) -> b.length() - a.length())
                .toList());
    }

    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int pos = 0;
        int len = input.length();

        while (pos < len) {
            char c = input.charAt(pos);

            if (Character.isWhitespace(c)) { pos++; continue; }

            if (isNumberStart(c, input, pos)) {
                boolean hasDot = false;
                int start = pos;
                int lennum = input.length();
                while (pos < lennum) {
                    char cnum = input.charAt(pos);
                    if (Character.isDigit(cnum)) {
                        pos++;
                    } else if (cnum == '.' && !hasDot) {
                        hasDot = true;
                        pos++;
                    } else {
                        break;
                    }
                }
                String numStr = input.substring(start, pos);
                if (hasDot) {
                        try{
                            tokens.add(new Token.DoubleLiteral(Double.parseDouble(numStr), pos, ""));
                        }catch (NumberFormatException e2){
                            tokens.add(new Token.Unknown(input, pos, "Uable to parse input string to number",input));
                            return tokens;
                        }
                } else {
                    // Check if value fits in int; if not, treat as double
                    try {
                        tokens.add(new Token.IntegerLiteral(Integer.parseInt(numStr), pos, ""));
                    } catch (NumberFormatException e) {
                        try{
                            tokens.add(new Token.DoubleLiteral(Double.parseDouble(numStr), pos, ""));
                        }catch (NumberFormatException e2){
                            tokens.add(new Token.Unknown(input, pos, "Uable to parse input string to number",input));
                            return tokens;
                        }
                        
                    }
                }
                continue;
            }

            if (c == '[') {
                int close = input.indexOf(']', pos + 1);
                if (close == -1) {
                    tokens.add(new Token.Variable("[" + remaining(input, pos), pos,
                            "Unclosed '[' for variable"));
                    return tokens;
                }
                String name = input.substring(pos + 1, close);
                if (name.isEmpty()) {
                    tokens.add(new Token.Variable("[]", pos, "Empty variable name"));
                    return tokens;
                }
                tokens.add(new Token.Variable(name, pos, ""));
                pos = close + 1;
                continue;
            }

            // Check multi-character infix operators (>=, <=, ==, &&, ||, ^^)
            boolean matchedMulti = false;
            for (String sym : MULTI_CHAR_OPS) {
                if (input.startsWith(sym, pos)) {
                    ExpressionOperator op = INFIX_MAP.get(sym);
                    if (op != null) {
                        tokens.add(new Token.Operator(op, pos, ""));
                        pos += sym.length();
                        matchedMulti = true;
                    }
                    break;
                }
            }
            if (matchedMulti) continue;

            // Check single-character infix operators (+, -, *, /, ^, >, <, |)
            String single = String.valueOf(c);
            if (INFIX_MAP.containsKey(single)) {
                tokens.add(new Token.Operator(INFIX_MAP.get(single), pos, ""));
                pos++;
                continue;
            }

            if (c == '(') { tokens.add(new Token.LeftParen(pos, "")); pos++; continue; }
            if (c == ')') { tokens.add(new Token.RightParen(pos, "")); pos++; continue; }
            if (c == ',') { tokens.add(new Token.Comma(pos, "")); pos++; continue; }

            // Identifiers: function names, booleans, or unknown
            if (Character.isLetter(c) || c == '_') {
                int end = pos;
                while (end < len && (Character.isLetterOrDigit(input.charAt(end))
                        || input.charAt(end) == '_')) {
                    end++;
                }
                String word = input.substring(pos, end);
                String upper = word.toUpperCase();

                if ("TRUE".equals(upper)) {
                    tokens.add(new Token.BooleanLiteral(true, pos, ""));
                    pos = end;
                    continue;
                }
                if ("FALSE".equals(upper)) {
                    tokens.add(new Token.BooleanLiteral(false, pos, ""));
                    pos = end;
                    continue;
                }

                if (FUNCTION_MAP.containsKey(upper)) {
                    tokens.add(new Token.Function(FUNCTION_MAP.get(upper), pos, ""));
                    pos = end;
                    continue;
                }

                tokens.add(new Token.Unknown(word, pos,
                        "Unknown identifier: " + word, remaining(input, pos)));
                return tokens;
            }

            tokens.add(new Token.Unknown(String.valueOf(c), pos,
                    "Unexpected character: '" + c + "'", remaining(input, pos)));
            return tokens;
        }

        return tokens;
    }

    private boolean isNumberStart(char c, String input, int pos) {
        if (Character.isDigit(c)) return true;
        return c == '.' && pos + 1 < input.length()
                && Character.isDigit(input.charAt(pos + 1));
    }
    private Token parseNumber(String input, int pos) {
        int end = pos;
        boolean hasDot = false;
        int len = input.length();
        while (end < len) {
            char c = input.charAt(end);
            if (Character.isDigit(c)) {
                end++;
            } else if (c == '.' && !hasDot) {
                hasDot = true;
                end++;
            } else {
                break;
            }
        }
        String numStr = input.substring(pos, end);
        if (hasDot) {
                try{
                    return new Token.DoubleLiteral(Double.parseDouble(numStr), pos, "");
                }catch (NumberFormatException e2){
                    return new Token.Unknown(input, pos, "Uable to parse input string to number",input);
                }
        } else {
            // Check if value fits in int; if not, treat as double
            try {
                return new Token.IntegerLiteral(Integer.parseInt(numStr), pos, "");
            } catch (NumberFormatException e) {
                try{
                    return new Token.DoubleLiteral(Double.parseDouble(numStr), pos, "");
                }catch (NumberFormatException e2){
                    return new Token.Unknown(input, pos, "Uable to parse input string to number",input);
                }
                
            }
        }
    }


    private String remaining(String input, int pos) {
        if (pos >= input.length()) return "";
        int end = Math.min(pos + 40, input.length());
        String s = input.substring(pos, end);
        return end < input.length() ? s + "..." : s;
    }
}