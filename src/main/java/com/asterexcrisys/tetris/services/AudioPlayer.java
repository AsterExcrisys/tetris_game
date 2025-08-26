package com.asterexcrisys.tetris.services;

import com.asterexcrisys.tetris.adapters.MusicPlayer;
import com.asterexcrisys.tetris.adapters.Playable;
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

    // TODO: replace MediaPlayer with AudioClip for sound effects

    private final Map<Enum<?>, Playable> audios;
    private AudioState state;
    private Enum<?> audio;

    public AudioPlayer(AudioType type) {
        audios = new HashMap<>();
        state = AudioState.IDLE;
        audio = null;
        switch (Objects.requireNonNull(type)) {
            case MUSIC_TRACK -> {
                for (MusicTrackType track : MusicTrackType.values()) {
                    MusicPlayer player = new MusicPlayer(new MediaPlayer(new Media(track.resource())));
                    player.setCycleCount(MediaPlayer.INDEFINITE);
                    audios.put(track, player);
                }
            }
            case SOUND_EFFECT -> {
                for (SoundEffectType effect : SoundEffectType.values()) {
                    MusicPlayer player = new MusicPlayer(new MediaPlayer(new Media(effect.resource())));
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

    public void setGlobalVolume(double volume) {
        for (Playable player : audios.values()) {
            player.setVolume(volume);
        }
    }

    public void play(Enum<?> audio) {
        if (state != AudioState.IDLE) {
            return;
        }
        if (audio == null || !audios.containsKey(audio)) {
            return;
        }
        state = AudioState.PLAYING;
        this.audio = audio;
        audios.get(this.audio).setListener(() -> {
            if (state != AudioState.PLAYING || audios.get(this.audio).getCycleCount() == MediaPlayer.INDEFINITE) {
                return;
            }
            state = AudioState.IDLE;
            audios.get(this.audio).stop();
            audios.get(this.audio).setListener(null);
            this.audio = null;
        });
        audios.get(this.audio).play();
    }

    public void play() {
        if (state != AudioState.STOPPED) {
            return;
        }
        if (audio == null) {
            return;
        }
        state = AudioState.PLAYING;
        audios.get(audio).play();
    }

    public void pause() {
        if (state != AudioState.PLAYING) {
            return;
        }
        if (audio == null) {
            return;
        }
        state = AudioState.PAUSED;
        audios.get(audio).pause();
    }

    public void resume() {
        if (state != AudioState.PAUSED) {
            return;
        }
        if (audio == null) {
            return;
        }
        state = AudioState.PLAYING;
        audios.get(audio).play();
    }

    public void stop() {
        if (state == AudioState.IDLE || state == AudioState.STOPPED) {
            return;
        }
        if (audio == null) {
            return;
        }
        state = AudioState.STOPPED;
        audios.get(audio).stop();
    }

    public void reset() {
        if (state == AudioState.IDLE) {
            return;
        }
        if (audio == null) {
            return;
        }
        state = AudioState.IDLE;
        audios.get(audio).stop();
        audios.get(audio).setListener(null);
        audio = null;
    }

    public void dispose() {
        state = AudioState.IDLE;
        audio = null;
        for (Playable player : audios.values()) {
            player.stop();
            player.setListener(null);
            player.dispose();
        }
        audios.clear();
    }

}