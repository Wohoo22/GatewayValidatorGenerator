package cloud.nextsol.core.service.dataTreeService;

import cloud.nextsol.core.model.DataNode;
import cloud.nextsol.core.service.parseService.ParseEnum;
import cloud.nextsol.core.service.parseService.ParseMessage;
import cloud.nextsol.core.service.parseService.ParseService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class InitializeDataTree {
    public static void initialize(LinkedHashMap<String, String> serviceMessageMapper, HashMap<String, List<DataNode>> initMessageList,
                                  HashMap<String, List<String>> initEnumList, String fileData) {

        for (int fileDataIndex = 0; fileDataIndex < fileData.length(); fileDataIndex++) {

            // if approach grpc message
            if (fileData.charAt(fileDataIndex) == 'm'
                    && fileData.substring(fileDataIndex, fileDataIndex + 7).equals("message")
                    && fileData.charAt(fileDataIndex + 7) == ' ') {

                // start with index right after 'message'
                fileDataIndex = ParseMessage.parseMessage(fileDataIndex + 7, initMessageList, initEnumList, fileData, "");

            } // if approach grpc service
            else if (fileData.charAt(fileDataIndex) == 's'
                    && fileData.substring(fileDataIndex, fileDataIndex + 7).equals("service")
                    && fileData.charAt(fileDataIndex + 7) == ' ') {

                // start with index right after 'service'
                fileDataIndex = ParseService.parseService(fileDataIndex, serviceMessageMapper, fileData);

            } // if approach grpc enum
            else if (fileData.charAt(fileDataIndex) == 'e'
                    && fileData.substring(fileDataIndex, fileDataIndex + 4).equals("enum")
                    && fileData.charAt(fileDataIndex + 4) == ' ') {

                // start with index right after 'enum'
                fileDataIndex = ParseEnum.parseEnum(fileDataIndex + 4, initEnumList, fileData, "");

            }
        }
    }
}
