package cloud.nextsol.core.fullDataTreeBuilderService;

import cloud.nextsol.core.fullDataTreeBuilderService.dataTreeService.ConstructMessageList;
import cloud.nextsol.core.fullDataTreeBuilderService.dataTreeService.InitializeDataTree;
import cloud.nextsol.core.fullDataTreeBuilderService.dataTreeService.MapServiceWithDataTree;
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

        System.out.println("Start initializing data tree ...");
        InitializeDataTree.initialize(serviceMessageMapper, initMessageList, initEnumList, fileData);
        System.out.println("Initialized data tree !");

        /*
        start constructing the message objects (building a complete general tree data structure)
        */
        System.out.println("Start constructing data tree ...");
        ConstructMessageList.construct(initMessageList, initEnumList);
        System.out.println("Constructed data tree !");

        /*
        map service to constructed data tree
        */
        LinkedHashMap<String, List<DataNode>> dataTree = new LinkedHashMap<>();

        System.out.println("Start mapping services with data tree ...");
        MapServiceWithDataTree.map(initMessageList, serviceMessageMapper, dataTree);
        System.out.println("Mapped services with data tree !");

        return dataTree;
    }

}
