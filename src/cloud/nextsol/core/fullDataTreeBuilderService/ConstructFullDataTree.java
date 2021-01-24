package cloud.nextsol.core.fullDataTreeBuilderService;

import cloud.nextsol.core.fullDataTreeBuilderService.dataTreeService.BuildDataTree;
import cloud.nextsol.core.fullDataTreeBuilderService.dataTreeService.ConstructMessageList;
import cloud.nextsol.core.fullDataTreeBuilderService.dataTreeService.InitializeDataTree;
import cloud.nextsol.core.model.DataNode;
import cloud.nextsol.core.utils.StringProcessingUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class ConstructFullDataTree {
    /*
     * HashMap <grpc_service_name , grpc_message>
     * */
    public static LinkedHashMap<String, List<DataNode>> buildFullDataTree(String fileData) {

        // replace all '/n' and '/r' and comments
        fileData = StringProcessingUtils.removeComments(fileData);
        fileData = fileData.replace('\n', ' ').replace('\r', ' ');

        /*
        initialize data for building full data tree later
         */
        LinkedHashMap<String, String> serviceMessageMapper = new LinkedHashMap<>(); // map services name with messages name
        HashMap<String, List<DataNode>> initMessageList = new HashMap<>(); // find all grpc messages along with their lv.1 nodes (a general tree data structure with height = 2)
        HashMap<String, List<String>> initEnumList = new HashMap<>(); // find all enum with their values

        InitializeDataTree.initialize(serviceMessageMapper, initMessageList, initEnumList, fileData);


        /*
        start constructing the message objects (building a complete general tree data structure)
        */
        ConstructMessageList.construct(initMessageList, initEnumList);

        /*
        build a completed data tree
        */
        LinkedHashMap<String, List<DataNode>> dataTree = new LinkedHashMap<>();
        BuildDataTree.build(initMessageList, serviceMessageMapper, dataTree);

        return dataTree;
    }

}
