package liusheng.main.app.bilibili.util;

public class StringUtils {
    public static String fileNameHandle(String name) {
        name =  name.replaceAll("[\\\\:*<>?|/\\s]","");
        return name;
    }

    public static boolean isEmpty(String name) {
        return name == null || name.length() == 0;
    }
}
