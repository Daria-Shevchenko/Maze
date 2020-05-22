package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MazeGame extends JPanel implements ActionListener {

    private Dimension d;
    private Image ii;
    private boolean inGame = true;
    private boolean dying = false;

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int PAC_ANIM_DELAY = 2;

    private Image hero;

    private int hero_x = BORDER+BRICK_SIZE*outsideWallCoef+1, hero_y = BORDER+BRICK_SIZE*outsideWallCoef+1, pacmand_x, pacmand_y;
    private int req_dx, req_dy;

    private Timer timer;

    private static final int BRICK_SIZE = 30;
    private static final int coefficientCorridor = 3;
    private static final int outsideWallCoef = 1;
    private static final int BORDER = 40;
    private int mazeWidth = 0;
    private int mazeHeight = 0;

    private int [][] map;

    ArrayList<String> bricks;

    public MazeGame(ArrayList<String> bricks) {
        this.bricks = bricks;
        if (bricks.size() != 0) {
            mazeWidth = calculateMazeLength(this.bricks.get(0).length());
            mazeHeight = calculateMazeLength(this.bricks.size());
        }
        map = new int[this.bricks.size()][this.bricks.get(0).length()];
        this.setSize(mazeWidth+2*BORDER, mazeHeight+2*BORDER);

        loadImages();
        initVariables();
        initBoard();

    }

    private void initBoard() {

        addKeyListener(new TAdapter());

        setFocusable(true);

        setBackground(Color.black);
    }

    private void loadImages() {
        hero = new ImageIcon("src/images/hero/gg4s.png").getImage();
    }

    private void initVariables() {
        d = new Dimension(400, 400);
        timer = new Timer(3, this);
        timer.start();
    }

    public void up(){
        req_dy = -1;
        req_dx = 0;
    }

    public void down(){
        req_dy = 1;
        req_dx = 0;
    }

    public void left(){
        req_dx = -1;
        req_dy = 0;
    }

    public void right(){
        req_dx = 1;
        req_dy = 0;
    }

    public void stop(){
        req_dx = 0;
        req_dy = 0;
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int code = e.getKeyCode();

            switch( code )
            {
                case KeyEvent.VK_UP:
                    if(hero_y>0){ up();
                    } else {stop();}
                    break;
                case KeyEvent.VK_DOWN:
                    if(hero_y+hero.getHeight(null)<getPanelHeight()) { down();
                    } else {stop();}
                    break;
                case KeyEvent.VK_LEFT:
                    if(hero_x>0) { left();
                    } else {stop();}
                    break;
                case KeyEvent.VK_RIGHT :
                    if(hero_x+hero.getWidth(null)<getPanelWidth()) {
                        right();
                    } else {stop();}
                    break;
            }

        }

        //Stop ball if key is released
        @Override
        public void keyReleased(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN ||
                    keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT){
                stop();
            }


        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(hero_x+req_dx>0 && hero_x+req_dx<this.getWidth()-hero.getWidth(null) && !isWall()) {
            hero_x += req_dx;
            repaint();
        }
        if(hero_y+req_dy>0 && hero_y+req_dy<this.getHeight()-hero.getHeight(null) && !isWall()) {
            hero_y += req_dy;
            repaint();
        }
    }


    private boolean isWall(){
        if(hero_x+req_dx<=BORDER+BRICK_SIZE*outsideWallCoef || hero_y+req_dy<=BORDER+BRICK_SIZE*outsideWallCoef){
            return true;
        }
        int height = calculateMazeLength(this.bricks.size());
        int length = calculateMazeLength(this.bricks.get(0).length());
        if(hero_x+hero.getWidth(null)+req_dx >= BORDER+length-BRICK_SIZE*outsideWallCoef ||
                hero_y+hero.getHeight(null)+req_dy >= BORDER+height-BRICK_SIZE*outsideWallCoef){
           return true;
        }

        if(isInnerWall(0) == 1 || isInnerWall(15) == 1 ||
                isInnerWall(30) == 1 || isInnerWall(45) == 1 ||
            isInnerWall(60) == 1 || isInnerWall(75) == 1 ||
                isInnerWall(90) == 1 || isInnerWall(100) == 1)
        {
            return true;
        }
        return false;
    }


    private int isInnerWall(int delt){

        int deltX = 0;
        int deltY = 0;

        int difX = 0;
        if(req_dx>0)
        {difX = hero.getWidth(null);
         deltY = (int)(delt*hero.getHeight(null)/100);
        } else if (req_dx<0) {
            deltY = (int)(delt*hero.getHeight(null)/100);
        }
        int difY = 0;
        if(req_dy>0){
            difY = hero.getHeight(null);
            deltX = (int)(delt*hero.getWidth(null)/100);
        } else if (req_dy<0){
            deltX = (int)(delt*hero.getWidth(null)/100);
        }


        int x0 = req_dx+difX+deltX+hero_x-BORDER-BRICK_SIZE*outsideWallCoef;
        int y0 = req_dy+difY+deltY+hero_y-BORDER-BRICK_SIZE*outsideWallCoef;
        int sX = (int)(x0/(BRICK_SIZE+coefficientCorridor * BRICK_SIZE));
        int sY = (int)(y0/(BRICK_SIZE+coefficientCorridor * BRICK_SIZE));
        int x1 =0;
        int y1 =0;

        double restX = x0-sX*(BRICK_SIZE+coefficientCorridor * BRICK_SIZE);
        if(restX<=coefficientCorridor * BRICK_SIZE)
        {x1 = sX*2+1;}else{ x1 = sX*2+2;}

        double restY = y0-sY*(BRICK_SIZE+coefficientCorridor * BRICK_SIZE);
        if(restY<=coefficientCorridor * BRICK_SIZE)
        {y1 = sY*2+1;}else{y1 = sY*2+2;}
        return map[y1][x1];
    }

    private void drawMaze(Graphics2D g2d) {

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        Color line = Color.YELLOW.brighter();
        Color fill = Color.ORANGE.darker();

        int currentBRICK_SIZE_Y = BRICK_SIZE;
        int currentBRICK_SIZE_X = BRICK_SIZE;
        int height = calculateMazeLength(this.bricks.size());
        int length = calculateMazeLength(this.bricks.get(0).length());
        g2d.setColor(fill);
        g2d.fillRect(BORDER, BORDER, length, BRICK_SIZE*outsideWallCoef);
        g2d.fillRect(BORDER, BORDER, BRICK_SIZE*outsideWallCoef, height);
        g2d.fillRect(BORDER, BORDER+height-BRICK_SIZE*outsideWallCoef, length, BRICK_SIZE*outsideWallCoef);
        g2d.fillRect(BORDER+length-BRICK_SIZE*outsideWallCoef, BORDER, BRICK_SIZE*outsideWallCoef, height);
        int y = BORDER+BRICK_SIZE*outsideWallCoef;
        int x = 0;
        boolean evenLine = false;
        boolean evenColumn = false;

        int j = 0;
        for (String wall: this.bricks){
            if (this.bricks.indexOf(wall)>0 && this.bricks.indexOf(wall) < this.bricks.size()-1) {

                char[] symbolsArray = new char[wall.toCharArray().length];
                symbolsArray = wall.toCharArray();

                if(evenLine) { currentBRICK_SIZE_Y = BRICK_SIZE;}
                else {currentBRICK_SIZE_Y = coefficientCorridor * BRICK_SIZE;}
                evenLine = ! evenLine;

                x = BORDER + BRICK_SIZE * outsideWallCoef;
                evenColumn = false;
                for (int i = 1; i < symbolsArray.length - 1; i++) {
                    if (evenColumn) {currentBRICK_SIZE_X = BRICK_SIZE;}
                    else { currentBRICK_SIZE_X = coefficientCorridor * BRICK_SIZE;}
                    evenColumn = ! evenColumn;

                    if (symbolsArray[i] == '#') {
                        g2d.setColor(fill);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        map[j][i] = 1;
                    }

                    if (symbolsArray[i] == '*') {
                        g2d.setColor(line);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        map[j][i] = 0;
                    }

                    x += currentBRICK_SIZE_X;
                }
                y += currentBRICK_SIZE_Y;
            }
            j++;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }


    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawMaze(g2d);

        if (inGame) {
            playGame(g2d);
        } else {
          //  showIntroScreen(g2d);
        }

        g2d.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    private void playGame(Graphics2D g2d) {

        if (dying) {

         //   death();

        } else {

            moveHero();
            drawHero(g2d);
        //    moveGhosts(g2d);
        //    checkMaze();
        }
    }

    private void moveHero() {

    }

    private void drawHero(Graphics2D g2d) {
        drawNewHero(g2d);
    }

    private void drawNewHero(Graphics2D g2d) {

        g2d.drawImage(hero, hero_x + 1, hero_y + 1, this);
        
    }


    public int calculateMazeLength(int numberOfColons){
        int mazeLength = 2*BRICK_SIZE*outsideWallCoef + (numberOfColons-3)/2*BRICK_SIZE+((numberOfColons-3)/2+1)*BRICK_SIZE*coefficientCorridor;
        return mazeLength;
    }

    public int getPanelWidth(){
        return this.mazeWidth+2*BORDER;
    }
    public int getPanelHeight(){
        return this.mazeHeight+2*BORDER;
    }
    public int getPanelBorder(){
        return this.BORDER;
    }


}

