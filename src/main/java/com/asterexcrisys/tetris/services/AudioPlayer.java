package com.asterexcrisys.tetris.services;

import com.asterexcrisys.tetris.types.AudioState;
import com.asterexcrisys.tetris.types.AudioType;
import com.asterexcrisys.tetris.types.MusicTrackType;
import com.asterexcrisys.tetris.types.SoundEffectType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AudioPlayer {

    private final Map<Enum<?>, MediaPlayer> audios;
    private AudioState state;
    private Enum<?> audio;

    public AudioPlayer(AudioType type) {
        audios = new HashMap<>();
        state = AudioState.IDLE;
        audio = null;
        switch (Objects.requireNonNull(type)) {
            case MUSIC_TRACK -> {
                for (MusicTrackType track : MusicTrackType.values()) {
                    MediaPlayer player = new MediaPlayer(new Media(track.resource()));
                    player.setCycleCount(MediaPlayer.INDEFINITE);
                    audios.put(track, player);
                }
            }
            case SOUND_EFFECT -> {
                for (SoundEffectType effect : SoundEffectType.values()) {
                    MediaPlayer player = new MediaPlayer(new Media(effect.resource()));
                    player.setCycleCount(1);
                    audios.put(effect, player);
                }
            }
        }
    }

    public double getVolume() {
        if (state == AudioState.IDLE) {
            return -1.0;
        }
        if (audio == null) {
            return -1.0;
        }
        return audios.get(audio).getVolume();
    }

    public void setVolume(double volume) {
        if (state == AudioState.IDLE) {
            return;
        }
        if (audio == null) {
            return;
        }
        audios.get(audio).setVolume(volume);
    }

    public void play(Enum<?> audio) {
        if (state != AudioState.IDLE) {
            return;
        }
        if (audio == null || !audios.containsKey(audio)) {
            return;
        }
        audios.get(audio).setOnEndOfMedia(() -> {
            if (audios.get(this.audio).getCycleCount() == MediaPlayer.INDEFINITE) {
                return;
            }
            audios.get(this.audio).setOnEndOfMedia(null);
            state = AudioState.IDLE;
            this.audio = null;
        });
        audios.get(audio).play();
        state = AudioState.PLAYING;
        this.audio = audio;
    }

    public void play() {
        if (state != AudioState.STOPPED) {
            return;
        }
        if (audio == null) {
            return;
        }
        audios.get(audio).play();
        state = AudioState.PLAYING;
    }

    public void pause() {
        if (state != AudioState.PLAYING) {
            return;
        }
        if (audio == null) {
            return;
        }
        audios.get(audio).pause();
        state = AudioState.PAUSED;
    }

    public void resume() {
        if (state != AudioState.PAUSED) {
            return;
        }
        if (audio == null) {
            return;
        }
        audios.get(audio).play();
        state = AudioState.PLAYING;
    }

    public void stop() {
        if (state == AudioState.IDLE || state == AudioState.STOPPED) {
            return;
        }
        if (audio == null) {
            return;
        }
        audios.get(audio).stop();
        state = AudioState.STOPPED;
    }

    public void reset() {
        if (state == AudioState.IDLE) {
            return;
        }
        if (audio == null) {
            return;
        }
        audios.get(audio).stop();
        audios.get(audio).setOnEndOfMedia(null);
        state = AudioState.IDLE;
        audio = null;
    }

    public void dispose() {
        for (MediaPlayer player : audios.values()) {
            player.stop();
            player.setOnEndOfMedia(null);
            player.dispose();
        }
        audios.clear();
        state = AudioState.IDLE;
        audio = null;
    }

}