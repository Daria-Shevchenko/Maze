package com.rgb;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**  клас для створення звуку
 *
 */
public class Sound implements AutoCloseable {
    private boolean released = false;
    private AudioInputStream stream = null;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;

    /**  конструктор для створення звуку
     *
     */
    public Sound(File f) {
        try {
            stream = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.addLineListener(new Listener());
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            released = true;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
            released = false;

            close();
        }
    }

    /**  повертає true - якщо трек був підвантажений
     *   false - якщо відбулася помилка
     */
    public boolean isReleased() {
        return released;
    }

    /**  перевіряє, чи трек вже запущений
     *
     */
    public boolean isPlaying() {
        return playing;
    }


    /**  breakOld перевіряє чи трек вже був запущений, якщо ні - запускає
     *
     */
    public void play(boolean breakOld) {
        if (released) {
            if (breakOld) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            } else if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
    }

    /**  запуск треку
     *
     */
    public void play() {
        play(true);
    }

    /**  зупиняє трек
     *
     */
    public void stop() {
        if (playing) {
            clip.stop();
        }
    }

    /**  закриває трек
     *
     */
    public void close() {
        if (clip != null)
            clip.close();

        if (stream != null)
            try {
                stream.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
    }

    /**  втановлює гучність(від 0(найтихша) до 1)
     *
     */
    public void setVolume(float x) {
        if (x<0) x = 0;
        if (x>1) x = 1;
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        volumeControl.setValue((max-min)*x+min);
    }

    /**  повертає гучність(від 0 до 1)
     *
     */
    public float getVolume() {
        float v = volumeControl.getValue();
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        return (v-min)/(max-min);
    }

    /** чекає поки трек закінчиться
     *
     */
    public void join() {
        if (!released) return;
        synchronized(clip) {
            try {
                while (playing)
                    clip.wait();
            } catch (InterruptedException exc) {}
        }
    }

    /** статичний метод для запуску музики
     *
     */
    public static Sound playSound(String path) {
        File f = new File(path);
        Sound snd = new Sound(f);
        snd.play();
        return snd;
    }
    /** статичний метод для зупинки музики
     *
     */
    public static Sound stop(String path) {
        File f = new File(path);
        Sound snd = new Sound(f);
        snd.stop();
        return snd;
    }
    /** клас лісенера
     *
     */
    private class Listener implements LineListener {
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                }
            }
        }
    }
}