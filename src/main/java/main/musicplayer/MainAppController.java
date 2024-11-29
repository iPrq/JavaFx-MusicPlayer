package main.musicplayer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MainAppController {

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
    Media media;
    MediaPlayer mediaPlayer;

    @FXML
    private Slider songVolumeSlider;

    @FXML
    private Pane titlePane;
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

    }

    @FXML
    void prevBtn(MouseEvent event) {

    }

}
