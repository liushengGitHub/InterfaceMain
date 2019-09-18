package liusheng.main.app.acfun.old.entity;

import com.google.gson.annotations.SerializedName;

import java.nio.file.Path;
import java.util.List;

public class M3U8Bean {

    /**
     * result : 0
     * playInfo : {"streams":[{"playUrls":["https://video.acfun.cn/59cfa39b0cf2bc92468430521402570701-auto.m3u8?auth_key=1561037919-101063798319813b18ecbf4ce92cc5fc235b2410f1d1ac0212120p202p181pa77c8a3af264456-acfun-81d0705a3f0cfb8b68b8535ad05ec3b6"],"format":2,"quality":1000,"size":0,"width":1920,"height":1080,"fps":0}],"duration":1419000}
     * host-name : hb2-acfun-kcs070.aliyun
     */

    private PlayInfoBean playInfo;
    @SerializedName("host-name")
    private String fileName;
    /**
     * host-name : hb2-acfun-kcs070.aliyun
     */

    private Path  dirPath;

    public Path getDirPath() {
        return dirPath;
    }

    public void setDirPath(Path dirPath) {
        this.dirPath = dirPath;
    }

    public PlayInfoBean getPlayInfo() {
        return playInfo;
    }

    public void setPlayInfo(PlayInfoBean playInfo) {
        this.playInfo = playInfo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public static class PlayInfoBean {
        /**
         * streams : [{"playUrls":["https://video.acfun.cn/59cfa39b0cf2bc92468430521402570701-auto.m3u8?auth_key=1561037919-101063798319813b18ecbf4ce92cc5fc235b2410f1d1ac0212120p202p181pa77c8a3af264456-acfun-81d0705a3f0cfb8b68b8535ad05ec3b6"],"format":2,"quality":1000,"size":0,"width":1920,"height":1080,"fps":0}]
         * duration : 1419000
         */

        private int duration;
        private List<StreamsBean> streams;

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public List<StreamsBean> getStreams() {
            return streams;
        }

        public void setStreams(List<StreamsBean> streams) {
            this.streams = streams;
        }

        public static class StreamsBean {
            /**
             * playUrls : ["https://video.acfun.cn/59cfa39b0cf2bc92468430521402570701-auto.m3u8?auth_key=1561037919-101063798319813b18ecbf4ce92cc5fc235b2410f1d1ac0212120p202p181pa77c8a3af264456-acfun-81d0705a3f0cfb8b68b8535ad05ec3b6"]
             * format : 2
             * quality : 1000
             * size : 0
             * width : 1920
             * height : 1080
             * fps : 0
             */

            private int format;
            private int quality;
            private int size;
            private int width;
            private int height;
            private int fps;
            private List<String> playUrls;

            public int getFormat() {
                return format;
            }

            public void setFormat(int format) {
                this.format = format;
            }

            public int getQuality() {
                return quality;
            }

            public void setQuality(int quality) {
                this.quality = quality;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getFps() {
                return fps;
            }

            public void setFps(int fps) {
                this.fps = fps;
            }

            public List<String> getPlayUrls() {
                return playUrls;
            }

            public void setPlayUrls(List<String> playUrls) {
                this.playUrls = playUrls;
            }
        }
    }
}
