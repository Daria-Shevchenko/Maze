package com.company;

import java.awt.*;

public class Enemy {
    private int lvl;
    private Toolkit t = Toolkit.getDefaultToolkit();
    private int width,height;
    private Image origin = t.getImage("src/images/other/enemy_s.png"), enemyImg;

    private boolean visible;
    private int x,y;

    private Point start;
    private Point finish;
    private int vel;

    /**
     *  orient = 0  - if don`t move
     *  orient = 1 - move on X
     *  orient = 2  - if move on Y
     */
    private int orient;

    Enemy(){
        this.visible = true;
        this.width = 60;
        this.height = 60;
        this.start = new Point(0,0);
        this.finish = new Point(0,0);
        this.x = start.x;
        this.y = start.y;
        this.vel = 0;
        this.enemyImg = origin.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        this.orient = 0;
        foundOrient();
        System.out.println("enemy created");
    }
    Enemy(int w, int h){
        this.visible = true;
        this.width = w;
        this.height = h;
        this.start = new Point(0,0);
        this.finish = new Point(0,0);
        this.x = start.x;
        this.y = start.y;
        this.vel = 0;
        this.enemyImg = origin.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        foundOrient();
        System.out.println("enemy created");
    }
    Enemy(int w, int h, Point s, Point f, int vel,int lvl){
        this.lvl = lvl;
        this.visible = true;
        this.width = w;
        this.height = h;
        this.start = new Point(s);
        this.finish = new Point(f);
        this.x = start.x;
        this.y = start.y;
        this.vel = vel;
        this.enemyImg = origin.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        foundOrient();
        System.out.println("enemy created");
    }

    public Image getEnemyImg() {
        return enemyImg;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {

        this.start = start;
        this.x = start.x;
        this.y = start.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point getFinish() {
        return finish;
    }

    public void setFinish(Point finish) {
        this.finish = finish;
    }

    public int getVel() {
        return vel;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    private void foundOrient(){
        if(start.x == finish.x && start.y != finish.y){
            this.orient = 2;
        }else if(start.y == finish.y && start.x != finish.x){
            this.orient = 1;
        }else {
            orient = 0;
        }
    }

    public boolean checkCollision(double frameW,double frameH) {
        if (orient == 1){
            if(x  < start.x || x > finish.x )
                return true;
        }
        if (orient == 2){
            if(y  < start.y || y > finish.y )
                return true;
        }
        return false;
    }
    public void move(double frameW,double frameH){
        //System.out.println(checkCollision(frameW,frameH));
        if(checkCollision(frameW,frameH) == true){
            vel = -vel;
        }

        if (orient == 1){
            x+=vel;
        }
        if (orient == 2){
            y+=vel;
        }
    }

    @Override
    public String toString() {
        String res="";
        res+="x,y "+this.x+" "+this.y+"\n";
        res+="orient "+this.orient+"\n";
        res+="show "+this.visible+"\n";
        return res;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }
}
