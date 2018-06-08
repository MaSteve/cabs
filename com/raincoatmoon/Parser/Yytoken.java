package com.raincoatmoon.Parser;

import com.raincoatmoon.Application;
import java_cup.runtime.Symbol;

public class Yytoken extends Symbol {
    Yytoken(int symb, int index, String text, int line, int charBegin, int charEnd, int col) {
        super(symb);
        this.value = this;
        m_index = index;
        m_text = text;
        m_line = line;
        m_charBegin = charBegin;
        m_charEnd = charEnd;
        m_col = col;
        if (Application.debug()) System.out.println(this);
    }
    public int m_index;
    public String m_text;
    public int m_line;
    public int m_charBegin;
    public int m_charEnd;
    public int m_col;
    public String toString() {
        return "Token #"+m_index+": "+m_text+" (line "+m_line+" col "+m_col+")";
    }
}
