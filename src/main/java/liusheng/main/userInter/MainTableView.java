package liusheng.main.userInter;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import liusheng.main.app.bilibili.AvDownloader;
import liusheng.main.app.bilibili.executor.ClosableFixedThreadPoolExecutor;
import liusheng.main.app.bilibili.executor.FailListExecutorService;
import liusheng.main.app.bilibili.executor.FailTask;
import liusheng.main.app.entity.SearchItem;
import liusheng.main.app.bilibili.donwload.DefaultDownloaderController;
import liusheng.main.app.bilibili.listener.DownloadSpeedListener;

import java.util.concurrent.ExecutorService;

public class MainTableView extends TableView<SearchItem> {

    private final FailListExecutorService imageLoadService;

    public MainTableView(FailListExecutorService imageLoadService) {
        this.imageLoadService = imageLoadService;
        TableView<SearchItem> tableView = this;
        TableColumn<SearchItem, String> indexColumn = new TableColumn<>("索引");
        indexColumn.setPrefWidth(50);
        // 设置滑动监听

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SearchItem>() {
            @Override
            public void changed(ObservableValue<? extends SearchItem> observable, SearchItem oldValue, SearchItem newValue) {
                System.out.println(newValue);
            }
        });
        indexColumn.setCellFactory(c -> {
            return new TableCell<SearchItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        this.setAlignment(Pos.CENTER);
                        setGraphic(new Label(String.valueOf(getIndex() + 1)));
                    }
                }
            };
        });

        TableColumn<SearchItem, String> nameColumn = new TableColumn<>("名字");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        nameColumn.setPrefWidth(150);
        nameColumn.setCellFactory(c -> {
            return new TableCell<SearchItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {

                        this.setAlignment(Pos.CENTER);
                        ObservableList<SearchItem> items = this.getTableView().getItems();
                        String title = items.get(getIndex()).getTitle();
                        Label value = new Label(title);
                        setGraphic(value);
                        Tooltip.install(value, new Tooltip(title));
                    }
                }

            };
        });
        TableColumn<SearchItem, String> imageColumn = new TableColumn<>("图片");
        imageColumn.setPrefWidth(120);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("imgSrc"));
        imageColumn.setCellFactory(c -> {
            return new TableCell<SearchItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {

                        this.setAlignment(Pos.CENTER);
                        ObservableList<SearchItem> items = this.getTableView().getItems();
                        // 用异步加载图片,同步会造成体验不好
                        ImageView imageView = new ImageView();
                        imageView.setFitHeight(100);
                        imageView.setFitWidth(100);
                        // 这可用避免索引溢出
                        setImage(items.get(getIndex()).getImgSrc(), imageView);
                        setGraphic(imageView);
                    }
                }

                // 用异步加载图片
                private void setImage(String imgSrc, ImageView imageView) {
                    // 重试三此加载
                    MainTableView.this.imageLoadService.execute(new FailTask(() -> {
                        try {
                            Image image = new Image(imgSrc);
                            Platform.runLater(() -> {
                              /*  imageView.setOnMouseClicked(e1 -> {
                                    if (Objects.isNull(imageView.getImage())) {
                                        setImage(items);
                                    }
                                });*/
                                imageView.setImage(image);
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));

                }
            };
        });


        TableColumn<SearchItem, String> authorColumn = new TableColumn<>("作者");
        authorColumn.setPrefWidth(120);

        authorColumn.setCellFactory(c -> {
            return new TableCell<SearchItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {

                        this.setAlignment(Pos.CENTER);
                        ObservableList<SearchItem> items = this.getTableView().getItems();


                        setGraphic(new Label(items.get(getIndex()).getAuthor()));
                    }
                }
            };
        });

        TableColumn<SearchItem, String> hrefColumn = new TableColumn<>("下载");
        hrefColumn.setPrefWidth(150);

        hrefColumn.setCellFactory(c -> {
            return new TableCell<SearchItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        this.setAlignment(Pos.CENTER);
                        ObservableList<SearchItem> items = this.getTableView().getItems();
                        DownloadButton button = new DownloadButton("下载", items.get(getIndex()).getHref());

                        button.setOnAction(e -> {
                            try {
                                AvDownloader instance = AvDownloader.getInstance();
                                instance.getWorks().put(button.getAvUrl());
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                        setGraphic(button);
                    }
                }
            };
        });

        tableView.getColumns().addAll(indexColumn, nameColumn, imageColumn, authorColumn, hrefColumn);
        tableView.setPrefWidth(600);
        tableView.setPrefHeight(350);
    }
}
