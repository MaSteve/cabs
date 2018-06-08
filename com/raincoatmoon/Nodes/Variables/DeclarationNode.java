package com.raincoatmoon.Nodes.Variables;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.TypeNode;
import com.raincoatmoon.Utils.Type;

public class DeclarationNode extends Declaration {
    private boolean array;
    private VarIDNode id;
    private StaticArrayNode arrayNode;
    private boolean global;

    public DeclarationNode(TypeNode type, VarIDNode id) {
        super(Declaration.VAR, id.token);
        array = false;
        this.type = type;
        this.id = id;
    }

    public DeclarationNode(TypeNode type, VarIDNode id, StaticArrayNode arrayNode) {
        super(Declaration.VAR, id.token);
        array = true;
        this.type = type;
        this.id = id;
        this.arrayNode = arrayNode;
    }

    public void setGlobal() {
        global = true;
    }

    public boolean isGlobal() {
        return global;
    }

    public boolean isArray() {
        return array;
    }

    public StaticArrayNode getArrayNode() {
        return arrayNode;
    }

    public Type getTYPE() {
        if (array) {
            return arrayNode.getTYPE();
        }
        return TYPE;
    }

    public TypeNode getTypeNode() {
        return type;
    }

    public String getDeclaration() {
        return (array? "Array": "") + type.getTYPE().getABSType() + " " + id;
    }

    private String getDefaultValueDeclaration() {
        if (array) {
            return "new Array" + type.getTYPE().getABSType() + "C(" + arrayNode.size() + ")";
        } else return type.getTYPE().getDefaultValue();
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        Application.newInst(getDeclaration());
        if (!(array && global)) {
            Application.newInst(" = ");
            Application.newInst(getDefaultValueDeclaration());
        }
        Application.newInst(";");
        Application.endInst();
    }

    public void globalInitTranslate() {
        Application.newInst(id.toString());
        Application.newInst(" = ");
        Application.newInst(getDefaultValueDeclaration());
        Application.newInst(";");
        Application.endInst();
    }

    @Override
    public String toString() {
        return "Var: " + type + (array? arrayNode: "") + " ID: " + id;
    }

    @Override
    public String getID() {
        return id.toString();
    }
}
