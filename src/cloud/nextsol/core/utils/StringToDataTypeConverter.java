package cloud.nextsol.core.utils;

import cloud.nextsol.core.enums.DataType;

public class StringToDataTypeConverter {
    public static DataType convert(String s) {
        if (isMap(s)) return DataType.META;
        if (isFieldMask(s)) return DataType.META;

        switch (s) {
            case "string":
                return DataType.STRING;
            case "bool":
                return DataType.BOOLEAN;
            case "double":
                return DataType.DOUBLE;
            case "float":
                return DataType.FLOAT;
            case "int32":
            case "uint32":
            case "sint32":
                return DataType.INT;
            case "int64":
            case "uint64":
            case "sint64":
                return DataType.LONG;
            case "repeated":
                return DataType.ARRAY;
            case "map<string, string>":
                return DataType.META;
        }
        return DataType.OBJECT;
    }

    private static boolean isMap(String s) {
        return s.length() >= 4 && s.startsWith("map<");
    }

    private static boolean isFieldMask(String s) {
        return s.equals("google.protobuf.FieldMask");
    }
}


