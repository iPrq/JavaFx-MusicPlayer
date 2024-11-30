package main.musicplayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MainAppController implements Initializable {

    @FXML
    private ImageView close;

    @FXML
    private ImageView minimise;

    @FXML
    private ImageView songIcon;

    @FXML
    private Label songNamelbl;

    @FXML
    private ProgressBar songProgressBar;
    private double x,y;
    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private int songNumber= 0;
    private Timer timer;
    private TimerTask timerTask;
    private boolean running;
    Media media;
    MediaPlayer mediaPlayer;

    @FXML
    private Slider songVolumeSlider;

    @FXML
    private Pane titlePane;
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<File>();
        directory = new File("src/main/resources/music");
        files = directory.listFiles();
        if (files != null) {
            songs.addAll(Arrays.asList(files));
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songNamelbl.setText(songs.get(songNumber).getName());
    }

    public void init(Stage stage) {
        titlePane.setOnMousePressed(MouseEvent -> {
                x = MouseEvent.getSceneX();
                y = MouseEvent.getSceneY();
        });
        titlePane.setOnMouseDragged(MouseEvent -> {
            stage.setX(MouseEvent.getScreenX()-x);
            stage.setY(MouseEvent.getScreenY()-y);
        });
        close.setOnMouseClicked(MouseEvent -> stage.close());
        minimise.setOnMouseClicked(MouseEvent -> stage.setIconified(true));
    }

    @FXML
    void playBtn(MouseEvent event) {
        if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
        else {
            mediaPlayer.play();
        }
    }

    @FXML
    void prevBtn(MouseEvent event) {
    }

    @FXML
    void nextBtn(MouseEvent event) {

    }

    void beginTimer() {

    }

    void cancelTimer() {

    }

}
