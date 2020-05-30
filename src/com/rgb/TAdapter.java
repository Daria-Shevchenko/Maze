package com.rgb;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TAdapter extends KeyAdapter {
    Maze m;
    StartPage startPage;
    public TAdapter(Maze maze, StartPage start){
        m=maze;
        startPage = start;
    }
    private void up(){
        m.setReq_dy(-1);
    }

    private void down(){
        m.setReq_dy(1);
    }

    private void left(){
        m.setReq_dx(-1);
    }

    private void right(){
        m.setReq_dx(1);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        m.setReq_dx(0);
        m.setReq_dy(0);
        int code = e.getKeyCode();
        //up
        if(code == KeyEvent.VK_UP && m.isInGame()){
            up();
        }
        //down
        if(code == KeyEvent.VK_DOWN && m.isInGame()){
            down();
        }
        //left
        if(code == KeyEvent.VK_LEFT && m.isInGame()){
            left();
        }
        //right
        if(code == KeyEvent.VK_RIGHT && m.isInGame()){
            right();
        }

        if(code == KeyEvent.VK_SPACE && m.isInGame()){
            if(m.isInGame()==true && m.isDying() == false){

                if(m.isHeart())
                    m.addHeroLives();
                if(m.isPortal())
                    m.nextLevel();
            }

            if(m.isGameFinished() == true && m.isDying() == false) {
                startPage.endPageWin();
            }
        }
        if(code == KeyEvent.VK_ENTER){
            startPage.endPageLoser();
        }

        if(code == KeyEvent.VK_ESCAPE)
        {
            startPage.pausePage();
        }

        if((code == KeyEvent.VK_E) && (e.isShiftDown()==false))
        {
            double new_speed = m.getMyHeroSpeed(m.gameLevel-1);
            m.setMyHeroSpeed(1, m.gameLevel-1);
        }
        if(code == KeyEvent.VK_A)
        {
            double new_speed = m.getMyHeroSpeed(m.gameLevel-1);
            m.setMyHeroSpeed(new_speed*1.05, m.gameLevel-1);
        }

        if((code == KeyEvent.VK_E) && (e.isShiftDown()))
        {
            double new_speed = m.getMyHeroSpeed(m.gameLevel-1);
            m.setMyHeroSpeed(new_speed*1.05, m.gameLevel-1);
        }

        if(code == KeyEvent.VK_D)
        {
            double new_speed = m.getMyHeroSpeed(m.gameLevel-1);
            m.setMyHeroSpeed(new_speed/1.05, m.gameLevel-1);
        }


        if(code == KeyEvent.VK_S)
        {
            double new_speed = m.getMyHeroSpeed(m.gameLevel-1);
            m.setMyHeroSpeed(new_speed/1.05, m.gameLevel-1);
        }

       /* if(code == KeyEvent.VK_PAGE_UP)
        {
        }

        if(code == KeyEvent.VK_PAGE_DOWN)
        {
        }
        */


        if(code == KeyEvent.VK_BACK_SPACE){


            System.out.println("____________________-");
            for(int i=0;i<m.bricks.size();i++){
                for(int k=0;k<m.bricks.get(0).length();k++){
                    System.out.print(m.map[i][k]+"");
                }
                System.out.println("");
            }
            System.out.println("____________________");
        }
    }
    private void findEnemyCorditate(){

    }

    //Stop ball if key is released
    @Override
    public void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_UP && m.isInGame()){
            m.setReq_dy(0);
        }
        //down
        if(code == KeyEvent.VK_DOWN && m.isInGame()){
            m.setReq_dy(0);
        }
        //left
        if(code == KeyEvent.VK_LEFT && m.isInGame()){
            m.setReq_dx(0);
        }
        //right
        if(code == KeyEvent.VK_RIGHT && m.isInGame()){
            m.setReq_dx(0);
        }

    }

}