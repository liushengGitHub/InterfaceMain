/*
package liusheng.main.app.acfun.old;

import liusheng.main.app.acfun.old.entity.SecondBean;
import liusheng.main.app.bilibili.util.ConnectionUtils;
import liusheng.main.process.AbstractLinkedListableProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SecondBeanToDisk extends AbstractLinkedListableProcessor<SecondBean,SecondBeanToDisk> {
    private final static String FIRST =  "#EXTINF:";
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    @Override
    protected void doProcess(SecondBean secondBean, List<Object> returnData) throws Throwable {
        executorService.execute(()->{
            try {
                String url = secondBean.getUrl();
                int i = url.lastIndexOf("/");
                if (i == -1) throw new RuntimeException();
                String pre = url.substring(0, i + 1);
                String m3u8Str = ConnectionUtils.getConnection(url).execute().body();
                Scanner scanner = new Scanner(m3u8Str);
                Path filePath = secondBean.getDirPath().resolve(secondBean.getFileName() + secondBean.getFbl() + ".ts");
                OutputStream outputStream = Files.newOutputStream(filePath);
                readAndWrite(pre, scanner, outputStream);
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            }catch (Exception e) {
                e.printStackTrace();
                throw  new RuntimeException(e);
            }
        });

    }

    private void readAndWrite(String pre, Scanner scanner, OutputStream outputStream) throws IOException {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith(FIRST)) {

                String newLine = scanner.nextLine();
                String videoUrl= pre + newLine;

                System.out.println(videoUrl);

                InputStream inputStream = new URL(videoUrl)
                        .openStream();

                byte[] bytes = new byte[102400];

                int length = -1;

                while ( (length = inputStream.read(bytes)) != -1){
                    outputStream.write(bytes,0,length);
                }
                try {
                    inputStream.close();
                }catch (Exception e) {}
            }

        }
    }
}
*/
