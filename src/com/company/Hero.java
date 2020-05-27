package com.company;

import javax.swing.*;
import java.awt.*;

public class Hero {

    private int level = 0;

    private double [] heroSpeed = {1.15, 1.05, 0.85, 0.85, 0.75, 0.7};

    private double pictureProportionToCell = 0.8;

    private Image [] hero_images_for_levels = {
            new ImageIcon("src/images/hero/gg1.png").getImage(), new ImageIcon("src/images/hero/gg2.png").getImage(),
            new ImageIcon("src/images/hero/gg3.png").getImage(), new ImageIcon("src/images/hero/gg4.png").getImage(),
            new ImageIcon("src/images/hero/gg5.png").getImage(), new ImageIcon("src/images/hero/gg6.png").getImage()};

    private Image heroOriginal;

    private Image heroImage;

    private double hero_x;
    private double hero_y;
    private double dx=0;
    private double dy=0;

    Hero(int new_level, int corridorLength, int hero_x0, int hero_y0){
        this.level = new_level;
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

    public Image getHeroImage() {
        return heroImage;
    }
}
