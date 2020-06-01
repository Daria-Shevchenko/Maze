package com.rgb;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**  class for creating music
 *
 */
public class Sound implements AutoCloseable {
    private boolean released = false;
    private AudioInputStream stream = null;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;

    /**  constructor
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

    /**   true - if track was uploaded
     *   false - if it was't
     */
    public boolean isReleased() {
        return released;
    }

    /**  check for playing
     *
     */
    public boolean isPlaying() {
        return playing;
    }


    /**  breakOld check if treck is playing, if not - start play
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

    /**  play sound
     *
     */
    public void play() {
        play(true);
    }

    /**  stop clip
     *
     */
    public void stop() {
        if (playing) {
            clip.stop();
        }
    }

    /**  close clip
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

    /**  set volume(from 1 to 0)
     *
     */
    public void setVolume(float x) {
        if (x<0) x = 0;
        if (x>1) x = 1;
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        volumeControl.setValue((max-min)*x+min);
    }

    /**  getter for volume (from 0 to 1)
     *
     */
    public float getVolume() {
        float v = volumeControl.getValue();
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        return (v-min)/(max-min);
    }

    /** wait for ending of the clip
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

    /** static method for play
     *
     */
    public static Sound playSound(String path) {
        File f = new File(path);
        Sound snd = new Sound(f);
        snd.play();
        return snd;
    }
    /** static method that stop clip
     *
     */
    public static Sound stop(String path) {
        File f = new File(path);
        Sound snd = new Sound(f);
        snd.stop();
        return snd;
    }
    /**  class listener
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