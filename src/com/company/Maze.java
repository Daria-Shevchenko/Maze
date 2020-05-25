package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Maze extends JPanel implements ActionListener {

    private Dimension d;
    private Image ii;
    private ArrayList<ArrayList> bricksLevels;
    public int gameLevel = 0;
    private int MAX_gamelevel = 6;

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    private boolean inGame = true;

    public boolean isDying() {
        return dying;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }

    private boolean dying = false;

    private Heart heart1 = new Heart(),
            heart2 = new Heart();

    private int quantityOfPickedHeartsOnFinishedLevels = 3;
    private int quantityOfPickedHeartsOnCurrentLevel = 0;

    public int getHeartsOnLvl(){
        return quantityOfPickedHeartsOnCurrentLevel;
    }
    public void addHeartsOnLvl(){
        quantityOfPickedHeartsOnCurrentLevel++;
    }

    public int getQuantityOfHearts() {
        return quantityOfPickedHeartsOnFinishedLevels+quantityOfPickedHeartsOnCurrentLevel;
    }
    private String pathToFileWithGameStatus = "src/mazeFiles/levelStatus.txt";
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

    private int portal_x=0, portal_y=0;

    private int hero_x = BORDER+BRICK_SIZE*outsideWallCoef+1, hero_y = BORDER+BRICK_SIZE*outsideWallCoef+1;
    private int req_dx=0, req_dy=0;
    private boolean canMove;
    private Timer timer;

    private static final int [] brick_sizes_for_levels ={17, 15, 13, 13, 12, 11};    // {15, 13, 13, 11, 10, 9};
    private static int BRICK_SIZE;
    private static final int coefficientCorridor = 4;
    private static final int outsideWallCoef = 1;
    private static final int BORDER = 10;
    private int mazeWidth = 0;
    private int mazeHeight = 0;

    Color [] mazeColorsForWalls = {Color.DARK_GRAY, Color.blue.darker(), Color.ORANGE.darker(), Color.magenta.darker(), Color.GREEN.darker(), Color.red.darker()};
    Color [] mazeColorsForCorridors = {Color.LIGHT_GRAY, Color.cyan.brighter(), Color.YELLOW.brighter(), Color.pink.darker(), Color.GREEN.brighter(), Color.pink};
    Color corridorColor;
    Color wallColor;

    public int [][] map;

    ArrayList<String> bricks;

    public Maze(ArrayList<ArrayList> bricksLevels) {
        this.bricksLevels = bricksLevels;
        timer  = new Timer(5, this);
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
        readGameStatusFromFile();
        if(gameLevel<MAX_gamelevel) {
            gameLevel++;
            quantityOfPickedHeartsOnCurrentLevel = 0;
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
        inGame=false;
        writeToFileGameStatus(0 + "|" + 3);
    }

    private void writeToFileGameStatus(String data){
            File file = new File(pathToFileWithGameStatus);
            FileWriter fr = null;
            try {
                fr = new FileWriter(file);
                fr.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                //close resources
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    /* Function reads from the file data about game level and number of hearts
     * Result is empty when operation is successful and contains error if not. */
    public String readGameStatusFromFile()
    {
        Scanner rd;
        String result = "";
        try
        {
            rd = new Scanner(new File(pathToFileWithGameStatus));
            while (rd.hasNextLine())
            {
                String str = rd.nextLine();
                // "|" is a symbol used to separate fields in saved file
                String[] record = str.split("[|]");
                gameLevel = Integer.parseInt(record[0]);
                quantityOfPickedHeartsOnFinishedLevels = Integer.parseInt(record[1]);

            }
            rd.close();
        }
        catch (IOException e)
        {
            result = "Data file lab2_items_db.txt is not found in folder " + pathToFileWithGameStatus + "\n"
                    + "You may open data file from another location.";
            // System.err.println("Problems with file opening");
        }
        return result;
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
            System.out.println("1st between hero and heart");
            return true;
        }
        if(x0>=heart2.getX() && x0<=heart2.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                && y0>=heart2.getY() && y0<=heart2.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
            heart2.setShow(false);
            System.out.println("2st between hero and heart");
            return true;
        }

        if(isIntersectionWithHeartByDiagonal()==true){
            System.out.println("3st between hero abd heart");
            return true;
        }

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
                System.out.println("1st diagon");
                return true;
            }
            if(x>=heart2.getX() && x<=heart2.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && yDiagonal>=heart2.getY() && yDiagonal<=heart2.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                heart2.setShow(false);
                System.out.println("2st diagon");
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
                System.out.println("3st diagon");
                return true;
            }
            if(xDiagonal>=heart2.getX() && xDiagonal<=heart2.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && y>=heart2.getY() && y<=heart2.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                heart2.setShow(false);
                System.out.println("4st diagon");
                return true;
            }
        }

        return false;
    }

    public boolean isHeart(){
        System.out.println("portal "+portal_x+" "+portal_y);
        System.out.println("h1 "+heart1.getX()+" "+heart1.getY());
        System.out.println("h2 "+heart2.getX()+" "+heart2.getY());
        System.out.println("Hero "+ hero_x+" "+hero_y);
        int [] checkedAngles = new int [360];
        for(int i=0; i<checkedAngles.length; i++) {
            checkedAngles [i] = i;
        }
        for (int angle: checkedAngles) {
            if(isIntersectionBetweenHeroAndHeart(angle) == true){
                System.out.println("isHeart true");
                return true;
            }
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
            System.out.println("1st between hero and portal");
            return true;
        }

        if(isIntersectionWithPortalByDiagonal()==true){
            System.out.println("1st portal diagonal");
            return true;
        }

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
                System.out.println("1st portal");
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
                System.out.println("2st portal");
                return true;
            }
        }

        return false;
    }

    public boolean isPortal(){
        System.out.println("portal "+portal_x+" "+portal_y);
        System.out.println("h1 "+heart1.getX()+" "+heart1.getY());
        System.out.println("h2 "+heart2.getX()+" "+heart2.getY());
        System.out.println("Hero "+ hero_x+" "+hero_y);
        int [] checkedAngles = new int [360];
        for(int i=0; i<checkedAngles.length; i++) {
            checkedAngles [i] = i;
        }
        for (int angle: checkedAngles) {
            if(isIntersectionBetweenHeroAndPortal(angle) == true){
                quantityOfPickedHeartsOnFinishedLevels = quantityOfPickedHeartsOnFinishedLevels + quantityOfPickedHeartsOnCurrentLevel;
                writeToFileGameStatus(gameLevel + "|" + quantityOfPickedHeartsOnFinishedLevels);
                System.out.println("isPortal true -- next level");
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

//        System.out.println("h1"+heart1.getX()+" "+heart1.getY());
//        System.out.println("h2"+heart2.getX()+" "+heart2.getY());
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

        }
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

