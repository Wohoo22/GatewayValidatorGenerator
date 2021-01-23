package cloud.nextsol.core.model;

import cloud.nextsol.core.enums.DataType;

import java.util.List;

public class DataNode {
    private DataType dataType;
    private String name;

    // to map object with a grpc message (use when dataType == OBJECT)
    private String objectMessageName;

    // only use these when dataType == ARRAY
    private DataType arrayItemDataType;

    // only use enumValues when dataType == DataType.ENUM
    private List<String> enumValues;

    // only use when dataType == OBJECT
    private List<DataNode> childDataNodes;


    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getArrayItemDataType() {
        return arrayItemDataType;
    }

    public void setArrayItemDataType(DataType arrayItemDataType) {
        this.arrayItemDataType = arrayItemDataType;
    }

    public String getObjectMessageName() {
        return objectMessageName;
    }

    public void setObjectMessageName(String objectMessageName) {
        this.objectMessageName = objectMessageName;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<String> enumValues) {
        this.enumValues = enumValues;
    }

    public List<DataNode> getChildDataNodes() {
        return childDataNodes;
    }

    public void setChildDataNodes(List<DataNode> childDataNodes) {
        this.childDataNodes = childDataNodes;
    }
}
