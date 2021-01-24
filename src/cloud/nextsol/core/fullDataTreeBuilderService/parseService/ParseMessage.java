package cloud.nextsol.core.fullDataTreeBuilderService.parseService;

import cloud.nextsol.core.enums.DataType;
import cloud.nextsol.core.model.DataNode;
import cloud.nextsol.core.utils.StringToDataTypeConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseMessage {
    /*
    start with the index right after 'message'
     */
    public static int parseMessage(int fileDataIndex, HashMap<String, List<DataNode>> initMessageList,
                                   HashMap<String, List<String>> initEnumList, String fileData, String parentMessage) {

        // to ensure the index is right after increase by messageName.length()
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;
        String messageName = parseMessageName(fileDataIndex, fileData);

        fileDataIndex += messageName.length();

        //append parent message if not empty
        if (!parentMessage.isEmpty()) messageName = parentMessage + "." + messageName;

        // make sure fileDataIndex had passed the open bracket
        while (fileData.charAt(fileDataIndex) != '{') fileDataIndex++;
        // pass the open bracket and start parsing data
        fileDataIndex += 1;

        List<DataNode> dataNodes = new ArrayList<>();
        int finishedIndex = parseMessageDataRecursively(fileDataIndex, fileData, dataNodes, initMessageList, initEnumList, messageName);

        initMessageList.put(messageName, dataNodes);

        return finishedIndex;
    }

    // just use for once
    private static String parseMessageName(int fileDataIndex, String fileData) {
        // pass all white space
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

        String name = "";
        while (fileData.charAt(fileDataIndex) != ' ' && fileData.charAt(fileDataIndex) != '{') {
            name = name.concat(String.valueOf(fileData.charAt(fileDataIndex)));
            fileDataIndex++;
        }
        return name;
    }

    // recursively parsing message data until meet a '}'
    private static int parseMessageDataRecursively(int fileDataIndex, String fileData, List<DataNode> dataNodes,
                                                   HashMap<String, List<DataNode>> initMessageList, HashMap<String, List<String>> initEnumList, String parentMessage) {

        if (isEndOfMessage(fileDataIndex, fileData)) {
            while (fileData.charAt(fileDataIndex) != '}') fileDataIndex++;
            // return index right after '}'
            return fileDataIndex + 1;
        }

        // to ensure the index is right after increase by dataTypeString.length()
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

        String dataTypeString = parseDataTypeInString(fileDataIndex, fileData);
        fileDataIndex += dataTypeString.length();

        if (dataTypeString.equals("enum")) {
            // start with index right after 'enum'
            fileDataIndex = ParseEnum.parseEnum(fileDataIndex, initEnumList, fileData, parentMessage);
            return parseMessageDataRecursively(fileDataIndex, fileData, dataNodes, initMessageList, initEnumList, parentMessage);
        } else if (dataTypeString.equals("message")) {
            // start with index right after 'message'
            fileDataIndex = parseMessage(fileDataIndex, initMessageList, initEnumList, fileData, parentMessage);
            return parseMessageDataRecursively(fileDataIndex, fileData, dataNodes, initMessageList, initEnumList, parentMessage);
        }

        // else: primitive data types
        DataNode dataNode = new DataNode();

        DataType dataType = StringToDataTypeConverter.convert(dataTypeString);
        // save the message which object map to
        if (dataType == DataType.OBJECT) dataNode.setObjectMessageName(dataTypeString);
        if (dataType == DataType.ARRAY) {
            // to ensure the index is right after increase by arrayItemDataTypeString.length()
            while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

            String arrayItemDataTypeString = parseDataTypeInString(fileDataIndex, fileData);
            fileDataIndex += arrayItemDataTypeString.length();

            DataType arrayItemDataType = StringToDataTypeConverter.convert(arrayItemDataTypeString);

            dataNode.setArrayItemDataType(arrayItemDataType);

            if (arrayItemDataType == DataType.OBJECT) dataNode.setObjectMessageName(arrayItemDataTypeString);
        }

        String fieldName = parseFieldName(fileDataIndex, fileData);

        dataNode.setDataType(dataType);
        dataNode.setName(fieldName);

        dataNodes.add(dataNode);

        // ready for parsing next data line
        while (fileData.charAt(fileDataIndex) != ';') fileDataIndex++;
        fileDataIndex += 1;

        return parseMessageDataRecursively(fileDataIndex, fileData, dataNodes, initMessageList, initEnumList, parentMessage);
    }

    // just use for once
    private static String parseDataTypeInString(int fileDataIndex, String fileData) {
        // pass all white space
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

        String dataTypeString = "";

        // keep parsing until meet a white space
        while (fileData.charAt(fileDataIndex) != ' ') {
            dataTypeString = dataTypeString.concat(String.valueOf(fileData.charAt(fileDataIndex)));
            fileDataIndex++;
        }

        return dataTypeString;
    }

    // just use for once
    private static String parseFieldName(int fileDataIndex, String fileData) {
        // pass all white space
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

        String fieldName = "";
        while (fileData.charAt(fileDataIndex) != ' ' && fileData.charAt(fileDataIndex) != '=') {
            fieldName = fieldName.concat(String.valueOf(fileData.charAt(fileDataIndex)));
            fileDataIndex++;
        }
        return fieldName;
    }

    private static boolean isEndOfMessage(int fileDataIndex, String fileData) {
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;
        return fileData.charAt(fileDataIndex) == '}';
    }

}
