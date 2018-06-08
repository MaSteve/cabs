package com.raincoatmoon.Nodes;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Nodes.Variables.DeclarationNode;
import com.raincoatmoon.Nodes.Variables.VarReferenceNode;
import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

import java.util.HashMap;

public class AssNode extends ParserNode {
    private VarReferenceNode var;
    private ParserNode exp;

    public AssNode(Yytoken token, VarReferenceNode var, ParserNode exp) {
        super(token);
        this.var = var;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "ASS: " + var + " = " + exp;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        var.solveReferences(previous);
        exp.solveReferences(previous);
    }

    public Type getTYPE() {
        Type varType = var.getTYPE();
        Type expType = exp.getTYPE();
        if (varType == expType && varType != Type.FAIL) TYPE = Type.OK;
        else {
            Application.notifyError(Application.TYPE_MSG + ": "
                    + varType + " != " + expType
                    + " (" + getLine() + ", " + getColumn() + ")");
            TYPE = Type.FAIL;
        }
        return TYPE;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        exp.translate(auxiliarVariableCounter);
        var.codeL(auxiliarVariableCounter);

        if (((DeclarationNode) var.getDef()).isGlobal()) {
            Application.newInst(exp.auxiliarVar());
            Application.newInst(");");
            Application.endInst();
        } else {
            Application.newInst(exp.auxiliarVar());
            if (((DeclarationNode) var.getDef()).isArray()) Application.newInst(")");
            Application.newInst(";");
            Application.endInst();
        }
    }

}
