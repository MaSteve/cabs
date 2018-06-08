package com.raincoatmoon.Nodes.Expressions;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;

import java.util.HashMap;

public class UnaryExpression extends ParserNode {
    private OperatorNode op;
    private ParserNode exp;

    public UnaryExpression(OperatorNode op, ParserNode exp) {
        this.op = op;
        this.exp = exp;
        op.alt();
    }

    @Override
    public String toString() {
        return "UnaryExpression: \n" + op + "\n" + exp;
    }

    public Type getTYPE() {
        if (op.getTYPE() == exp.getTYPE()) TYPE = op.getTYPE();
        else {
            Application.notifyError(Application.TYPE_MSG + ": " + Application.TYPE_EXP_MSG + op.getTYPE()
                    + " (" + op.getLine() + ", " + op.getColumn() + ")");
            TYPE = Type.FAIL;
        }
        return TYPE;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        exp.translate(auxiliarVariableCounter);
        auxiliarVar = auxiliarVariableCounter.nextPreambleIndx();
        Application.newInst(op.retType().getABSType() + " " + auxiliarVar + " = ");
        Application.newInst("(");
        op.translate(auxiliarVariableCounter);
        Application.newInst(exp.auxiliarVar());
        Application.newInst(");");
        Application.endInst();
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        exp.solveReferences(previous);
    }
}
