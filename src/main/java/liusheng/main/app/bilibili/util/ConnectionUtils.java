package liusheng.main.app.bilibili.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionUtils {
    public  static Connection getConnection(String url) {
        return Jsoup.connect(url)
                .ignoreContentType(true)
                .timeout(60000);
    }

    public static HttpURLConnection getNativeConnection(String videoUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(videoUrl).openConnection();

        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);

        return connection;
    }
}
