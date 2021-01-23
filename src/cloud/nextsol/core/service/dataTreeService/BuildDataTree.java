package cloud.nextsol.core.service.dataTreeService;

import cloud.nextsol.core.model.DataNode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildDataTree {
    public static void build(HashMap<String, List<DataNode>> initMessageList, LinkedHashMap<String, String> serviceMessageMapper,
                      LinkedHashMap<String, List<DataNode>> dataTree) {

        for (Map.Entry<String, String> serviceAndMessage : serviceMessageMapper.entrySet()) {
            // key == service_name
            // value == message_name

            dataTree.put(serviceAndMessage.getKey(), initMessageList.get(serviceAndMessage.getValue()));
        }
    }
}
