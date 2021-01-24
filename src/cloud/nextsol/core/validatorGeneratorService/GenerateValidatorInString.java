package cloud.nextsol.core.validatorGeneratorService;

import cloud.nextsol.core.enums.DataType;
import cloud.nextsol.core.model.DataNode;
import cloud.nextsol.core.utils.DataTypeToValidatorType;
import cloud.nextsol.core.utils.StringProcessingUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerateValidatorInString {

    private static final String lineBreak = "\n";

    public static String generate(LinkedHashMap<String, List<DataNode>> dataTree) {

        String result = "";

        // initialize the file
        result = result.concat("import { validatorConfig } from '../core/interfaces/validator_config';" + lineBreak);
        result = result.concat("import { coreValidator } from '../libs/coreValidator';" + lineBreak);
        result = StringProcessingUtils.addLineBreak(result, 2);


        // validator config
        result = result.concat("const helloValidator: validatorConfig = {" + lineBreak);
        result = result.concat(whiteSpace(2) + "service: 'helloService'," + lineBreak);
        result = result.concat(whiteSpace(2) + "functions: [" + lineBreak);

        // start generating here
        for (Map.Entry<String, List<DataNode>> service : dataTree.entrySet()) {
            String serviceName = service.getKey();
            List<DataNode> dataNodes = service.getValue();

            // begin a service object
            result = result.concat(whiteSpace(4) + "{" + lineBreak);
            // service name
            result = result.concat(whiteSpace(6) + "name: '" + serviceName + "'," + lineBreak);
            // begin a schema
            result = result.concat(whiteSpace(6) + "schema: {" + lineBreak);

            // start generating schema detail
            result = generateSchemaDetailRecursiveLy(dataNodes, 8, result);

            // end a schema
            result = result.concat(whiteSpace(6) + "}," + lineBreak);
            // end a service object
            result = result.concat(whiteSpace(4) + "}," + lineBreak);
        }

        // end of file
        result = result.concat(whiteSpace(2) + "]," + lineBreak);
        result = result.concat("};" + lineBreak);
        result = result.concat("export default helloValidator;");

        return result;
    }

    private static String generateSchemaDetailRecursiveLy(List<DataNode> dataNodes, int whiteSpaceCount, String result) {
        for (DataNode dataNode : dataNodes) {

            // meta data type doesn't need to be validated
            if (dataNode.getDataType() == DataType.META || dataNode.getArrayItemDataType() == DataType.META) continue;

            String validatorDataType = DataTypeToValidatorType.convert(dataNode.getDataType());
            result = result.concat(whiteSpace(whiteSpaceCount) + dataNode.getName() + ": {" + lineBreak);
            result = result.concat(whiteSpace(whiteSpaceCount + 2) + "type: '" + validatorDataType + "'," + lineBreak);

            if (dataNode.getDataType() == DataType.ARRAY) {

                String validatorArrayDataType = DataTypeToValidatorType.convert(dataNode.getArrayItemDataType());
                result = result.concat(whiteSpace(whiteSpaceCount + 2) + "items: {" + lineBreak);
                result = result.concat(whiteSpace(whiteSpaceCount + 4) + "type: '" + validatorArrayDataType + "'," + lineBreak);

                if (dataNode.getArrayItemDataType() == DataType.OBJECT) {
                    result = result.concat(whiteSpace(whiteSpaceCount + 4) + "props: {" + lineBreak);

                    result = generateSchemaDetailRecursiveLy(dataNode.getChildDataNodes(), whiteSpaceCount + 6, result);

                    result = result.concat(whiteSpace(whiteSpaceCount + 4) + "}," + lineBreak);

                } else if (dataNode.getArrayItemDataType() == DataType.ENUM) {

                    result = result.concat(whiteSpace(whiteSpaceCount + 4) + "enum: " + validatorEnumConverter(dataNode.getEnumValues()) + "," + lineBreak);

                }

                result = result.concat(whiteSpace(whiteSpaceCount + 2) + "}," + lineBreak);

            } else if (dataNode.getDataType() == DataType.OBJECT) {

                result = result.concat(whiteSpace(whiteSpaceCount + 2) + "props: {" + lineBreak);

                result = generateSchemaDetailRecursiveLy(dataNode.getChildDataNodes(), whiteSpaceCount + 4, result);

                result = result.concat(whiteSpace(whiteSpaceCount + 2) + "}," + lineBreak);

            } else if (dataNode.getDataType() == DataType.ENUM) {

                result = result.concat(whiteSpace(whiteSpaceCount + 2) + "enum: " + validatorEnumConverter(dataNode.getEnumValues()) + "," + lineBreak);

            }

            result = result.concat(whiteSpace(whiteSpaceCount) + "}," + lineBreak);
        }
        return result;
    }

    private static String whiteSpace(int whiteSpaceCount) {
        String s = "";
        for (int i = 0; i < whiteSpaceCount; i++) s = s.concat(" ");
        return s;
    }

    private static String validatorEnumConverter(List<String> enumValues) {
        String s = "[";
        for (int i = 0; i < enumValues.size(); i++) {
            String validatorEnum = "'" + enumValues.get(i) + "'";
            if (i < enumValues.size() - 1) validatorEnum = validatorEnum.concat(",");
            s = s.concat(validatorEnum);
        }
        s = s.concat("]");
        return s;
    }
}
