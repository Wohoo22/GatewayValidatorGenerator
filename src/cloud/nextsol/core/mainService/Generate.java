package cloud.nextsol.core.mainService;

import cloud.nextsol.core.fullDataTreeBuilderService.ConstructFullDataTree;
import cloud.nextsol.core.model.DataNode;
import cloud.nextsol.core.validatorGeneratorService.GenerateValidatorInString;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

public class Generate {
    public static void generate(String protoFileDir, String outputFileDir) throws IOException {
        String grpcFileData = Files.readString(Path.of(protoFileDir), StandardCharsets.UTF_8);

        LinkedHashMap<String, List<DataNode>> dataTree = ConstructFullDataTree.buildFullDataTree(grpcFileData);

        String result = GenerateValidatorInString.generate(dataTree);

        FileWriter outputFile = new FileWriter(outputFileDir);
        outputFile.write(result);
        outputFile.close();
    }
}
