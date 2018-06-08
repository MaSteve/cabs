package com.raincoatmoon.Nodes.Functions;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

import java.util.HashMap;

public class ReturnNode extends ParserNode{
    private ParserNode exp;

    public ReturnNode(Yytoken token, ParserNode exp) {
        super(token);
        this.exp = exp;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        exp.translate(auxiliarVariableCounter);
        Application.newInst("return ");
        Application.newInst(exp.auxiliarVar());
        Application.newInst(";");
        Application.endInst();
    }

    public Type getTYPE() {
        TYPE = exp.getTYPE();
        return TYPE;
    }

    @Override
    public void solveReferences(HashMap<String, Declaration> previous) {
        exp.solveReferences(previous);
    }

    @Override
    public String toString() {
        return "RETURN:\n" + exp;
    }
}
