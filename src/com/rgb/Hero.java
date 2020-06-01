package com.rgb;

import javax.swing.*;
import java.awt.*;
/**
 *
 * Hero - element of the game that is responsible for hero on game screen
 * @author Khrystyna Boiko
 */
public class Hero {
    /**level is level of the game*/
    private int level = 0;
    /**heroSpeed is an array of hero speeds on different levels*/
    private double [] heroSpeed = {1, 1.2, 1, 1, 0.9, 1.2};
    /**minSpeed is minimum speed for the level*/
    private double minSpeed;
    /**pictureProportionToCell is proportion of picture length to cell length*/
    private double pictureProportionToCell = 0.8;
    /**hero_images_for_levels is an array of hero images on different levels*/
    private Image [] hero_images_for_levels = {
            new ImageIcon("src/images/hero/gg1.png").getImage(), new ImageIcon("src/images/hero/gg2.png").getImage(),
            new ImageIcon("src/images/hero/gg3.png").getImage(), new ImageIcon("src/images/hero/gg4.png").getImage(),
            new ImageIcon("src/images/hero/gg5.png").getImage(), new ImageIcon("src/images/hero/gg6.png").getImage()};
    /**heroOriginal is an original image of hero on the level*/
    private Image heroOriginal;
    /**heroImage is scaled image of hero getting from heroOriginal*/
    private Image heroImage;
    /**heroRadius is radius of hero image*/
    private int heroRadius = 0;
    /**heroCenter_x is half length of the hero image on X in pixels*/
    private int heroCenter_x= 0;
    /**heroCenter_y is half length of the hero image on Y in pixels*/
    private int heroCenter_y = 0;
    /**hero_x is X coordinate of the hero*/
    private double hero_x;
    /**hero_y is Y coordinate of the hero*/
    private double hero_y;
    /**dx is displacement of hero on X*/
    private double dx=0;
    /**dy is displacement of hero on Y*/
    private double dy=0;
    /**corridorLength is length of the cell (corridor) in maze*/
    private int corridorLength = 0;
    /**Create a hero
     * @param new_level - level of the game
     * @param new_corridorLength - length of corridor in a maze
     * @param hero_x0 - X coordinate of the hero
     * @param hero_y0 - Y coordinate of the hero
     * */
    Hero(int new_level, int new_corridorLength, int hero_x0, int hero_y0){
        this.level = new_level;
        this.minSpeed =heroSpeed[level-1];
        this.corridorLength = new_corridorLength;
        this.heroOriginal = hero_images_for_levels[this.level-1];
        double length = corridorLength*pictureProportionToCell;
        this.heroImage = heroOriginal.getScaledInstance((int)length, (int)length, Image.SCALE_DEFAULT);
        this.hero_x = hero_x0;
        this.hero_y = hero_y0;
    }
    /**Set X coordinate for hero
     * @param hero_x - X coordinate of hero*/
    public void setHero_x(double hero_x) {
        this.hero_x = hero_x;
    }
    /**Set Y coordinate for hero
     * @param hero_y - Y coordinate of hero*/
    public void setHero_y(double hero_y) {
        this.hero_y = hero_y;
    }
    /**Gets X coordinate of the hero
     * @return An integer representing X coordinate of the hero*/
    public double getHero_x() {
        return hero_x;
    }
    /**Gets Y coordinate of the hero
     * @return An integer representing Y coordinate of the hero*/
    public double getHero_y() {
        return hero_y;
    }
    /**Change X coordinate of the hero by displacement of hero on X*/
    public void changeHero_x() {
        this.hero_x = this.hero_x + this.dx;
    }
    /**Change Y coordinate of the hero by displacement of hero on Y*/
    public void changeHero_y() {
        this.hero_y = this.hero_y + this.dy;
    }
    /**Set displacement of hero on X
     * @param dx - displacement of hero on X*/
    public void setDx(int dx) {
        this.dx = dx*this.heroSpeed[this.level-1];
    }
    /**Set displacement of hero on Y
     * @param dy - displacement of hero on Y*/
    public void setDy(int dy) {
        this.dy = dy*this.heroSpeed[this.level-1];
    }
    /**Gets displacement of hero on X
     * @return An integer representing a displacement of hero on X*/
    public double getDx() {
        return dx;
    }
    /**Gets displacement of hero on Y
     * @return An integer representing a displacement of hero on Y*/
    public double getDy() {
        return dy;
    }
    /**Set parameters heroRadius, heroCenter_x, heroCenter_y*/
    public void setHeroRadiusAndCenter() {
        this.heroRadius = (int)(corridorLength*pictureProportionToCell/2);
        this.heroCenter_x = (int)(corridorLength*pictureProportionToCell/2);
        this.heroCenter_y = (int)(corridorLength*pictureProportionToCell/2);
    }
    /**Gets a radius of hero image
     * @return An integer representing a radius of hero image*/
    public int getHeroRadius() {
        return heroRadius;
    }
    /**Gets center of the hero - half of the hero image on X
     * @return An integer representing a half length of the hero image on X in pixels*/
    public int getHeroCenter_x() {
        return heroCenter_x;
    }
    /**Gets center of the hero - half of the hero image on Y
     * @return An integer representing a half length of the hero image on Y in pixels*/
    public int getHeroCenter_y() {
        return heroCenter_y;
    }
    /**Gets a hero image
     * @return An image representing a hero image on the level*/
    public Image getHeroImage() {
        return heroImage;
    }
    /**Set hero speed
    @param new_heroSpeed A new speed for hero
    @param level A level of the game */
    public void setHeroSpeed(double new_heroSpeed, int level) {
        if(new_heroSpeed<=this.minSpeed+0.5 && new_heroSpeed>=this.minSpeed)
            this.heroSpeed [level] = new_heroSpeed;
    }
    /**Gets hero speed
    @param level A level of the game
    @return An integer representing a hero speed on the level*/
    public double getHeroSpeed(int level){
        return this.heroSpeed[level];
    }

}
