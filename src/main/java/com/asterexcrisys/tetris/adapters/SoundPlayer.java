package com.asterexcrisys.tetris.adapters;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SoundPlayer implements Playable {

    private final AudioClip clip;
    private final ScheduledExecutorService executor;
    private final AtomicReference<ScheduledFuture<?>> future;
    private final AtomicReference<Runnable> listener;
    private final long duration;

    public SoundPlayer(AudioClip clip) {
        this.clip = Objects.requireNonNull(clip);
        executor = Executors.newSingleThreadScheduledExecutor();
        future = new AtomicReference<>(null);
        listener = new AtomicReference<>(null);
        duration = (long) (new Media(this.clip.getSource())).getDuration().toMillis();
    }

    @Override
    public int getCycleCount() {
        return clip.getCycleCount();
    }

    @Override
    public void setCycleCount(int cycleCount) {
        clip.setCycleCount(cycleCount);
    }

    @Override
    public double getVolume() {
        return clip.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        clip.setVolume(volume);
    }

    @Override
    public Runnable getListener() {
        return listener.get();
    }

    @Override
    public void setListener(Runnable listener) {
        this.listener.set(listener);
    }

    @Override
    public void play() {
        if (future.get() != null) {
            return;
        }
        clip.play();
        future.set(executor.schedule(this::listen, duration, TimeUnit.MILLISECONDS));
    }

    @Override
    public void pause() {
        if (future.get() == null) {
            return;
        }
        clip.stop();
        future.get().cancel(true);
    }

    @Override
    public void resume() {
        if (future.get() != null) {
            return;
        }
        clip.play();
        future.set(executor.schedule(this::listen, duration, TimeUnit.MILLISECONDS));
    }

    @Override
    public void stop() {
        if (future.get() == null) {
            return;
        }
        clip.stop();
        future.get().cancel(true);
    }

    @Override
    public void dispose() {
        clip.stop();
        executor.shutdown();
        synchronized (future) {
            if (future.get() != null) {
                future.get().cancel(true);
                future.set(null);
            }
        }
        listener.set(null);
    }

    @Override
    public void close() {
        dispose();
    }

    private void listen() {
        synchronized (listener) {
            if (listener.get() != null) {
                listener.get().run();
            }
        }
        future.set(null);
    }

}