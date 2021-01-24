package cloud.nextsol.core.utils;

import cloud.nextsol.core.enums.DataType;

public class DataTypeToValidatorType {
    public static String convert(DataType dataType) {
        switch (dataType) {
            case OBJECT:
                return "object";
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
                return "number";
            case STRING:
            case ENUM:
                return "string";
            case BOOLEAN:
                return "boolean";
            case ARRAY:
                return "array";
            case META:
                return "meta";
        }
        return "";
    }
}
