package cloud.nextsol.core.fullDataTreeBuilderService.dataTreeService;

import cloud.nextsol.core.enums.DataType;
import cloud.nextsol.core.model.DataNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstructMessageList {
    public static void construct(HashMap<String, List<DataNode>> initMessageList
            , HashMap<String, List<String>> initEnumList) {
        for (Map.Entry<String, List<DataNode>> messageData : initMessageList.entrySet()) {
            buildMessageRecursively(messageData.getValue(), initMessageList, initEnumList, messageData.getKey());
        }
    }

    private static void buildMessageRecursively(List<DataNode> dataNodes, HashMap<String, List<DataNode>> initMessageList,
                                                HashMap<String, List<String>> initEnumList, String parentMessageName) {
        for (DataNode dataNode : dataNodes) {

            if (dataNode.getDataType() == DataType.OBJECT ||
                    (dataNode.getDataType() == DataType.ARRAY && dataNode.getArrayItemDataType() == DataType.OBJECT)) {

                String objectMessageName = dataNode.getObjectMessageName();
                String nestedObjectMessageName = parentMessageName + "." + objectMessageName;

                if (initMessageList.containsKey(objectMessageName)) {

                    // dynamic programming (avoid rebuilding childDataNodes if it had been built before)
                    if (dataNode.getChildDataNodes() == null || dataNode.getChildDataNodes().size() == 0)
                        buildMessageRecursively(initMessageList.get(objectMessageName), initMessageList, initEnumList, objectMessageName);

                    dataNode.setChildDataNodes(initMessageList.get(objectMessageName));

                } else if (initMessageList.containsKey(nestedObjectMessageName)) {

                    // dynamic programming (avoid rebuilding childDataNodes if it had been built before)
                    if (dataNode.getChildDataNodes() == null || dataNode.getChildDataNodes().size() == 0)
                        buildMessageRecursively(initMessageList.get(nestedObjectMessageName), initMessageList, initEnumList, nestedObjectMessageName);

                    dataNode.setChildDataNodes(initMessageList.get(nestedObjectMessageName));

                } else if (initEnumList.containsKey(objectMessageName)) {

                    if (dataNode.getDataType() == DataType.OBJECT)
                        dataNode.setDataType(DataType.ENUM);
                    else
                        dataNode.setArrayItemDataType(DataType.ENUM);

                    dataNode.setEnumValues(initEnumList.get(objectMessageName));

                } else if (initEnumList.containsKey(nestedObjectMessageName)) {

                    if (dataNode.getDataType() == DataType.OBJECT)
                        dataNode.setDataType(DataType.ENUM);
                    else
                        dataNode.setArrayItemDataType(DataType.ENUM);

                    dataNode.setEnumValues(initEnumList.get(nestedObjectMessageName));

                } else {

                    System.err.println("Can't find message or enum with name: " + dataNode.getObjectMessageName());

                }

            }
        }
    }
}
