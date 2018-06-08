package com.raincoatmoon.Nodes.Variables;

import com.raincoatmoon.Application;
import com.raincoatmoon.Nodes.Expressions.NumNode;
import com.raincoatmoon.Nodes.ParserNode;
import com.raincoatmoon.Utils.Type;

import java.util.ArrayList;
import java.util.List;

public class StaticArrayNode extends ParserNode {
    private List<NumNode> expressions;

    public StaticArrayNode(NumNode exp) {
        expressions = new ArrayList<>();
        expressions.add(exp);
    }
    public StaticArrayNode(NumNode exp, StaticArrayNode node) {
        expressions = node.getExpressions();
        expressions.add(exp);
    }

    public List<NumNode> getExpressions() {
        return expressions;
    }

    public int getDim() {
        return expressions.size();
    }

    public static boolean check(StaticArrayNode node1, StaticArrayNode node2) {
        List<NumNode> exps1 = node1.getExpressions();
        List<NumNode> exps2 = node2.getExpressions();
        boolean ok = exps1.size() == exps2.size();
        for (int i = 0; i < exps1.size() && ok; i++) {
            ok = exps1.get(i).getValue() == exps2.get(i).getValue();
        }
        return ok;
    }

    public Type getTYPE() {
        TYPE = Type.OK;
        for (NumNode exp : expressions) {
            if (exp.getValue() <= 0) {
                Application.notifyError(Application.ZERO_MSG
                        + " (" + exp.getLine() + ", " + exp.getColumn() + ")");
                TYPE = Type.FAIL;
            }
        }
        return TYPE;
    }

    public String size() {
        int res = 1;
        for (NumNode exp : expressions) {
            res *= exp.getValue();
        }
        return "" + res;
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
