package com.rgb;

import java.awt.*;


/**
 * Enemy - element of the game that is responsible for enemy on game screen
 * @author Kate Kolokhina
 */
public class Enemy {

    private Toolkit t = Toolkit.getDefaultToolkit();
    private Image origin = t.getImage("src/images/other/enemy.png"), enemyImg;

    private int lvl;
    private int width,height;
    private double x,y;
    private double vel;

    private boolean visible;

    private Point start;
    private Point finish;

    private int enemyRadius = 0;
    private int enemyCenter_x = 0;
    private int enemyCenter_y = 0;

    /**
     *  Orientation of moving of enemy
     *  orient = 0  - if don`t move
     *  orient = 1 - move on X
     *  orient = 2  - if move on Y
     */
    private int orient;

    /**
     * Create a basic enemy, that can`t move
     */
    Enemy(){
        this.visible = false;
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
        //   System.out.println("enemy created");
    }

    /**
     * Create a enemy, that can`t move
     * @param w - width of enemy
     * @param h - height of enemy
     */
    Enemy(int w, int h){
        this.visible = false;
        this.width = w;
        this.height = h;
        this.start = new Point(0,0);
        this.finish = new Point(0,0);
        this.x = start.x;
        this.y = start.y;
        this.vel = 0;
        this.enemyImg = origin.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        foundOrient();
        //   System.out.println("enemy created");
    }

    /**
     * Create a enemy
     * @param w - width of enemy
     * @param h - height of enemy
     * @param s - coordinates of the starting point of the enemy trajectory
     * @param f - coordinates of the end point of the enemy trajectory
     * @param vel - enemy movement speed
     * @param lvl - level at which this enemy is
     */
    Enemy(int w, int h, Point s, Point f, double vel,int lvl){
        this.lvl = lvl;
        this.visible = false;
        this.width = w;
        this.height = h;
        this.start = new Point(s);
        this.finish = new Point(f);
        this.x = start.x;
        this.y = start.y;
        this.vel = vel;
        this.enemyImg = origin.getScaledInstance(width,height,Image.SCALE_DEFAULT);
        foundOrient();
        //  System.out.println("enemy created");
    }

    /**
     * Gets enemy width
     * @return An integer representing the enemy width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set up enemy width
     * @param width - enemy width
     */
    public void setWidth(int width) {
        this.width = width;
        this.enemyImg = origin.getScaledInstance(width,height,Image.SCALE_DEFAULT);
    }

    /**
     * Gets enemy height
     * @return An integer representing enemy height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set up enemy height
     * @param height - enemy height
     */
    public void setHeight(int height) {
        this.height = height;
        this.enemyImg = origin.getScaledInstance(width,height,Image.SCALE_DEFAULT);
    }

    /**
     * Gets enemy radius
     * @return An integer representing enemy radius
     */
    public int getEnemyRadius() {
        return enemyRadius;
    }

    /**
     * Calculates enemy radius and coordinates of enemy center
     */
    public void setEnemyRadiusAndCenter(){
        this.enemyRadius = (int)((this.width+this.height)/4);
        this.enemyCenter_x = this.width/2;
        this.enemyCenter_y = this.height/2;
    }

    /**
     * Gets the x-coordinate of the enemy’s center
     * @return An integer representing x-coordinate of the enemy’s center
     */
    public int getEnemyCenter_x() {
        return enemyCenter_x;
    }

    /**
     * Gets the y-coordinate of the enemy’s center
     * @return An integer representing y-coordinate of the enemy’s center
     */
    public int getEnemyCenter_y() {
        return enemyCenter_y;
    }

    /**
     * Gets enemy image
     * @return enemy image
     */
    public Image getEnemyImg() {
        return enemyImg;
    }

    /**
     * Gets enemy visibility
     * @return A boolean representing enemy visibility
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set up enemy visibility
     * @param visible - enemy visibility
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets coordinates of the starting point of the enemy trajectory
     * @return A point representing coordinates starting point of the enemy trajectory
     */
    public Point getStart() {
        return start;
    }

    /**
     * Set up coordinates of the starting point of the enemy trajectory
     * @param start - A point representing coordinates starting point of the enemy trajectory
     */
    public void setStart(Point start) {

        this.start = start;
        this.x = start.x;
        this.y = start.y;
    }

    /**
     * Gets the x location of enemy on screen
     * @return An integer representing x location of enemy on screen
     */
    public double getX() {
        return x;
    }

    /**
     * Set up the x location of enemy on screen
     * @param x - x location of enemy on screen
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y location of enemy on screen
     * @return An integer representing y location of enemy on screen
     */
    public double getY() {
        return y;
    }

    /**
     * Set up the y location of enemy on screen
     * @param y - y location of enemy on screen
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets coordinates of the end point of the enemy trajectory
     * @return A point representing coordinates of the end point of the enemy trajectory
     */
    public Point getFinish() {
        return finish;
    }

    /**
     * Set up coordinates of the end point of the enemy trajectory
     * @param finish - A point representing coordinates of the end point of the enemy trajectory
     */
    public void setFinish(Point finish) {
        this.finish = finish;
    }

    /**
     * Gets enemy movement speed
     * @return A double representing enemy movement speed
     */
    public double getVel() {
        return vel;
    }

    /**
     * Set up enemy movement speed
     * @param vel - enemy movement speed
     */
    public void setVel(double vel) {
        this.vel = vel;
    }

    /**
     * Gets level at which this enemy is
     * @return An integer representing level at which this enemy is
     */
    public int getLvl() {
        return lvl;
    }

    /**
     * Set up level at which this enemy is
     * @param lvl - level at which this enemy is
     */
    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    /**
     * This method check the collision between enemy and hero or game world
     * @param frameW - width of frame
     * @param frameH - height of frame
     * @return
     */
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

    /**
     * This method move the enemy on screen
     * @param frameW - width of frame
     * @param frameH - height of frame
     */
    public void move(double frameW,double frameH){
        //System.out.println(checkCollision(frameW,frameH));
        if(checkCollision(frameW,frameH) == true){
            vel = -vel;
        }
        //   double r = Math.random()-0.5;
        //   int sign = (int) Math.signum(r);
        if (orient == 1){
            x+=vel/**sign*/;
        }
        if (orient == 2){
            y+=vel/**sign*/;
        }
    }

    /**
     * This method is calculated orientation of moving of enemy
     */
    private void foundOrient(){
        if(start.x == finish.x && start.y != finish.y){
            this.orient = 2;
        }else if(start.y == finish.y && start.x != finish.x){
            this.orient = 1;
        }else {
            orient = 0;
        }
    }
}
