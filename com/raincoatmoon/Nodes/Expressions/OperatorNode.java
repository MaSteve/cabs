package com.raincoatmoon.Nodes.Expressions;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Utils.Type;
import com.raincoatmoon.Parser.Yytoken;

public class OperatorNode extends ParserNode {

    private int op;

    public OperatorNode(Yytoken token) {
        super(token);
        op = token.m_index;
        switch (op) {
            case NEG: TYPE = Type.INT; break;
            case ASS: TYPE = Type.OK; break;
            case LT: TYPE = Type.INT; break;
            case GT: TYPE = Type.INT; break;
            case LE: TYPE = Type.INT; break;
            case GE: TYPE = Type.INT; break;
            case EQ: TYPE = Type.INT; break;
            case NEQ: TYPE = Type.INT; break;
            case PLUS: TYPE = Type.INT; break;
            case MINUS: TYPE = Type.INT; break;
            case TIMES: TYPE = Type.INT; break;
            case DIV: TYPE = Type.INT; break;
            case MOD: TYPE = Type.INT; break;
            case AND: TYPE = Type.BOOL; break;
            case OR: TYPE = Type.BOOL; break;
            case NOT: TYPE = Type.BOOL; break;
            default: TYPE = Type.FAIL; break;
        }
    }

    public void alt() {
        if (op == MINUS) op = NEG;
    }

    @Override
    public String toString() {
        switch (op) {
            case NEG: return " - ";
            case ASS: return " := ";
            case LT: return " < ";
            case GT: return " > ";
            case LE: return " <= ";
            case GE: return " >= ";
            case EQ: return " == ";
            case NEQ: return " != ";
            case PLUS: return " + ";
            case MINUS: return " - ";
            case TIMES: return " * ";
            case DIV: return " / ";
            case MOD: return " % ";
            case AND: return " and ";
            case OR: return " or ";
            case NOT: return " not ";
            default: return " ERROR ";
        }
    }

    public int getOp() {
        return op;
    }

    public int getInstSize() {
        return 1;
    }

    public Type retType() {
        switch (op) {
            case NEG: return TYPE.INT;
            case ASS: return TYPE.FAIL;
            case LT: return TYPE.BOOL;
            case GT: return TYPE.BOOL;
            case LE: return TYPE.BOOL;
            case GE: return TYPE.BOOL;
            case EQ: return TYPE.BOOL;
            case NEQ: return TYPE.BOOL;
            case PLUS: return TYPE.INT;
            case MINUS: return TYPE.INT;
            case TIMES: return TYPE.INT;
            case DIV: return TYPE.INT;
            case MOD: return TYPE.INT;
            case AND: return TYPE.BOOL;
            case OR: return TYPE.BOOL;
            case NOT: return TYPE.BOOL;
            default: return TYPE.FAIL;
        }
    }

    @Override
    public void translate(AuxiliarVariableCounter auxiliarVariableCounter) {
        switch (op) {
            case NEG: Application.newInst("-"); break;
            case LT: Application.newInst(" < "); break;
            case GT: Application.newInst(" > "); break;
            case LE: Application.newInst(" <= "); break;
            case GE: Application.newInst(" >= "); break;
            case EQ: Application.newInst(" == "); break;
            case NEQ: Application.newInst(" != "); break;
            case PLUS: Application.newInst(" + "); break;
            case MINUS: Application.newInst(" - "); break;
            case TIMES: Application.newInst(" * "); break;
            case DIV: Application.newInst(" / "); break;
            case MOD: Application.newInst(" % "); break;
            case AND: Application.newInst(" && "); break;
            case OR: Application.newInst(" || "); break;
            case NOT: Application.newInst("!"); break;
        }
    }

    public static final int NEG = 0;
    public static final int ASS = 1;
    public static final int LT = 6;
    public static final int GT = 7;
    public static final int LE = 8;
    public static final int GE = 9;
    public static final int EQ = 10;
    public static final int NEQ = 11;
    public static final int PLUS = 12;
    public static final int MINUS = 13;
    public static final int TIMES = 14;
    public static final int DIV = 15;
    public static final int MOD = 16;
    public static final int AND = 17;
    public static final int OR = 18;
    public static final int NOT = 19;
}
