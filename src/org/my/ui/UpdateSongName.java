package org.my.ui;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.my.util.DatabaseOperator;

/**
 * Created by Administrator on 2016/5/24.
 */
public class UpdateSongName extends Application {
    private String song_name;
    private DatabaseOperator dbOperator;

    public UpdateSongName(String song_name) throws Exception {
        Stage s = new Stage();
        this.song_name = song_name;
        dbOperator = new DatabaseOperator("127.0.0.1",1433,"sa","123456","MusicDataAna");
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
        Button btn_sure = new Button("修改");
        TextField tf = new TextField();
        FlowPane pane = new FlowPane();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().add(new Label("更改之后的信息:   "));
        pane.getChildren().add(tf);
        pane.getChildren().add(btn_sure);
        Scene scene = new Scene(pane,200,70);
        primaryStage.setScene(scene);
        primaryStage.setTitle("更改信息");
        primaryStage.setResizable(false);
        primaryStage.show();
        btn_sure.setOnAction(event -> {
            String new_songName = tf.getText();
            System.out.println(new_songName);
            dbOperator.updateTable("update song set song_name = '"+new_songName+"' where song_name = '"+song_name+"';");
            Event.fireEvent(primaryStage,new WindowEvent(primaryStage,WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }

    public static void main(String[] args){
        launch(args);
    }
}
