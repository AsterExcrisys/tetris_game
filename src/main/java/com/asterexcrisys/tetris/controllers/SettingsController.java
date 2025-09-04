package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.GlobalSettings;
import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.types.AudioState;
import com.asterexcrisys.tetris.types.MusicTrackType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SettingsController {

    @FXML
    private ComboBox<String> musicTrackChoice;

    @FXML
    private CheckBox useFixedColors;

    @FXML
    private Slider musicVolume;

    @FXML
    private Button musicVolumeTestButton;

    @FXML
    private Slider soundVolume;

    @FXML
    private Button soundVolumeTestButton;

    private static final Logger LOGGER = Logger.getLogger(SettingsController.class.getName());

    private final GlobalSettings settings;
    private final MediaPlayer player;
    private Stage stage;
    private AudioState musicState;
    private AudioState soundState;

    public SettingsController() {
        settings = GlobalSettings.getInstance();
        player = new MediaPlayer(new Media(
                Objects.requireNonNull(MainApplication.class.getResource(ResourceConstants.TEST_AUDIO)).toExternalForm()
        ));
        stage = null;
        musicState = AudioState.IDLE;
        soundState = AudioState.IDLE;
        player.setCycleCount(1);
    }

    public void setStage(Stage stage) {
        this.stage = Objects.requireNonNull(stage);
    }

    @FXML
    private void initialize() {
        musicTrackChoice.getItems().setAll(Arrays.stream(MusicTrackType.values()).map(MusicTrackType::name).toList());
        musicTrackChoice.getSelectionModel().select(settings.getMusicTrack().name());
        useFixedColors.setSelected(settings.getUseFixedColors());
        musicVolume.setMin(0.0);
        musicVolume.setMax(1.0);
        musicVolume.setValue(settings.getMusicVolume());
        soundVolume.setMin(0.0);
        soundVolume.setMax(1.0);
        soundVolume.setValue(settings.getSoundVolume());
    }

    @FXML
    private void onMusicVolumeTestButtonClick() {
        if (soundState != AudioState.IDLE) {
            return;
        }
        if (musicState != AudioState.IDLE) {
            if (musicState == AudioState.DISPOSED) {
                return;
            }
            player.stop();
            player.setOnEndOfMedia(null);
            musicState = AudioState.IDLE;
            musicVolumeTestButton.setText("Test");
            return;
        }
        player.setVolume(musicVolume.getValue());
        player.setOnEndOfMedia(() -> {
            if (musicState != AudioState.PLAYING) {
                return;
            }
            player.stop();
            player.setOnEndOfMedia(null);
            musicState = AudioState.IDLE;
            musicVolumeTestButton.setText("Test");
        });
        player.play();
        musicState = AudioState.PLAYING;
        musicVolumeTestButton.setText("Stop");
    }

    @FXML
    private void onSoundVolumeTestButtonClick() {
        if (musicState != AudioState.IDLE) {
            return;
        }
        if (soundState != AudioState.IDLE) {
            if (soundState == AudioState.DISPOSED) {
                return;
            }
            player.stop();
            player.setOnEndOfMedia(null);
            soundState = AudioState.IDLE;
            soundVolumeTestButton.setText("Test");
            return;
        }
        player.setVolume(musicVolume.getValue());
        player.setOnEndOfMedia(() -> {
            if (soundState != AudioState.PLAYING) {
                return;
            }
            player.stop();
            player.setOnEndOfMedia(null);
            soundState = AudioState.IDLE;
            soundVolumeTestButton.setText("Test");
        });
        player.play();
        soundState = AudioState.PLAYING;
        soundVolumeTestButton.setText("Stop");
    }

    @FXML
    private void onSaveButtonClick() {
        if ((musicState != AudioState.IDLE && musicState != AudioState.DISPOSED) || (soundState != AudioState.IDLE && soundState != AudioState.DISPOSED)) {
            player.stop();
            player.setOnEndOfMedia(null);
            musicState = AudioState.IDLE;
            soundState = AudioState.IDLE;
            musicVolumeTestButton.setText("Test");
            soundVolumeTestButton.setText("Test");
        }
        settings.setMusicTrack(MusicTrackType.valueOf(musicTrackChoice.getSelectionModel().getSelectedItem()));
        settings.setUseFixedColors(useFixedColors.isSelected());
        settings.setMusicVolume(musicVolume.getValue());
        settings.setSoundVolume(soundVolume.getValue());
        settings.save();
    }

    @FXML
    private void onBackButtonClick() {
        if ((musicState != AudioState.IDLE && musicState != AudioState.DISPOSED) || (soundState != AudioState.IDLE && soundState != AudioState.DISPOSED)) {
            player.stop();
            player.setOnEndOfMedia(null);
            player.dispose();
            musicState = AudioState.DISPOSED;
            soundState = AudioState.DISPOSED;
            musicVolumeTestButton.setText("Test");
            soundVolumeTestButton.setText("Test");
        }
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.GAME_VIEW));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 800);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, exception.getMessage(), exception);
            return;
        }
        GameController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
    }

}