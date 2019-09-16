package liusheng.main.app.bilibili.processor;

import liusheng.main.app.bilibili.donwload.RetryDownloader;
import liusheng.main.app.bilibili.entity.DownloadEntity;
import liusheng.main.app.bilibili.entity.NewVideoBean;
import liusheng.main.app.bilibili.entity.OldVideoBean;
import liusheng.main.app.bilibili.executor.MergeFile;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HandleOldVideoBean extends AbstractLinkedListableProcessor<OldVideoBean, HandleOldVideoBean> {
    private final Logger logger = Logger.getLogger(HandleOldVideoBean.class);

    private int retry = 3;
    private List<? extends  RetryDownloader.DownloaderListener> downloaderListeners;
    public HandleOldVideoBean(List<? extends RetryDownloader.DownloaderListener> downloaderListeners, int retry) {
        this.downloaderListeners = downloaderListeners;

        this.retry = retry;
        //this.addListener(new DefaultProcessListener(retry));
    }
    @Override
    protected void doProcess(OldVideoBean oldVideoBean, List<Object> returnData) throws Throwable {
        Optional.ofNullable(oldVideoBean.getData())
                .map(OldVideoBean.DataBean::getDurl)
                .ifPresent(durlBeans -> {
                    if (durlBeans.isEmpty()) return ;
                    boolean b = durlBeans.size() > 1;
                    List<String> paths = new LinkedList<>();
                    Path dirPath = oldVideoBean.getDirFile().toPath();
                    String refererUrl = oldVideoBean.getUrl();
                    String name = oldVideoBean.getName();
                    durlBeans.forEach(durlBean -> {

                        try {
                            int order = durlBean.getOrder();
                            String videoUrl = durlBean.getUrl();

                            List<String> backup_url = durlBean.getBackup_url() == null ? Collections.emptyList(): (List<String>) durlBean.getBackup_url();

                            Path filePath = dirPath.resolve( (b ? order + "_" : "") + name + ".flv");

                            paths.add(filePath.toString());

                            RetryDownloader retryDownloader = new RetryDownloader();
                            retryDownloader.getDownloaderListeners().addAll(downloaderListeners);
                            retryDownloader. process(new DownloadEntity(refererUrl,videoUrl,backup_url,filePath,dirPath,retry));

                        } catch (Throwable e) {


                            e.printStackTrace();

                            throw new RuntimeException(e);
                        }

                        logger.debug(refererUrl + "  Download Completed ");
                    });

                    try {
                        if (b) {
                            new MergeFile(paths, name, dirPath.toString()).run();
                        }

                    } catch (Exception e) {

                        throw new RuntimeException(e);
                    }
                });
    }

}
