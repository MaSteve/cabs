package com.raincoatmoon.Nodes.Functions;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.BlockNode;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.TypeNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

import java.util.HashMap;
import java.util.Map;

public class FuncDeclarationNode extends Declaration {
    private String id;
    private ArgumentListNode arg;
    private boolean arguments;
    private BlockNode block;
    private ReturnNode returnExp;

    public FuncDeclarationNode(Yytoken token, TypeNode type, ArgumentListNode arg, BlockNode block, ReturnNode returnExp) {
        super(Declaration.FUNC, token);
        id = token.m_text;
        this.type = type;
        this.arg = arg;
        arguments = true;
        this.block = block;
        this.returnExp = returnExp;
    }

    public FuncDeclarationNode(Yytoken token, TypeNode type, BlockNode block, ReturnNode returnExp) {
        super(Declaration.FUNC, token);
        id = token.m_text;
        this.type = type;
        arguments = false;
        this.block = block;
        this.returnExp = returnExp;
    }

    public boolean hasArguments() {
        return arguments;
    }

    public ArgumentListNode getArguments() {
        return arg;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        HashMap<String, Declaration> variables = new HashMap<>();
        for (Map.Entry<String, Declaration> entry: previous.entrySet()) {
            variables.put(entry.getKey(), entry.getValue());
        }
        if (arguments) {
            arg.solveReferences(variables);
            variables = arg.getVariables();
        }
        block.solveReferences(variables);
        variables = block.getVariables();
        if (returnExp != null) returnExp.solveReferences(variables);
    }

    public Type getTYPE() {
        if (block.getTYPE() == TYPE.OK) {
            Type ret = returnExp != null? returnExp.getTYPE(): Type.VOID;
            if (ret == type.getTYPE() && ret != Type.FAIL) TYPE = Type.OK;
            else {
                Application.notifyError(Application.RETURN_TYPE_MSG);
                TYPE = Type.FAIL;
            }
        } else  TYPE = Type.FAIL;
        return TYPE;
    }

    private String getHeader() {
        return type.getTYPE().getABSType() + " " + id + "(" + (arguments? arg.getHeaderArguments(): "")+ ")";
    }

    public void translateHeader() {
        Application.newInst(getHeader() + ";");
        Application.endInst();
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        Application.newInst(getHeader() + " {");
        Application.endInst();

        block.translate(auxiliarVariableCounter);

        if (returnExp != null) returnExp.translate(auxiliarVariableCounter);

        Application.newInst("}");
        Application.endInst();
    }

    @Override
    public String toString() {
        return "Func: " + type + " " + id + " " +  (arguments? arg: "") + " { " + block + " } " + "Return: " + returnExp;
    }

    @Override
    public String getID() {
        return id;
    }
}
