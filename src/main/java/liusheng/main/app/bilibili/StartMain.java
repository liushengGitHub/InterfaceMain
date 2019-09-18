package liusheng.main.app.bilibili;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartMain extends Application {
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

        Label searchLabel = new Label("搜索");
        searchLabel.setPrefHeight(40);
        searchLabel.setPrefWidth(80);
        searchLabel.setStyle("-fx-background-color: white");
        searchLabel.setAlignment(Pos.CENTER);
        searchLabel.setFont(Font.font("微软雅黑", 25));

        Button searchButton = new Button("搜索");
        searchButton.setPrefWidth(100);
        searchButton.setPrefHeight(20);
        searchButton.setStyle("-fx-background-color: white");
        searchButton.setAlignment(Pos.CENTER);
        searchButton.setFont(Font.font("微软雅黑", 20));


        searchHbox.getChildren().addAll(searchLabel, searchText, searchButton);
        searchHbox.setStyle("-fx-background-color: red");
        searchHbox.setPadding(new Insets(10, 0, 10, 0));
        searchHbox.setPrefHeight(80);
        searchHbox.setAlignment(Pos.CENTER);

        HBox.setMargin(searchLabel, new Insets(0, 0, 0, 30));
        HBox.setMargin(searchText, new Insets(0, 30, 0, 40));

        VBox mainBox = new VBox();

        mainBox.setStyle("-fx-background-color: blue");

        VBox listMain = new VBox();
        listMain.setStyle("-fx-background-color: orange");


        SearchEvent searchEvent = new SearchEvent(searchText, mainBox);


        searchButton.setOnAction(searchEvent);


        mainBox.getChildren().addAll(listMain);


        main.getChildren().addAll(searchHbox, mainBox);


        Scene scene = new Scene(main, 600, 480);
        scene.getStylesheets().addAll("css/pagintion.css");
        // 添加快捷键
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            searchEvent.handle(null);
        });

        primaryStage.setOnCloseRequest(v->{
            Platform.exit();
        });
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        searchText.requestFocus();// 获取焦点 ,要放在最后面
    }


}
