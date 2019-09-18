package liusheng.main.app.acfun.processor;

import cn.hutool.core.util.URLUtil;
import liusheng.main.app.acfun.entity.DataBean;
import liusheng.main.app.acfun.entity.M3u8Bean;
import liusheng.main.process.AbstractLinkedListableProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class M3u8BeanToDisk extends AbstractLinkedListableProcessor<M3u8Bean, M3u8BeanToDisk> {
    private final static String FIRST =  "#EXTINF:";
    private final Path filePath;

    public M3u8BeanToDisk(Path filePath) throws IOException {
        this.filePath = filePath;
        if (!Files.exists(filePath)){
            Files.createDirectories(filePath);
        }
    }

    @Override
    protected void doProcess(M3u8Bean m3u8Bean, List<Object> returnData) throws Throwable {
        this.pipeline().getExecutorService().execute(()->{
            try {
                DataBean dataBean = m3u8Bean.getDataBean();

                String m3u8 = Optional.of(dataBean)
                        .map(DataBean::getAdaptationSet)
                        .map(DataBean.AdaptationSetBean::getRepresentation)
                        .map(list -> {
                            return list.stream().map(DataBean.AdaptationSetBean.RepresentationBean::getM3u8)
                                    .findFirst().orElseThrow(RuntimeException::new);
                        }).orElseThrow(RuntimeException::new);

                Scanner scanner = new Scanner(m3u8);
                OutputStream outputStream = Files.newOutputStream(filePath.resolve( UUID.randomUUID().toString() +".ts"));
                readAndWrite("", scanner, outputStream);
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
