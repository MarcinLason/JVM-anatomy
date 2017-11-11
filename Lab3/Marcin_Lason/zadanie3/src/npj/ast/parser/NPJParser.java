// $ANTLR 3.5.2 src/npj/ast/parser/NPJ.g 2016-11-10 18:15:26

package npj.ast.parser;

import npj.ast.data.*;
import npj.ast.statements.*;
import npj.ast.parser.*;
import java.io.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class NPJParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "INTEGER_CONSTANT", "STRING_CONSTANT", 
		"WS", "'\"'", "'.'", "';'", "'='", "'Collect'", "'HeapAnalyze'", "'NULL'", 
		"'Print'", "'VarDeclS'", "'VarDeclT'", "'\\n'"
	};
	public static final int EOF=-1;
	public static final int T__7=7;
	public static final int T__8=8;
	public static final int T__9=9;
	public static final int T__10=10;
	public static final int T__11=11;
	public static final int T__12=12;
	public static final int T__13=13;
	public static final int T__14=14;
	public static final int T__15=15;
	public static final int T__16=16;
	public static final int T__17=17;
	public static final int INTEGER_CONSTANT=4;
	public static final int STRING_CONSTANT=5;
	public static final int WS=6;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public NPJParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public NPJParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return NPJParser.tokenNames; }
	@Override public String getGrammarFileName() { return "src/npj/ast/parser/NPJ.g"; }



	public static List<Statement> parse(InputStream input) throws IOException, RecognitionException {
	    NPJLexer lex = new NPJLexer(new ANTLRInputStream(input));
	    CommonTokenStream tokens = new CommonTokenStream(lex);

	    NPJParser parser = new NPJParser(tokens);
	    return parser.program();
	}



	// $ANTLR start "program"
	// src/npj/ast/parser/NPJ.g:48:1: program returns [List<Statement> stms] : statements ;
	public final List<Statement> program() throws RecognitionException {
		List<Statement> stms = null;


		List<Statement> statements1 =null;

		try {
			// src/npj/ast/parser/NPJ.g:49:2: ( statements )
			// src/npj/ast/parser/NPJ.g:49:4: statements
			{
			pushFollow(FOLLOW_statements_in_program149);
			statements1=statements();
			state._fsp--;

			stms = statements1;
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return stms;
	}
	// $ANTLR end "program"



	// $ANTLR start "statements"
	// src/npj/ast/parser/NPJ.g:52:1: statements returns [List<Statement> stms] : (s= term_statement )* EOF ;
	public final List<Statement> statements() throws RecognitionException {
		List<Statement> stms = null;


		Statement s =null;

		try {
			// src/npj/ast/parser/NPJ.g:53:2: ( (s= term_statement )* EOF )
			// src/npj/ast/parser/NPJ.g:53:30: (s= term_statement )* EOF
			{
			List<Statement> program = new ArrayList<Statement>();
			// src/npj/ast/parser/NPJ.g:54:2: (s= term_statement )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==STRING_CONSTANT||LA1_0==9||(LA1_0 >= 11 && LA1_0 <= 12)||(LA1_0 >= 14 && LA1_0 <= 16)) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// src/npj/ast/parser/NPJ.g:54:3: s= term_statement
					{
					pushFollow(FOLLOW_term_statement_in_statements203);
					s=term_statement();
					state._fsp--;

					program.add(s);
					}
					break;

				default :
					break loop1;
				}
			}

			match(input,EOF,FOLLOW_EOF_in_statements219); 
			stms = program;
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return stms;
	}
	// $ANTLR end "statements"



	// $ANTLR start "term_statement"
	// src/npj/ast/parser/NPJ.g:58:1: term_statement returns [Statement s] : st= statement ';' ( '\\n' )? ;
	public final Statement term_statement() throws RecognitionException {
		Statement s = null;


		Statement st =null;

		try {
			// src/npj/ast/parser/NPJ.g:59:2: (st= statement ';' ( '\\n' )? )
			// src/npj/ast/parser/NPJ.g:59:4: st= statement ';' ( '\\n' )?
			{
			pushFollow(FOLLOW_statement_in_term_statement261);
			st=statement();
			state._fsp--;

			match(input,9,FOLLOW_9_in_term_statement263); 
			// src/npj/ast/parser/NPJ.g:59:23: ( '\\n' )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==17) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// src/npj/ast/parser/NPJ.g:59:24: '\\n'
					{
					match(input,17,FOLLOW_17_in_term_statement266); 
					}
					break;

			}

			s = st;
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return s;
	}
	// $ANTLR end "term_statement"



	// $ANTLR start "statement"
	// src/npj/ast/parser/NPJ.g:62:1: statement returns [Statement s] : ( variable_declaration | assignment | 'Print' id= string_constant | 'Print' '\"' string_constant '\"' | 'HeapAnalyze' | 'Collect' |);
	public final Statement statement() throws RecognitionException {
		Statement s = null;


		String id =null;
		Declaration variable_declaration2 =null;
		Assignment assignment3 =null;
		String string_constant4 =null;

		try {
			// src/npj/ast/parser/NPJ.g:63:2: ( variable_declaration | assignment | 'Print' id= string_constant | 'Print' '\"' string_constant '\"' | 'HeapAnalyze' | 'Collect' |)
			int alt3=7;
			switch ( input.LA(1) ) {
			case 15:
			case 16:
				{
				alt3=1;
				}
				break;
			case STRING_CONSTANT:
				{
				alt3=2;
				}
				break;
			case 14:
				{
				int LA3_3 = input.LA(2);
				if ( (LA3_3==7) ) {
					alt3=4;
				}
				else if ( (LA3_3==STRING_CONSTANT) ) {
					alt3=3;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 3, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 12:
				{
				alt3=5;
				}
				break;
			case 11:
				{
				alt3=6;
				}
				break;
			case 9:
				{
				alt3=7;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}
			switch (alt3) {
				case 1 :
					// src/npj/ast/parser/NPJ.g:63:4: variable_declaration
					{
					pushFollow(FOLLOW_variable_declaration_in_statement289);
					variable_declaration2=variable_declaration();
					state._fsp--;

					s = variable_declaration2;
					}
					break;
				case 2 :
					// src/npj/ast/parser/NPJ.g:64:4: assignment
					{
					pushFollow(FOLLOW_assignment_in_statement313);
					assignment3=assignment();
					state._fsp--;

					s = assignment3;
					}
					break;
				case 3 :
					// src/npj/ast/parser/NPJ.g:65:4: 'Print' id= string_constant
					{
					match(input,14,FOLLOW_14_in_statement347); 
					pushFollow(FOLLOW_string_constant_in_statement353);
					id=string_constant();
					state._fsp--;

					s = PrintStatement.ofDereference(id);
					}
					break;
				case 4 :
					// src/npj/ast/parser/NPJ.g:66:4: 'Print' '\"' string_constant '\"'
					{
					match(input,14,FOLLOW_14_in_statement369); 
					match(input,7,FOLLOW_7_in_statement371); 
					pushFollow(FOLLOW_string_constant_in_statement373);
					string_constant4=string_constant();
					state._fsp--;

					match(input,7,FOLLOW_7_in_statement375); 
					s = PrintStatement.ofLiteral(string_constant4);
					}
					break;
				case 5 :
					// src/npj/ast/parser/NPJ.g:67:4: 'HeapAnalyze'
					{
					match(input,12,FOLLOW_12_in_statement388); 
					s = HeapAnalyze.instance();
					}
					break;
				case 6 :
					// src/npj/ast/parser/NPJ.g:68:4: 'Collect'
					{
					match(input,11,FOLLOW_11_in_statement419); 
					s = Collect.instance();
					}
					break;
				case 7 :
					// src/npj/ast/parser/NPJ.g:69:42: 
					{
					s = EmptyStatement.instance();
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return s;
	}
	// $ANTLR end "statement"



	// $ANTLR start "variable_declaration"
	// src/npj/ast/parser/NPJ.g:72:1: variable_declaration returns [Declaration d] : ( 'VarDeclT' string_constant | 'VarDeclS' name= string_constant '\"' val= string_constant '\"' | 'VarDeclS' string_constant 'NULL' );
	public final Declaration variable_declaration() throws RecognitionException {
		Declaration d = null;


		String name =null;
		String val =null;
		String string_constant5 =null;
		String string_constant6 =null;

		try {
			// src/npj/ast/parser/NPJ.g:73:2: ( 'VarDeclT' string_constant | 'VarDeclS' name= string_constant '\"' val= string_constant '\"' | 'VarDeclS' string_constant 'NULL' )
			int alt4=3;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==16) ) {
				alt4=1;
			}
			else if ( (LA4_0==15) ) {
				int LA4_2 = input.LA(2);
				if ( (LA4_2==STRING_CONSTANT) ) {
					int LA4_3 = input.LA(3);
					if ( (LA4_3==7) ) {
						alt4=2;
					}
					else if ( (LA4_3==13) ) {
						alt4=3;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 4, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 4, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}

			switch (alt4) {
				case 1 :
					// src/npj/ast/parser/NPJ.g:73:4: 'VarDeclT' string_constant
					{
					match(input,16,FOLLOW_16_in_variable_declaration495); 
					pushFollow(FOLLOW_string_constant_in_variable_declaration497);
					string_constant5=string_constant();
					state._fsp--;

					d = TDeclaration.of(string_constant5);
					}
					break;
				case 2 :
					// src/npj/ast/parser/NPJ.g:74:4: 'VarDeclS' name= string_constant '\"' val= string_constant '\"'
					{
					match(input,15,FOLLOW_15_in_variable_declaration543); 
					pushFollow(FOLLOW_string_constant_in_variable_declaration549);
					name=string_constant();
					state._fsp--;

					match(input,7,FOLLOW_7_in_variable_declaration551); 
					pushFollow(FOLLOW_string_constant_in_variable_declaration557);
					val=string_constant();
					state._fsp--;

					match(input,7,FOLLOW_7_in_variable_declaration559); 
					d = SDeclaration.of(name, val);
					}
					break;
				case 3 :
					// src/npj/ast/parser/NPJ.g:75:4: 'VarDeclS' string_constant 'NULL'
					{
					match(input,15,FOLLOW_15_in_variable_declaration568); 
					pushFollow(FOLLOW_string_constant_in_variable_declaration570);
					string_constant6=string_constant();
					state._fsp--;

					match(input,13,FOLLOW_13_in_variable_declaration572); 
					d = SDeclaration.of(string_constant6, null);
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return d;
	}
	// $ANTLR end "variable_declaration"



	// $ANTLR start "assignment"
	// src/npj/ast/parser/NPJ.g:78:1: assignment returns [Assignment assign] : d= deref '=' r= rvalue ;
	public final Assignment assignment() throws RecognitionException {
		Assignment assign = null;


		Dereference d =null;
		RValue r =null;

		try {
			// src/npj/ast/parser/NPJ.g:79:2: (d= deref '=' r= rvalue )
			// src/npj/ast/parser/NPJ.g:79:4: d= deref '=' r= rvalue
			{
			pushFollow(FOLLOW_deref_in_assignment625);
			d=deref();
			state._fsp--;

			match(input,10,FOLLOW_10_in_assignment627); 
			pushFollow(FOLLOW_rvalue_in_assignment631);
			r=rvalue();
			state._fsp--;

			assign = Assignment.of(d, r);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return assign;
	}
	// $ANTLR end "assignment"



	// $ANTLR start "rvalue"
	// src/npj/ast/parser/NPJ.g:82:1: rvalue returns [RValue value] : ( deref | 'NULL' |i= integer_constant | '\"' s= string_constant '\"' );
	public final RValue rvalue() throws RecognitionException {
		RValue value = null;


		int i =0;
		String s =null;
		Dereference deref7 =null;

		try {
			// src/npj/ast/parser/NPJ.g:83:2: ( deref | 'NULL' |i= integer_constant | '\"' s= string_constant '\"' )
			int alt5=4;
			switch ( input.LA(1) ) {
			case STRING_CONSTANT:
				{
				alt5=1;
				}
				break;
			case 13:
				{
				alt5=2;
				}
				break;
			case INTEGER_CONSTANT:
				{
				alt5=3;
				}
				break;
			case 7:
				{
				alt5=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}
			switch (alt5) {
				case 1 :
					// src/npj/ast/parser/NPJ.g:83:4: deref
					{
					pushFollow(FOLLOW_deref_in_rvalue651);
					deref7=deref();
					state._fsp--;

					value = deref7;
					}
					break;
				case 2 :
					// src/npj/ast/parser/NPJ.g:84:4: 'NULL'
					{
					match(input,13,FOLLOW_13_in_rvalue686); 
					value = Nil.value();
					}
					break;
				case 3 :
					// src/npj/ast/parser/NPJ.g:85:4: i= integer_constant
					{
					pushFollow(FOLLOW_integer_constant_in_rvalue724);
					i=integer_constant();
					state._fsp--;

					value = IntLiteral.of(i);
					}
					break;
				case 4 :
					// src/npj/ast/parser/NPJ.g:86:4: '\"' s= string_constant '\"'
					{
					match(input,7,FOLLOW_7_in_rvalue744); 
					pushFollow(FOLLOW_string_constant_in_rvalue750);
					s=string_constant();
					state._fsp--;

					match(input,7,FOLLOW_7_in_rvalue752); 
					value = StringLiteral.of(s);
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "rvalue"



	// $ANTLR start "deref"
	// src/npj/ast/parser/NPJ.g:89:1: deref returns [Dereference d] : string_constant deref2 ;
	public final Dereference deref() throws RecognitionException {
		Dereference d = null;


		String string_constant8 =null;
		String deref29 =null;

		try {
			// src/npj/ast/parser/NPJ.g:90:2: ( string_constant deref2 )
			// src/npj/ast/parser/NPJ.g:90:4: string_constant deref2
			{
			pushFollow(FOLLOW_string_constant_in_deref775);
			string_constant8=string_constant();
			state._fsp--;

			pushFollow(FOLLOW_deref2_in_deref777);
			deref29=deref2();
			state._fsp--;

			d = Dereference.of(string_constant8 + deref29);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return d;
	}
	// $ANTLR end "deref"



	// $ANTLR start "deref2"
	// src/npj/ast/parser/NPJ.g:93:1: deref2 returns [String tail] : (| '.' string_constant d= deref2 );
	public final String deref2() throws RecognitionException {
		String tail = null;


		String d =null;
		String string_constant10 =null;

		try {
			// src/npj/ast/parser/NPJ.g:94:2: (| '.' string_constant d= deref2 )
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( ((LA6_0 >= 9 && LA6_0 <= 10)) ) {
				alt6=1;
			}
			else if ( (LA6_0==8) ) {
				alt6=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// src/npj/ast/parser/NPJ.g:94:46: 
					{
					tail = "";
					}
					break;
				case 2 :
					// src/npj/ast/parser/NPJ.g:95:4: '.' string_constant d= deref2
					{
					match(input,8,FOLLOW_8_in_deref2843); 
					pushFollow(FOLLOW_string_constant_in_deref2845);
					string_constant10=string_constant();
					state._fsp--;

					pushFollow(FOLLOW_deref2_in_deref2851);
					d=deref2();
					state._fsp--;

					tail = "." + string_constant10 + d;
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return tail;
	}
	// $ANTLR end "deref2"



	// $ANTLR start "string_constant"
	// src/npj/ast/parser/NPJ.g:98:1: string_constant returns [String value] : s= STRING_CONSTANT ;
	public final String string_constant() throws RecognitionException {
		String value = null;


		Token s=null;

		try {
			// src/npj/ast/parser/NPJ.g:99:2: (s= STRING_CONSTANT )
			// src/npj/ast/parser/NPJ.g:99:4: s= STRING_CONSTANT
			{
			s=(Token)match(input,STRING_CONSTANT,FOLLOW_STRING_CONSTANT_in_string_constant883); 
			value = (s!=null?s.getText():null);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "string_constant"



	// $ANTLR start "integer_constant"
	// src/npj/ast/parser/NPJ.g:102:1: integer_constant returns [int value] : v= INTEGER_CONSTANT ;
	public final int integer_constant() throws RecognitionException {
		int value = 0;


		Token v=null;

		try {
			// src/npj/ast/parser/NPJ.g:103:2: (v= INTEGER_CONSTANT )
			// src/npj/ast/parser/NPJ.g:103:4: v= INTEGER_CONSTANT
			{
			v=(Token)match(input,INTEGER_CONSTANT,FOLLOW_INTEGER_CONSTANT_in_integer_constant904); 
			value = Integer.valueOf((v!=null?v.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "integer_constant"

	// Delegated rules



	public static final BitSet FOLLOW_statements_in_program149 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_term_statement_in_statements203 = new BitSet(new long[]{0x000000000001DA20L});
	public static final BitSet FOLLOW_EOF_in_statements219 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_statement_in_term_statement261 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_9_in_term_statement263 = new BitSet(new long[]{0x0000000000020002L});
	public static final BitSet FOLLOW_17_in_term_statement266 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_variable_declaration_in_statement289 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_assignment_in_statement313 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_14_in_statement347 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_string_constant_in_statement353 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_14_in_statement369 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_7_in_statement371 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_string_constant_in_statement373 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_7_in_statement375 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_12_in_statement388 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_11_in_statement419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_16_in_variable_declaration495 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_string_constant_in_variable_declaration497 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_15_in_variable_declaration543 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_string_constant_in_variable_declaration549 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_7_in_variable_declaration551 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_string_constant_in_variable_declaration557 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_7_in_variable_declaration559 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_15_in_variable_declaration568 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_string_constant_in_variable_declaration570 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_13_in_variable_declaration572 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_deref_in_assignment625 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_10_in_assignment627 = new BitSet(new long[]{0x00000000000020B0L});
	public static final BitSet FOLLOW_rvalue_in_assignment631 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_deref_in_rvalue651 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_13_in_rvalue686 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_integer_constant_in_rvalue724 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_7_in_rvalue744 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_string_constant_in_rvalue750 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_7_in_rvalue752 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_constant_in_deref775 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_deref2_in_deref777 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_8_in_deref2843 = new BitSet(new long[]{0x0000000000000020L});
	public static final BitSet FOLLOW_string_constant_in_deref2845 = new BitSet(new long[]{0x0000000000000100L});
	public static final BitSet FOLLOW_deref2_in_deref2851 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_CONSTANT_in_string_constant883 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INTEGER_CONSTANT_in_integer_constant904 = new BitSet(new long[]{0x0000000000000002L});
}
