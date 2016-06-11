package org.my.ui;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.my.util.DatabaseOperator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/23.
 */
public class FavrtList extends Application {
    private String favrt_list_id = "1";
    private DatabaseOperator dbOperator;
    private GridPane gridPane;
    private LinkedList<CheckBox> checkbox_list;

    public FavrtList(){}
    public FavrtList(String favrt_list_id) throws Exception {
        Stage s = new Stage();
        this.favrt_list_id = favrt_list_id;
        dbOperator = new DatabaseOperator("127.0.0.1",1433,"sa","123456","MusicDataAna");
        checkbox_list = new LinkedList<>();
        start(s);
    }
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Button btn_sure = new Button("确认");
        btn_sure.setOnAction(event -> {
            Event.fireEvent(primaryStage,new WindowEvent(primaryStage,WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        Button btn_remove = new Button("移除");
        btn_remove.setOnAction(event -> {
            for(CheckBox c : checkbox_list){
                if (c.isSelected()){
                    try {
                        removeFromFavrtList(c.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        BorderPane borderPane = new BorderPane();
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        HBox songname_box = new HBox();
        songname_box.setPadding(new Insets(20,0,0,0));
        Label song_name = new Label("歌曲名称");
        songname_box.setAlignment(Pos.TOP_CENTER);
        songname_box.getChildren().add(song_name);
        gridPane.add(songname_box,0,0);
        borderPane.setCenter(gridPane);
        HBox box = new HBox();
        borderPane.setBottom(box);
        box.getChildren().add(btn_sure);
        box.getChildren().add(btn_remove);
        box.setPrefHeight(60);
        box.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(borderPane,300,500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("喜欢的列表");
        primaryStage.show();
        readFavrtList(favrt_list_id);
    }

    private void removeFromFavrtList(String song_name) throws SQLException {
        ResultSet result_song = dbOperator.selectFromTable("select song_id from song where song_name = '"+song_name+"';");
        while (result_song.next()){
            String song_id = result_song.getString(1);
            dbOperator.deleteFromTable("delete from favrt_song_list where song_id = '"+song_id+"';");
        }

    }

    private void readFavrtList(String favrt_list_id) throws SQLException {
        int cow = 1;
        ResultSet result_favrt = dbOperator.selectFromTable("select Song_id from favrt_song_list where favrt_list = "+favrt_list_id+";");
        while(result_favrt.next()){
            String song_id = result_favrt.getString(1);
            ResultSet result_song = dbOperator.selectFromTable("select song_name from song where song_id = "+song_id+";");
            while (result_song.next()){
                String song_name = result_song.getString(1);
                add2Pane(song_name,cow++);
            }
        }
    }

    private void add2Pane(String song_name,int cow){
        CheckBox checkBox = new CheckBox(song_name);
        gridPane.add(checkBox,0,cow);
        checkbox_list.add(checkBox);
    }
    public static void main(String[] args){
        launch(args);
    }
}
