package com.raincoatmoon.Nodes.ControlStructures;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;

import java.util.HashMap;

public class LoopNode extends ParserNode {
    private ParserNode cond;
    private ParserNode block;

    public LoopNode(ParserNode cond, ParserNode block) {
        this.cond = cond;
        this.block = block;
        controlStructure = true;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        cond.solveReferences(previous);
        block.solveReferences(previous);
    }

    public Type getTYPE() {
        if (cond.getTYPE() == Type.BOOL && block.getTYPE() == Type.OK) TYPE = Type.OK;
        else TYPE = Type.FAIL;
        return TYPE;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        cond.translate(auxiliarVariableCounter);
        String condVariable = cond.auxiliarVar();
        Application.newInst("while (" + condVariable + ") {");
        Application.endInst();
        block.translate(auxiliarVariableCounter);
        cond.translate(auxiliarVariableCounter);
        Application.newInst(condVariable + " = " + cond.auxiliarVar() + ";");
        Application.endInst();
        Application.newInst("}");
        Application.endInst();
    }

    @Override
    public String toString() {
        return "While: " + cond + " { " + block + " } ";
    }
}
