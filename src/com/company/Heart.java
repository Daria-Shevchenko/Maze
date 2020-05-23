package com.company;

public class Heart {
    private boolean show;
    private int x,y;
    private int xMap,yMap;

    public int getxMap() {
        return xMap;
    }

    public void setxMap(int xMap) {
        this.xMap = xMap;
    }

    public int getyMap() {
        return yMap;
    }

    public void setyMap(int yMap) {
        this.yMap = yMap;
    }

    Heart(){
        show=true;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isShow() {
        return show;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
