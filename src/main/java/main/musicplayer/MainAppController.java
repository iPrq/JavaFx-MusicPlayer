package main.musicplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.DirectoryChooser;
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
    private Label directorybtn;

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

        songVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(songVolumeSlider.getValue()*0.01);
            }
        });
        songProgressBar.setStyle("-fx-accent: #000026");
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

        directorybtn.setOnMouseClicked(MouseEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directory = directoryChooser.showDialog(stage);
            if (directory != null) {
                directorybtn.setText(directory.getAbsolutePath());
            }
        });
    }

    @FXML
    void playBtn(MouseEvent event) {
        if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            cancelTimer();
        }
        else {
            mediaPlayer.play();
            beginTimer();
        }
    }

    @FXML
    void prevBtn(MouseEvent event) {
        if(songNumber > 0) {
            if (running) {
                cancelTimer();
            }
            songNumber--;
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songNamelbl.setText(songs.get(songNumber).getName());
            mediaPlayer.play();
            beginTimer();
        }
        else {
            if (running) {
                cancelTimer();
            }
            songNumber = songs.size()-1;
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songNamelbl.setText(songs.get(songNumber).getName());
            mediaPlayer.play();
            beginTimer();
        }
    }

    @FXML
    void nextBtn(MouseEvent event) {

        if(songNumber < songs.size()- 1) {
            if(running) {
                cancelTimer();
            }
            songNumber++;
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songNamelbl.setText(songs.get(songNumber).getName());
            mediaPlayer.play();
            beginTimer();
        }
        else {
            if (running) {
                cancelTimer();
            }
            songNumber = 0;
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songNamelbl.setText(songs.get(songNumber).getName());
            mediaPlayer.play();
            beginTimer();
        }
    }

    void beginTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current/end);
                if (current/end == 1) {
                    System.out.println("True");
                    running = false;
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000,1000);
    }

    void cancelTimer() {
        running = false;
        timer.cancel();
    }
}
