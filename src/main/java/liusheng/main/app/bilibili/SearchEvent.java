package liusheng.main.app.bilibili;

import cn.hutool.core.util.ClassLoaderUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import liusheng.main.app.bilibili.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchEvent implements EventHandler<ActionEvent> {
    public static final String HTTPS_SEARCH_BILIBILI_COM_ALL = "https://search.bilibili.com/all?keyword=%s&page=%s";
    private final TextField searchText;

    private final VBox mainBox;


    public SearchEvent(TextField searchText, VBox mainBox) {
        this.searchText = searchText;
        this.mainBox = mainBox;
    }

    static class SearchService extends ScheduledService<SearchPage> {
        private final VBox mainBox;
        private final String text;
        private final Map<Integer, AnchorPane> map = new HashMap<>();
        private final Map<Integer, ScheduledService<?>> scheduledServiceMap = new HashMap<>();

        SearchService(String text, VBox mainBox) {
            this.text = text;
            this.mainBox = mainBox;
        }

        @Override
        protected Task<SearchPage> createTask() {
            return new SearchTask(text, this, mainBox, map,scheduledServiceMap);
        }
    }

    static class SearchTask extends Task<SearchPage> {
        private final SearchService searchService;
        private final VBox mainBox;
        private Map<Integer, AnchorPane> map;
        private Map<Integer, ScheduledService<?>> scheduledServiceMap;
        private final Gson gson = new Gson();
        private final SearchInfoParser parser = new SearchInfoParser();
        private final String text;

        SearchTask(String text, SearchService searchService, VBox mainBox, Map<Integer, AnchorPane> map, Map<Integer, ScheduledService<?>> scheduledServiceMap) {
            this.text = text;
            this.searchService = searchService;
            this.mainBox = mainBox;
            this.map = map;
            this.scheduledServiceMap = scheduledServiceMap;
        }

        @Override
        protected SearchPage call() throws Exception {


            try {

                Object parse = getObject(String.format(HTTPS_SEARCH_BILIBILI_COM_ALL, text, String.valueOf(1)));

                List<SearchItem> list = getItems(parse);

                Integer pageNumber = Optional.ofNullable(parse)
                        .map(o -> {
                            if (o instanceof SearchJson) {
                                SearchJson json = (SearchJson) o;
                                return Optional.of(json)
                                        .map(SearchJson::getFlow)
                                        .map(SearchJson.FlowBean::getGetMixinFlow)
                                        .map(SearchJson.FlowBean.GetMixinFlow::getExtra)
                                        .map(SearchJson.FlowBean.GetMixinFlow.ExtraBean::getNumPages)
                                        .orElse(1);
                            } else if (o instanceof SearchJson1) {
                                SearchJson1 searchJson1 = (SearchJson1) o;

                                return Optional.of(searchJson1)
                                        .map(SearchJson1::getFlow)
                                        .map(SearchJson1.FlowBean::getGetMixinFlow)
                                        .map(SearchJson1.FlowBean.GetMixinFlow::getExtra)
                                        .map(SearchJson1.FlowBean.GetMixinFlow.ExtraBean::getNumPages).orElse(1);
                            } else {
                                return 1;
                            }
                        }).orElse(1);

                return new SearchPage(list, pageNumber);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        private List<SearchItem> getItems(String url) throws IOException {
            Object parse = getObject(url);
            return getItems(parse);
        }

        private List<SearchItem> getItems(Object parse) throws IOException {

            List<ResultBean> resultBeans = (List<ResultBean>) Optional.ofNullable(parse)
                    .map(o -> {
                        if (o instanceof SearchJson) {
                            SearchJson json = (SearchJson) o;
                            return Optional.of(json)
                                    .map(SearchJson::getFlow)
                                    .map(SearchJson.FlowBean::getGetMixinFlow)
                                    .map(SearchJson.FlowBean.GetMixinFlow::getResult)
                                    .map(list -> {
                                        return list.stream().filter(r -> "video".equalsIgnoreCase(r.getResult_type())).findFirst().orElse(null);
                                    }).map(SearchJson.FlowBean.GetMixinFlow.ResultBean::getData).map(
                                            // 通过TypeToken 或者数据间接转换
                                            m -> gson.fromJson(gson.toJson(m), new TypeToken<List<ResultBean>>() {
                                            }.getType())

                                    ).orElse(Collections.emptyList());
                        } else if (o instanceof SearchJson1) {
                            SearchJson1 searchJson1 = (SearchJson1) o;

                            return Optional.of(searchJson1)
                                    .map(SearchJson1::getFlow)
                                    .map(SearchJson1.FlowBean::getGetMixinFlow)
                                    .map(SearchJson1.FlowBean.GetMixinFlow::getResult).orElse(Collections.<ResultBean>emptyList());
                        } else {
                            return Collections.<ResultBean>emptyList();
                        }
                    }).orElse(Collections.<ResultBean>emptyList());
            return resultBeans.stream().map(r -> {
                return new SearchItem(r.getArcurl(), StringUtils.delelteHtmlTag(r.getTitle()), StringUtils.htmlAbsolutionPath(r.getPic()), r.getAuthor());
            }).collect(Collectors.toList());
        }

        private Object getObject(String url) throws IOException {
            return parser.parse(url);
        }

        @Override
        protected void updateValue(SearchPage value) {
            super.updateValue(value);
            searchService.cancel();
            ObservableList<Node> children = mainBox.getChildren();
            Node node = children.get(children.size() - 1);
            if (node instanceof Pagination) {
                children.remove(node);
                map.clear();
            }
            Pagination pagination = new Pagination(value.getPages(), 0);
            pagination.setPrefHeight(400);
            pagination.setMaxPageIndicatorCount(5);
            pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
            pagination.setPageFactory(t -> {

                AnchorPane anchorPane = map.computeIfAbsent(t, t1 -> new AnchorPane());

                final ObservableList<Node> nodes = anchorPane.getChildren();
                if (nodes.isEmpty()) {

                    List<SearchItem> items = value.getItems();
                    if (items != null) {
                        // 第一此有value
                        MainTableView mainTableView = new MainTableView();
                        mainTableView.getItems().addAll(items);
                        nodes.addAll(mainTableView);
                        value.setItems(null);
                    } else {

                        ImageView imageView = new ImageView();
                        imageView.setImage(new Image(ClassLoaderUtil.getClassLoader().
                                getResource("loading.png").toString()));
                        loadData(t, nodes);
                        imageView.setOnMouseClicked(e->{
                            ScheduledService<?> scheduledService = scheduledServiceMap.get(t);
                            if (scheduledService != null) {
                                if ( scheduledService.getState() == State.FAILED) {
                                    nodes.clear();
                                    loadData(t,nodes);
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

        private void loadData(Integer t, ObservableList<Node> nodes) {
            ScheduledService<List<SearchItem>> service = new ScheduledService<List<SearchItem>>() {
                @Override
                protected Task createTask() {
                    ScheduledService<List<SearchItem>> service1 = this;
                    return new Task<List<SearchItem>>() {
                        @Override
                        protected List<SearchItem> call() throws Exception {

                            List<SearchItem> items1 = getItems(String.format(HTTPS_SEARCH_BILIBILI_COM_ALL, text, String.valueOf(t + 1)));

                            return items1;
                        }

                        @Override
                        protected void updateValue(List<SearchItem> value) {
                            super.updateValue(value);
                            service1.cancel();
                            nodes.clear();
                            MainTableView mainTableView = new MainTableView();
                            nodes.add(mainTableView);
                            mainTableView.getItems().
                                    addAll(value);

                        }
                    };
                }
            };
            scheduledServiceMap.put(t,service);
            service.setDelay(Duration.ZERO);
            service.start();
        }
    }

    @Override
    public void handle(ActionEvent event) {
        String text = searchText.getText();
        SearchService searchService = new SearchService(text, mainBox);
        searchService.start();

    }
}
