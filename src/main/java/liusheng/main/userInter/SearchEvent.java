package liusheng.main.userInter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import liusheng.main.annotation.ThreadSafe;
import liusheng.main.app.bilibili.AvDownloader;
import liusheng.main.app.bilibili.donwload.DefaultDownloaderController;
import liusheng.main.app.bilibili.executor.ClosableFixedThreadPoolExecutor;
import liusheng.main.app.bilibili.executor.FailListExecutorService;
import liusheng.main.app.bilibili.executor.FailTask;
import liusheng.main.app.bilibili.listener.DownloadSpeedListener;
import liusheng.main.app.entity.SearchItem;
import liusheng.main.app.entity.SearchPage;
import liusheng.main.userInter.entity.ComboBoxEntity;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;


@ThreadSafe
public class SearchEvent implements EventHandler<ActionEvent> {
    public static final String HTTPS_SEARCH_BILIBILI_COM_ALL = "https://search.bilibili.com/all?keyword=%s&page=%s";
    private final TextField searchText;

    private final VBox mainBox;
    private final ComboBox<ComboBoxEntity> comboBox;

    /**
     * 专门用来加载图片的
     */
    private final FailListExecutorService imageLoadService;

    public SearchEvent(TextField searchText, VBox mainBox, ComboBox<ComboBoxEntity> comboBox, Queue<FailTask> queue) {
        this.searchText = searchText;
        this.mainBox = mainBox;
        this.comboBox = comboBox;
        imageLoadService = new FailListExecutorService(queue);

        // 启动下载任务
        AvDownloader instance = AvDownloader.getInstance();
        if (!instance.isStart()) {
            instance.start(new DefaultDownloaderController(), imageLoadService, new DownloadSpeedListener());
        }
    }

    static class SearchService extends Service<SearchPage> {
        private final VBox mainBox;
        private ComboBoxEntity comboBoxEntity;
        private final String text;
        private final Map<Integer, AnchorPane> map = new HashMap<>();
        private final Map<Integer, Service<?>> scheduledServiceMap = new HashMap<>();
        private final FailListExecutorService imageLoadService;

        SearchService(String text, VBox mainBox, ComboBoxEntity comboBoxEntity, FailListExecutorService imageLoadService) {
            this.text = text;
            this.mainBox = mainBox;
            this.comboBoxEntity = comboBoxEntity;
            this.imageLoadService = imageLoadService;
        }

        @Override
        protected Task<SearchPage> createTask() {
            return new SearchTask(text, this, mainBox, map, scheduledServiceMap, comboBoxEntity, imageLoadService);
        }
    }

    static class SearchTask extends Task<SearchPage> {
        private final SearchService searchService;
        private final VBox mainBox;
        private Map<Integer, AnchorPane> map;
        private Map<Integer, Service<?>> scheduledServiceMap;
        private ComboBoxEntity comboBoxEntity;
        //搜索文本
        private final String text;
        private final FailListExecutorService imageLoadService;

        SearchTask(String text, SearchService searchService, VBox mainBox, Map<Integer, AnchorPane> map, Map<Integer,
                Service<?>> scheduledServiceMap, ComboBoxEntity comboBoxEntity, FailListExecutorService imageLoadService) {
            this.text = text;
            this.searchService = searchService;
            this.mainBox = mainBox;
            this.map = map;
            this.scheduledServiceMap = scheduledServiceMap;
            this.comboBoxEntity = comboBoxEntity;
            this.imageLoadService = imageLoadService;
        }

        @Override
        protected SearchPage call() throws Exception {
            // 搜索第一页的
            Object parse = getObject(String.format(comboBoxEntity.getPattern(), text, 1));
            return comboBoxEntity.getSearchPageParser().parse(parse);
        }

        private Object getObject(String url) throws IOException {
            return comboBoxEntity.getParser().parse(url);
        }

        @Override
        protected void failed() {

            ObservableList<Node> children = mainBox.getChildren();
            removeLastComponent(children);
            children.add(new Button("搜索失败 : " + comboBoxEntity.getLabelName()));
        }

        @Override
        protected void updateValue(SearchPage value) {
            super.updateValue(value);
            searchService.cancel();
            ObservableList<Node> children = mainBox.getChildren();
            // 长度为2 说明 下面有显示 ,则移除下面
            removeLastComponent(children);

            List<SearchItem> items = value.getItems();

            // 第一次 ,如果没有数据, 第二次item 就已经为null 了
            if (items != null && items.isEmpty()) {
                mainBox.getChildren().add(new Button("没有数据"));
                return;
            }

            Pagination pagination = new Pagination(value.getPages(), 0);
            pagination.setPrefHeight(400);
            pagination.setMaxPageIndicatorCount(5);
            pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

            pagination.setPageFactory(t -> {

                AnchorPane anchorPane = map.computeIfAbsent(t, t1 -> new AnchorPane());
                final ObservableList<Node> nodes = anchorPane.getChildren();
                if (nodes.isEmpty()) {
                    List<SearchItem> items1 = value.getItems();
                    // 第一次有数据
                    if (items1 != null) {
                        nodes.add(new Button("没有数据"));
                        // 第一此有value
                        MainTableView mainTableView = new MainTableView(imageLoadService);
                        mainTableView.getItems().addAll(items1);
                        nodes.addAll(mainTableView);
                        value.setItems(null);
                    } else {
                        // 之后每页的数据
                        ImageView imageView = new ImageView();
                        imageView.setImage(new Image(ClassLoaderUtil.getClassLoader().
                                getResource("loading.png").toString()));
                        loadData(t, nodes);
                        imageView.setOnMouseClicked(e -> {
                            Service<?> service = scheduledServiceMap.get(t);
                            if (service != null) {
                                if (service.getState() == State.FAILED) {
                                    nodes.clear();
                                    loadData(t, nodes);
                                }
                            }
                        });
                        nodes.add(imageView);
                    }
                }
                return anchorPane;
            });
            children.add(pagination);
        }

        private void removeLastComponent(ObservableList<Node> children) {
            if (children.size() == 2) {
                Node last = children.get(children.size() - 1);
                children.remove(last);
                if (last instanceof Pagination) map.clear();
            }
        }

        private void loadData(Integer t, ObservableList<Node> nodes) {
            Service<List<SearchItem>> service = new Service<List<SearchItem>>() {
                @Override
                protected Task createTask() {
                    return new Task<List<SearchItem>>() {
                        @Override
                        protected List<SearchItem> call() throws Exception {
                            String url = String.format(comboBoxEntity.getPattern(), text, String.valueOf(t + 1));
                            return comboBoxEntity.getSearchPageParser().parse(comboBoxEntity.getParser().parse(url)).getItems();
                        }

                        @Override
                        protected void updateValue(List<SearchItem> value) {
                            super.updateValue(value);

                            nodes.clear();
                            if (CollectionUtil.isEmpty(value)) {
                                nodes.add(new Button("没有数据"));
                            } else {
                                MainTableView mainTableView = new MainTableView(imageLoadService);
                                nodes.add(mainTableView);
                                mainTableView.getItems().
                                        addAll(value);
                            }

                        }

                        /**
                         *
                         * 失败
                         */
                        @Override
                        protected void failed() {
                            // 失败也要取消,否则无线循环
                            nodes.clear();
                            nodes.add(new Button("点击重试"));
                        }
                    };
                }
            };
            service.start();
        }
    }

    @Override
    public void handle(ActionEvent event) {
        ComboBoxEntity comboBoxEntity = getSelectedItem();
        String text = searchText.getText();
        SearchService searchService = new SearchService(text, mainBox, comboBoxEntity, imageLoadService);
        searchService.start();

    }

    private ComboBoxEntity getSelectedItem() {
        ObservableList<ComboBoxEntity> items = comboBox.getItems();
        if (CollectionUtil.isEmpty(items)) throw new RuntimeException();
        SingleSelectionModel<ComboBoxEntity> selectionModel = comboBox.getSelectionModel();
        int i = IntStream.range(0, items.size()).filter(selectionModel::isSelected).findFirst().orElse(0);
        return items.get(i);
    }
}
