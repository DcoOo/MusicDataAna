package org.my.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.my.util.DatabaseOperator;
import org.my.util.TableItemValues;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/19.
 */
public class MainInteractions extends Application {

    private ChoiceBox searchCont_choice;
    private TextField searchInfo_textfd;
    private Button search_btn;
    private FlowPane top_pane;
    private BorderPane mainPane;
    private HBox top_hbox;
    private LinkedList<CheckBox> checkBox_list;
    private CheckBox selectAll;
    private DatabaseOperator dbOperator;
    private GridPane checkBox_pane;
    private BorderPane leftCont_pane;
    private HBox search_box;

    private Button btn_addFavt;
    private Button btn_delete;
    private Button btn_showFavrt;

    private TextArea lyric_area;
    private TextArea comment_area;

    private FlowPane rightCont_pane;
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
        /**初始化操作数据库的对象 IP 127.0.0.1 端口 1433 用户名 sa 密码 123456 使用的数据库 MusicDataAna
         * */
        dbOperator = new DatabaseOperator("127.0.0.1",1433,"sa","123456","MusicDataAna");
        //top_pane表示输入查询的条件，包括依靠什么查询以及查询的信息
        top_pane = new FlowPane();
        top_hbox = new HBox();
        top_pane.setPrefWidth(1300);
        search_btn = new Button("查询");
        search_btn.setPrefWidth(80);
        searchCont_choice = new ChoiceBox();
        searchCont_choice.setPrefWidth(100);
        searchCont_choice.getItems().addAll("歌曲名称","歌手","专辑名","热度");
        searchCont_choice.setValue("歌曲名称");
        searchInfo_textfd = new TextField("输入要查询的歌曲名称、歌手或者专辑名~");
        searchInfo_textfd.setPrefWidth(300);
        top_hbox.getChildren().addAll(searchCont_choice,searchInfo_textfd,search_btn);
        top_hbox.setAlignment(Pos.CENTER);
        top_hbox.setPrefWidth(top_pane.getPrefWidth());
        top_hbox.setPadding(new Insets(15));
        top_pane.getChildren().add(top_hbox);

        //左边的内容
        leftCont_pane = new BorderPane();
        leftCont_pane.setPrefWidth(900);
        checkBox_list = new LinkedList<>();
        reInitCheckBox_pane();
        search_box = new HBox();
        search_box.setPrefHeight(100);
        search_box.setAlignment(Pos.CENTER_RIGHT);
        selectAll = new CheckBox("全选");
        selectAll.setOnMouseClicked(event -> {
            boolean flag = selectAll.isSelected();
            System.out.println(flag);
            for (CheckBox c : checkBox_list){
                c.setSelected(flag);
            }
        });
        btn_addFavt = new Button("♥添加到喜欢列表");
        btn_addFavt.setOnAction( event -> {
            for (CheckBox c : checkBox_list){
                if (c.isSelected()){
                    //插入到Favrt_song_list
                    ResultSet result_song = dbOperator.selectFromTable("select song_id from song where song_name = '"+c.getText()+"';");
                    try {
                        result_song.next();
                        dbOperator.add2table("Favrt_song_list",new TableItemValues(new String[]{"1",result_song.getString(1)}));

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btn_delete = new Button("删除");
        btn_delete.setOnAction( e->{
            for (CheckBox c : checkBox_list){
                if (c.isSelected()){
                    try {
                        deleteBySongName(c.getText(),new String[]{"favrt_song_list","song_map","comment","song"});
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        btn_showFavrt = new Button("♥查看喜欢列表");
        btn_showFavrt.setOnAction( event -> {
            try {
                new FavrtList("1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        search_box.getChildren().add(selectAll);
        search_box.getChildren().add(btn_addFavt);
        search_box.getChildren().add(btn_showFavrt);
        search_box.getChildren().add(btn_delete);
        leftCont_pane.setBottom(search_box);
        //右边的内容
        rightCont_pane = new FlowPane();
        HBox lyricArea_box = new HBox();
        HBox commentArea_box = new HBox();
        rightCont_pane.setAlignment(Pos.BASELINE_LEFT);
        rightCont_pane.setPrefHeight(600-top_pane.getPrefHeight());
        rightCont_pane.setPrefWidth(1300-rightCont_pane.getPrefWidth());
        lyric_area = new TextArea();
        lyric_area.setPrefHeight(rightCont_pane.getPrefHeight() * 0.4);
        lyric_area.setPrefWidth(rightCont_pane.getPrefWidth() * 0.4);
        comment_area = new TextArea();
        comment_area.setPrefHeight(rightCont_pane.getPrefHeight() * 0.4);
        comment_area.setPrefWidth(rightCont_pane.getPrefWidth() * 0.4);
        rightCont_pane.setOrientation(Orientation.VERTICAL);
        lyricArea_box.getChildren().add(lyric_area);
        lyricArea_box.setAlignment(Pos.CENTER_RIGHT);
        commentArea_box.getChildren().add(comment_area);
        commentArea_box.setAlignment(Pos.CENTER_RIGHT);
        lyricArea_box.setAlignment(Pos.CENTER_RIGHT);
        rightCont_pane.setOrientation(Orientation.VERTICAL);
        rightCont_pane.getChildren().add(lyricArea_box);
        rightCont_pane.getChildren().add(commentArea_box);
        //CenterPane用于显示内容
        BorderPane centerPane = new BorderPane();
        centerPane.setLeft(leftCont_pane);
        centerPane.setCenter(rightCont_pane);
        //BorderPane 是MainPane，包括所有的Pane
        mainPane = new BorderPane();
        mainPane.setTop(top_pane);
        mainPane.setCenter(centerPane);

        Scene scene = new Scene(mainPane,1350,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("音乐数据分析平台");
        primaryStage.setResizable(false);
        primaryStage.show();

        //响应事件
        //点击输入框，输入框默认的文本消失
        searchInfo_textfd.setOnMouseClicked( e ->{
            searchInfo_textfd.setText("");
        });

        search_btn.setOnAction( e ->{
            checkBox_list = new LinkedList<CheckBox>();
            reInitCheckBox_pane();
            String choiceValue = searchCont_choice.getValue().toString();
            String searchValue = searchInfo_textfd.getText();
            if (choiceValue.equals("歌曲名称")){
                searchSongName(searchValue);
            }
            if (choiceValue.equals("歌手")){
                try {
                    searchSingerName(searchValue);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            if (choiceValue.equals("专辑名")){
                try {
                    searchSpecialName(searchValue);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

            if (choiceValue.equals("热度")){
                searchCommentCount(searchValue);
            }
        });
    }

    private void deleteBySongName(String song_name,String...table_name) throws SQLException {
        String song_id;
        ResultSet result_song = dbOperator.selectFromTable("select song_id from song where song_name = '"+song_name+"';");
        while (result_song.next()){
            song_id = result_song.getString(1);
            //删除喜欢列表中的这首歌
            for (String c : table_name){
                dbOperator.deleteFromTable("delete from "+c +" where song_id = "+song_id+";");
            }

        }
    }

    public void searchCommentCount(String commentCount){
        ResultSet result_song;
        String SQLStemt = "SELECT Song_id,S_comt_cot,Song_name FROM song where s_comt_cot >= '"+commentCount+"';";
        String song_name = "";
        result_song = dbOperator.selectFromTable(SQLStemt);
        int cow = 2;
        try {
            while(result_song.next()){
                int song_id = result_song.getInt(1);
                int comment_count = result_song.getInt(2);
                song_name = result_song.getString(3);
                ResultSet result_map = dbOperator.selectFromTable("select singer_id,special_id from song_map where song_id = '"+song_id+"';");
                while (result_map.next()){
                    int singer_id = result_map.getInt(1);
                    int special_id = result_map.getInt(2);
                    ResultSet result_singer = dbOperator.selectFromTable("select singer_name from singer where singer_id = '"+singer_id+"';");
                    ResultSet result_special = dbOperator.selectFromTable("select special_name from special where special_id = '"+special_id+"';");
                    result_singer.next();
                    String singer_name = result_singer.getString(1);
                    result_special.next();
                    String special_name = result_special.getString(1);
                    addCheckBox(cow++,new String[]{song_name,singer_name,special_name,comment_count+""});
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void searchSpecialName(String searchValue) throws SQLException {
        ResultSet result_special = dbOperator.selectFromTable("select special_id from special where special_name = '"+searchValue+"';");
        int cow = 1;
        while (result_special.next()){
            int special_id = result_special.getInt(1);
            ResultSet result_map = dbOperator.selectFromTable("select song_id,singer_id from song_map where special_id = '"+special_id+"';");
            while(result_map.next()){
                int song_id = result_map.getInt(1);
                ResultSet result_song = dbOperator.selectFromTable("select song_name,S_comt_cot from song where song_id = '"+song_id+"';");
                result_song.next();
                String song_name = result_song.getString(1);
                String comment_count = result_song.getString(2);
                int singer_id = result_map.getInt(2);
                ResultSet result_singer = dbOperator.selectFromTable("select singer_name from singer where singer_id = '"+singer_id+"';");
                result_singer.next();
                String singer_name = result_singer.getString(1);
                addCheckBox(cow++,new String[]{song_name,singer_name,searchValue,comment_count});
            }

        }
    }

    public void searchSongName(String searchValue){
        ResultSet result_song;
        String SQLStemt = "SELECT Song_id,S_comt_cot FROM song where song_name = '"+searchValue+"';";
        result_song = dbOperator.selectFromTable(SQLStemt);
        int cow = 2;
        try {
            while(result_song.next()){
                int song_id = result_song.getInt(1);
                int comment_count = result_song.getInt(2);
                ResultSet result_map = dbOperator.selectFromTable("select singer_id,special_id from song_map where song_id = '"+song_id+"';");
                result_map.next();
                int singer_id = result_map.getInt(1);
                int special_id = result_map.getInt(2);
                ResultSet result_singer = dbOperator.selectFromTable("select singer_name from singer where singer_id = '"+singer_id+"';");
                ResultSet result_special = dbOperator.selectFromTable("select special_name from special where special_id = '"+special_id+"';");
                result_singer.next();
                String singer_name = result_singer.getString(1);
                result_special.next();
                String special_name = result_special.getString(1);
                addCheckBox(cow++,new String[]{searchValue,singer_name,special_name,comment_count+""});
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void searchSingerName(String searchValue) throws SQLException {
        ResultSet result_singer = dbOperator.selectFromTable("select singer_id from singer where singer_name = '"+searchValue+"';");
        int cow = 1;
        while (result_singer.next()){
            int singer_id = result_singer.getInt(1);
            ResultSet result_map = dbOperator.selectFromTable("select song_id,special_id from song_map where singer_id = '"+singer_id+"';");
            while(result_map.next()){
                int song_id = result_map.getInt(1);
                ResultSet result_song = dbOperator.selectFromTable("select song_name,S_comt_cot from song where song_id = '"+song_id+"';");
                result_song.next();
                String song_name = result_song.getString(1);
                String comment_count = result_song.getString(2);
                int special_id = result_map.getInt(2);
                ResultSet result_special = dbOperator.selectFromTable("select special_name from special where special_id = '"+special_id+"';");
                result_special.next();
                String special_name = result_special.getString(1);
                addCheckBox(cow++,new String[]{song_name,searchValue,special_name,comment_count});
            }

        }
    }

    public void reInitCheckBox_pane(){
        checkBox_pane = new GridPane();
        checkBox_pane.setAlignment(Pos.CENTER_LEFT);
        ScrollPane scrollPane = new ScrollPane(checkBox_pane);
        checkBox_pane.setPrefHeight(leftCont_pane.getPrefHeight()-200);
        checkBox_pane.setPrefWidth(800);
        checkBox_pane.setAlignment(Pos.TOP_LEFT);
        checkBox_pane.add(new Label("      歌曲名称"),0,0);
        checkBox_pane.add(new Label("      歌手"),1,0);
        checkBox_pane.add(new Label("   所属专辑"),2,0);
        checkBox_pane.add(new Label("   评论"),3,0);
        leftCont_pane.setPadding(new Insets(0,0,0,60));
        leftCont_pane.setCenter(scrollPane);
    }


    public void addCheckBox(int cow,String...values){
        CheckBox song_checkb = new CheckBox(values[0]);
        song_checkb.setPadding(new Insets(0,20,10,0));
        Label singer_lab = new Label(values[1]);
        singer_lab.setPadding(new Insets(0,20,10,0));
        Label special_lab = new Label(values[2]);
        special_lab.setPadding(new Insets(0,20,10,0));
        Label commentCont_lab = new Label(values[3]);
        commentCont_lab.setPadding(new Insets(0,20,10,0));
        Button showLyCom_btn = new Button(" 查看");

        showLyCom_btn.setOnAction(e ->{
            String songName = values[0];
            String lyric_content;
            String comment_userName;
            String comment_content;
            String song_id;
            String comment_area_text = "";
            ResultSet result_song = dbOperator.selectFromTable("select song_id,song_lyric from song where song_name = '"+songName+"';");
            try {
                result_song.next();
                song_id = result_song.getString(1);
                lyric_content = result_song.getString(2);
                lyric_area.setText(lyric_content);
                ResultSet result_comment = dbOperator.selectFromTable("select user_name,comment_cont from comment where song_id = '"+song_id+"';");

                while (result_comment.next()){
                    comment_userName = result_comment.getString(1);
                    comment_content= result_comment.getString(2);
                    comment_area_text += comment_userName +" : " +comment_content+ "\n";
                }
                comment_area.setText(comment_area_text);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        Button changeInfo_btn = new Button("修改");
        changeInfo_btn.setOnAction(event -> {
            String song_name = values[0];
            try {
                new UpdateSongName(song_name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        commentCont_lab.setPadding(new Insets(0,20,10,0));
        checkBox_pane.add(song_checkb,0,cow);
        checkBox_pane.add(singer_lab,1,cow);
        checkBox_pane.add(special_lab,2,cow);
        checkBox_pane.add(commentCont_lab,3,cow);
        checkBox_pane.add(showLyCom_btn,4,cow);
        checkBox_pane.add(changeInfo_btn,5,cow);
        checkBox_list.add(song_checkb);
    }

    public static void main(String[] args){
        launch(args);
    }
}
