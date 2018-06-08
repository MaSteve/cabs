package com.raincoatmoon.Nodes.Functions;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Nodes.Variables.DeclarationNode;
import com.raincoatmoon.Nodes.Variables.StaticArrayNode;
import com.raincoatmoon.Nodes.Variables.VarReferenceNode;
import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

import java.util.HashMap;
import java.util.List;

public class FuncCallNode extends ParserNode {
    private String id;
    private VarListNode list;
    private boolean arg;
    private boolean async;

    private Declaration def;

    public FuncCallNode(Yytoken token, VarListNode list) {
        super(token);
        init(token, list, false);
    }

    public FuncCallNode(Yytoken token) {
        super(token);
        init(token, null, false);
    }

    public FuncCallNode(Yytoken token, VarListNode list, boolean async) {
        super(token);
        init(token, list, true);
    }

    public FuncCallNode(Yytoken token, boolean async) {
        super(token);
        init(token, null, async);
    }

    private void init(Yytoken token, VarListNode list, boolean async) {
        this.id = token.m_text;
        this.list = list;
        this.arg = list != null;
        this.async = async;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        if (!previous.containsKey(id)) {
            Application.notifyError(Application.UNKNOWN_MSG
                    + " " + id
                    + " (" + getLine() + ", " + getColumn() + ")");
        } else {
            def = previous.get(id);
        }
        if (arg) list.solveReferences(previous);
    }

    private boolean argChecker(VarListNode list, ArgumentListNode arg) {
        List<ParserNode> variables = list.getVariables();
        List<ArgumentNode> arguments = arg.getArguments();
        if (arguments.size() == variables.size()) {
            boolean ok = true;
            for (int i = 0; i < arguments.size(); i++) {
                if (variables.get(i).isReferenceNode() && ((VarReferenceNode) variables.get(i)).isArray() && arguments.get(i).isArray()) {
                    ok &= StaticArrayNode.check(((DeclarationNode) ((VarReferenceNode) variables.get(i)).getDef()).getArrayNode(),
                            arguments.get(i).getArrayNode());
                } else if (!arguments.get(i).isArray()) {
                    if (variables.get(i).isReferenceNode() && ((VarReferenceNode) variables.get(i)).isArray()) ok = false;
                    Type varType = variables.get(i).getTYPE();
                    Type argType = arguments.get(i).getDeclTYPE();
                    ok &= varType == argType && argType != Type.FAIL;
                } else ok = false;
            }

            return ok;
        }
        return false;
    }

    public Type getTYPE() {
        if (def == null || def.getClassType() != Declaration.FUNC) {
            TYPE = Type.FAIL;
        } else {
            if (arg && ((FuncDeclarationNode) def).hasArguments()) {
                if (argChecker(list, ((FuncDeclarationNode) def).getArguments())) {
                    TYPE = async? Type.OK: def.getDeclTYPE();
                } else TYPE = Type.FAIL;
            } else if (!arg && !((FuncDeclarationNode) def).hasArguments()) {
                TYPE = async? Type.OK: def.getDeclTYPE();
            } else {
                TYPE = Type.FAIL;
            }

            if (TYPE == Type.FAIL) {
                Application.notifyError(Application.TYPE_MSG + ": "
                        + Application.ARG_MSG + id
                        + " (" + getLine() + ", " + getColumn() + ")");
            }
        }
        return TYPE;
    }

    private String preamble(AuxiliarVariableCounter counter) {
        String auxiliarVar = counter.nextPreambleIndx();
        Application.newInst("Int"+ id + " " + auxiliarVar + " = new Imp" + id + "(globalval);");
        Application.endInst();
        return auxiliarVar;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        String call = preamble(auxiliarVariableCounter);
        if (arg) list.translate(auxiliarVariableCounter);
        if (async) Application.newInst(call + "!");
        else {
            auxiliarVar = auxiliarVariableCounter.nextPreambleIndx();
            Application.newInst(def.getDeclTYPE().getABSType() + " " + auxiliarVar + " = await " + call + "!");
        }
        Application.newInst(id);
        Application.newInst("(");
        if (arg) Application.newInst(list.auxiliarVar());
        Application.newInst(");");
        Application.endInst();
    }

    @Override
    public String toString() {
        return "call: " + id + " ( " + (arg? list: "") + " ) ";
    }
}
