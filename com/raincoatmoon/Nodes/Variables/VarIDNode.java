package com.raincoatmoon.Nodes.Variables;

import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Parser.Yytoken;

public class VarIDNode extends ParserNode {
    private String value;

    public VarIDNode(Yytoken token) {
        super(token);
        value = token.m_text;
    }

    @Override
    public String toString() {
        return value;
    }
}
