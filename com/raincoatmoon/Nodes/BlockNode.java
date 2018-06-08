package com.raincoatmoon.Nodes;


import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Nodes.Variables.DeclarationNode;
import com.raincoatmoon.Utils.Type;

import java.util.*;

public class BlockNode extends ParserNode {
    private HashMap<String, Declaration> variables;
    private HashSet<String> ids;

    private List<ParserNode> instructions;

    public BlockNode() {
        instructions = new ArrayList<>();
    }
    public BlockNode(ParserNode inst, BlockNode node) {
        instructions = node.getInstructions();
        instructions.add(inst);
    }

    public List<ParserNode> getInstructions() {
        return instructions;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        variables = new HashMap<>();
        ids = new HashSet<>();
        for (Map.Entry<String, Declaration> entry: previous.entrySet()) {
            variables.put(entry.getKey(), entry.getValue());
            ids.add(entry.getKey());
        }

        for (ParserNode inst: instructions) {
            if (inst.isDecl()) {
                variables.put(((DeclarationNode) inst).getID(), ((DeclarationNode) inst));
                if (!ids.contains(((DeclarationNode) inst).getID()))
                    ids.add(((DeclarationNode) inst).getID());
                else Application.notifyError(Application.DUPLICATED_MSG
                        + " " + ((DeclarationNode) inst).getID()
                        + " (" + inst.getLine() + ", " + inst.getColumn() + ")");
            }
            inst.solveReferences(variables);
        }

        if (Application.debug()) System.out.println("BlockRef");
    }

    public Type getTYPE() {
        TYPE = Type.OK;
        for (ParserNode inst : instructions) {
            if (inst.getTYPE() != Type.OK) {
                TYPE = Type.FAIL;
                //break; Multiple errors.
            }
        }
        return TYPE;
    }

    public HashMap<String, Declaration> getVariables() {
        return variables;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        for (ParserNode inst : instructions) {
            inst.translate(auxiliarVariableCounter);
        }
    }

    @Override
    public String toString() {
        String s = "";
        boolean first = false;
        for (ParserNode inst : instructions) {
            if (first) s += "\n";
            else first = true;
            s += inst.toString() + ";";
        }
        return s;
    }
}
