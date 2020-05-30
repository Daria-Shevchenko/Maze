package com.rgb;

/**
 *
 * Heart - element of the game that is responsible for restoring the player’s lives
 * @author Kate Kolokhina
 */
public class Heart {

    private boolean show;
    private int x,y;
    private int xMap,yMap;

    /**
     * Create a new heart
     */
    Heart(){
        show = true;
    }

    /**
     * Set up x location of heart in the level map
     * @param xMap - x location of heart in the level map
     */
    public void setxMap(int xMap) {
        this.xMap = xMap;
    }

    /**
     * Set up y location of heart in the level map
     * @param yMap - y location of heart in the level map
     */
    public void setyMap(int yMap) {
        this.yMap = yMap;
    }

    /**
     * Set up heart visibility
     * @param show - heart visibility
     */
    public void setShow(boolean show) {
        this.show = show;
    }

    /**
     * Set up x location of heart on screen
     * @param x - x location of heart on screen
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set up y location of heart on screen
     * @param y - y location of heart on screen
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the heart visibility
     * @return A boolean representing heart visibility
     */
    public boolean isShow() {
        return show;
    }

    /**
     * Gets the x location of heart on screen
     * @return A integer representing x location of heart on screen
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y location of heart on screen
     * @return A integer representing y location of heart on screen
     */
    public int getY() {
        return y;
    }


}
