/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }
    
    private void string_buf_append(String str) {
        if (string_buf.length() < MAX_STR_CONST) {
            string_buf.append(str);
        }
        /* check length AFTER appending */
        if (string_buf.length() >= MAX_STR_CONST) {
            yybegin(STRING_ERROR);
        }
    }
    
    private void string_buf_append(char c) {
        string_buf_append(String.valueOf(c));
    } 

    private int comment_level = 0;
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
    /* it's ok to reach EOF in normal state */
    case INLINECOMMENT: 
    /* it's ok to reach EOF in inlinecomment */
	    return new Symbol(TokenConstants.EOF);
    case COMMENT:
    /* it's an error to reach EOF in comment */
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
    /* it's an error to reach EOF in string */
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in String constant");
    }
%eofval}

%class CoolLexer
%cup
%state COMMENT
%state INLINECOMMENT
%state STRING
%state STRING_ERROR

UPPER            = [A-Z]
LOWER            = [a-z]
ALPHA            = [A-Za-z]
DIGIT            = [0-9]
TYPID            = {UPPER}({ALPHA}|{DIGIT}|_)*
OBJID            = {LOWER}({ALPHA}|{DIGIT}|_)*
INTEGER          = {DIGIT}+
SPACE            = [ \f\r\b\t\x0b]+
STRINGS          = [^\\\n\0\"]*


%%

<YYINITIAL>"=>"			                    { /* Sample lexical rule for "=>" arrow.
                                                 Further lexical rules should be defined
                                                 here, after the last %% separator */
                                              return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>","                              { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>"."                              { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>";"                              { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>":"                              { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>"@"                              { return new Symbol(TokenConstants.AT); }
<YYINITIAL>"<-"                             { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"~"                              { return new Symbol(TokenConstants.NEG); }
<YYINITIAL>"*"                              { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"/"                              { return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"+"                              { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"-"                              { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"="                              { return new Symbol(TokenConstants.EQ); }
<YYINITIAL>"<"                              { return new Symbol(TokenConstants.LT); }
<YYINITIAL>"<="                             { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"{"                              { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>"}"                              { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>"("                              { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")"                              { return new Symbol(TokenConstants.RPAREN); }

<YYINITIAL>[Cc][Ll][Aa][Ss][Ss]             { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>[Ee][Ll][Ss][Ee]                 { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>f[Aa][Ll][Ss][Ee]                { return new Symbol(TokenConstants.BOOL_CONST, java.lang.Boolean.FALSE); }
<YYINITIAL>[Ff][Ii]                         { return new Symbol(TokenConstants.FI); }
<YYINITIAL>[Ii][Ff]                         { return new Symbol(TokenConstants.IF); }
<YYINITIAL>[Ii][Nn]                         { return new Symbol(TokenConstants.IN); }
<YYINITIAL>[Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss] { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>[Ii][Ss][Vv][Oo][Ii][Dd]         { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>[Ll][Ee][Tt]                     { return new Symbol(TokenConstants.LET); }
<YYINITIAL>[Ll][Oo][Oo][Pp]                 { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>[Pp][Oo][Oo][Ll]                 { return new Symbol(TokenConstants.POOL); }
<YYINITIAL>[Tt][Hh][Ee][Nn]                 { return new Symbol(TokenConstants.THEN); }
<YYINITIAL>[Ww][Hh][Ii][Ll][Ee]             { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>[Cc][Aa][Ss][Ee]                 { return new Symbol(TokenConstants.CASE); }
<YYINITIAL>[Ee][Ss][Aa][Cc]                 { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>[Nn][Ee][Ww]                     { return new Symbol(TokenConstants.NEW); }
<YYINITIAL>[Oo][Ff]                         { return new Symbol(TokenConstants.OF); }
<YYINITIAL>[Nn][Oo][Tt]                     { return new Symbol(TokenConstants.NOT); }
<YYINITIAL>t[Rr][Uu][Ee]                    { return new Symbol(TokenConstants.BOOL_CONST, java.lang.Boolean.TRUE); }

<YYINITIAL>{INTEGER}                        
{ 
    String intString = yytext();
    AbstractSymbol entry = AbstractTable.inttable.addString(intString);                                            
    return new Symbol(TokenConstants.INT_CONST, entry);
}

<YYINITIAL>{TYPID}
{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}

<YYINITIAL>{OBJID}
{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}

<YYINITIAL>\"
{ 
    yybegin(STRING);
    string_buf.setLength(0); /* clear string buffer */
}

<STRING>{STRINGS}
{ 
    String str = yytext();
    string_buf_append(str);
}

<STRING>\0
{   /* null character in string, this is an error */
    yybegin(STRING_ERROR);
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}

<STRING>\\\0
{   /* escaped null character in string, this is an error */
    yybegin(STRING_ERROR);
    return new Symbol(TokenConstants.ERROR, "String contains escaped null character");
}

<STRING>"\b"    { string_buf_append('\b'); }
<STRING>"\f"    { string_buf_append('\f'); }
<STRING>"\t"    { string_buf_append('\t'); }
<STRING>"\n"    { string_buf_append('\n'); }
<STRING>\\\n    { string_buf_append('\n');  curr_lineno++; }
<STRING>\\.
{   /* any escaped character except \b\f\t\n is just the char */ 
    String str = yytext();
    string_buf_append(str.charAt(str.length() - 1));
}

<STRING>\n
{   /* newline without escape in string, this is an error */
    curr_lineno++;
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); 
}

<STRING>\"
{   /* end of string, return string buffer */
    yybegin(YYINITIAL);
    AbstractSymbol entry = AbstractTable.stringtable.addString(string_buf.toString());
    return new Symbol(TokenConstants.STR_CONST, entry);
}

<STRING_ERROR>[^\"\n\\] { /* in string error state, but nothing wrong here */ }
<STRING_ERROR>\\\n      { /* legal newline */ curr_lineno++; }

<STRING_ERROR>\n
{   /* unescaped newline error reported whenever encountered,
       here only report possible too long string error after newline. */
    curr_lineno++;
    yybegin(YYINITIAL);
    if (string_buf.length() >= MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long"); 
    }
}

<STRING_ERROR>\"
{   /* unterminated string reported when EOF reached,
       here only report possible too long string error. */
    yybegin(YYINITIAL);
    if (string_buf.length() >= MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long"); 
    }
}

<STRING_ERROR>\\.       { /* match escaped sequence */ }

<YYINITIAL>{SPACE}      {}
<YYINITIAL>"--"         { yybegin(INLINECOMMENT); }
<YYINITIAL>"(*"         { yybegin(COMMENT); comment_level++; }
<YYINITIAL,COMMENT>\n   { curr_lineno++; }
<INLINECOMMENT>\n       { curr_lineno++; yybegin(YYINITIAL); }

<COMMENT>"(*"           { comment_level++; }
<COMMENT>"*)"
{   /* back to YYINITAIL if nested comments match */
    comment_level--;
    if (comment_level == 0) yybegin(YYINITIAL); 
}

<YYINITIAL>"*)"         { return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }

<COMMENT,INLINECOMMENT>[\r\x0b]+    {/* JLex dot metacharactor doesn't match newline 
                                        and carriage return. JLex doesn't know vertical
                                        tab \v so use numeric \x0b insted */}
<COMMENT,INLINECOMMENT>.    {}

.                           { /* This rule should be the very last
                                 in your lexical specification and
                                 will match match everything not
                                 matched by other lexical rules. */
                              return new Symbol(TokenConstants.ERROR, yytext()); }
