package com.company;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TAdapter extends KeyAdapter {
    Maze m;
    public TAdapter(Maze maze){
        m=maze;
    }
    private void up(){
        m.setReq_dy(-2);
    }

    private void down(){
        m.setReq_dy(2);
    }

    private void left(){
        m.setReq_dx(-2);
    }

    private void right(){
        m.setReq_dx(2);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        m.setReq_dx(0);
        m.setReq_dy(0);
        int code = e.getKeyCode();
        //up
        if(code == KeyEvent.VK_UP){
            up();
        }
        //down
        if(code == KeyEvent.VK_DOWN){
            down();
        }
        //left
        if(code == KeyEvent.VK_LEFT){
            left();
        }
        //right
        if(code == KeyEvent.VK_RIGHT){
            right();
        }

        if(code == KeyEvent.VK_SPACE){
            m.isHeart();
            m.isPortal();
             /*   if(isHeart()){
                    if(positionGGonMap.getX()==heart1.getxMap() && positionGGonMap.getY()==heart1.getyMap())
                        heart1.setShow(false);
                    else{
                        if(positionGGonMap.getX()==heart2.getxMap() && positionGGonMap.getY()==heart2.getyMap())
                            heart2.setShow(false);
                    }
                }
              */
        }

        if(code == KeyEvent.VK_BACK_SPACE){
            System.out.println("____________________-");
            for(int i=0;i<m.bricks.size();i++){
                for(int k=0;k<m.bricks.get(0).length();k++){
                    System.out.print(m.map[i][k]+" ");
                }
                System.out.println("");
            }
            System.out.println("____________________");
            System.out.println("");
        }
    }

    //Stop ball if key is released
    @Override
    public void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_UP){
            m.setReq_dy(0);
        }
        //down
        if(code == KeyEvent.VK_DOWN){
            m.setReq_dy(0);
        }
        //left
        if(code == KeyEvent.VK_LEFT){
            m.setReq_dx(0);
        }
        //right
        if(code == KeyEvent.VK_RIGHT){
            m.setReq_dx(0);
        }

    }

}