package liusheng.main.app.acfun.old;

import liusheng.main.app.acfun.old.entity.M3U8Bean;
import liusheng.main.app.acfun.old.entity.SecondBean;
import liusheng.main.process.AbstractLinkedListableProcessor;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class M3U8BeanToSecondBean extends AbstractLinkedListableProcessor<M3U8Bean, M3U8BeanToSecondBean> {
    private final static String FIRST = "#EXT-X-STREAM-INF:";
    private final static String FBL = "RESOLUTION=";

    @Override
    protected void doProcess(M3U8Bean m3U8Bean, List returnData) throws Throwable {
        Optional.ofNullable(m3U8Bean.getPlayInfo())
                .map(M3U8Bean.PlayInfoBean::getStreams)
                .ifPresent(streamsBeans -> {
                    int size = streamsBeans.size();

                    if (size == 0) return;
                    List<String> playUrls = streamsBeans.get(0).getPlayUrls();
                    if (playUrls == null || playUrls.isEmpty()) return;

                    String url = playUrls.get(0);
                    try {

                        InputStream inputStream = new URL(url).openStream();
                        StringWriter stringWriter = new StringWriter();
                        IOUtils.copy(inputStream, stringWriter, StandardCharsets.UTF_8);
                        Scanner scanner = new Scanner(stringWriter.toString());

                        List<SecondBean> list = new LinkedList<>();
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();

                            if (line.startsWith(FIRST)) {
                                int i = line.lastIndexOf(FBL);
                                String fbl = "";
                                if (i != -1) {
                                    fbl = line.substring(i +FBL.length());
                                }else {
                                    fbl = UUID.randomUUID().toString();
                                }

                                String nextLine = scanner.nextLine();
                                int end = url.lastIndexOf("/");
                                if (end == -1) return;
                                // 从右往左找
                                String temp = url.substring(0, end + 1);

                                String videoUrl = nextLine.matches("https?://.*") ? nextLine: temp + nextLine;
                                SecondBean secondBean = new SecondBean(videoUrl,fbl,m3U8Bean.getFileName(),m3U8Bean.getDirPath());

                                list.add(secondBean);
                            }
                        }
                        returnData.addAll(list);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
