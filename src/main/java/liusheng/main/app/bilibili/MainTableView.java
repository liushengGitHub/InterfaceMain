package liusheng.main.app.bilibili;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import liusheng.main.app.bilibili.donwload.DefaultDownloader;
import liusheng.main.app.bilibili.listener.DownloadSpeedListener;

public class MainTableView extends TableView<SearchItem> {

    public MainTableView() {
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

        imageColumn.setCellFactory(c -> {
            return new TableCell<SearchItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {

                        this.setAlignment(Pos.CENTER);
                        ObservableList<SearchItem> items = this.getTableView().getItems();
                        String src = items.get(getIndex()).getImgSrc();
                        ImageView image = new ImageView();
                        try {
                            image.setImage(items.get(getIndex()).getImage()
                            );
                            image.setFitHeight(100);
                            image.setFitWidth(100);
                            //  throw  new RuntimeException();
                        } catch (Exception e) {
                            e.printStackTrace();
                            image.setOnMouseClicked(e1 -> {
                                image.setImage(items.get(getIndex()).getImage());
                            });
                        }

                        setGraphic(image);
                    }
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
                                if (!instance.isStart()) {
                                    instance.start(new DefaultDownloader(), new DownloadSpeedListener());
                                }

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
