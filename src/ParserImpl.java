public class ParserImpl extends Parser {
    @Override
    public Expr do_parse() throws Exception {
        if (tokens == null) {
            throw new Exception("Empty input");
        }
        Expr result = parseT();
        if (tokens != null) {
            throw new Exception("Unexpected tokens after expression");
        }
        return result;
    }

    private Expr parseT() throws Exception {
        Expr left = parseF();
        
        if (tokens != null && 
            (tokens.elem.ty == TokenType.PLUS || tokens.elem.ty == TokenType.MINUS)) {
            Token op = tokens.elem;
            tokens = tokens.rest;
            Expr right = parseT();  // Recursively parse the rest for right associativity
            if (op.ty == TokenType.PLUS) {
                return new PlusExpr(left, right);
            } else {
                return new MinusExpr(left, right);
            }
        }
        return left;
    }

    private Expr parseF() throws Exception {
        Expr left = parseLit();
        
        if (tokens != null && 
            (tokens.elem.ty == TokenType.TIMES || tokens.elem.ty == TokenType.DIV)) {
            Token op = tokens.elem;
            tokens = tokens.rest;
            Expr right = parseF();  // Recursively parse the rest for right associativity
            if (op.ty == TokenType.TIMES) {
                return new TimesExpr(left, right);
            } else {
                return new DivExpr(left, right);
            }
        }
        return left;
    }

    private Expr parseLit() throws Exception {
        if (tokens == null) {
            throw new Exception("Unexpected end of input");
        }
        
        if (tokens.elem.ty == TokenType.NUM) {
            Token num = tokens.elem;
            tokens = tokens.rest;
            return new FloatExpr(Float.parseFloat(num.lexeme));
        } 
        else if (tokens.elem.ty == TokenType.LPAREN) {
            tokens = tokens.rest;
            Expr e = parseT();
            if (tokens == null || tokens.elem.ty != TokenType.RPAREN) {
                throw new Exception("Missing right parenthesis");
            }
            tokens = tokens.rest;
            return e;
        }
        
        throw new Exception("Expected number or left parenthesis");
    }
}