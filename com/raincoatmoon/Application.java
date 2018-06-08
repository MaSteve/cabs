package com.raincoatmoon;

import java.io.FileOutputStream;
import java.io.IOException;

public class Application {
    private static boolean debug = false;
    private static boolean error = false;
    private static boolean verbose = false;
    private static int instID = 0;
    private static String output = "a.asm";
    private static FileOutputStream fileOutputStream;


    public static boolean debug() {
        return debug;
    }

    public static boolean isOK() {
        return !error;
    }

    public static void notifyError(String msg) {
        error = true;
        System.err.println("ERROR: " + msg);
    }

    public static void setFile(String output1) {
        output = output1;
    }

    public static void prepare() throws IOException {
        instID = 0;
        fileOutputStream = new FileOutputStream(output);
    }

    public static void reset() {
        error = false;
    }

    public static void close() throws IOException {
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static void newComment(String comment) {
        if (verbose) {
            String output = "{" + comment + "}";
            save(output);
        }
    }

    public static void newInst(String inst) {
        String output = inst;
        save(output);
        instID++;
    }

    public static void endInst() {
        save("\n");
    }

    public static int jump(int len) {
        return instID + len + 1;
    }

    public static int getInstID() {
        return instID;
    }

    private static void save(String s) {
        if (debug) System.out.println(s);
        try {
            fileOutputStream.write((s).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static final String LEX_MSG = "Error léxico: ";
    public static final String SYNTAX_MSG = "Error sintáctico: ";
    public static final String DUPLICATED_MSG = "Símbolo duplicado";
    public static final String UNKNOWN_MSG = "Símbolo desconocido";
    public static final String TYPE_MSG = "Lío de tipos";
    public static final String TYPE_EXP_MSG = "Tipo esperado: ";
    public static final String ARRAY_MSG = "No es un array ";
    public static final String ARRAY2_MSG = "Indice indeterminado de ";
    public static final String ARRAY3_MSG = " es un array";
    public static final String ARG_MSG = "No existe esa signatura para ";
    public static final String ZERO_MSG = "Tamaño 0";
    public static final String MAIN_MSG = "Función main no encontrada";
    public static final String RETURN_TYPE_MSG = "Tipo del return erróneo.";
    public static final String UNKNOWN_BLOCK_MSG = "Bloque desconocido";
    public static final String ARG_LEN_MSG = "Son necesarios dos paths: <archivo fuente> <output>";
}
