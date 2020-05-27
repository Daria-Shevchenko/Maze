package com.company;

import javax.swing.*;
import java.awt.*;

public class Hero {

    private int level = 0;

    private double pictureProportionToCell = 0.8;

    private Image [] hero_images_for_levels = {
            new ImageIcon("src/images/hero/gg1.png").getImage(), new ImageIcon("src/images/hero/gg2.png").getImage(),
            new ImageIcon("src/images/hero/gg3.png").getImage(), new ImageIcon("src/images/hero/gg4.png").getImage(),
            new ImageIcon("src/images/hero/gg5.png").getImage(), new ImageIcon("src/images/hero/gg6.png").getImage()};

    private Image heroOriginal;

    private Image heroImage;

    private int hero_x;
    private int hero_y;
    private int dx=0;
    private int dy=0;

    Hero(int new_level, int corridorLength, int hero_x0, int hero_y0){
        this.level = new_level;
        this.heroOriginal = hero_images_for_levels[this.level-1];
        double length = corridorLength*pictureProportionToCell;
        this.heroImage = heroOriginal.getScaledInstance((int)length, (int)length, Image.SCALE_DEFAULT);
        this.hero_x = hero_x0;
        this.hero_y = hero_y0;
    }

    public int getHero_x() {
        return hero_x;
    }

    public int getHero_y() {
        return hero_y;
    }

    public void changeHero_x() {
        this.hero_x = this.hero_x + this.dx;
    }

    public void changeHero_y() {
        this.hero_y = this.hero_y + this.dy;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public Image getHeroImage() {
        return heroImage;
    }
}
