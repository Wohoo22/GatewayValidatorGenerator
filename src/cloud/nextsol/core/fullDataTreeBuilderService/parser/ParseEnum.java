package cloud.nextsol.core.fullDataTreeBuilderService.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseEnum {
    /*
    start with the index right after 'enum'
    */
    public static int parseEnum(int fileDataIndex, HashMap<String, List<String>> initEnumList, String fileData, String parentMessage) {

        // to ensure the index is right after increase by enumName.length()
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;
        String enumName = parseEnumName(fileDataIndex, fileData);

        fileDataIndex += enumName.length();

        //append parent message if not empty
        if (!parentMessage.isEmpty()) enumName = parentMessage + "." + enumName;

        // make sure fileDataIndex had passed the open bracket
        while (fileData.charAt(fileDataIndex) != '{') fileDataIndex++;
        // pass the open bracket and start parsing data
        fileDataIndex += 1;

        List<String> enumValues = new ArrayList<>();
        int finishedIndex = parseEnumValuesRecursively(fileDataIndex, enumValues, fileData);

        initEnumList.put(enumName, enumValues);

        return finishedIndex;
    }

    // use just for once
    private static String parseEnumName(int fileDataIndex, String fileData) {
        // pass all white space
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

        String name = "";
        while (fileData.charAt(fileDataIndex) != ' ' && fileData.charAt(fileDataIndex) != '{') {
            name = name.concat(String.valueOf(fileData.charAt(fileDataIndex)));
            fileDataIndex++;
        }
        return name;
    }

    // recursively parsing enum value until meet a '}'
    private static int parseEnumValuesRecursively(int fileDataIndex, List<String> enumValues, String fileData) {

        if (isEndOfEnum(fileDataIndex, fileData)) {
            while (fileData.charAt(fileDataIndex) != '}') fileDataIndex++;
            // return index right after '}'
            return fileDataIndex + 1;
        }

        // to ensure the index is right after increase by enumValue.length()
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

        String enumValue = parseEnumValue(fileDataIndex, fileData);
        fileData += enumValue.length();

        // find the ';' to continue parsing enum
        while (fileData.charAt(fileDataIndex) != ';') fileDataIndex++;
        fileDataIndex += 1;

        enumValues.add(enumValue);

        return parseEnumValuesRecursively(fileDataIndex, enumValues, fileData);
    }

    // use just for once
    private static String parseEnumValue(int fileDataIndex, String fileData) {
        // pass all white space
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

        String enumValue = "";

        // keep parsing until meet a white space or '='
        while (fileData.charAt(fileDataIndex) != ' ' && fileData.charAt(fileDataIndex) != '=') {
            enumValue = enumValue.concat(String.valueOf(fileData.charAt(fileDataIndex)));
            fileDataIndex++;
        }
        return enumValue;
    }

    private static boolean isEndOfEnum(int fileDataIndex, String fileData) {
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;
        return fileData.charAt(fileDataIndex) == '}';
    }
}
