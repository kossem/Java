import java.util.*;

enum Tag { LPAREN,RPAREN,NUMBER,ERROR,PLUS,DEF,MINUS,MULTIPLY,DIVIDE; }
class Lexem {
    private String s;
    private Tag t;
    Lexem(Tag temp, String str) { t = temp; s = str;}
    public Tag tag() { return t; }
    public String op() { return s;}
}
class ParserException extends Exception {
    public ParserException(String str) {
        super(str);
    }
}
class Parser {
    private static Hashtable<String, Integer> def = new Hashtable<>();
    private static Lexem lex;
    private static String string;
    protected static Scanner in = new Scanner(System.in);

    protected static void lexr(String expr, Deque<Lexem> lexems) {
        for (int i = 0; i < expr.length(); i++) {
            char tag = expr.charAt(i);
            if (Character.isLetter(tag)) {
                for (string = ""; Character.isDigit(tag) || Character.isLetter(tag);tag = expr.charAt(++i)) {
                    string += Character.toString(tag);
                }
                if (!def.containsKey(string)) {
                    def.put(string, in.nextInt());
                }
                lexems.add(new Lexem(Tag.DEF, string));
                i--;
            } else if (Character.isDigit(tag)) {
                for (string = ""; Character.isDigit(tag); tag = expr.charAt(++i)) {
                    string += Character.toString(tag);
                }
                lexems.add(new Lexem(Tag.NUMBER, string));
                i--;
            } else if(tag == ' ') {
                continue;
            } else if (tag == '+') {
                lexems.add(new Lexem(Tag.PLUS, "+"));
            } else if (tag == '-') {
                lexems.add(new Lexem(Tag.MINUS, "-"));
            } else if (tag == '*') {
                lexems.add(new Lexem(Tag.MULTIPLY, "*"));
            } else if (tag == '/') {
                lexems.add(new Lexem(Tag.DIVIDE, "/"));
            } else if (tag == ')') {
                lexems.add(new Lexem(Tag.RPAREN, ")"));
            } else if (tag == '(') {
                lexems.add(new Lexem(Tag.LPAREN, "("));
            } else {
                lexems.add(new Lexem(Tag.ERROR, Character.toString(tag)));
            }
        }
    }

    protected static int parse_Expr(Deque<Lexem> lexems) throws ParserException {
        int parse_res = parse_Term(lexems);
        while (lex != null) {
            if (lex.tag() == Tag.LPAREN || lex.tag() == Tag.DEF || lex.tag() == Tag.ERROR) {
                throw new ParserException("Error");
            } else if (lex.tag() == Tag.PLUS) {
                parse_res += parse_Term(lexems);
            } else if (lex.tag() == Tag.MINUS) {
                parse_res -= parse_Term(lexems);
            } else {
                break;
            }
        }
        return parse_res;
    }

    protected static int parse_Term(Deque<Lexem> lexems) throws ParserException {
        int parse_res = parse_Power(lexems);
        while (lex != null) {
            if (lex.tag() == Tag.MULTIPLY) {
                parse_res *= parse_Power(lexems);
            } else if (lex.tag() == Tag.DIVIDE) {
                parse_res /= parse_Power(lexems);
            } else {
                break;
            }
        }
        return parse_res;
    }

    protected static int parse_Power(Deque<Lexem> lexems) throws ParserException {
        int parse_res;
        lex = lexems.poll();
        if (lex.tag() == Tag.MINUS) {
            return -parse_Power(lexems);
        } else if (lex.tag() == Tag.DEF) {
            parse_res = def.get(lex.op());
        } else if (lex.tag() == Tag.NUMBER) {
            parse_res = Integer.parseInt(lex.op());
        } else if (lex.tag() == Tag.LPAREN) {
            parse_res = parse_Expr(lexems);
            if (lex.tag() != Tag.RPAREN)
                throw new ParserException("There is no ')");
        } else {
            throw new ParserException("Error");
        }
        lex = lexems.poll();
        return parse_res;
    }
}
public class Calc extends Parser{
    public static void main(String[] args) {
        Deque<Lexem> lexems = new ArrayDeque<>();
        String expression = in.nextLine();
        lexr(expression + ' ', lexems);
        try { System.out.println(parse_Expr(lexems)); }
        catch(Exception e) { System.out.println("error"); }
    }
}
