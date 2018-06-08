package com.raincoatmoon.Utils;

public enum Type {
    INT,
    BOOL,
    VOID,
    OK,
    FAIL,
    UNKNOWN;

    public String getABSType() {
        switch (this) {
            case INT: return "Int";
            case BOOL: return "Bool";
            case VOID: return "Unit";
            default: return "";
        }
    }

    public String getDefaultValue() {
        switch (this) {
            case INT: return "0";
            case BOOL: return "False";
            default: return "";
        }
    }

    public boolean validType() {
        switch (this) {
            case INT:
            case BOOL: return true;
            default: return false;
        }
    }
}
