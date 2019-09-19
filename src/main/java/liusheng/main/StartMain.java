package liusheng.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import liusheng.main.app.acfun.parser.AcfunSearchInfoParser;
import liusheng.main.app.acfun.parser.AcfunSearchPageParser;
import liusheng.main.app.bilibili.AvDownloader;
import liusheng.main.app.bilibili.executor.ClosableFixedThreadPoolExecutor;
import liusheng.main.app.bilibili.executor.FailListExecutorService;
import liusheng.main.app.bilibili.executor.FailTask;
import liusheng.main.app.bilibili.parser.BilibiliSearchInfoParser;
import liusheng.main.app.bilibili.parser.BilibiliSearchPageParser;
import liusheng.main.userInter.ComboBoxLabel;
import liusheng.main.userInter.SearchEvent;
import liusheng.main.userInter.entity.ComboBoxEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class StartMain extends Application {

    public static final String HTTPS_WWW_ACFUN_CN_REST_PC_DIRECT_SEARCH_VIDEO_KEYWORD = "https://www.acfun.cn/rest/pc-direct/search/video?keyword=%s&pCursor=%s&channelId=0&sortType=1";

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox main = new VBox();
        main.setPrefWidth(600);
        main.setPrefHeight(480);

        HBox searchHbox = new HBox();


        TextField searchText = new TextField();
        searchText.setPrefHeight(40);

        searchText.setPrefWidth(300);
        // 设置 提示关键子
        searchText.setPromptText("请输入关键字");
        searchText.setFocusTraversable(false);

        /**
         * 下拉列表
         */
        ComboBox<ComboBoxEntity> comboBox = new ComboBox<>();
        List<ComboBoxEntity> comboBoxEntities = Arrays.asList(
                new ComboBoxEntity(SearchEvent.HTTPS_SEARCH_BILIBILI_COM_ALL, "Bilibili", new BilibiliSearchInfoParser(), new BilibiliSearchPageParser()),
                new ComboBoxEntity(HTTPS_WWW_ACFUN_CN_REST_PC_DIRECT_SEARCH_VIDEO_KEYWORD, "Acfun", new AcfunSearchInfoParser(),
                        new AcfunSearchPageParser())
        );
        comboBox.setConverter(new StringConverter<ComboBoxEntity>() {
            @Override
            public String toString(ComboBoxEntity object) {
                return object.getLabelName().toString();
            }

            @Override
            public ComboBoxEntity fromString(String string) {
                throw new RuntimeException();
            }
        });
        comboBox.setCellFactory(listView -> {
            return new ListCell<ComboBoxEntity>() {
                @Override
                protected void updateItem(ComboBoxEntity item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        ObservableList<ComboBoxEntity> items = listView.getItems();
                        ComboBoxEntity entity = items.get(getIndex());
                        setGraphic(new ComboBoxLabel(entity));
                    }
                }
            };
        });


        comboBox.setPrefHeight(40);
        comboBox.setPrefWidth(100);
        comboBox.setStyle("-fx-background-color: white");
        comboBox.getItems().addAll(comboBoxEntities);
        // 默认选择第一个 ,要放在后面
        comboBox.getSelectionModel().selectFirst();

        Button searchButton = new Button("搜索");
        searchButton.setPrefWidth(100);
        searchButton.setPrefHeight(20);
        searchButton.setStyle("-fx-background-color: white");
        searchButton.setAlignment(Pos.CENTER);
        searchButton.setFont(Font.font("微软雅黑", 20));


        searchHbox.getChildren().addAll(comboBox, searchText, searchButton);
        searchHbox.setBorder(new Border(new BorderStroke(Paint.valueOf("blue"), BorderStrokeStyle.SOLID
                , new CornerRadii(0), new BorderWidths(2))));

        searchHbox.setPadding(new Insets(10, 0, 10, 0));
        searchHbox.setPrefHeight(80);
        searchHbox.setAlignment(Pos.CENTER);

        HBox.setMargin(comboBox, new Insets(0, 0, 0, 10));
        HBox.setMargin(searchText, new Insets(0, 30, 0, 40));

        VBox mainBox = new VBox();

        mainBox.setStyle("-fx-background-color: blue");

        VBox listMain = new VBox();
        listMain.setStyle("-fx-background-color: orange");


        SearchEvent searchEvent = new SearchEvent(searchText, mainBox, comboBox,new ConcurrentLinkedQueue<>());


        searchButton.setOnAction(searchEvent);


        mainBox.getChildren().addAll(listMain);


        main.getChildren().addAll(searchHbox, mainBox);


        Scene scene = new Scene(main, 600, 480);
        scene.getStylesheets().addAll("css/pagintion.css");
        // 添加快捷键
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            searchEvent.handle(null);
        });

        primaryStage.setOnCloseRequest(v -> {
            Platform.exit();
        });
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        searchText.requestFocus();// 获取焦点 ,要放在最后面
    }


}
