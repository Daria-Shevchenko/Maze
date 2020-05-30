package com.rgb;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class of game control through the keyboard
 */
public class TAdapter extends KeyAdapter {
    Maze m;
    StartPage startPage;

    /**
     * Constructor
     * @param maze - reference to the maze class
     * @param start - reference to the game frame class
     */
    public TAdapter(Maze maze, StartPage start){
        m = maze;
        startPage = start;
    }

    /**
     *  Change the hero’s displacement by y if the hero moves up
     */
    private void up(){
        m.setReq_dy(-1);
    }

    /**
     * Change the hero’s displacement by y if the hero moves down
     */
    private void down(){
        m.setReq_dy(1);
    }

    /**
     * Change the hero’s displacement by x if the hero moves left
     */
    private void left(){
        m.setReq_dx(-1);
    }

    /**
     * Change the hero’s displacement by x if the hero moves right
     */
    private void right(){
        m.setReq_dx(1);
    }

    /**
     * Override method that check when the key goes down
     * @param e -- An event which indicates that a keystroke occurred in a component
     */
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

                if(m.isHeart()==true) {
                    m.addHeroLives();
                }
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
        if(code == KeyEvent.VK_A)
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
            m.setMyHeroSpeed(1, m.gameLevel-1);
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

    /**
     * Stop hero if key is released
     * @param e - An event which indicates that a keystroke occurred in a component
     */
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