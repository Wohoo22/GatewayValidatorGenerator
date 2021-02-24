package cloud.nextsol.core;

import cloud.nextsol.core.mainService.Generate;

import java.io.IOException;

public class Main {

    private static final String protoFile = "D:\\Work\\work-space\\nexttech\\core-grpc-proto\\co-order\\co-order.proto";

    private static final String outputFile = "C:\\Users\\ADMIN\\Desktop\\test.txt";

    public static void main(String[] args) throws IOException {
        Generate.generate(protoFile, outputFile);
    }
}
