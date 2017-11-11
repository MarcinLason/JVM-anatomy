grammar NPJ;

options {
	language = Java;
}

@lexer::header {
package npj.ast.parser;
}

@header {
package npj.ast.parser;

import npj.ast.data.*;
import npj.ast.statements.*;
import npj.ast.parser.*;
import java.io.*;
}

@parser::members {

public static List<Statement> parse(InputStream input) throws IOException, RecognitionException {
    NPJLexer lex = new NPJLexer(new ANTLRInputStream(input));
    CommonTokenStream tokens = new CommonTokenStream(lex);

    NPJParser parser = new NPJParser(tokens);
    return parser.program();
}
}

// lexer

STRING_CONSTANT
	: (('A'..'Z') | ('a'..'z'))(('a'..'z') | ('A'..'Z') | ('0'..'9'))*
	;

INTEGER_CONSTANT
	: ('0'..'9')+
	;

WS
	: (' ' | '\t' | '\r' | '\n') { $channel = HIDDEN;}
	;

// Parser


program returns [List<Statement> stms]
	: statements    {$stms  = $statements.stms;}
	;

statements returns [List<Statement> stms]
	:                           {List<Statement> program = new ArrayList<Statement>();}
	(s = term_statement         {program.add($s.s);}
	)* EOF                      {$stms = program;}
	;

term_statement returns [Statement s]
	: st = statement ';' ('\n')?     {$s = $st.s;}
	;

statement returns [Statement s]
	: variable_declaration                  {$s = $variable_declaration.d;}
	| assignment                            {$s = $assignment.assign;}
	| 'Print' id = string_constant          {$s = PrintStatement.ofDereference($id.value);}
	| 'Print' '"' string_constant '"'       {$s = PrintStatement.ofLiteral($string_constant.value);}
	| 'HeapAnalyze'                         {$s = HeapAnalyze.instance();}
	| 'Collect'                             {$s = Collect.instance();}
	| /* epsilon */                         {$s = EmptyStatement.instance();}
	;

variable_declaration returns [Declaration d]
	: 'VarDeclT' string_constant                                        {$d = TDeclaration.of($string_constant.value);}
	| 'VarDeclS' name = string_constant '"' val = string_constant '"'   {$d = SDeclaration.of($name.value, $val.value);}
	| 'VarDeclS' string_constant 'NULL'                                 {$d = SDeclaration.of($string_constant.value, null);}
	;

assignment returns [Assignment assign]
	: d = deref '=' r=rvalue    {$assign = Assignment.of($d.d, $r.value);}
	;

rvalue returns [RValue value]
	: deref                             {$value = $deref.d;}
	| 'NULL'                            {$value = Nil.value();}
	| i = integer_constant              {$value = IntLiteral.of($i.value);}
	| '"' s = string_constant '"'       {$value = StringLiteral.of($s.value);}
	;

deref returns [Dereference d]
	: string_constant deref2             {$d = Dereference.of($string_constant.value + $deref2.tail);}
	;

deref2 returns [String tail]
	: /*epsilon*/                               {$tail = "";}
	| '.' string_constant d = deref2            {$tail = "." + $string_constant.value + $d.tail;}
	;

string_constant returns [String value]
	: s = STRING_CONSTANT {$value = $s.text;}
	;

integer_constant returns [int value]
	: v = INTEGER_CONSTANT {$value = Integer.valueOf($v.text);}
	;
