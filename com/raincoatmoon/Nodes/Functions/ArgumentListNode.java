package com.raincoatmoon.Nodes.Functions;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.ParserNode;

import java.util.*;

public class ArgumentListNode extends ParserNode {
    private HashMap<String, Declaration> variables;
    private HashSet<String> ids;

    private List<ArgumentNode> arguments;

    public ArgumentListNode(ArgumentNode arg) {
        arguments = new ArrayList<>();
        arguments.add(arg);
    }
    public ArgumentListNode(ArgumentNode arg, ArgumentListNode node) {
        arguments = node.getArguments();
        arguments.add(arg);
    }

    public List<ArgumentNode> getArguments() {
        return arguments;
    }

    public void solveReferences(HashMap<String, Declaration> previous) {
        variables = new HashMap<>();
        ids = new HashSet<>();
        for (Map.Entry<String, Declaration> entry: previous.entrySet()) {
            variables.put(entry.getKey(), entry.getValue());
        }

        for (ArgumentNode arg : arguments) {
            variables.put(arg.getID(), arg);
            if (!ids.contains(arg.getID()))
                ids.add(arg.getID());
            else Application.notifyError(Application.DUPLICATED_MSG
                    + " " + arg.getID()
                    + " (" + arg.getLine() + ", " + arg.getColumn() + ")");
        }
    }

    public HashMap<String, Declaration> getVariables() {
        return variables;
    }

    public String getHeaderArguments() {
        String s = "";
        boolean first = false;
        for (ArgumentNode arg : arguments) {
            if (first) s += ", ";
            else first = true;
            s += arg.getDeclaration();
        }
        return s;
    }

    @Override
    public String toString() {
        String s = "";
        boolean first = false;
        for (ArgumentNode arg : arguments) {
            if (first) s += ", ";
            else first = true;
            s += arg.toString();
        }
        return s;
    }
}
