package com.raincoatmoon.Nodes.Functions;

import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VarListNode extends ParserNode {

    private List<ParserNode> variables;

    public VarListNode(ParserNode var) {
        variables = new ArrayList<>();
        variables.add(var);
    }
    public VarListNode(ParserNode var, VarListNode node) {
        variables = node.getVariables();
        variables.add(var);
    }

    public List<ParserNode> getVariables() {
        return variables;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        for (ParserNode var: variables) {
            var.solveReferences(previous);
        }
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        for (ParserNode var : variables) {
            var.translate(auxiliarVariableCounter);
        }
    }

    @Override
    public String auxiliarVar() {
        String res = "";
        boolean first = false;
        for (ParserNode var : variables) {
            if (first) res += ", ";
            else first = true;
            res += var.auxiliarVar();
        }
        return res;
    }

    @Override
    public String toString() {
        String s = "";
        boolean first = false;
        for (ParserNode var : variables) {
            if (first) s += ", ";
            else first = true;
            s += var.toString();
        }
        return s;
    }
}
