package cloud.nextsol.core.utils;

public class StringProcessingUtils {
    public static String removeComments(String s) {
        String res = "";

        boolean singleLineCmt = false;
        boolean multipleLineCmt = false;

        for (int i = 0; i < s.length(); i++) {
            if (singleLineCmt && s.charAt(i) == '\n')
                singleLineCmt = false;
            else if (multipleLineCmt && s.charAt(i) == '*' && s.charAt(i + 1) == '/')
                multipleLineCmt = false;
            else if (s.charAt(i) == '/' && s.charAt(i) == '/')
                singleLineCmt = true;
            else if (s.charAt(i) == '/' && s.charAt(i) == '*')
                multipleLineCmt = true;
            else if (!singleLineCmt && !multipleLineCmt)
                res = res.concat(String.valueOf(s.charAt(i)));
        }

        return res;
    }
}
