package com.raincoatmoon;

import com.raincoatmoon.Nodes.GlobalBlockNode;
import com.raincoatmoon.Nodes.Variables.AuxiliarVariableCounter;
import com.raincoatmoon.Parser.parser;

import java.io.IOException;

public class Manager {
    private static GlobalBlockNode block;

    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.out.println(Application.ARG_LEN_MSG);
            return;
        }
        parser.main(args);
    }

    public static void init(GlobalBlockNode block1) throws IOException {
        if (Application.isOK()) {
            System.out.println("OK");
            block = block1;
            GlobalBlockNode.reset();
            block.initGlobalReferences();
            GlobalBlockNode.initReferences();
            boolean check = GlobalBlockNode.typeChecker();
            System.out.println("CHECK: " + check);
            if (check && Application.isOK() && GlobalBlockNode.hasMain()) {
                Application.prepare();
                AuxiliarVariableCounter auxiliarVariableCounter = new AuxiliarVariableCounter();
                block.translate(auxiliarVariableCounter);
                Application.close();
            } else {
                System.out.println("Compilation error");
            }
        } else {
            System.out.println("Compilation error");
        }
        System.out.println("Bye");
    }
}
