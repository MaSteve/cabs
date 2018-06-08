package com.raincoatmoon.Nodes;

import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

public abstract class Declaration extends ParserNode {
    protected int typeClass;
    protected TypeNode type;
    public static final int FUNC = 0;
    public static final int VAR = 1;
    public static final int ARG = 2;

    protected Declaration(int type) {
        super();
        this.typeClass = type;
        decl = true;
        TYPE = Type.OK;
    }
    protected Declaration(int type, Yytoken yytoken) {
        super(yytoken);
        this.typeClass = type;
        decl = true;
        TYPE = Type.OK;
    }
    public abstract String getID();

    public Type getDeclTYPE() {
        return type.getTYPE();
    }

    public int getClassType() {
        return typeClass;
    }
}
