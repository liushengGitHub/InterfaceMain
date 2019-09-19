package liusheng.main.app.acfun.processor;

import liusheng.main.annotation.ThreadSafe;
import liusheng.main.app.acfun.entity.DataBean;
import liusheng.main.app.acfun.entity.M3u8Bean;
import liusheng.main.app.acfun.executor.AcfunTask;
import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.executor.FailTask;
import liusheng.main.app.bilibili.util.StringUtils;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

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
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class M3u8BeanToDisk extends AbstractLinkedListableProcessor<M3u8Bean, M3u8BeanToDisk> {

    private final Path dirPath;
    private final Logger logger = Logger.getLogger(M3u8BeanToDisk.class);
    private final AtomicInteger nameGenerator = new AtomicInteger(1);
    public M3u8BeanToDisk(Path dirPath) {
        this.dirPath = dirPath;

    }

    @Override
    protected void doProcess(M3u8Bean m3u8Bean, List<Object> returnData) throws Throwable {

        String title = getTitle(m3u8Bean);
        Path filePath = dirPath.resolve(StringUtils.fileNameHandle(title));
        // 创建子文件夹
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath);
        }
        this.pipeline().getExecutorService().execute(new FailTask(new AcfunTask(m3u8Bean,filePath)));

    }
    // 获取标题

    private String getTitle(M3u8Bean m3u8Bean) {
        String title = m3u8Bean.getTitle();
        if (StringUtils.isEmpty(title)) title = m3u8Bean.getBangumiTitle();
        if (StringUtils.isEmpty(title)) title = String.valueOf(nameGenerator.getAndIncrement());
        return title;
    }
}
