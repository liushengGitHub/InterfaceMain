package liusheng.main.app.bilibili.space;

import com.google.gson.Gson;
import liusheng.main.app.bilibili.AvDownloader;
import liusheng.main.app.bilibili.parser.PageInfoParser;
import liusheng.main.util.ConnectionUtils;
import liusheng.main.download.Downloader;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SpaceDownloader implements Downloader {
    //https://space.bilibili.com/ajax/member/getSubmitVideos?mid=161419374&pagesize=30&tid=0&page=1&keyword=&order=pubdate
    private final String dir;
    public static final String HTTPS_SPACE_BILIBILI_COM = "https://space.bilibili.com/";
    public static final String VIDEO = "/video";
    private static Pattern patten = Pattern.compile(HTTPS_SPACE_BILIBILI_COM + "\\d+" + VIDEO + ".*");

    public SpaceDownloader(String dir) {
        this.dir = dir;
    }

    // https://space.bilibili.com/ajax/member/getSubmitVideos?mid=161419374&pagesize=30&tid=0&page=1&keyword=&order=pubdate
    @Override
    public void download(String url) throws Throwable {
        // 如果是空间号
        if (url.matches("\\d+")) {
            // 拼成 url
             url =  HTTPS_SPACE_BILIBILI_COM + url + VIDEO;
        }

        if (!patten.matcher(url).matches()) throw new IllegalArgumentException();


        String id = url.substring(HTTPS_SPACE_BILIBILI_COM.length(), url.indexOf("/", HTTPS_SPACE_BILIBILI_COM.length()));


        SpaceParam spaceParam = new SpaceParam(id, "1");

        String spaceUrl = spaceParam.toString();


        SpaceEntity spaceEntity = getSpaceEntity(spaceUrl);

        int pages = spaceEntity.getData().getPages();

        AvDownloader avDownloader = new AvDownloader();
       /* avDownloader
                .start(new DefaultDownloaderController(),new DownloadSpeedListener());*/

        List<String> list = IntStream.rangeClosed(1, pages).boxed().flatMap(i -> {
            spaceParam.setPage(String.valueOf(i));
            try {
                return downloadVid(getSpaceEntity(spaceParam.toString())).stream();
            } catch (IOException e) {
                return Stream.<String>empty();
            }

        }).collect(Collectors.toList());


        list.forEach(u-> {
            try {
                avDownloader.getWorks().put(u);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    private SpaceEntity getSpaceEntity(String spaceUrl) throws IOException {
        String body = ConnectionUtils.getConnection(spaceUrl).execute().body();
        return new Gson().fromJson(body, SpaceEntity.class);
    }

    private List<String> downloadVid(SpaceEntity spaceEntity) {
        return spaceEntity.getData().getVlist().stream().map(bean -> {
            int aid = bean.getAid();
            String url = PageInfoParser.HTTPS_WWW_BILIBILI_COM_VIDEO_AV + aid;
            return url;
        }).collect(Collectors.toList());
    }
}
