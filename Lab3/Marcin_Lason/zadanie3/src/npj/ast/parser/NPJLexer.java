// $ANTLR 3.5.2 src/npj/ast/parser/NPJ.g 2016-11-10 18:15:26

package npj.ast.parser;


import org.antlr.runtime.*;

@SuppressWarnings("all")
public class NPJLexer extends Lexer {
    public static final int EOF = -1;
    public static final int T__7 = 7;
    public static final int T__8 = 8;
    public static final int T__9 = 9;
    public static final int T__10 = 10;
    public static final int T__11 = 11;
    public static final int T__12 = 12;
    public static final int T__13 = 13;
    public static final int T__14 = 14;
    public static final int T__15 = 15;
    public static final int T__16 = 16;
    public static final int T__17 = 17;
    public static final int INTEGER_CONSTANT = 4;
    public static final int STRING_CONSTANT = 5;
    public static final int WS = 6;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[]{};
    }

    public NPJLexer() {
    }

    public NPJLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }

    public NPJLexer(CharStream input, RecognizerSharedState state) {
        super(input, state);
    }

    @Override
    public String getGrammarFileName() {
        return "src/npj/ast/parser/NPJ.g";
    }

    // $ANTLR start "T__7"
    public final void mT__7() throws RecognitionException {
        try {
            int _type = T__7;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:11:6: ( '\"' )
            // src/npj/ast/parser/NPJ.g:11:8: '\"'
            {
                match('\"');
            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__7"

    // $ANTLR start "T__8"
    public final void mT__8() throws RecognitionException {
        try {
            int _type = T__8;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:12:6: ( '.' )
            // src/npj/ast/parser/NPJ.g:12:8: '.'
            {
                match('.');
            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__8"

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:13:6: ( ';' )
            // src/npj/ast/parser/NPJ.g:13:8: ';'
            {
                match(';');
            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__9"

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:14:7: ( '=' )
            // src/npj/ast/parser/NPJ.g:14:9: '='
            {
                match('=');
            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:15:7: ( 'Collect' )
            // src/npj/ast/parser/NPJ.g:15:9: 'Collect'
            {
                match("Collect");

            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:16:7: ( 'HeapAnalyze' )
            // src/npj/ast/parser/NPJ.g:16:9: 'HeapAnalyze'
            {
                match("HeapAnalyze");

            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:17:7: ( 'NULL' )
            // src/npj/ast/parser/NPJ.g:17:9: 'NULL'
            {
                match("NULL");

            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:18:7: ( 'Print' )
            // src/npj/ast/parser/NPJ.g:18:9: 'Print'
            {
                match("Print");

            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:19:7: ( 'VarDeclS' )
            // src/npj/ast/parser/NPJ.g:19:9: 'VarDeclS'
            {
                match("VarDeclS");

            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:20:7: ( 'VarDeclT' )
            // src/npj/ast/parser/NPJ.g:20:9: 'VarDeclT'
            {
                match("VarDeclT");

            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:21:7: ( '\\n' )
            // src/npj/ast/parser/NPJ.g:21:9: '\\n'
            {
                match('\n');
            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "STRING_CONSTANT"
    public final void mSTRING_CONSTANT() throws RecognitionException {
        try {
            int _type = STRING_CONSTANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:34:2: ( ( ( 'A' .. 'Z' ) | ( 'a' .. 'z' ) ) ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) | ( '0' .. '9' ) )* )
            // src/npj/ast/parser/NPJ.g:34:4: ( ( 'A' .. 'Z' ) | ( 'a' .. 'z' ) ) ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) | ( '0' .. '9' ) )*
            {
                if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
                // src/npj/ast/parser/NPJ.g:34:29: ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) | ( '0' .. '9' ) )*
                loop1:
                while (true) {
                    int alt1 = 2;
                    int LA1_0 = input.LA(1);
                    if (((LA1_0 >= '0' && LA1_0 <= '9') || (LA1_0 >= 'A' && LA1_0 <= 'Z') || (LA1_0 >= 'a' && LA1_0 <= 'z'))) {
                        alt1 = 1;
                    }

                    switch (alt1) {
                        case 1:
                            // src/npj/ast/parser/NPJ.g:
                        {
                            if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
                                input.consume();
                            } else {
                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                recover(mse);
                                throw mse;
                            }
                        }
                        break;

                        default:
                            break loop1;
                    }
                }

            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "STRING_CONSTANT"

    // $ANTLR start "INTEGER_CONSTANT"
    public final void mINTEGER_CONSTANT() throws RecognitionException {
        try {
            int _type = INTEGER_CONSTANT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:38:2: ( ( '0' .. '9' )+ )
            // src/npj/ast/parser/NPJ.g:38:4: ( '0' .. '9' )+
            {
                // src/npj/ast/parser/NPJ.g:38:4: ( '0' .. '9' )+
                int cnt2 = 0;
                loop2:
                while (true) {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);
                    if (((LA2_0 >= '0' && LA2_0 <= '9'))) {
                        alt2 = 1;
                    }

                    switch (alt2) {
                        case 1:
                            // src/npj/ast/parser/NPJ.g:
                        {
                            if ((input.LA(1) >= '0' && input.LA(1) <= '9')) {
                                input.consume();
                            } else {
                                MismatchedSetException mse = new MismatchedSetException(null, input);
                                recover(mse);
                                throw mse;
                            }
                        }
                        break;

                        default:
                            if (cnt2 >= 1) break loop2;
                            EarlyExitException eee = new EarlyExitException(2, input);
                            throw eee;
                    }
                    cnt2++;
                }

            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "INTEGER_CONSTANT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src/npj/ast/parser/NPJ.g:42:2: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // src/npj/ast/parser/NPJ.g:42:4: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
                if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || input.LA(1) == '\r' || input.LA(1) == ' ') {
                    input.consume();
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    recover(mse);
                    throw mse;
                }
                _channel = HIDDEN;
            }

            state.type = _type;
            state.channel = _channel;
        } finally {
            // do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    @Override
    public void mTokens() throws RecognitionException {
        // src/npj/ast/parser/NPJ.g:1:8: ( T__7 | T__8 | T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | STRING_CONSTANT | INTEGER_CONSTANT | WS )
        int alt3 = 14;
        switch (input.LA(1)) {
            case '\"': {
                alt3 = 1;
            }
            break;
            case '.': {
                alt3 = 2;
            }
            break;
            case ';': {
                alt3 = 3;
            }
            break;
            case '=': {
                alt3 = 4;
            }
            break;
            case 'C': {
                int LA3_5 = input.LA(2);
                if ((LA3_5 == 'o')) {
                    int LA3_14 = input.LA(3);
                    if ((LA3_14 == 'l')) {
                        int LA3_20 = input.LA(4);
                        if ((LA3_20 == 'l')) {
                            int LA3_25 = input.LA(5);
                            if ((LA3_25 == 'e')) {
                                int LA3_30 = input.LA(6);
                                if ((LA3_30 == 'c')) {
                                    int LA3_35 = input.LA(7);
                                    if ((LA3_35 == 't')) {
                                        int LA3_39 = input.LA(8);
                                        if (((LA3_39 >= '0' && LA3_39 <= '9') || (LA3_39 >= 'A' && LA3_39 <= 'Z') || (LA3_39 >= 'a' && LA3_39 <= 'z'))) {
                                            alt3 = 12;
                                        } else {
                                            alt3 = 5;
                                        }

                                    } else {
                                        alt3 = 12;
                                    }

                                } else {
                                    alt3 = 12;
                                }

                            } else {
                                alt3 = 12;
                            }

                        } else {
                            alt3 = 12;
                        }

                    } else {
                        alt3 = 12;
                    }

                } else {
                    alt3 = 12;
                }

            }
            break;
            case 'H': {
                int LA3_6 = input.LA(2);
                if ((LA3_6 == 'e')) {
                    int LA3_15 = input.LA(3);
                    if ((LA3_15 == 'a')) {
                        int LA3_21 = input.LA(4);
                        if ((LA3_21 == 'p')) {
                            int LA3_26 = input.LA(5);
                            if ((LA3_26 == 'A')) {
                                int LA3_31 = input.LA(6);
                                if ((LA3_31 == 'n')) {
                                    int LA3_36 = input.LA(7);
                                    if ((LA3_36 == 'a')) {
                                        int LA3_40 = input.LA(8);
                                        if ((LA3_40 == 'l')) {
                                            int LA3_43 = input.LA(9);
                                            if ((LA3_43 == 'y')) {
                                                int LA3_46 = input.LA(10);
                                                if ((LA3_46 == 'z')) {
                                                    int LA3_49 = input.LA(11);
                                                    if ((LA3_49 == 'e')) {
                                                        int LA3_50 = input.LA(12);
                                                        if (((LA3_50 >= '0' && LA3_50 <= '9') || (LA3_50 >= 'A' && LA3_50 <= 'Z') || (LA3_50 >= 'a' && LA3_50 <= 'z'))) {
                                                            alt3 = 12;
                                                        } else {
                                                            alt3 = 6;
                                                        }

                                                    } else {
                                                        alt3 = 12;
                                                    }

                                                } else {
                                                    alt3 = 12;
                                                }

                                            } else {
                                                alt3 = 12;
                                            }

                                        } else {
                                            alt3 = 12;
                                        }

                                    } else {
                                        alt3 = 12;
                                    }

                                } else {
                                    alt3 = 12;
                                }

                            } else {
                                alt3 = 12;
                            }

                        } else {
                            alt3 = 12;
                        }

                    } else {
                        alt3 = 12;
                    }

                } else {
                    alt3 = 12;
                }

            }
            break;
            case 'N': {
                int LA3_7 = input.LA(2);
                if ((LA3_7 == 'U')) {
                    int LA3_16 = input.LA(3);
                    if ((LA3_16 == 'L')) {
                        int LA3_22 = input.LA(4);
                        if ((LA3_22 == 'L')) {
                            int LA3_27 = input.LA(5);
                            if (((LA3_27 >= '0' && LA3_27 <= '9') || (LA3_27 >= 'A' && LA3_27 <= 'Z') || (LA3_27 >= 'a' && LA3_27 <= 'z'))) {
                                alt3 = 12;
                            } else {
                                alt3 = 7;
                            }

                        } else {
                            alt3 = 12;
                        }

                    } else {
                        alt3 = 12;
                    }

                } else {
                    alt3 = 12;
                }

            }
            break;
            case 'P': {
                int LA3_8 = input.LA(2);
                if ((LA3_8 == 'r')) {
                    int LA3_17 = input.LA(3);
                    if ((LA3_17 == 'i')) {
                        int LA3_23 = input.LA(4);
                        if ((LA3_23 == 'n')) {
                            int LA3_28 = input.LA(5);
                            if ((LA3_28 == 't')) {
                                int LA3_33 = input.LA(6);
                                if (((LA3_33 >= '0' && LA3_33 <= '9') || (LA3_33 >= 'A' && LA3_33 <= 'Z') || (LA3_33 >= 'a' && LA3_33 <= 'z'))) {
                                    alt3 = 12;
                                } else {
                                    alt3 = 8;
                                }

                            } else {
                                alt3 = 12;
                            }

                        } else {
                            alt3 = 12;
                        }

                    } else {
                        alt3 = 12;
                    }

                } else {
                    alt3 = 12;
                }

            }
            break;
            case 'V': {
                int LA3_9 = input.LA(2);
                if ((LA3_9 == 'a')) {
                    int LA3_18 = input.LA(3);
                    if ((LA3_18 == 'r')) {
                        int LA3_24 = input.LA(4);
                        if ((LA3_24 == 'D')) {
                            int LA3_29 = input.LA(5);
                            if ((LA3_29 == 'e')) {
                                int LA3_34 = input.LA(6);
                                if ((LA3_34 == 'c')) {
                                    int LA3_38 = input.LA(7);
                                    if ((LA3_38 == 'l')) {
                                        switch (input.LA(8)) {
                                            case 'S': {
                                                int LA3_44 = input.LA(9);
                                                if (((LA3_44 >= '0' && LA3_44 <= '9') || (LA3_44 >= 'A' && LA3_44 <= 'Z') || (LA3_44 >= 'a' && LA3_44 <= 'z'))) {
                                                    alt3 = 12;
                                                } else {
                                                    alt3 = 9;
                                                }

                                            }
                                            break;
                                            case 'T': {
                                                int LA3_45 = input.LA(9);
                                                if (((LA3_45 >= '0' && LA3_45 <= '9') || (LA3_45 >= 'A' && LA3_45 <= 'Z') || (LA3_45 >= 'a' && LA3_45 <= 'z'))) {
                                                    alt3 = 12;
                                                } else {
                                                    alt3 = 10;
                                                }

                                            }
                                            break;
                                            default:
                                                alt3 = 12;
                                        }
                                    } else {
                                        alt3 = 12;
                                    }

                                } else {
                                    alt3 = 12;
                                }

                            } else {
                                alt3 = 12;
                            }

                        } else {
                            alt3 = 12;
                        }

                    } else {
                        alt3 = 12;
                    }

                } else {
                    alt3 = 12;
                }

            }
            break;
            case '\n': {
                alt3 = 11;
            }
            break;
            case 'A':
            case 'B':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'O':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z': {
                alt3 = 12;
            }
            break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9': {
                alt3 = 13;
            }
            break;
            case '\t':
            case '\r':
            case ' ': {
                alt3 = 14;
            }
            break;
            default:
                NoViableAltException nvae =
                        new NoViableAltException("", 3, 0, input);
                throw nvae;
        }
        switch (alt3) {
            case 1:
                // src/npj/ast/parser/NPJ.g:1:10: T__7
            {
                mT__7();

            }
            break;
            case 2:
                // src/npj/ast/parser/NPJ.g:1:15: T__8
            {
                mT__8();

            }
            break;
            case 3:
                // src/npj/ast/parser/NPJ.g:1:20: T__9
            {
                mT__9();

            }
            break;
            case 4:
                // src/npj/ast/parser/NPJ.g:1:25: T__10
            {
                mT__10();

            }
            break;
            case 5:
                // src/npj/ast/parser/NPJ.g:1:31: T__11
            {
                mT__11();

            }
            break;
            case 6:
                // src/npj/ast/parser/NPJ.g:1:37: T__12
            {
                mT__12();

            }
            break;
            case 7:
                // src/npj/ast/parser/NPJ.g:1:43: T__13
            {
                mT__13();

            }
            break;
            case 8:
                // src/npj/ast/parser/NPJ.g:1:49: T__14
            {
                mT__14();

            }
            break;
            case 9:
                // src/npj/ast/parser/NPJ.g:1:55: T__15
            {
                mT__15();

            }
            break;
            case 10:
                // src/npj/ast/parser/NPJ.g:1:61: T__16
            {
                mT__16();

            }
            break;
            case 11:
                // src/npj/ast/parser/NPJ.g:1:67: T__17
            {
                mT__17();

            }
            break;
            case 12:
                // src/npj/ast/parser/NPJ.g:1:73: STRING_CONSTANT
            {
                mSTRING_CONSTANT();

            }
            break;
            case 13:
                // src/npj/ast/parser/NPJ.g:1:89: INTEGER_CONSTANT
            {
                mINTEGER_CONSTANT();

            }
            break;
            case 14:
                // src/npj/ast/parser/NPJ.g:1:106: WS
            {
                mWS();

            }
            break;

        }
    }


}
