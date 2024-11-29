module main.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens main.musicplayer to javafx.fxml;
    exports main.musicplayer;
}