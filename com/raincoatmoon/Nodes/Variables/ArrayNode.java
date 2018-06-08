package com.raincoatmoon.Nodes.Variables;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.Expressions.NumNode;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Utils.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArrayNode extends ParserNode {
    private StaticArrayNode def;

    private List<ParserNode> expressions;

    public ArrayNode(ParserNode exp) {
        expressions = new ArrayList<>();
        expressions.add(exp);
    }
    public ArrayNode(ParserNode exp, ArrayNode node) {
        expressions = node.getExpressions();
        expressions.add(exp);
    }

    public List<ParserNode> getExpressions() {
        return expressions;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        for (ParserNode exp : expressions)
            exp.solveReferences(previous);
    }

    public int getDim() {
        return expressions.size();
    }

    public Type getTYPE() {
        TYPE = Type.OK;
        for (ParserNode exp : expressions) {
            if (exp.getTYPE() != Type.INT) TYPE = Type.FAIL;
        }
        return TYPE;
    }

    public void setStaticArrayNode(StaticArrayNode node) {
        def = node;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter){
        List<NumNode> dimensions = def.getExpressions();
        auxiliarVar = auxiliarVariableCounter.nextPreambleIndx();
        Application.newInst(Type.INT.getABSType() + " " + auxiliarVar + " = " + Type.INT.getDefaultValue() + ";");
        Application.endInst();
        int d = 1;
        for (int i = 0; i < expressions.size(); i++) {
            expressions.get(i).translate(auxiliarVariableCounter);
            Application.newInst(auxiliarVar + " = " + auxiliarVar + " + (" + d + " * " + expressions.get(i).auxiliarVar() + ");");
            Application.endInst();
            d *= dimensions.get(i).getValue();
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (ParserNode exp : expressions) {
            s += "[" + exp.toString() + "]";
        }
        return s;
    }
}
