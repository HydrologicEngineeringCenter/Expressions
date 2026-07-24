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
                    && OperatorPrecedence.getPrecedence(op) > 0) {
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
                int end = skipNumber(input, pos);
                String numStr = input.substring(pos, end);
                try {
                    tokens.add(new Token.Number(Double.parseDouble(numStr), pos, ""));
                    pos = end;
                } catch (NumberFormatException e) {
                    tokens.add(new Token.Unknown(numStr, pos,
                            "Invalid number literal", remaining(input, pos)));
                    return tokens;
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
                    tokens.add(new Token.StringLiteral("TRUE", pos, ""));
                    pos = end;
                    continue;
                }
                if ("FALSE".equals(upper)) {
                    tokens.add(new Token.StringLiteral("FALSE", pos, ""));
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

    private int skipNumber(String input, int pos) {
        int len = input.length();
        boolean hasDot = false;
        while (pos < len) {
            char c = input.charAt(pos);
            if (Character.isDigit(c)) pos++;
            else if (c == '.' && !hasDot) { hasDot = true; pos++; }
            else break;
        }
        return pos;
    }

    private String remaining(String input, int pos) {
        if (pos >= input.length()) return "";
        int end = Math.min(pos + 40, input.length());
        String s = input.substring(pos, end);
        return end < input.length() ? s + "..." : s;
    }
}