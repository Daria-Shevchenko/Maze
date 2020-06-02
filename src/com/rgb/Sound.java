package com.rgb;

import java.io.File;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.util.Duration;


/**  class to play music
 *
 */
public class Sound {

    /**  clip - MP3 object, plays music
     * */
    private MediaPlayer clip = null;

    /**  constructor
     * @param f - file where MP3 sound located
     */
    public Sound(File f) {
            new javafx.embed.swing.JFXPanel();
            String uriString = new File(f.getPath()).toURI().toString();
            clip = new MediaPlayer(new Media(uriString));
    }

    /**  check for playing
     * @return - returns boolean if clip is playing music now
     */
    public boolean isPlaying() {
        if (clip !=null) {
            if (MediaPlayer.Status.PLAYING.equals(clip.getStatus())) return true;
        }
        return false;
    }

  /**  play sound
   *
   * @param autoPlayMusic - is boolean indicating if music will be played again after finish
     *if true then start again when finished
     *if false then play only 1 time
     */
    public void play(Boolean autoPlayMusic ) {
        if(clip != null) {
            clip.seek(Duration.ZERO);
            clip.play();
            clip.setAutoPlay(autoPlayMusic);
            if (autoPlayMusic){clip.setCycleCount(MediaPlayer.INDEFINITE);}
            else {clip.setCycleCount(0);}
        }
    }

    /**  stop clip
     *
     */
    public void stop() {
        if (clip !=null) {
            if (MediaPlayer.Status.PLAYING.equals(clip.getStatus())) {
                clip.stop();
                clip.setAutoPlay(false);
                clip.setCycleCount(0);
            }
        }
    }

    /**  close clip
     *
     */
    public void close() {
        clip.dispose();
        clip = null;
    }

    /**  set volume(from 1 to 0)
     *@param newVolume - is double from 0 to 1 indicates new level of sound volume to be applied to clip
     */
    public void setVolume(double newVolume) {
        clip.setVolume(newVolume);
    }

    /**  getter for volume (from 0 to 1)
     * @return - returns double from 0 to 1 indicating current level of clip sound volume
     */
    public double getVolume() {
        return clip.getVolume();
    }
}