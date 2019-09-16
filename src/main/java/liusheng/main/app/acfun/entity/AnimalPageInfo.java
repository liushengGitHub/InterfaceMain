package liusheng.main.app.acfun.entity;

import java.util.List;

public class AnimalPageInfo {

    /**
     * album : {"id":5024637,"title":"神雕实验室","intro":"5月14日，腾讯漫动画《沙雕实验室》，开箱有惊喜！","coverImageV":"https://imgs.aixifan.com/image-manager_1557804786597-6n4xO4bjFU","coverImageH":"https://imgs.aixifan.com/image-manager_1557804791221-bHMAVEQ46s","tags":[{"id":488,"name":"国产","sort":1},{"id":9,"name":"萌","sort":2}],"mediaType":1,"channelAId":155,"channelAName":"番剧","channelBId":120,"channelBName":"国产动画","contentType":1,"online":1,"userId":-1,"albumType":3,"status":3,"isAcfunOnly":0,"lastUpdateTime":1560914678000,"lastVideoId":325564,"lastVideoName":"第27集","groups":[{"id":33913,"name":"未分组","sort":0,"isDefault":1}],"episodes":[],"onlineTime":1557804836000,"viewsCount":10829,"comments":0,"stowCount":187,"createdAt":1557804836000,"createrId":352,"updatedAt":1560914681000,"updaterId":352,"contentsCount":27,"displayIos":1,"onBillboard":0,"year":2019,"month":5,"day":14,"week":2,"extendsStatus":1,"allowDownload":0,"allowComment":1,"webUpdateTime":"11:32","platformWeb":1,"platformIos":1,"platformAndroid":1,"webDisplay":0,"internalTag":""}
     * video : {"part":"5024637_323076","sort":10,"videos":[{"id":323076,"groupId":33913,"albumId":5024637,"jcContentId":10215351,"danmakuId":10231832,"newTitle":"网购有风险，验货擦亮眼","intr":"《神雕实验室》漫动画，每周二-周六更新，腾讯动漫抢先看！","image":"https://imgs.aixifan.com/FmA04NXZPjzxKCn3BT6WxW0yjIHd","mediaType":1,"videoId":10231832,"sourceType":"zhuzhan","sourceId":"6907844","urlWeb":"","urlMobile":"","sort":10,"onlineTime":1557815678000,"episodeName":"第1话","createdAt":1557815699000,"createrId":352,"updatedAt":1557815699000,"playLimit":0}],"id":"323076"}
     */

    private AlbumBean album;
    private VideoBean video;

    public AlbumBean getAlbum() {
        return album;
    }

    public void setAlbum(AlbumBean album) {
        this.album = album;
    }

    public VideoBean getVideo() {
        return video;
    }

    public void setVideo(VideoBean video) {
        this.video = video;
    }

    public static class AlbumBean {
        /**
         * id : 5024637
         * title : 神雕实验室
         * intro : 5月14日，腾讯漫动画《沙雕实验室》，开箱有惊喜！
         * coverImageV : https://imgs.aixifan.com/image-manager_1557804786597-6n4xO4bjFU
         * coverImageH : https://imgs.aixifan.com/image-manager_1557804791221-bHMAVEQ46s
         * tags : [{"id":488,"name":"国产","sort":1},{"id":9,"name":"萌","sort":2}]
         * mediaType : 1
         * channelAId : 155
         * channelAName : 番剧
         * channelBId : 120
         * channelBName : 国产动画
         * contentType : 1
         * online : 1
         * userId : -1
         * albumType : 3
         * status : 3
         * isAcfunOnly : 0
         * lastUpdateTime : 1560914678000
         * lastVideoId : 325564
         * lastVideoName : 第27集
         * groups : [{"id":33913,"name":"未分组","sort":0,"isDefault":1}]
         * episodes : []
         * onlineTime : 1557804836000
         * viewsCount : 10829
         * comments : 0
         * stowCount : 187
         * createdAt : 1557804836000
         * createrId : 352
         * updatedAt : 1560914681000
         * updaterId : 352
         * contentsCount : 27
         * displayIos : 1
         * onBillboard : 0
         * year : 2019
         * month : 5
         * day : 14
         * week : 2
         * extendsStatus : 1
         * allowDownload : 0
         * allowComment : 1
         * webUpdateTime : 11:32
         * platformWeb : 1
         * platformIos : 1
         * platformAndroid : 1
         * webDisplay : 0
         * internalTag :
         */

        private int id;
        private String title;
        private String intro;
        private String coverImageV;
        private String coverImageH;
        private int mediaType;
        private int channelAId;
        private String channelAName;
        private int channelBId;
        private String channelBName;
        private int contentType;
        private int online;
        private int userId;
        private int albumType;
        private int status;
        private int isAcfunOnly;
        private long lastUpdateTime;
        private int lastVideoId;
        private String lastVideoName;
        private long onlineTime;
        private int viewsCount;
        private int comments;
        private int stowCount;
        private long createdAt;
        private int createrId;
        private long updatedAt;
        private int updaterId;
        private int contentsCount;
        private int displayIos;
        private int onBillboard;
        private int year;
        private int month;
        private int day;
        private int week;
        private int extendsStatus;
        private int allowDownload;
        private int allowComment;
        private String webUpdateTime;
        private int platformWeb;
        private int platformIos;
        private int platformAndroid;
        private int webDisplay;
        private String internalTag;
        private List<TagsBean> tags;
        private List<GroupsBean> groups;
        private List<?> episodes;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getCoverImageV() {
            return coverImageV;
        }

        public void setCoverImageV(String coverImageV) {
            this.coverImageV = coverImageV;
        }

        public String getCoverImageH() {
            return coverImageH;
        }

        public void setCoverImageH(String coverImageH) {
            this.coverImageH = coverImageH;
        }

        public int getMediaType() {
            return mediaType;
        }

        public void setMediaType(int mediaType) {
            this.mediaType = mediaType;
        }

        public int getChannelAId() {
            return channelAId;
        }

        public void setChannelAId(int channelAId) {
            this.channelAId = channelAId;
        }

        public String getChannelAName() {
            return channelAName;
        }

        public void setChannelAName(String channelAName) {
            this.channelAName = channelAName;
        }

        public int getChannelBId() {
            return channelBId;
        }

        public void setChannelBId(int channelBId) {
            this.channelBId = channelBId;
        }

        public String getChannelBName() {
            return channelBName;
        }

        public void setChannelBName(String channelBName) {
            this.channelBName = channelBName;
        }

        public int getContentType() {
            return contentType;
        }

        public void setContentType(int contentType) {
            this.contentType = contentType;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getAlbumType() {
            return albumType;
        }

        public void setAlbumType(int albumType) {
            this.albumType = albumType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIsAcfunOnly() {
            return isAcfunOnly;
        }

        public void setIsAcfunOnly(int isAcfunOnly) {
            this.isAcfunOnly = isAcfunOnly;
        }

        public long getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(long lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public int getLastVideoId() {
            return lastVideoId;
        }

        public void setLastVideoId(int lastVideoId) {
            this.lastVideoId = lastVideoId;
        }

        public String getLastVideoName() {
            return lastVideoName;
        }

        public void setLastVideoName(String lastVideoName) {
            this.lastVideoName = lastVideoName;
        }

        public long getOnlineTime() {
            return onlineTime;
        }

        public void setOnlineTime(long onlineTime) {
            this.onlineTime = onlineTime;
        }

        public int getViewsCount() {
            return viewsCount;
        }

        public void setViewsCount(int viewsCount) {
            this.viewsCount = viewsCount;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getStowCount() {
            return stowCount;
        }

        public void setStowCount(int stowCount) {
            this.stowCount = stowCount;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public int getCreaterId() {
            return createrId;
        }

        public void setCreaterId(int createrId) {
            this.createrId = createrId;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getUpdaterId() {
            return updaterId;
        }

        public void setUpdaterId(int updaterId) {
            this.updaterId = updaterId;
        }

        public int getContentsCount() {
            return contentsCount;
        }

        public void setContentsCount(int contentsCount) {
            this.contentsCount = contentsCount;
        }

        public int getDisplayIos() {
            return displayIos;
        }

        public void setDisplayIos(int displayIos) {
            this.displayIos = displayIos;
        }

        public int getOnBillboard() {
            return onBillboard;
        }

        public void setOnBillboard(int onBillboard) {
            this.onBillboard = onBillboard;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public int getExtendsStatus() {
            return extendsStatus;
        }

        public void setExtendsStatus(int extendsStatus) {
            this.extendsStatus = extendsStatus;
        }

        public int getAllowDownload() {
            return allowDownload;
        }

        public void setAllowDownload(int allowDownload) {
            this.allowDownload = allowDownload;
        }

        public int getAllowComment() {
            return allowComment;
        }

        public void setAllowComment(int allowComment) {
            this.allowComment = allowComment;
        }

        public String getWebUpdateTime() {
            return webUpdateTime;
        }

        public void setWebUpdateTime(String webUpdateTime) {
            this.webUpdateTime = webUpdateTime;
        }

        public int getPlatformWeb() {
            return platformWeb;
        }

        public void setPlatformWeb(int platformWeb) {
            this.platformWeb = platformWeb;
        }

        public int getPlatformIos() {
            return platformIos;
        }

        public void setPlatformIos(int platformIos) {
            this.platformIos = platformIos;
        }

        public int getPlatformAndroid() {
            return platformAndroid;
        }

        public void setPlatformAndroid(int platformAndroid) {
            this.platformAndroid = platformAndroid;
        }

        public int getWebDisplay() {
            return webDisplay;
        }

        public void setWebDisplay(int webDisplay) {
            this.webDisplay = webDisplay;
        }

        public String getInternalTag() {
            return internalTag;
        }

        public void setInternalTag(String internalTag) {
            this.internalTag = internalTag;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<GroupsBean> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupsBean> groups) {
            this.groups = groups;
        }

        public List<?> getEpisodes() {
            return episodes;
        }

        public void setEpisodes(List<?> episodes) {
            this.episodes = episodes;
        }

        public static class TagsBean {
            /**
             * id : 488
             * name : 国产
             * sort : 1
             */

            private int id;
            private String name;
            private int sort;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }

        public static class GroupsBean {
            /**
             * id : 33913
             * name : 未分组
             * sort : 0
             * isDefault : 1
             */

            private int id;
            private String name;
            private int sort;
            private int isDefault;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getIsDefault() {
                return isDefault;
            }

            public void setIsDefault(int isDefault) {
                this.isDefault = isDefault;
            }
        }
    }

    public static class VideoBean {
        /**
         * part : 5024637_323076
         * sort : 10
         * videos : [{"id":323076,"groupId":33913,"albumId":5024637,"jcContentId":10215351,"danmakuId":10231832,"newTitle":"网购有风险，验货擦亮眼","intr":"《神雕实验室》漫动画，每周二-周六更新，腾讯动漫抢先看！","image":"https://imgs.aixifan.com/FmA04NXZPjzxKCn3BT6WxW0yjIHd","mediaType":1,"videoId":10231832,"sourceType":"zhuzhan","sourceId":"6907844","urlWeb":"","urlMobile":"","sort":10,"onlineTime":1557815678000,"episodeName":"第1话","createdAt":1557815699000,"createrId":352,"updatedAt":1557815699000,"playLimit":0}]
         * id : 323076
         */

        private String part;
        private int sort;
        private String id;
        private List<VideosBean> videos;

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<VideosBean> getVideos() {
            return videos;
        }

        public void setVideos(List<VideosBean> videos) {
            this.videos = videos;
        }

        public static class VideosBean {
            /**
             * id : 323076
             * groupId : 33913
             * albumId : 5024637
             * jcContentId : 10215351
             * danmakuId : 10231832
             * newTitle : 网购有风险，验货擦亮眼
             * intr : 《神雕实验室》漫动画，每周二-周六更新，腾讯动漫抢先看！
             * image : https://imgs.aixifan.com/FmA04NXZPjzxKCn3BT6WxW0yjIHd
             * mediaType : 1
             * videoId : 10231832
             * sourceType : zhuzhan
             * sourceId : 6907844
             * urlWeb :
             * urlMobile :
             * sort : 10
             * onlineTime : 1557815678000
             * episodeName : 第1话
             * createdAt : 1557815699000
             * createrId : 352
             * updatedAt : 1557815699000
             * playLimit : 0
             */

            private int id;
            private int groupId;
            private int albumId;
            private int jcContentId;
            private int danmakuId;
            private String newTitle;
            private String intr;
            private String image;
            private int mediaType;
            private int videoId;
            private String sourceType;
            private String sourceId;
            private String urlWeb;
            private String urlMobile;
            private int sort;
            private long onlineTime;
            private String episodeName;
            private long createdAt;
            private int createrId;
            private long updatedAt;
            private int playLimit;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getGroupId() {
                return groupId;
            }

            public void setGroupId(int groupId) {
                this.groupId = groupId;
            }

            public int getAlbumId() {
                return albumId;
            }

            public void setAlbumId(int albumId) {
                this.albumId = albumId;
            }

            public int getJcContentId() {
                return jcContentId;
            }

            public void setJcContentId(int jcContentId) {
                this.jcContentId = jcContentId;
            }

            public int getDanmakuId() {
                return danmakuId;
            }

            public void setDanmakuId(int danmakuId) {
                this.danmakuId = danmakuId;
            }

            public String getNewTitle() {
                return newTitle;
            }

            public void setNewTitle(String newTitle) {
                this.newTitle = newTitle;
            }

            public String getIntr() {
                return intr;
            }

            public void setIntr(String intr) {
                this.intr = intr;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getMediaType() {
                return mediaType;
            }

            public void setMediaType(int mediaType) {
                this.mediaType = mediaType;
            }

            public int getVideoId() {
                return videoId;
            }

            public void setVideoId(int videoId) {
                this.videoId = videoId;
            }

            public String getSourceType() {
                return sourceType;
            }

            public void setSourceType(String sourceType) {
                this.sourceType = sourceType;
            }

            public String getSourceId() {
                return sourceId;
            }

            public void setSourceId(String sourceId) {
                this.sourceId = sourceId;
            }

            public String getUrlWeb() {
                return urlWeb;
            }

            public void setUrlWeb(String urlWeb) {
                this.urlWeb = urlWeb;
            }

            public String getUrlMobile() {
                return urlMobile;
            }

            public void setUrlMobile(String urlMobile) {
                this.urlMobile = urlMobile;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public long getOnlineTime() {
                return onlineTime;
            }

            public void setOnlineTime(long onlineTime) {
                this.onlineTime = onlineTime;
            }

            public String getEpisodeName() {
                return episodeName;
            }

            public void setEpisodeName(String episodeName) {
                this.episodeName = episodeName;
            }

            public long getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(long createdAt) {
                this.createdAt = createdAt;
            }

            public int getCreaterId() {
                return createrId;
            }

            public void setCreaterId(int createrId) {
                this.createrId = createrId;
            }

            public long getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(long updatedAt) {
                this.updatedAt = updatedAt;
            }

            public int getPlayLimit() {
                return playLimit;
            }

            public void setPlayLimit(int playLimit) {
                this.playLimit = playLimit;
            }
        }
    }
}
