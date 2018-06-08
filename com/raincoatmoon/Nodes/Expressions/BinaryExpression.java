package com.raincoatmoon.Nodes.Expressions;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;

import java.util.HashMap;

public class BinaryExpression extends ParserNode {
    private OperatorNode op;
    private ParserNode exp1, exp2;

    public BinaryExpression(ParserNode exp1, OperatorNode op, ParserNode exp2) {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        exp1.solveReferences(previous);
        exp2.solveReferences(previous);
    }

    public Type getTYPE() {
        Type exp1Type = exp1.getTYPE();
        if (exp1Type == exp2.getTYPE() && exp1Type == op.getTYPE()) TYPE = op.retType();
        else {
            Application.notifyError(Application.TYPE_MSG + ": " + Application.TYPE_EXP_MSG + op.getTYPE()
                    + " (" + op.getLine() + ", " + op.getColumn() + ")");
            TYPE = Type.FAIL;
        }
        return TYPE;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        exp1.translate(auxiliarVariableCounter);
        exp2.translate(auxiliarVariableCounter);
        auxiliarVar = auxiliarVariableCounter.nextPreambleIndx();
        Application.newInst(op.retType().getABSType() + " " + auxiliarVar + " = ");
        Application.newInst("(");
        Application.newInst(exp1.auxiliarVar());
        op.translate(auxiliarVariableCounter);
        Application.newInst(exp2.auxiliarVar());
        Application.newInst(");");
        Application.endInst();
    }

    @Override
    public String toString() {
        return "BinaryExpression: \n" + exp1 + "\n" + op + "\n" + exp2;
    }
}
