package com.raincoatmoon.Parser;

import com.raincoatmoon.Application;

class Sample {
    public static void main(String argv[]) throws java.io.IOException {
        /*Yylex yy = new Yylex(System.in);
        Yytoken t;
        while ((t = yy.yylex()) != null)
            System.out.println(t);*/
    }
}

class Utility {
    public static void assertExp (boolean expr) {
        if (false == expr) {
            throw (new Error("Error: Assertion failed."));
        }
    }

    private static final String errorMsg[] = {
        "Error: Unmatched end-of-comment punctuation.",
        "Error: Unmatched start-of-comment punctuation.",
        "Error: Unclosed string.",
        "Error: Illegal character."
    };

    public static final int E_ENDCOMMENT = 0;
    public static final int E_STARTCOMMENT = 1;
    public static final int E_UNCLOSEDSTR = 2;
    public static final int E_UNMATCHED = 3;

    public static void error(int code) {
        System.out.println(errorMsg[code]);
    }
}

%%

%{
    private int col_offset = 0;
%}
%unicode
%line
%char
%cup
%state COMMENT

ALPHA=[A-Za-z]
SHARP=[#]
DIGIT=[0-9]
WHITE_SPACE_CHAR=[\ \t\b]
END_LINE = (\n)*
COMMENT_TEXT=([^\n])*

%%

<YYINITIAL> ";" { return (new Yytoken(sym.SEMI, 0, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "=" { return (new Yytoken(sym.ASS, 1, yytext(), yyline, yychar, yychar+2, yychar-col_offset)); }
<YYINITIAL> "{" { return (new Yytoken(sym.LB, 2, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "}" { return (new Yytoken(sym.RB, 3, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "(" { return (new Yytoken(sym.LP, 4, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> ")" { return (new Yytoken(sym.RP, 5, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "<" { return (new Yytoken(sym.LT, 6, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> ">" { return (new Yytoken(sym.GT, 7, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "<=" { return (new Yytoken(sym.LE, 8, yytext(), yyline, yychar, yychar+2, yychar-col_offset)); }
<YYINITIAL> ">=" { return (new Yytoken(sym.GE, 9, yytext(), yyline, yychar, yychar+2, yychar-col_offset)); }
<YYINITIAL> "==" { return (new Yytoken(sym.EQ, 10, yytext(), yyline, yychar, yychar+2, yychar-col_offset)); }
<YYINITIAL> "!=" { return (new Yytoken(sym.NEQ, 11, yytext(), yyline, yychar, yychar+2, yychar-col_offset)); }
<YYINITIAL> "+" { return (new Yytoken(sym.PLUS, 12, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "-" { return (new Yytoken(sym.MINUS, 13, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "*" { return (new Yytoken(sym.TIMES, 14, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "/" { return (new Yytoken(sym.DIV, 15, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "%" { return (new Yytoken(sym.MOD, 16, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "&&" { return (new Yytoken(sym.AND, 17, yytext(), yyline, yychar, yychar+2, yychar-col_offset)); }
<YYINITIAL> "||" { return (new Yytoken(sym.OR, 18, yytext(), yyline, yychar, yychar+2, yychar-col_offset)); }
<YYINITIAL> "!" { return (new Yytoken(sym.NOT, 19, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "," { return (new Yytoken(sym.COMMA, 20, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "[" { return (new Yytoken(sym.LBR, 21, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }
<YYINITIAL> "]" { return (new Yytoken(sym.RBR, 22, yytext(), yyline, yychar, yychar+1, yychar-col_offset)); }


<YYINITIAL> {END_LINE} { col_offset = yychar + yytext().length(); }

<YYINITIAL> {WHITE_SPACE_CHAR}+ { }

<YYINITIAL> {DIGIT}+ {
	return (new Yytoken(sym.NUM, 42,yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
}
<YYINITIAL> {ALPHA}({ALPHA}|{DIGIT}|_)* {
    String str = yytext();
    switch(str) {
        case "if": return (new Yytoken(sym.IF, 50, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        case "else": return (new Yytoken(sym.ELSE, 51, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        case "while": return (new Yytoken(sym.LOOP, 52, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        case "return": return (new Yytoken(sym.RETURN, 55, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        case "int": return (new Yytoken(sym.TYPE, 60, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        case "bool": return (new Yytoken(sym.TYPE, 61, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        case "void": return (new Yytoken(sym.PROC, 62, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        case "true": return (new Yytoken(sym.BOOL, 70, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        case "false": return (new Yytoken(sym.BOOL, 71, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        case "thread": return (new Yytoken(sym.THREAD, 90, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
        default: return (new Yytoken(sym.ID, 80, yytext(),yyline,yychar,yychar + yytext().length(), yychar-col_offset));
    }
}

<YYINITIAL> {SHARP} { yybegin(COMMENT); }

<COMMENT> {COMMENT_TEXT} { }

<COMMENT> {END_LINE} { yybegin(YYINITIAL); col_offset = yychar + yytext().length(); }

<YYINITIAL,COMMENT> . {
        Application.notifyError(Application.LEX_MSG + "<" + yytext() + "> (" + yyline + ", " + (yychar-col_offset) + ")");
        //System.out.println("Illegal character: <" + yytext() + ">");
	    //Utility.error(Utility.E_UNMATCHED);
}
