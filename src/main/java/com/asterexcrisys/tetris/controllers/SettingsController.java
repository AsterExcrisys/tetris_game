package com.asterexcrisys.tetris.controllers;

import com.asterexcrisys.tetris.GlobalSettings;
import com.asterexcrisys.tetris.MainApplication;
import com.asterexcrisys.tetris.constants.ResourceConstants;
import com.asterexcrisys.tetris.types.AudioState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public final class SettingsController {

    @FXML
    private Slider musicVolume;

    @FXML
    private Button musicVolumeTestButton;

    @FXML
    private Slider soundVolume;

    @FXML
    private Button soundVolumeTestButton;

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
            musicState = AudioState.IDLE;
            player.stop();
            player.setOnEndOfMedia(null);
            musicVolumeTestButton.setText("Test");
            return;
        }
        musicState = AudioState.PLAYING;
        player.setVolume(musicVolume.getValue());
        player.setOnEndOfMedia(() -> {
            if (musicState != AudioState.PLAYING) {
                return;
            }
            musicState = AudioState.IDLE;
            player.stop();
            player.setOnEndOfMedia(null);
            musicVolumeTestButton.setText("Test");
        });
        player.play();
        musicVolumeTestButton.setText("Stop");
    }

    @FXML
    private void onSoundVolumeTestButtonClick() {
        if (musicState != AudioState.IDLE) {
            return;
        }
        if (soundState != AudioState.IDLE) {
            soundState = AudioState.IDLE;
            player.stop();
            player.setOnEndOfMedia(null);
            soundVolumeTestButton.setText("Test");
            return;
        }
        soundState = AudioState.PLAYING;
        player.setVolume(musicVolume.getValue());
        player.setOnEndOfMedia(() -> {
            if (soundState != AudioState.PLAYING) {
                return;
            }
            soundState = AudioState.IDLE;
            player.stop();
            player.setOnEndOfMedia(null);
            soundVolumeTestButton.setText("Test");
        });
        player.play();
        soundVolumeTestButton.setText("Stop");
    }

    @FXML
    private void onSaveButtonClick() {
        if (musicState != AudioState.IDLE || soundState != AudioState.IDLE) {
            musicState = AudioState.IDLE;
            soundState = AudioState.IDLE;
            player.stop();
            player.setOnEndOfMedia(null);
            musicVolumeTestButton.setText("Test");
            soundVolumeTestButton.setText("Test");
        }
        settings.setMusicVolume(musicVolume.getValue());
        settings.setSoundVolume(soundVolume.getValue());
    }

    @FXML
    private void onBackButtonClick() {
        if (musicState != AudioState.IDLE || soundState != AudioState.IDLE) {
            musicState = AudioState.IDLE;
            soundState = AudioState.IDLE;
            player.stop();
            player.setOnEndOfMedia(null);
            musicVolumeTestButton.setText("Test");
            soundVolumeTestButton.setText("Test");
        }
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(ResourceConstants.GAME_VIEW));
        Scene scene;
        try {
            scene = new Scene(loader.load(), 600, 800);
        } catch (IOException ignored) {
            return;
        }
        GameController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
    }

}