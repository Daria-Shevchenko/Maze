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
    private ArrayList<ArrayList> bricksLevels;
    private int gameLevel = 0;
    private int MAX_gamelevel = 6;
    private boolean inGame = true;
    private boolean dying = false;
    private Heart heart1 = new Heart(),
            heart2 = new Heart();

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int PAC_ANIM_DELAY = 2;

    private int pictureShift = 6;
    private int pictureLengthInCell = 4;

    Image [] hero_images_for_levels = {new ImageIcon("src/images/hero/gg1s.png").getImage(), new ImageIcon("src/images/hero/gg2s.png").getImage(),
            new ImageIcon("src/images/hero/gg3s.png").getImage(), new ImageIcon("src/images/hero/gg4s.png").getImage(),
            new ImageIcon("src/images/hero/gg5s.png").getImage(), new ImageIcon("src/images/hero/gg6s.png").getImage()};

    private Image heroOriginal,hero,heart,portal;
    private int positionMapX,PositionMapY;

    private int portal_x, portal_y;

    private int hero_x = BORDER+BRICK_SIZE*outsideWallCoef+1, hero_y = BORDER+BRICK_SIZE*outsideWallCoef+1;
    private int req_dx, req_dy;
    private boolean canMove;
    private Point positionGGonMap = new Point();

    private Timer timer;

    private static final int [] brick_sizes_for_levels = {15, 13, 13, 11, 10, 9};
    private static int BRICK_SIZE;
    private static final int coefficientCorridor = 4;
    private static final int outsideWallCoef = 1;
    private static final int BORDER = 40;
    private int mazeWidth = 0;
    private int mazeHeight = 0;

    Color [] mazeColorsForWalls = {Color.DARK_GRAY, Color.blue.darker(), Color.ORANGE.darker(), Color.magenta.darker(), Color.GREEN.darker(), Color.red.darker()};
    Color [] mazeColorsForCorridors = {Color.LIGHT_GRAY, Color.cyan.brighter(), Color.YELLOW.brighter(), Color.pink.darker(), Color.GREEN.brighter(), Color.pink};
    Color corridorColor;
    Color wallColor;

    public int [][] map;

    ArrayList<String> bricks;

    public MazeGame(ArrayList<ArrayList> bricksLevels) {
        this.bricksLevels = bricksLevels;
        timer  = new Timer(9, this);
        timer.start();
        nextLevel();
    }

    private void loadImages() {
        Toolkit t=Toolkit.getDefaultToolkit();
        heart=t.getImage("src/images/other/heart_red_s.png");
        portal = new ImageIcon("src/images/other/heart_black_s.png").getImage();
        heroOriginal = hero_images_for_levels[gameLevel-1];
        hero = heroOriginal.getScaledInstance(pictureLengthInCell *BRICK_SIZE*coefficientCorridor/ pictureShift, pictureLengthInCell *BRICK_SIZE*coefficientCorridor/ pictureShift, Image.SCALE_DEFAULT);
    }

    private void initVariables() {
        d = new Dimension(400, 400);
    }

    public void nextLevel(){
        if(gameLevel<MAX_gamelevel) {
            gameLevel++;
            BRICK_SIZE = brick_sizes_for_levels[gameLevel-1];
            heart1.setShow(true);
            heart2.setShow(true);
            hero_x = BORDER + BRICK_SIZE * outsideWallCoef + 1;
            hero_y = BORDER + BRICK_SIZE * outsideWallCoef + 1;
            this.bricks = this.bricksLevels.get(gameLevel-1);
            wallColor = mazeColorsForWalls[gameLevel-1];
            corridorColor = mazeColorsForCorridors[gameLevel-1];
            if (bricks.size() != 0) {
                mazeWidth = calculateMazeLength(this.bricks.get(0).length());
                mazeHeight = calculateMazeLength(this.bricks.size());
            }
            map = new int[this.bricks.size()][this.bricks.get(0).length()];
            this.setSize(mazeWidth + 2 * BORDER, mazeHeight + 2 * BORDER);
            loadImages();
            initVariables();
        } else {
            gameOver();
        }
    }

    private void gameOver(){

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

   /* private Point findPositionInMap(int angle) {

        int x_center = hero.getWidth(null)/2;
        int y_center = hero.getHeight(null)/2;

        double rX = hero.getWidth(null)*findCos(angle)/2;
    double rY = hero.getHeight(null)*findSin(angle)/2;

    int deltX = x_center + (int)(rX);
    int deltY = y_center + (int)(rY);

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



    // System.out.println("gg- "+x1+ " "+y1);
    // System.out.println("heart1- "+heart1.getxMap()+ " "+heart1.getyMap());
    // System.out.println("heart2- "+heart2.getxMap()+ " "+heart2.getyMap());
        return new Point(x1,y1);
}

    */

    private boolean isIntersectionBetweenHeroAndHeart(int angle){
        int x_center = hero.getWidth(null)/2;
        int y_center = hero.getHeight(null)/2;

        double rX = hero.getWidth(null)*findCos(angle)/2;
        double rY = hero.getHeight(null)*findSin(angle)/2;

        int deltX = x_center + (int)(rX);
        int deltY = y_center + (int)(rY);

        int x0 = deltX+hero_x;
        int y0 = deltY+hero_y;

        if(x0>=heart1.getX() && x0<=heart1.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                && y0>=heart1.getY() && y0<=heart1.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
            heart1.setShow(false);
            return true;
        }
        if(x0>=heart2.getX() && x0<=heart2.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                && y0>=heart2.getY() && y0<=heart2.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
            heart2.setShow(false);
            return true;
        }

        if(isIntersectionWithHeartByDiagonal()==true){return true;}

        return false;
    }

    private boolean isIntersectionWithHeartByDiagonal(){

        /*horizontal diagonal*/
        int xFirst = hero_x;
        int xLast = hero_x + hero.getWidth(null);
        int yDiagonal = hero_y+hero.getHeight(null)/2;

        for(int x=xFirst; x <=xLast; x++){
            if(x>=heart1.getX() && x<=heart1.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && yDiagonal>=heart1.getY() && yDiagonal<=heart1.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                heart1.setShow(false);
                return true;
            }
            if(x>=heart2.getX() && x<=heart2.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && yDiagonal>=heart2.getY() && yDiagonal<=heart2.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                heart2.setShow(false);
                return true;
            }
        }

        /*vertical diagonal*/
        int yFirst = hero_y;
        int yLast = hero_y + hero.getHeight(null);
        int xDiagonal = hero_x+hero.getWidth(null)/2;

        for(int y=yFirst; y <=yLast; y++){
            if(xDiagonal>=heart1.getX() && xDiagonal<=heart1.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && y>=heart1.getY() && y<=heart1.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                heart1.setShow(false);
                return true;
            }
            if(xDiagonal>=heart2.getX() && xDiagonal<=heart2.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && y>=heart2.getY() && y<=heart2.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                heart2.setShow(false);
                return true;
            }
        }

        return false;
    }

    public boolean isHeart(){

      /*  int [] checkedAngles = new int [181];

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
                positionGGonMap = findPositionInMap(angle);
                //System.out.println("COLLECT");
                return true;
            }
        }*/

        int [] checkedAngles = new int [360];
        for(int i=0; i<checkedAngles.length; i++) {
            checkedAngles [i] = i;
        }
        for (int angle: checkedAngles) {
            if(isIntersectionBetweenHeroAndHeart(angle) == true){return true;}
        }

        return false;
    }

    private boolean isIntersectionBetweenHeroAndPortal(int angle){
        int x_center = hero.getWidth(null)/2;
        int y_center = hero.getHeight(null)/2;

        double rX = hero.getWidth(null)*findCos(angle)/2;
        double rY = hero.getHeight(null)*findSin(angle)/2;

        int deltX = x_center + (int)(rX);
        int deltY = y_center + (int)(rY);

        int x0 = deltX+hero_x;
        int y0 = deltY+hero_y;

        if(x0>=portal_x && x0<=portal_x+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                && y0>=portal_y && y0<=portal_y+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
            return true;
        }

        if(isIntersectionWithPortalByDiagonal()==true){return true;}

        return false;
    }

    private boolean isIntersectionWithPortalByDiagonal(){
        /*horizontal diagonal*/
        int xFirst = hero_x;
        int xLast = hero_x + hero.getWidth(null);
        int yDiagonal = hero_y+hero.getHeight(null)/2;

        for(int x=xFirst; x <=xLast; x++){
            if(x>=portal_x && x<=portal_x+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && yDiagonal>=portal_y && yDiagonal<=portal_y+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                return true;
            }
        }

        /*vertical diagonal*/
        int yFirst = hero_y;
        int yLast = hero_y + hero.getHeight(null);
        int xDiagonal = hero_x+hero.getWidth(null)/2;

        for(int y=yFirst; y <=yLast; y++){
            if(xDiagonal>=portal_x && xDiagonal<=portal_x+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && y>=portal_y && y<=portal_y+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                return true;
            }
        }

        return false;
    }

    public boolean isPortal(){
        int [] checkedAngles = new int [360];
        for(int i=0; i<checkedAngles.length; i++) {
            checkedAngles [i] = i;
        }
        for (int angle: checkedAngles) {
            if(isIntersectionBetweenHeroAndPortal(angle) == true){nextLevel();
                return true;}
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
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Color heartColor = Color.PINK;
        // Color portal = Color.BLUE.darker();

        int currentBRICK_SIZE_Y = BRICK_SIZE;
        int currentBRICK_SIZE_X = BRICK_SIZE;
        int height = calculateMazeLength(this.bricks.size());
        int length = calculateMazeLength(this.bricks.get(0).length());
        g2d.setColor(wallColor);
        g2d.fillRect(BORDER, BORDER, length, BRICK_SIZE*outsideWallCoef);
        g2d.fillRect(BORDER, BORDER, BRICK_SIZE*outsideWallCoef, height);
        g2d.fillRect(BORDER, BORDER+height-BRICK_SIZE*outsideWallCoef, length, BRICK_SIZE*outsideWallCoef);
        g2d.fillRect(BORDER+length-BRICK_SIZE*outsideWallCoef, BORDER, BRICK_SIZE*outsideWallCoef, height);
        int y = BORDER+BRICK_SIZE*outsideWallCoef;
        int x = 0;
        boolean evenLine = false;
        boolean evenColumn = false;
        int heartCount = 0;
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
                        g2d.setColor(wallColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        map[j][i] = 1;
                    }

                    if (symbolsArray[i] == '*') {
                        g2d.setColor(corridorColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        map[j][i] = 0;
                    }
                    if (symbolsArray[i] == 'P') {
                        g2d.setColor(corridorColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);

                        portal_x = x+currentBRICK_SIZE_X/ pictureShift;
                        portal_y = y+currentBRICK_SIZE_X/ pictureShift;

                        g2d.drawImage(portal,portal_x,portal_y, pictureLengthInCell *currentBRICK_SIZE_X/ pictureShift, pictureLengthInCell *currentBRICK_SIZE_Y/ pictureShift,this);

                        map[j][i] = 2;
                    }
                    if (symbolsArray[i] == 'H') {
                        g2d.setColor(corridorColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);

                        heartCount++;

                        if((heart1.isShow()==false && heartCount==1) || (heart2.isShow()==false && heartCount==2)){
                            map[j][i]=0;
                        }
                        if((heart1.isShow()==true && heartCount==1) || (heart2.isShow()==true && heartCount==2)){
                            map[j][i]=3;
                        }


                        if(heart1.isShow()==true && heartCount==1){
                            heart1.setX(x+currentBRICK_SIZE_X/ pictureShift);
                            heart1.setY(y+currentBRICK_SIZE_Y/ pictureShift);
                            g2d.drawImage(heart,heart1.getX(),heart1.getY(), pictureLengthInCell *currentBRICK_SIZE_X/ pictureShift, pictureLengthInCell *currentBRICK_SIZE_Y/ pictureShift,this);
                            heart1.setxMap(i);
                            heart1.setyMap(j);
                        }

                        if(heart2.isShow()==true && heartCount==2){
                            heart2.setX(x+currentBRICK_SIZE_X/ pictureShift);
                            heart2.setY(y+currentBRICK_SIZE_Y/ pictureShift);
                            g2d.drawImage(heart,heart2.getX(),heart2.getY(), pictureLengthInCell *currentBRICK_SIZE_X/ pictureShift, pictureLengthInCell *currentBRICK_SIZE_Y/ pictureShift,this);
                            heart2.setxMap(i);
                            heart2.setyMap(j);
                        }

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

        //     g2d.drawImage(ii, 5, 5, this);
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


    public void setReq_dy(int req_dy) {
        this.req_dy = req_dy;
    }

    public void setReq_dx(int req_dx) {
        this.req_dx = req_dx;
    }
}

