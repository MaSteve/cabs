package com.raincoatmoon.Nodes;

import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

public class TypeNode extends ParserNode {
    private String value;

    public TypeNode(Yytoken token) {
        super(token);
        value = token.m_text;
    }

    @Override
    public String toString() {
        return value;
    }

    public Type getTYPE() {
        switch (value) {
            case "int": return Type.INT;
            case "bool": return Type.BOOL;
            case "void": return Type.VOID;
            default: return Type.FAIL;
        }
    }
}
