package test;

import cloud.nextsol.core.enums.DataType;
import cloud.nextsol.core.model.DataNode;
import cloud.nextsol.core.fullDataTreeBuilderService.ConstructFullDataTree;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

public class ConstructFullDataTreeTest {
    public static void main(String[] args) throws IOException {
        String grpcFilePath = "C:\\Users\\ADMIN\\Desktop\\sample_test_input.proto";
        String grpcFileData = Files.readString(Path.of(grpcFilePath), StandardCharsets.UTF_8);

        LinkedHashMap<String, List<DataNode>> dataTree = ConstructFullDataTree.buildFullDataTree(grpcFileData);

        // assert there are enough services
        assert dataTree.size() == 1;
        assert dataTree.containsKey("createBody");

        DataNode body = new DataNode();
        body.setChildDataNodes(dataTree.get("createBody"));
        body.setDataType(DataType.OBJECT);
        assertBody(body);

        System.out.println("TEST PASSED");
    }

    public static void assertBody(DataNode bodyDataNode) {
        assert bodyDataNode.getDataType() == DataType.OBJECT;
        assert bodyDataNode.getChildDataNodes().size() == 10;

        List<DataNode> childDataNodes = bodyDataNode.getChildDataNodes();
        assert childDataNodes.size() == 10;

        assert childDataNodes.get(0).getDataType() == DataType.STRING;
        assert childDataNodes.get(0).getName().equals("name");

        assert childDataNodes.get(1).getDataType() == DataType.FLOAT;
        assert childDataNodes.get(1).getName().equals("neurons");

        assert childDataNodes.get(2).getDataType() == DataType.DOUBLE;
        assert childDataNodes.get(2).getName().equals("bodyCells");

        assert childDataNodes.get(3).getDataType() == DataType.INT;
        assert childDataNodes.get(3).getName().equals("hands");

        assert childDataNodes.get(4).getDataType() == DataType.LONG;
        assert childDataNodes.get(4).getName().equals("legs");

        assert childDataNodes.get(5).getDataType() == DataType.BOOLEAN;
        assert childDataNodes.get(5).getName().equals("healthy");

        assert childDataNodes.get(6).getName().equals("age");
        assertAge(childDataNodes.get(6));

        assert childDataNodes.get(7).getName().equals("moods");
        assertMoodStatusArray(childDataNodes.get(7));

        assert childDataNodes.get(8).getName().equals("brain");
        assertBrain(childDataNodes.get(8));

        assert childDataNodes.get(9).getDataType() == DataType.META;
        assert childDataNodes.get(9).getName().equals("map");

    }

    public static void assertAge(DataNode ageDataNode) {
        assert ageDataNode.getDataType() == DataType.ENUM;
        assert ageDataNode.getEnumValues().contains("YOUNG");
        assert ageDataNode.getEnumValues().contains("OLD");
    }

    public static void assertMoodStatusArray(DataNode moodStatusDataNodeArray) {
        assert moodStatusDataNodeArray.getDataType() == DataType.ARRAY;
        assert moodStatusDataNodeArray.getArrayItemDataType() == DataType.ENUM;
        assert moodStatusDataNodeArray.getEnumValues().contains("SAD");
        assert moodStatusDataNodeArray.getEnumValues().contains("HAPPY");
        assert moodStatusDataNodeArray.getEnumValues().contains("BORED");
    }

    public static void assertBrain(DataNode brainDataNode) {
        assert brainDataNode.getDataType() == DataType.OBJECT;

        List<DataNode> childDataNodes = brainDataNode.getChildDataNodes();
        assert childDataNodes.size() == 3;

        assert childDataNodes.get(0).getDataType() == DataType.LONG;
        assert childDataNodes.get(0).getName().equals("brainCells");

        assert childDataNodes.get(1).getDataType() == DataType.ENUM;
        assert childDataNodes.get(1).getName().equals("brainIq");
        assert childDataNodes.get(1).getEnumValues().contains("SMART");
        assert childDataNodes.get(1).getEnumValues().contains("STUPID");

        assert childDataNodes.get(2).getName().equals("memories");
        DataNode memoryArrayDataNode = childDataNodes.get(2);
        assertMemoryArray(memoryArrayDataNode);
    }

    public static void assertMemoryArray(DataNode memoryArrayDataNode) {
        assert memoryArrayDataNode.getDataType() == DataType.ARRAY;
        assert memoryArrayDataNode.getArrayItemDataType() == DataType.OBJECT;

        List<DataNode> childDataNodes = memoryArrayDataNode.getChildDataNodes();
        assert childDataNodes.size() == 3;

        assert childDataNodes.get(0).getDataType() == DataType.STRING;
        assert childDataNodes.get(0).getName().equals("time");

        assert childDataNodes.get(1).getName().equals("memoryDetail");
        assertMemoryDetail(childDataNodes.get(1));

        assert childDataNodes.get(2).getName().equals("peopleInvolved");
        assertHumanArray(childDataNodes.get(2));
    }

    public static void assertMemoryDetail(DataNode memoryDetailDataNode) {
        assert memoryDetailDataNode.getDataType() == DataType.OBJECT;

        List<DataNode> childDataNodes = memoryDetailDataNode.getChildDataNodes();
        assert childDataNodes.size() == 2;

        assert childDataNodes.get(0).getDataType() == DataType.STRING;
        assert childDataNodes.get(0).getName().equals("description");

        assert childDataNodes.get(1).getDataType() == DataType.ENUM;
        assert childDataNodes.get(1).getName().equals("type");
        assert childDataNodes.get(1).getEnumValues().contains("GOOD");
        assert childDataNodes.get(1).getEnumValues().contains("BAD");

    }

    public static void assertHumanArray(DataNode humanArrayDataNode) {
        assert humanArrayDataNode.getDataType() == DataType.ARRAY;
        assert humanArrayDataNode.getArrayItemDataType() == DataType.OBJECT;

        List<DataNode> childDataNodes = humanArrayDataNode.getChildDataNodes();
        assert childDataNodes.size() == 2;

        assert childDataNodes.get(0).getDataType() == DataType.STRING;
        assert childDataNodes.get(0).getName().equals("name");

        assert childDataNodes.get(1).getDataType() == DataType.ENUM;
        assert childDataNodes.get(1).getName().equals("relationShip");
        assert childDataNodes.get(1).getEnumValues().contains("CLOSE");
        assert childDataNodes.get(1).getEnumValues().contains("KNOWN");
        assert childDataNodes.get(1).getEnumValues().contains("UNKNOWN");

    }
}
