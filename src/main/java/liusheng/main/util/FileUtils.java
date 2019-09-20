package liusheng.main.util;

import java.io.*;
import java.net.SocketTimeoutException;

public class FileUtils {
    public static void copy(InputStream inputStream , OutputStream outputStream,boolean big) throws IOException {
        try {

            int size = big ? 102400 : 10240;

            byte[] bytes = new byte[size];

            int length = -1;

            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
        }catch (IOException e) {
            throw  e;
        }
    }
    public static void copy(InputStream inputStream , OutputStream outputStream) throws IOException{
        copy(inputStream,outputStream,false);
    }
}
