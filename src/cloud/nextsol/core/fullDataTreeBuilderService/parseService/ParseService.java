package cloud.nextsol.core.fullDataTreeBuilderService.parseService;

import java.util.LinkedHashMap;

public class ParseService {
    public static int parseService(int fileDataIndex, LinkedHashMap<String, String> serviceMessageMapper, String fileData) {

        // make sure index passed '{'
        while (fileData.charAt(fileDataIndex) != '{') fileDataIndex++;
        fileDataIndex += 1;

        fileDataIndex = parseRpcRecursively(fileDataIndex, serviceMessageMapper, fileData);

        return fileDataIndex;
    }

    public static int parseRpcRecursively(int fileDataIndex, LinkedHashMap<String, String> serviceMessageMapper, String fileData) {

        if (isEndOfService(fileDataIndex, fileData)) {
            while (fileData.charAt(fileDataIndex) != '}') fileDataIndex++;
            // return index right after '}'
            return fileDataIndex + 1;
        }

        // pass all white space
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

        // pass the 'rpc'
        fileDataIndex += 3;

        // pass all white space -> come to service name
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;

        // parse serviceName
        String serviceName = "";
        while (fileData.charAt(fileDataIndex) != '(') {
            serviceName = serviceName.concat(String.valueOf(fileData.charAt(fileDataIndex)));
            fileDataIndex++;
        }

        // parse the message which service map to
        String messageName = "";
        while (fileData.charAt(fileDataIndex) == '(') fileDataIndex++;
        while (fileData.charAt(fileDataIndex) != ')') {
            messageName = messageName.concat(String.valueOf(fileData.charAt(fileDataIndex)));
            fileDataIndex++;
        }

        serviceMessageMapper.put(serviceName, messageName);

        // pass the ';' to ready parsing next rpc
        while (fileData.charAt(fileDataIndex) != ';') fileDataIndex++;
        fileDataIndex += 1;

        return parseRpcRecursively(fileDataIndex, serviceMessageMapper, fileData);
    }

    private static boolean isEndOfService(int fileDataIndex, String fileData) {
        while (fileData.charAt(fileDataIndex) == ' ') fileDataIndex++;
        return fileData.charAt(fileDataIndex) == '}';
    }
}
