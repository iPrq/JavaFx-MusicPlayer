package main.musicplayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.Timer;

public class MainAppController implements Initializable{
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
    private ImageView pauseIcon;

    @FXML
    private Slider songVolumeSlider;
    @FXML
    private ImageView playIcon;

    @FXML
    private Label directorybtn;

    @FXML
    private Pane titlePane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pauseIcon.setVisible(false);
        pauseIcon.setManaged(false);
    }

    public void songInit() {
        try {
            songs = new ArrayList<File>();
            files = directory.listFiles();
            if (files != null) {
                songs.addAll(Arrays.asList(files));
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songNamelbl.setText(songs.get(songNumber).getName());
            setIcon(songs.get(songNumber));

            songVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    mediaPlayer.setVolume(songVolumeSlider.getValue() * 0.01);
                }
            });
            songProgressBar.setStyle("-fx-accent: #000026");

        } catch (Exception e) {
            MainApplication.showerror("Invalid Directory","Please enter a valid Directory with only songs");
        }
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
            directoryChooser.setTitle("Choose the Directory for Songs");
            directory = directoryChooser.showDialog(stage);
            if (directory != null) {
                directorybtn.setText(directory.getAbsolutePath());
                songInit();
            }
        });
    }

    @FXML
    void playBtn(MouseEvent event) {
        if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            cancelTimer();
            playIcon.setManaged(true);
            playIcon.setVisible(true);
            pauseIcon.setVisible(false);
            pauseIcon.setManaged(false);

        }
        else {
            mediaPlayer.play();
            beginTimer();
            playIcon.setManaged(false);
            playIcon.setVisible(false);
            pauseIcon.setManaged(true);
            pauseIcon.setVisible(true);
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
            setIcon(songs.get(songNumber));
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
            setIcon(songs.get(songNumber));
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
            setIcon(songs.get(songNumber));
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
            setIcon(songs.get(songNumber));
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
    void setIcon(File file) {
        Image songimage = getSongIcon(file);
        if (songimage != null) {
            songIcon.setImage(songimage);
        }
    }

    private javafx.scene.image.Image getSongIcon(File file) {
        FileSystemView fileview = FileSystemView.getFileSystemView();
        Icon icon = fileview.getSystemIcon(file);
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2d = bufferedImage.createGraphics();
        icon.paintIcon(null, g2d, 0, 0); g2d.dispose();
        g2d.dispose();
        return SwingFXUtils.toFXImage(bufferedImage,null);
    }
}
