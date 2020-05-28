package com.company;

import javax.swing.*;
import java.awt.*;

public class Hero {

    private int level = 0;

    private double [] heroSpeed = {1.2, 1.1, 1, 1, 0.75, 0.75};

    private double pictureProportionToCell = 0.8;

    private Image [] hero_images_for_levels = {
            new ImageIcon("src/images/hero/gg1.png").getImage(), new ImageIcon("src/images/hero/gg2.png").getImage(),
            new ImageIcon("src/images/hero/gg3.png").getImage(), new ImageIcon("src/images/hero/gg4.png").getImage(),
            new ImageIcon("src/images/hero/gg5.png").getImage(), new ImageIcon("src/images/hero/gg6.png").getImage()};

    private Image heroOriginal;

    private Image heroImage;

    private int heroRadius = 0;

    public int getHeroCenter_x() {
        return heroCenter_x;
    }

    public int getHeroCenter_y() {
        return heroCenter_y;
    }

    private int heroCenter_x= 0;
    private int heroCenter_y = 0;

    public void setHero_x(double hero_x) {
        this.hero_x = hero_x;
    }

    public void setHero_y(double hero_y) {
        this.hero_y = hero_y;
    }

    private double hero_x;
    private double hero_y;
    private double dx=0;
    private double dy=0;
    private int corridorLength = 0;

    Hero(int new_level, int new_corridorLength, int hero_x0, int hero_y0){
        this.level = new_level;
        this.corridorLength = new_corridorLength;
        this.heroOriginal = hero_images_for_levels[this.level-1];
        double length = corridorLength*pictureProportionToCell;
        this.heroImage = heroOriginal.getScaledInstance((int)length, (int)length, Image.SCALE_DEFAULT);
        this.hero_x = hero_x0;
        this.hero_y = hero_y0;
    }

    public double getHero_x() {
        return hero_x;
    }

    public double getHero_y() {
        return hero_y;
    }

    public void changeHero_x() {
        this.hero_x = this.hero_x + this.dx;
    }

    public void changeHero_y() {
        this.hero_y = this.hero_y + this.dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx*this.heroSpeed[this.level-1];
    }

    public double getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy*this.heroSpeed[this.level-1];
    }

    public int getHeroRadius() {
        return heroRadius;
    }

    public void setHeroRadiusAndCenter() {
        this.heroRadius = (int)(corridorLength*pictureProportionToCell/2);
        this.heroCenter_x = (int)(corridorLength*pictureProportionToCell/2);
        this.heroCenter_y = (int)(corridorLength*pictureProportionToCell/2);
    }

    public Image getHeroImage() {
        return heroImage;
    }
}
