package com.raincoatmoon.Nodes.Variables;

public class AuxiliarVariableCounter {
    private int preamble;

    public AuxiliarVariableCounter() {
        preamble = 0;
    }

    public String nextPreambleIndx() {
        preamble++;
        return "aux_var_" + (preamble - 1);
    }
}
