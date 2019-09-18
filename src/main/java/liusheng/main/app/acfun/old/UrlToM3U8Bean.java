/*
package liusheng.main.app.acfun.old.processor;

import com.google.gson.Gson;
import liusheng.main.app.acfun.old.entity.AnimalPageInfo;
import liusheng.main.app.acfun.old.entity.M3U8Bean;
import liusheng.main.app.acfun.old.entity.PageInfo;
import liusheng.main.app.bilibili.util.ConnectionUtils;
import liusheng.main.app.bilibili.util.StringUtils;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class UrlToM3U8Bean extends AbstractLinkedListableProcessor<String, UrlToM3U8Bean> {
    private static final String API = "https://www.acfun.cn/rest/pc-direct/play/playInfo/m3u8Auto?videoId=";
    private static final String PREFIX = "var pageInfo =";
    private static final String SENCOND_PREFIX = "var bgmInfo =";
    private static final String ANIMAL_PREFIX = "https://www.acfun.cn/bangumi";
    private static final String AC_PREFIX = "https://www.acfun.cn/v";
    private final String dir;

    public UrlToM3U8Bean(String dir) {
        this.dir = Objects.requireNonNull(dir);
    }

    @Override
    protected void doProcess(String url, List<Object> returnData) throws Throwable {

//10375618

        Document document = ConnectionUtils.getConnection(url).get();

        String script = document.select("script").stream().map(Element::html).filter(html -> {

                return     html.contains(PREFIX);
                }
        )
                .findFirst().orElseThrow(RuntimeException::new);


        Gson gson = new Gson();
        int videoId = -1;
        String dirName = null;
        if (url.startsWith(ANIMAL_PREFIX)) {
            int i = script.indexOf(SENCOND_PREFIX);
            script = script.substring(PREFIX.length(), i);
            AnimalPageInfo animalPageInfo = gson.fromJson(script, AnimalPageInfo.class);

            AnimalPageInfo.VideoBean videoBean = animalPageInfo.getVideo();
            AnimalPageInfo.AlbumBean album = animalPageInfo.getAlbum();

            videoId = Optional.ofNullable(videoBean).
                    map(AnimalPageInfo.VideoBean::getVideos).filter(videosBeans -> videosBeans.size() > 0).map(videosBeans -> videosBeans.get(0))
                    .map(AnimalPageInfo.VideoBean.VideosBean::getDanmakuId)
                    .orElse(-1);

            dirName = Optional.ofNullable(album).map(AnimalPageInfo.AlbumBean::getTitle).orElse("");
        } else {
            script = script.substring(PREFIX.length());
            PageInfo pageInfo = gson.fromJson(script, PageInfo.class);
            videoId = pageInfo.getVideoId();
            dirName = pageInfo.getTitle();
        }
        if (videoId <= 0) throw new UnsupportedOperationException();

        dirName = StringUtils.isEmpty(dirName) ? UUID.randomUUID().toString() : StringUtils.fileNameHandle(dirName);

        Path path = Paths.get(dir, dirName);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String body = ConnectionUtils.getConnection(API + videoId)
                .header("Referer", url)
                .execute().body();

        M3U8Bean m3U8Bean = gson.fromJson(body, M3U8Bean.class);
        m3U8Bean.setFileName(dirName);
        m3U8Bean.setDirPath(path);

        returnData.add(m3U8Bean);
    }

    private int findFirstNotBetter(String url) {
        int n = 0;
        for (int i = url.length() - 1; i >= 0; i--) {
            char c = url.charAt(i);
            if (c >= '0' && c <= '9') {
                n++;
            } else {
                break;
            }
        }
        return n;
    }
}
*/
