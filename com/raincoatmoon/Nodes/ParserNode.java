package com.raincoatmoon.Nodes;

import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

import java.util.HashMap;

public abstract class ParserNode {
    public Yytoken token;
    protected boolean decl = false;
    protected boolean controlStructure = false;
    protected Type TYPE = Type.UNKNOWN;
    protected String auxiliarVar = null;


    public void solveReferences(HashMap<String, Declaration> previous) {

    }

    public ParserNode() {
        token = null;
    }

    public ParserNode(Yytoken token) {
        this.token = token;
    }

    public int getLine() {
        return token == null? -1: token.m_line;
    }

    public int getColumn() {
        return token == null? -1: token.m_col;
    }

    public boolean isDecl() {
        return decl;
    }

    public boolean isControlStructure() {
        return controlStructure;
    }

    public void setTYPE(Type type) {
        this.TYPE = type;
    }

    public Type getTYPE() {
        return TYPE;
    }

    @Override
    public abstract String toString();

    public boolean isReferenceNode() {
        return false;
    }

    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {

    }

    public String auxiliarVar() {
        return auxiliarVar;
    }
}
