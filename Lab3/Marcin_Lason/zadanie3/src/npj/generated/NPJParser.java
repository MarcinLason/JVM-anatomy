// Generated from NPJ.g4 by ANTLR 4.5.3
package npj.generated;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class NPJParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            NULL = 10, INT = 11, STRING = 12, QUOTED = 13, WS = 14;
    public static final int
            RULE_program = 0, RULE_statements = 1, RULE_statement = 2, RULE_varDecl = 3,
            RULE_varDeclT = 4, RULE_varDeclSConst = 5, RULE_varDeclSNull = 6, RULE_assignment = 7,
            RULE_lValue = 8, RULE_rValue = 9, RULE_print = 10, RULE_printStringConst = 11,
            RULE_printStringMessage = 12, RULE_printQuoted = 13, RULE_heapAnalyze = 14,
            RULE_collect = 15;
    public static final String[] ruleNames = {
            "program", "statements", "statement", "varDecl", "varDeclT", "varDeclSConst",
            "varDeclSNull", "assignment", "lValue", "rValue", "print", "printStringConst",
            "printStringMessage", "printQuoted", "heapAnalyze", "collect"
    };

    private static final String[] _LITERAL_NAMES = {
            null, "';'", "'VarDeclT'", "'VarDeclS'", "'\"'", "'='", "'.'", "'Print'",
            "'HeapAnalyze'", "'Collect'", "'NULL'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
            null, null, null, null, null, null, null, null, null, null, "NULL", "INT",
            "STRING", "QUOTED", "WS"
    };
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "NPJ.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public NPJParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class ProgramContext extends ParserRuleContext {
        public StatementsContext statements() {
            return getRuleContext(StatementsContext.class, 0);
        }

        public ProgramContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_program;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterProgram(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitProgram(this);
        }
    }

    public final ProgramContext program() throws RecognitionException {
        ProgramContext _localctx = new ProgramContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_program);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(32);
                statements(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class StatementsContext extends ParserRuleContext {
        public StatementContext statement() {
            return getRuleContext(StatementContext.class, 0);
        }

        public StatementsContext statements() {
            return getRuleContext(StatementsContext.class, 0);
        }

        public StatementsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_statements;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterStatements(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitStatements(this);
        }
    }

    public final StatementsContext statements() throws RecognitionException {
        return statements(0);
    }

    private StatementsContext statements(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        StatementsContext _localctx = new StatementsContext(_ctx, _parentState);
        StatementsContext _prevctx = _localctx;
        int _startState = 2;
        enterRecursionRule(_localctx, 2, RULE_statements, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(35);
                    statement();
                }
                _ctx.stop = _input.LT(-1);
                setState(41);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new StatementsContext(_parentctx, _parentState);
                                pushNewRecursionContext(_localctx, _startState, RULE_statements);
                                setState(37);
                                if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                setState(38);
                                statement();
                            }
                        }
                    }
                    setState(43);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class StatementContext extends ParserRuleContext {
        public VarDeclContext varDecl() {
            return getRuleContext(VarDeclContext.class, 0);
        }

        public AssignmentContext assignment() {
            return getRuleContext(AssignmentContext.class, 0);
        }

        public PrintContext print() {
            return getRuleContext(PrintContext.class, 0);
        }

        public HeapAnalyzeContext heapAnalyze() {
            return getRuleContext(HeapAnalyzeContext.class, 0);
        }

        public CollectContext collect() {
            return getRuleContext(CollectContext.class, 0);
        }

        public StatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_statement;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterStatement(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitStatement(this);
        }
    }

    public final StatementContext statement() throws RecognitionException {
        StatementContext _localctx = new StatementContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_statement);
        try {
            setState(59);
            switch (_input.LA(1)) {
                case T__1:
                case T__2:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(44);
                    varDecl();
                    setState(45);
                    match(T__0);
                }
                break;
                case STRING:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(47);
                    assignment();
                    setState(48);
                    match(T__0);
                }
                break;
                case T__6:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(50);
                    print();
                    setState(51);
                    match(T__0);
                }
                break;
                case T__7:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(53);
                    heapAnalyze();
                    setState(54);
                    match(T__0);
                }
                break;
                case T__8:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(56);
                    collect();
                    setState(57);
                    match(T__0);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class VarDeclContext extends ParserRuleContext {
        public VarDeclTContext varDeclT() {
            return getRuleContext(VarDeclTContext.class, 0);
        }

        public VarDeclSConstContext varDeclSConst() {
            return getRuleContext(VarDeclSConstContext.class, 0);
        }

        public VarDeclSNullContext varDeclSNull() {
            return getRuleContext(VarDeclSNullContext.class, 0);
        }

        public VarDeclContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_varDecl;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterVarDecl(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitVarDecl(this);
        }
    }

    public final VarDeclContext varDecl() throws RecognitionException {
        VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_varDecl);
        try {
            setState(64);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(61);
                    varDeclT();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(62);
                    varDeclSConst();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(63);
                    varDeclSNull();
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class VarDeclTContext extends ParserRuleContext {
        public TerminalNode STRING() {
            return getToken(NPJParser.STRING, 0);
        }

        public VarDeclTContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_varDeclT;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterVarDeclT(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitVarDeclT(this);
        }
    }

    public final VarDeclTContext varDeclT() throws RecognitionException {
        VarDeclTContext _localctx = new VarDeclTContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_varDeclT);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(66);
                match(T__1);
                setState(67);
                match(STRING);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class VarDeclSConstContext extends ParserRuleContext {
        public List<TerminalNode> STRING() {
            return getTokens(NPJParser.STRING);
        }

        public TerminalNode STRING(int i) {
            return getToken(NPJParser.STRING, i);
        }

        public VarDeclSConstContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_varDeclSConst;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterVarDeclSConst(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitVarDeclSConst(this);
        }
    }

    public final VarDeclSConstContext varDeclSConst() throws RecognitionException {
        VarDeclSConstContext _localctx = new VarDeclSConstContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_varDeclSConst);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(69);
                match(T__2);
                setState(70);
                match(STRING);
                setState(71);
                match(T__3);
                setState(72);
                match(STRING);
                setState(73);
                match(T__3);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class VarDeclSNullContext extends ParserRuleContext {
        public TerminalNode STRING() {
            return getToken(NPJParser.STRING, 0);
        }

        public TerminalNode NULL() {
            return getToken(NPJParser.NULL, 0);
        }

        public VarDeclSNullContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_varDeclSNull;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterVarDeclSNull(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitVarDeclSNull(this);
        }
    }

    public final VarDeclSNullContext varDeclSNull() throws RecognitionException {
        VarDeclSNullContext _localctx = new VarDeclSNullContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_varDeclSNull);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(75);
                match(T__2);
                setState(76);
                match(STRING);
                setState(77);
                match(NULL);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class AssignmentContext extends ParserRuleContext {
        public LValueContext lValue() {
            return getRuleContext(LValueContext.class, 0);
        }

        public RValueContext rValue() {
            return getRuleContext(RValueContext.class, 0);
        }

        public AssignmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterAssignment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitAssignment(this);
        }
    }

    public final AssignmentContext assignment() throws RecognitionException {
        AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_assignment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(79);
                lValue(0);
                setState(80);
                match(T__4);
                setState(81);
                rValue();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class LValueContext extends ParserRuleContext {
        public TerminalNode STRING() {
            return getToken(NPJParser.STRING, 0);
        }

        public LValueContext lValue() {
            return getRuleContext(LValueContext.class, 0);
        }

        public LValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_lValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterLValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitLValue(this);
        }
    }

    public final LValueContext lValue() throws RecognitionException {
        return lValue(0);
    }

    private LValueContext lValue(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        LValueContext _localctx = new LValueContext(_ctx, _parentState);
        LValueContext _prevctx = _localctx;
        int _startState = 16;
        enterRecursionRule(_localctx, 16, RULE_lValue, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(84);
                    match(STRING);
                }
                _ctx.stop = _input.LT(-1);
                setState(91);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new LValueContext(_parentctx, _parentState);
                                pushNewRecursionContext(_localctx, _startState, RULE_lValue);
                                setState(86);
                                if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                setState(87);
                                match(T__5);
                                setState(88);
                                match(STRING);
                            }
                        }
                    }
                    setState(93);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class RValueContext extends ParserRuleContext {
        public LValueContext lValue() {
            return getRuleContext(LValueContext.class, 0);
        }

        public TerminalNode NULL() {
            return getToken(NPJParser.NULL, 0);
        }

        public TerminalNode INT() {
            return getToken(NPJParser.INT, 0);
        }

        public TerminalNode STRING() {
            return getToken(NPJParser.STRING, 0);
        }

        public RValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_rValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterRValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitRValue(this);
        }
    }

    public final RValueContext rValue() throws RecognitionException {
        RValueContext _localctx = new RValueContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_rValue);
        try {
            setState(100);
            switch (_input.LA(1)) {
                case STRING:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(94);
                    lValue(0);
                }
                break;
                case NULL:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(95);
                    match(NULL);
                }
                break;
                case INT:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(96);
                    match(INT);
                }
                break;
                case T__3:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(97);
                    match(T__3);
                    setState(98);
                    match(STRING);
                    setState(99);
                    match(T__3);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class PrintContext extends ParserRuleContext {
        public PrintStringConstContext printStringConst() {
            return getRuleContext(PrintStringConstContext.class, 0);
        }

        public PrintStringMessageContext printStringMessage() {
            return getRuleContext(PrintStringMessageContext.class, 0);
        }

        public PrintQuotedContext printQuoted() {
            return getRuleContext(PrintQuotedContext.class, 0);
        }

        public PrintContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_print;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterPrint(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitPrint(this);
        }
    }

    public final PrintContext print() throws RecognitionException {
        PrintContext _localctx = new PrintContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_print);
        try {
            setState(105);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 5, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(102);
                    printStringConst();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(103);
                    printStringMessage();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(104);
                    printQuoted();
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class PrintStringConstContext extends ParserRuleContext {
        public TerminalNode STRING() {
            return getToken(NPJParser.STRING, 0);
        }

        public PrintStringConstContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_printStringConst;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterPrintStringConst(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitPrintStringConst(this);
        }
    }

    public final PrintStringConstContext printStringConst() throws RecognitionException {
        PrintStringConstContext _localctx = new PrintStringConstContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_printStringConst);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(107);
                match(T__6);
                setState(108);
                match(STRING);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class PrintStringMessageContext extends ParserRuleContext {
        public TerminalNode STRING() {
            return getToken(NPJParser.STRING, 0);
        }

        public PrintStringMessageContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_printStringMessage;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterPrintStringMessage(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitPrintStringMessage(this);
        }
    }

    public final PrintStringMessageContext printStringMessage() throws RecognitionException {
        PrintStringMessageContext _localctx = new PrintStringMessageContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_printStringMessage);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(110);
                match(T__6);
                setState(111);
                match(T__3);
                setState(112);
                match(STRING);
                setState(113);
                match(T__3);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class PrintQuotedContext extends ParserRuleContext {
        public TerminalNode QUOTED() {
            return getToken(NPJParser.QUOTED, 0);
        }

        public PrintQuotedContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_printQuoted;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterPrintQuoted(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitPrintQuoted(this);
        }
    }

    public final PrintQuotedContext printQuoted() throws RecognitionException {
        PrintQuotedContext _localctx = new PrintQuotedContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_printQuoted);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(115);
                match(T__6);
                setState(116);
                match(QUOTED);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class HeapAnalyzeContext extends ParserRuleContext {
        public HeapAnalyzeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_heapAnalyze;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterHeapAnalyze(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitHeapAnalyze(this);
        }
    }

    public final HeapAnalyzeContext heapAnalyze() throws RecognitionException {
        HeapAnalyzeContext _localctx = new HeapAnalyzeContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_heapAnalyze);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(118);
                match(T__7);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CollectContext extends ParserRuleContext {
        public CollectContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_collect;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).enterCollect(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof NPJListener) ((NPJListener) listener).exitCollect(this);
        }
    }

    public final CollectContext collect() throws RecognitionException {
        CollectContext _localctx = new CollectContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_collect);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(120);
                match(T__8);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 1:
                return statements_sempred((StatementsContext) _localctx, predIndex);
            case 8:
                return lValue_sempred((LValueContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean statements_sempred(StatementsContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 1);
        }
        return true;
    }

    private boolean lValue_sempred(LValueContext _localctx, int predIndex) {
        switch (predIndex) {
            case 1:
                return precpred(_ctx, 1);
        }
        return true;
    }

    public static final String _serializedATN =
            "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\20}\4\2\t\2\4\3\t" +
                    "\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4" +
                    "\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\3\3\3\3" +
                    "\3\3\3\3\3\3\7\3*\n\3\f\3\16\3-\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3" +
                    "\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4>\n\4\3\5\3\5\3\5\5\5C\n\5\3\6\3\6\3\6\3" +
                    "\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n" +
                    "\3\n\3\n\7\n\\\n\n\f\n\16\n_\13\n\3\13\3\13\3\13\3\13\3\13\3\13\5\13g" +
                    "\n\13\3\f\3\f\3\f\5\fl\n\f\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3" +
                    "\17\3\17\3\20\3\20\3\21\3\21\3\21\2\4\4\22\22\2\4\6\b\n\f\16\20\22\24" +
                    "\26\30\32\34\36 \2\2y\2\"\3\2\2\2\4$\3\2\2\2\6=\3\2\2\2\bB\3\2\2\2\nD" +
                    "\3\2\2\2\fG\3\2\2\2\16M\3\2\2\2\20Q\3\2\2\2\22U\3\2\2\2\24f\3\2\2\2\26" +
                    "k\3\2\2\2\30m\3\2\2\2\32p\3\2\2\2\34u\3\2\2\2\36x\3\2\2\2 z\3\2\2\2\"" +
                    "#\5\4\3\2#\3\3\2\2\2$%\b\3\1\2%&\5\6\4\2&+\3\2\2\2\'(\f\3\2\2(*\5\6\4" +
                    "\2)\'\3\2\2\2*-\3\2\2\2+)\3\2\2\2+,\3\2\2\2,\5\3\2\2\2-+\3\2\2\2./\5\b" +
                    "\5\2/\60\7\3\2\2\60>\3\2\2\2\61\62\5\20\t\2\62\63\7\3\2\2\63>\3\2\2\2" +
                    "\64\65\5\26\f\2\65\66\7\3\2\2\66>\3\2\2\2\678\5\36\20\289\7\3\2\29>\3" +
                    "\2\2\2:;\5 \21\2;<\7\3\2\2<>\3\2\2\2=.\3\2\2\2=\61\3\2\2\2=\64\3\2\2\2" +
                    "=\67\3\2\2\2=:\3\2\2\2>\7\3\2\2\2?C\5\n\6\2@C\5\f\7\2AC\5\16\b\2B?\3\2" +
                    "\2\2B@\3\2\2\2BA\3\2\2\2C\t\3\2\2\2DE\7\4\2\2EF\7\16\2\2F\13\3\2\2\2G" +
                    "H\7\5\2\2HI\7\16\2\2IJ\7\6\2\2JK\7\16\2\2KL\7\6\2\2L\r\3\2\2\2MN\7\5\2" +
                    "\2NO\7\16\2\2OP\7\f\2\2P\17\3\2\2\2QR\5\22\n\2RS\7\7\2\2ST\5\24\13\2T" +
                    "\21\3\2\2\2UV\b\n\1\2VW\7\16\2\2W]\3\2\2\2XY\f\3\2\2YZ\7\b\2\2Z\\\7\16" +
                    "\2\2[X\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^\23\3\2\2\2_]\3\2\2\2`g" +
                    "\5\22\n\2ag\7\f\2\2bg\7\r\2\2cd\7\6\2\2de\7\16\2\2eg\7\6\2\2f`\3\2\2\2" +
                    "fa\3\2\2\2fb\3\2\2\2fc\3\2\2\2g\25\3\2\2\2hl\5\30\r\2il\5\32\16\2jl\5" +
                    "\34\17\2kh\3\2\2\2ki\3\2\2\2kj\3\2\2\2l\27\3\2\2\2mn\7\t\2\2no\7\16\2" +
                    "\2o\31\3\2\2\2pq\7\t\2\2qr\7\6\2\2rs\7\16\2\2st\7\6\2\2t\33\3\2\2\2uv" +
                    "\7\t\2\2vw\7\17\2\2w\35\3\2\2\2xy\7\n\2\2y\37\3\2\2\2z{\7\13\2\2{!\3\2" +
                    "\2\2\b+=B]fk";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}