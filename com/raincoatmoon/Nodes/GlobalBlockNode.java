package com.raincoatmoon.Nodes;


import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Functions.FuncDeclarationNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Nodes.Variables.DeclarationNode;
import com.raincoatmoon.Utils.Type;

import java.util.*;

public class GlobalBlockNode extends ParserNode {
    private static HashMap<String, FuncDeclarationNode> functions;
    private static HashMap<String, DeclarationNode> variables;
    private static HashMap<String, Declaration> global_references;
    private static HashSet<String> ids;

    private List<Declaration> declarations;

    public GlobalBlockNode() {
        declarations = new ArrayList<>();
    }
    public GlobalBlockNode(Declaration inst, GlobalBlockNode node) {
        declarations = node.getDeclarations();
        declarations.add(inst);
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public void initGlobalReferences() {
        for (Declaration inst : declarations) {
            global_references.put(inst.getID(), inst);
            if (!ids.contains(inst.getID())) ids.add(inst.getID());
            else Application.notifyError(Application.DUPLICATED_MSG
                    + " " + inst.getID()
                    + " (" + inst.getLine() + ", " + inst.getColumn() + ")");

            if (inst.getClassType() == Declaration.FUNC) {
                functions.put(inst.getID(), (FuncDeclarationNode) inst);
            } else {
                variables.put(inst.getID(), (DeclarationNode) inst);
                ((DeclarationNode) inst).setGlobal();
            }
        }
    }

    public static void reset() {
        functions = new HashMap<>();
        variables = new HashMap<>();
        global_references = new HashMap<>();
        ids = new HashSet<>();
    }

    public static void initReferences() {
        for (Map.Entry<String, FuncDeclarationNode> entry: functions.entrySet()) {
            entry.getValue().solveReferences(global_references);
        }
    }

    public static boolean typeChecker() {
        boolean ret = true;
        for (Map.Entry<String, FuncDeclarationNode> entry: functions.entrySet()) {
            ret = entry.getValue().getTYPE() == Type.OK && ret;
        }
        return ret;
    }

    public static boolean hasMain() {
        boolean ok = functions.containsKey("main") && !functions.get("main").hasArguments();
        if (!ok) Application.notifyError(Application.MAIN_MSG);
        return ok;
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        Application.newInst("module Cabs;");
        Application.endInst();
        Application.newInst("import * from ABS.StdLib;");
        Application.endInst();

        for (Type type: Type.values()) {
            if (type.validType())
                Application.newInst(String.format("interface Array%1$s {\n" +
                        "%1$s getV(Int indx);\n" +
                        "Unit setV(Int indx, %1$s value);\n" +
                        "}\n" +
                        "class Array%1$sC(Int size) implements Array%1$s{\n" +
                        "List<%1$s> list = copy(%2$s, size);\n" +
                        "%1$s getV(Int indx) {\n" +
                        "return nth(this.list, indx);\n" +
                        "}\n" +
                        "Unit setV(Int indx, %1$s value) {\n" +
                        "Int i = 0;\n" +
                        "List<%1$s> prev = Nil;\n" +
                        "List<%1$s> post = list;\n" +
                        "while (i < indx) {\n" +
                        "%1$s elem = head(post);\n" +
                        "prev = appendright(prev, elem);\n" +
                        "post = tail(post);\n" +
                        "i = i + 1;\n" +
                        "}\n" +
                        "prev = appendright(prev, value);\n" +
                        "post = tail(post);\n" +
                        "this.list = concatenate(prev, post);\n" +
                        "}\n" +
                        "}\n", type.getABSType(), type.getDefaultValue()));
        }

        // Global variables' headers
        Application.newInst("interface GLOBAL {");
        Application.endInst();
        for (Map.Entry<String, DeclarationNode> entry: variables.entrySet()) {
            if (entry.getValue().isArray()) {
                Application.newInst("Array"+ entry.getValue().getTypeNode().getTYPE().getABSType() + " retrieve" + entry.getKey() + "();");
                Application.endInst();
                Application.newInst(entry.getValue().getTypeNode().getTYPE().getABSType() + " get" + entry.getKey() + "(Int indx);");
                Application.endInst();
                Application.newInst("Unit set" + entry.getKey() + "(Int indx, " + entry.getValue().getTypeNode().getTYPE().getABSType() + " val);");
                Application.endInst();
            } else {
                Application.newInst(entry.getValue().getTypeNode().getTYPE().getABSType() + " get" + entry.getKey() + "();");
                Application.endInst();
                Application.newInst("Unit set" + entry.getKey() + "(" + entry.getValue().getTypeNode().getTYPE().getABSType() + " val);");
                Application.endInst();
            }
        }
        Application.newInst("Unit initialize();");
        Application.endInst();
        Application.newInst("}");
        Application.endInst();

        Application.newInst("class GlobalVariables() implements GLOBAL {");
        Application.endInst();
        for (Map.Entry<String, DeclarationNode> entry: variables.entrySet()) {
            entry.getValue().translate(auxiliarVariableCounter);
        }

        // Global array initialization
        Application.newInst("Unit initialize() {");
        Application.endInst();
        for (Map.Entry<String, DeclarationNode> entry: variables.entrySet()) {
            if (entry.getValue().isArray()) {
                entry.getValue().globalInitTranslate();
            }
        }
        Application.newInst("}");
        Application.endInst();

        for (Map.Entry<String, DeclarationNode> entry: variables.entrySet()) {
            if (entry.getValue().isArray()) {
                Application.newInst("Array" + entry.getValue().getTypeNode().getTYPE().getABSType() + " retrieve" + entry.getKey());
                Application.newInst("() {");
                Application.endInst();
                Application.newInst("return " + entry.getKey() + ";");
                Application.endInst();
                Application.newInst("}");
                Application.endInst();
            }

            Application.newInst(entry.getValue().getTypeNode().getTYPE().getABSType() + " get" + entry.getKey());
            if (entry.getValue().isArray()) {
                Application.newInst("(Int indx) {");
                Application.endInst();
                Application.newInst("return await " + entry.getKey() + "!getV(indx);");
            } else {
                Application.newInst("() {");
                Application.endInst();
                Application.newInst("return " + entry.getKey() + ";");
            }
            Application.endInst();
            Application.newInst("}");
            Application.endInst();
            Application.newInst("Unit set" + entry.getKey());
            if (entry.getValue().isArray()) {
                Application.newInst("(Int indx, " + entry.getValue().getTypeNode().getTYPE().getABSType() + " val) {");
                Application.endInst();
                Application.newInst("await " + entry.getKey() + "!setV(indx, val);");
            } else {
                Application.newInst("(" + entry.getValue().getTypeNode().getTYPE().getABSType() + " val) {");
                Application.endInst();
                Application.newInst(entry.getKey() + " = val;");
            }
            Application.endInst();
            Application.newInst("}");
            Application.endInst();
        }
        Application.newInst("}");
        Application.endInst();

        // Functions' headers
        for (Map.Entry<String, FuncDeclarationNode> entry: functions.entrySet()) {
            Application.newInst("interface Int" + entry.getKey() + " {");
            Application.endInst();
            entry.getValue().translateHeader();
            Application.newInst("}");
            Application.endInst();
        }

        for (Map.Entry<String, FuncDeclarationNode> entry: functions.entrySet()) {
            Application.newInst("class Imp" + entry.getKey() + "(GLOBAL globalval) implements Int" + entry.getKey() + " {");
            Application.endInst();
            entry.getValue().translate(auxiliarVariableCounter);
            Application.newInst("}");
            Application.endInst();
        }

        Application.newInst("{");
        Application.endInst();
        Application.newInst("GLOBAL globalval = new GlobalVariables();");
        Application.endInst();
        Application.newInst("await globalval!initialize();");
        Application.endInst();
        Application.newInst("Intmain prog = new Impmain(globalval);");
        Application.endInst();
        Application.newInst("await prog!main();");
        Application.endInst();
        Application.newInst("}");
        Application.endInst();
    }

    public static Declaration getGlobalVarReference(String id) {
        return global_references.get(id);
    }

    public static Declaration getFunctionReference(String id) {
        return functions.get(id);
    }

    @Override
    public String toString() {
        String s = "";
        boolean first = false;
        for (Declaration inst : declarations) {
            if (first) s += "\n";
            else first = true;
            s += inst.toString() + ";";
        }
        return s;
    }

    public static String getRefs() {
        String ret = "Func:\n";
        for (Map.Entry<String, FuncDeclarationNode> entry: functions.entrySet()) {
            ret += entry.getKey() + "\n";
        }
        ret += "Var:\n";
        for (Map.Entry<String, DeclarationNode> entry: variables.entrySet()) {
            ret += entry.getKey() + "\n";
        }
        return ret;
    }
}
