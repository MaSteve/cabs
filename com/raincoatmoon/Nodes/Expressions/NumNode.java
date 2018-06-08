package com.raincoatmoon.Nodes.Expressions;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

public class NumNode extends ParserNode {
    private int value;

    public NumNode(Yytoken token) {
        super(token);
        value = Integer.valueOf(token.m_text);
    }

    public Type getTYPE() {
        return Type.INT;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        auxiliarVar = auxiliarVariableCounter.nextPreambleIndx();
        Application.newInst(getTYPE().getABSType() + " " + auxiliarVar + " = " + value + ";");
        Application.endInst();
    }

    @Override
    public String toString() {
        return "NumNode: " + value;
    }
}
