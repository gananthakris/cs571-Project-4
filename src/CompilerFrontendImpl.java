public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    @Override
    protected void init_lexer() {
        lex = new LexerImpl();
        
        // NUM: [0-9]*\.[0-9]+
        Automaton numAutomaton = new AutomatonImpl();
        numAutomaton.addState(0, true, false);  // start
        numAutomaton.addState(1, false, false); // after decimal
        numAutomaton.addState(2, false, true);  // accepted state
        
        // Optional digits before decimal
        for (char d = '0'; d <= '9'; d++) {
            numAutomaton.addTransition(0, d, 0);
        }
        
        // Decimal point
        numAutomaton.addTransition(0, '.', 1);
        
        // Required digits after decimal
        for (char d = '0'; d <= '9'; d++) {
            numAutomaton.addTransition(1, d, 2);
            numAutomaton.addTransition(2, d, 2);
        }
        
        // PLUS: \+
        Automaton plusAutomaton = new AutomatonImpl();
        plusAutomaton.addState(0, true, false);
        plusAutomaton.addState(1, false, true);
        plusAutomaton.addTransition(0, '+', 1);
        
        // MINUS: -
        Automaton minusAutomaton = new AutomatonImpl();
        minusAutomaton.addState(0, true, false);
        minusAutomaton.addState(1, false, true);
        minusAutomaton.addTransition(0, '-', 1);
        
        // TIMES: \*
        Automaton timesAutomaton = new AutomatonImpl();
        timesAutomaton.addState(0, true, false);
        timesAutomaton.addState(1, false, true);
        timesAutomaton.addTransition(0, '*', 1);
        
        // DIV: /
        Automaton divAutomaton = new AutomatonImpl();
        divAutomaton.addState(0, true, false);
        divAutomaton.addState(1, false, true);
        divAutomaton.addTransition(0, '/', 1);
        
        // LPAREN: \(
        Automaton lparenAutomaton = new AutomatonImpl();
        lparenAutomaton.addState(0, true, false);
        lparenAutomaton.addState(1, false, true);
        lparenAutomaton.addTransition(0, '(', 1);
        
        // RPAREN: \)
        Automaton rparenAutomaton = new AutomatonImpl();
        rparenAutomaton.addState(0, true, false);
        rparenAutomaton.addState(1, false, true);
        rparenAutomaton.addTransition(0, ')', 1);
        
        // WHITE_SPACE: (' '|\n|\r|\t)*
        Automaton wsAutomaton = new AutomatonImpl();
        wsAutomaton.addState(0, true, true);
        wsAutomaton.addTransition(0, ' ', 0);
        wsAutomaton.addTransition(0, '\n', 0);
        wsAutomaton.addTransition(0, '\r', 0);
        wsAutomaton.addTransition(0, '\t', 0);

        lex.add_automaton(TokenType.NUM, numAutomaton);
        lex.add_automaton(TokenType.PLUS, plusAutomaton);
        lex.add_automaton(TokenType.MINUS, minusAutomaton);
        lex.add_automaton(TokenType.TIMES, timesAutomaton);
        lex.add_automaton(TokenType.DIV, divAutomaton);
        lex.add_automaton(TokenType.LPAREN, lparenAutomaton);
        lex.add_automaton(TokenType.RPAREN, rparenAutomaton);
        lex.add_automaton(TokenType.WHITE_SPACE, wsAutomaton);
    }
}