package com.raincoatmoon.Nodes.Variables;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

import java.util.HashMap;

public class VarReferenceNode extends ParserNode {
    private String id;
    private boolean array;
    private ArrayNode arrayNode;

    private Declaration def;

    public VarReferenceNode(Yytoken token) {
        super(token);
        id = token.m_text;
        array = false;
    }

    public VarReferenceNode (Yytoken token, ArrayNode arrayNode) {
        super(token);
        id = token.m_text;
        array = true;
        this.arrayNode = arrayNode;
    }

    public boolean isArray() {
        return array || ((DeclarationNode) def).isArray(); // This is not a bug. We have to take care of arrays as arguments
    }

    public Declaration getDef() {
        return def;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        if (!previous.containsKey(id)) {
            Application.notifyError(Application.UNKNOWN_MSG
                    + " " + id
                    + " (" + getLine() + ", " + getColumn() + ")");
        } else {
            def = previous.get(id);
        }
        if (array) arrayNode.solveReferences(previous);
    }

    public Type getTYPE() {
        if (def == null || def.getClassType() == Declaration.FUNC) {
            TYPE = Type.FAIL;
        } else {
            if (array) {
                if (((DeclarationNode)def).isArray()){
                    StaticArrayNode staticArrayNode = (((DeclarationNode)def).getArrayNode());
                    Type arrayType = arrayNode.getTYPE();
                    if (staticArrayNode.getDim() == arrayNode.getDim() && arrayType == Type.OK) {
                        TYPE = def.getDeclTYPE();
                    } else {
                        Application.notifyError(Application.TYPE_MSG + ": "
                                + Application.ARRAY2_MSG + id
                                + " (" + getLine() + ", " + getColumn() + ")");
                        TYPE = Type.FAIL;
                    }
                } else {
                    Application.notifyError(Application.TYPE_MSG + ": "
                            + Application.ARRAY_MSG + id
                            + " (" + getLine() + ", " + getColumn() + ")");
                    TYPE = Type.FAIL;
                }
            } else {
                if (((DeclarationNode)def).isArray()) {
                    Application.notifyError(Application.TYPE_MSG + ": "
                            + id + Application.ARRAY3_MSG
                            + " (" + getLine() + ", " + getColumn() + ")");
                    TYPE = Type.FAIL;
                }
                else TYPE = def.getDeclTYPE();
            }
        }
        return TYPE;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        codeDir(auxiliarVariableCounter);
    }

    public void codeL(AuxiliarVariableCounter auxiliarVariableCounter) {
        if (array) {
            arrayNode.setStaticArrayNode(((DeclarationNode) def).getArrayNode());
            arrayNode.translate(auxiliarVariableCounter);
        }
        if (((DeclarationNode)def).isGlobal()) {
            Application.newInst("await globalval!set" + id + "(");
            if (array) Application.newInst(arrayNode.auxiliarVar() + ", ");
        } else {
            if (array) Application.newInst("await " + id + "!setV(" + arrayNode.auxiliarVar() + ", ");
            else {
                Application.newInst(id);
                Application.newInst(" = ");
            }
        }
    }

    private void codeDir(AuxiliarVariableCounter auxiliarVariableCounter) {
        if (array) {
            arrayNode.setStaticArrayNode(((DeclarationNode) def).getArrayNode());
            arrayNode.translate(auxiliarVariableCounter);

            auxiliarVar = auxiliarVariableCounter.nextPreambleIndx();
            Application.newInst(((DeclarationNode) def).getTypeNode().getTYPE().getABSType() + " " + auxiliarVar + " = ");
            if (((DeclarationNode) def).isGlobal()) {
                Application.newInst("await globalval!get" + id + "(" + arrayNode.auxiliarVar() + ");");
            } else {
                Application.newInst("await " + id + "!getV(" + arrayNode.auxiliarVar() + ");");
            }
            Application.endInst();
        } else {
            if (((DeclarationNode) def).isGlobal()) {
                auxiliarVar = auxiliarVariableCounter.nextPreambleIndx();
                if (isArray()) Application.newInst("Array"+ ((DeclarationNode) def).getTypeNode().getTYPE().getABSType() + " " + auxiliarVar + " = await globalval!retrieve" + id + "();");
                else Application.newInst(((DeclarationNode) def).getTypeNode().getTYPE().getABSType() + " " + auxiliarVar + " = await globalval!get" + id + "();");
                Application.endInst();
            } else {
                auxiliarVar = id;
                //Application.newInst(id);
            }
        }
    }


    @Override
    public String toString() {
        return "Ref: " + id + (array? arrayNode: "");
    }

    public boolean isReferenceNode() {
        return true;
    }
}
