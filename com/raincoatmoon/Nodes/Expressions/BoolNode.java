package com.raincoatmoon.Nodes.Expressions;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

public class BoolNode extends ParserNode {
    private boolean value;

    public BoolNode(Yytoken token) {
        super(token);
        value = token.m_text.equals("true");
    }

    public Type getTYPE() {
        return Type.BOOL;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        auxiliarVar = auxiliarVariableCounter.nextPreambleIndx();
        Application.newInst(getTYPE().getABSType() + " " + auxiliarVar + " = ");
        Application.newInst(value? "True": "False");
        Application.newInst(";");
        Application.endInst();
    }

    @Override
    public String toString() {
        return "BoolNode: " + value;
    }
}
