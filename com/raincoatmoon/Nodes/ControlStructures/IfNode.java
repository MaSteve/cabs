package com.raincoatmoon.Nodes.ControlStructures;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;

import java.util.HashMap;

public class IfNode extends ParserNode {
    private ParserNode cond;
    private ParserNode block1;
    private ParserNode block2;
    private boolean elseBranch;

    public IfNode(ParserNode cond, ParserNode block1, ParserNode block2) {
        this.cond = cond;
        this.block1 = block1;
        this.block2 = block2;
        elseBranch = true;
        controlStructure = true;
    }

    public IfNode(ParserNode cond, ParserNode block1) {
        this.cond = cond;
        this.block1 = block1;
        elseBranch = false;
        controlStructure = true;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        cond.solveReferences(previous);
        block1.solveReferences(previous);
        if (elseBranch) block2.solveReferences(previous);
    }

    public Type getTYPE() {
        Type condType = cond.getTYPE();
        Type block2Type = Type.OK;
        if (elseBranch) block2Type = block2.getTYPE();
        if (condType == Type.BOOL && block1.getTYPE() == block2Type && block2Type == Type.OK) TYPE = Type.OK;
        else TYPE = Type.FAIL;
        return TYPE;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        cond.translate(auxiliarVariableCounter);
        Application.newInst("if (" + cond.auxiliarVar() + ") {");
        Application.endInst();
        block1.translate(auxiliarVariableCounter);
        if (elseBranch) {
            Application.newInst("} ");
            Application.newInst("else {");
            Application.endInst();
            block2.translate(auxiliarVariableCounter);
        }
        Application.newInst("}");
        Application.endInst();
    }

    @Override
    public String toString() {
        return "If: " + cond + " ; " + " { " + block1 + " } " + (elseBranch? "Else { " + block2 + " } ": "");
    }
}
