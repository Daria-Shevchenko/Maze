package com.company;

import javax.swing.*;
import javax.swing.text.Element;
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

    private Image hero,heart;

    private int hero_x = BORDER+BRICK_SIZE*outsideWallCoef+1, hero_y = BORDER+BRICK_SIZE*outsideWallCoef+1;
    private int req_dx, req_dy;
    private boolean canMove;

    private Timer timer;

    private static final int BRICK_SIZE = 17;
    private static final int coefficientCorridor = 4;
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
            mazeHeight = calculateMazeLength(this.bricks.size())    ;
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
        Toolkit t=Toolkit.getDefaultToolkit();
        heart=t.getImage("src/images/other/heart_red_s.png");
        hero = new ImageIcon("src/images/hero/gg4s.png").getImage();

    }

    private void initVariables() {
        d = new Dimension(400, 400);
        timer = new Timer(9, this);
        timer.start();
    }

    public void up(){
        req_dy = -2;
    }

    public void down(){
        req_dy = 2;
    }

    public void left(){
        req_dx = -2;
    }

    public void right(){
        req_dx = 2;
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            req_dx = 0;
            req_dy = 0;
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

            if(code == KeyEvent.VK_BACK_SPACE){
                System.out.println("____________________-");
                for(int i=0;i<bricks.size();i++){
                    for(int k=0;k<bricks.get(0).length();k++){
                        System.out.print(map[i][k]+" ");
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
            if(code == KeyEvent.VK_SPACE){
                if(isHeart()){
                    //collectHeart();
                }
            }
            if(code == KeyEvent.VK_UP){
                req_dy=0;
            }
            //down
            if(code == KeyEvent.VK_DOWN){
                req_dy=0;
            }
            //left
            if(code == KeyEvent.VK_LEFT){
                req_dx=0;
            }
            //right
            if(code == KeyEvent.VK_RIGHT){
                req_dx=0;
            }

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        canMove = true;
        if(isWall()){
            canMove = false;
        }
        if(canMove){
            hero_x += req_dx;
            hero_y += req_dy;
        }
        repaint();
    }

    private void collectHeart(int angle) {

        int x_center = hero.getWidth(null)/2;
        int y_center = hero.getHeight(null)/2;

        double rX = hero.getWidth(null)*findCos(angle)/2;
        double rY = hero.getHeight(null)*findSin(angle)/2;

        int deltX = x_center + (int)(rX);
        int deltY = y_center + (int)(rY);

        //hero_x - реальная координата персонажа в окне
        //hero_x-BORDER-BRICK_SIZE*outsideWallCoef - координата персонажа относительно начала лабиринта

        int x0 = req_dx+deltX+hero_x-BORDER-BRICK_SIZE*outsideWallCoef;
        int y0 = req_dy+deltY+hero_y-BORDER-BRICK_SIZE*outsideWallCoef;
        int sX = (int)(x0/(BRICK_SIZE+coefficientCorridor * BRICK_SIZE));
        int sY = (int)(y0/(BRICK_SIZE+coefficientCorridor * BRICK_SIZE));
        int x1=0,y1=0;
        double restX = x0-sX*(BRICK_SIZE+coefficientCorridor * BRICK_SIZE);
        if(restX<=coefficientCorridor * BRICK_SIZE)
        {x1 = sX*2+1;}else{ x1 = sX*2+2;}

        double restY = y0-sY*(BRICK_SIZE+coefficientCorridor * BRICK_SIZE);
        if(restY<=coefficientCorridor * BRICK_SIZE)
        {y1 = sY*2+1;}else{y1 = sY*2+2;}



        System.out.println(x1+ " "+y1+"; "+sX+" "+sY);
    }

    private boolean isHeart(){

        int [] checkedAngles = new int [181];

        if(req_dy>0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i;
            }
        } else if (req_dy<0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i+180;
            }
        }

        if(req_dx>0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i-90;
            }
        } else if (req_dx<0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i+90;
            }
        }

        for (int angle: checkedAngles) {
            if(isInnerWallForEllipse(angle) == 3)
            {
                collectHeart(angle);
                return true;
            }
        }
        return false;
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

        int [] checkedAngles = new int [181];

        if(req_dy>0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i;
            }
        } else if (req_dy<0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i+180;
            }
        }

        if(req_dx>0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i-90;
            }
        } else if (req_dx<0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i+90;
            }
        }

        for (int angle: checkedAngles) {
            if(isInnerWallForEllipse(angle) == 1)
            {
                return true;
            }
        }

        return false;
    }

    private int isInnerWallForEllipse(int angle){

        int x_center = hero.getWidth(null)/2;
        int y_center = hero.getHeight(null)/2;

        double rX = hero.getWidth(null)*findCos(angle)/2;
        double rY = hero.getHeight(null)*findSin(angle)/2;

        int deltX = x_center + (int)(rX);
        int deltY = y_center + (int)(rY);

        //hero_x - реальная координата персонажа в окне
        //hero_x-BORDER-BRICK_SIZE*outsideWallCoef - координата персонажа относительно начала лабиринта

        int x0 = req_dx+deltX+hero_x-BORDER-BRICK_SIZE*outsideWallCoef;
        int y0 = req_dy+deltY+hero_y-BORDER-BRICK_SIZE*outsideWallCoef;
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

    /**Calculate and return cosine of angle*/
    private double findCos(double degree){
        /**Converting values to radians */
        double a = Math.toRadians(degree);
        double cos = Math.cos(a);
        /**Consider special angles in calculating*/
        if(degree!=0 && degree % 180 != 0 && degree % 90 == 0){cos = 0;}
        return cos;
    }
    /**Calculate and return sine of angle*/
    private double findSin(double degree){
        /**Converting values to radians */
        double a = Math.toRadians(degree);
        double sin = Math.sin(a);
        /**Consider special angles in calculating*/
        if(degree == 0 || degree % 180 == 0){sin = 0;}
        return sin;
    }

    private int isInnerWallForRect(int delt){

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
//        Color heart = Color.PINK;
        Color portal = Color.BLUE.darker();

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
                    if (symbolsArray[i] == 'P') {
                        g2d.setColor(portal);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        map[j][i] = 2;
                    }
                    if (symbolsArray[i] == 'H') {
                        g2d.setColor(line);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        map[j][i] = 3;
                        //System.out.println(j+" "+i);

                        JLabel img = new JLabel(new ImageIcon("src/images/other/heart_red_s.png"));
                        img.setBounds(x+currentBRICK_SIZE_X/6,y+currentBRICK_SIZE_Y/6,4*currentBRICK_SIZE_X/6, 4*currentBRICK_SIZE_Y/6);
                        img.setVisible(true);
                        this.add(img);
                        //g2d.drawImage(heart,x+currentBRICK_SIZE_X/6,y+currentBRICK_SIZE_Y/6,4*currentBRICK_SIZE_X/6, 4*currentBRICK_SIZE_Y/6,this);
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

