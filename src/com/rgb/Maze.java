package com.rgb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Maze extends JPanel implements ActionListener {

    private  ArrayList<Enemy> enemies = new ArrayList<>();
    private Map<Point, Point> sf;

    private ArrayList<ArrayList> bricksLevels;

    public int gameLevel = 0;
    private int MAX_gamelevel = 6;

    private boolean gameFinished = false;
    private boolean inGame = false;
    private boolean dying = false;
    private boolean canMove;

    private Hero myHero;

    private Heart heart1 = new Heart(),
            heart2 = new Heart();

    private int defaultHeroLives = 3;
    private int heroLives = defaultHeroLives;
    private int heroLivesOnLevelStart = defaultHeroLives;

    private String pathToFileWithGameStatus = "src/mazeFiles/levelStatus.txt";
    private Timer timer;

//    private final int BLOCK_SIZE = 24;
//    private final int N_BLOCKS = 15;

    public int [][] map;
    ArrayList<String> bricks;

    private int pictureShift = 6;
    private int pictureLengthInCell = 4;
    private Image heart,portal;

    private int portal_x=0, portal_y=0;

    private static final int [] brick_sizes_for_levels ={17, 15, 13, 13, 12, 11};    // {15, 13, 13, 11, 10, 9};
    private static int BRICK_SIZE;
    private static final int coefficientCorridor = 4;
    private static final int outsideWallCoef = 1;
    private static final int BORDER = 10;
    private int mazeWidth = 0;
    private int mazeHeight = 0;
    private int enemyW,enemyH;

    private int numberOfAngles = 360;
    private double [] cosOfAngle = new double [numberOfAngles];
    private double [] sinOfAngle = new double [numberOfAngles];
    private int [] deltX = new int [numberOfAngles];
    private int [] deltY = new int [numberOfAngles];

    ArrayList<Enemy> enemiesFromCurrentLevel = new ArrayList<Enemy>();

    private Image [] portal_images_for_levels = {
            new ImageIcon("src/images/portal/portal1.png").getImage(), new ImageIcon("src/images/portal/portal2.png").getImage(),
            new ImageIcon("src/images/portal/portal3.png").getImage(), new ImageIcon("src/images/portal/portal4.png").getImage(),
            new ImageIcon("src/images/portal/portal5.png").getImage(), new ImageIcon("src/images/portal/portal6.png").getImage()};
    Color [] mazeColorsForWalls = {Color.DARK_GRAY, new Color(24, 32, 75), new Color(34, 56, 37), new Color(59, 58, 37), new Color(48, 40, 30), new Color(42, 11, 7)};
    Color [] mazeColorsForCorridors = {Color.LIGHT_GRAY, new Color(73, 80, 115), new Color(70, 129, 80), new Color(163, 155, 65), new Color(164, 112, 48), new Color(120, 79, 74)};

    Color corridorColor;
    Color wallColor;
    //Color enemyColor = Color.RED.darker();

    public Maze(ArrayList<ArrayList> bricksLevels) {
    //    System.out.println("Maze - it is Constructor");
        this.bricksLevels = bricksLevels;
        timer  = new Timer(5, this);
        timer.start();
        listOfEnemies();
        nextLevel();
        initAnglesValues();
    }

    private void initAnglesValues(){
     //   System.out.println("initAngleValues");
        int x_center = myHero.getHeroImage().getWidth(null) / 2;
        int y_center = myHero.getHeroImage().getHeight(null) / 2;

        for(int i=0; i<numberOfAngles; i++) {
            sinOfAngle[i] = findSin(i);
            cosOfAngle[i] = findCos(i);

            double rX = myHero.getHeroImage().getWidth(null) * cosOfAngle[i] / 2;
            double rY = myHero.getHeroImage().getHeight(null) * sinOfAngle[i] / 2;

            deltX [i] = x_center + (int) (rX);
            deltY [i] = y_center + (int) (rY);
        }
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

    private int getDistance(int x1, int y1, int x2, int y2){
        return (int)Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isDying() {
        return dying;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }

    public void addHeroLives(){
        heroLives++;
        writeToFileGameStatus((gameLevel-1) + "|" + heroLives);
      //  System.out.println(heroLives);
    }

    public void setHeroLives(int new_heroLives) {
        this.heroLives = new_heroLives;
        this.heroLivesOnLevelStart = new_heroLives;
        writeToFileGameStatus((gameLevel-1) + "|" + heroLives);
     //   System.out.println(heroLives);
    }

    public int getHeroLivesOnLevelStart() {
        return heroLivesOnLevelStart;
    }

    public int getHeroLives(){
        return heroLives;
    }

    public void setMyHeroSpeed(double new_heroSpeed, int level) {
        this.myHero.setHeroSpeed(new_heroSpeed, level);
    }

    public double getMyHeroSpeed(int level){
        return this.myHero.getHeroSpeed(level);
    }

    /**
     *
     * FILE - GAME STATUS
     *
     */

    private void writeToFileGameStatus(String data){
    //    System.out.println("writeToFileGameStatus");
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
    {//System.out.println("readGameStatusFromFile");
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
                heroLives = Integer.parseInt(record[1]);

            }
            rd.close();
        }
        catch (IOException e)
        {
            result = "File is not found " + pathToFileWithGameStatus;
        }
        return result;
    }

    /**
     *  LOADING IMAGES FOR MAZE
     */
    private void loadImages() {
     //   System.out.println("loadImages");
        Toolkit t=Toolkit.getDefaultToolkit();
        heart = t.getImage("src/images/other/heart_red_s.png");
        portal = portal_images_for_levels[this.gameLevel-1];
     }

    public void nextLevel(){
      //  System.out.println("nextLevel");
        readGameStatusFromFile();
        if(gameLevel<MAX_gamelevel) {
            gameLevel++;
            BRICK_SIZE = brick_sizes_for_levels[gameLevel-1];
            heart1.setShow(true);
            heart2.setShow(true);
            myHero = new Hero(gameLevel, BRICK_SIZE*coefficientCorridor, BORDER + BRICK_SIZE * outsideWallCoef + 1, BORDER + BRICK_SIZE * outsideWallCoef + 1);
            myHero.setHeroRadiusAndCenter();
            heroLivesOnLevelStart = heroLives;
            this.bricks = this.bricksLevels.get(gameLevel-1);
            wallColor = mazeColorsForWalls[gameLevel-1];
            corridorColor = mazeColorsForCorridors[gameLevel-1];
            if (bricks.size() != 0) {
                mazeWidth = calculateMazeLength(this.bricks.get(0).length());
                mazeHeight = calculateMazeLength(this.bricks.size());
            }
            map = new int[this.bricks.size()][this.bricks.get(0).length()];

            enemyW = BRICK_SIZE*coefficientCorridor;
            enemyH = BRICK_SIZE*coefficientCorridor;
            for (Enemy enemy : enemies) {
                enemy.setWidth(enemyW);
                enemy.setHeight(enemyH);
            }
            enemyOnMap();
            addEnemyToMaze();
            enemiesFromCurrentLevel = getEnemiesFromCurrentLevel();
            this.setSize(mazeWidth + 2 * BORDER, mazeHeight + 2 * BORDER);
            loadImages();

        } else {
            gameOver();
        }
    }

    private void listOfEnemies() {
     //   System.out.println("listOfEnemies");
        enemyH = 50;
        enemyW = 50;

        /**
         * 1 lvl
         */
        enemies.add(new Enemy(enemyW,enemyH,new Point(14,4), new Point(18,4),0.6,1));
        enemies.add(new Enemy(enemyW,enemyH,new Point(6,6), new Point(6,8),0.6,1));
        enemies.add(new Enemy(enemyW,enemyH,new Point(16,10), new Point(18,10),0.6,1));

        /**
         * 2 lvl
         */
        enemies.add(new Enemy(enemyW,enemyH,new Point(18,2), new Point(18,4),0.5,2));
        enemies.add(new Enemy(enemyW,enemyH,new Point(10,4), new Point(10,8),0.5,2));
        enemies.add(new Enemy(enemyW,enemyH,new Point(14,12), new Point(14,16),0.47,2));

        /**
         * 3 lvl
         */
        enemies.add(new Enemy(enemyW,enemyH,new Point(18,6 ), new Point(22,6),0.4,3));
        enemies.add(new Enemy(enemyW,enemyH,new Point(12,8), new Point(16,8),0.6,3));
        enemies.add(new Enemy(enemyW,enemyH,new Point(4,10), new Point(4,16),0.3,3));
        //enemies.add(new Enemy(enemyW,enemyH,new Point(16,14), new Point(16,16),0.4,3));

        /**
         * 4 lvl
         */
        enemies.add(new Enemy(enemyW,enemyH,new Point(14,6 ), new Point(16,6),0.3,4));
        //enemies.add(new Enemy(enemyW,enemyH,new Point(14,8), new Point(14,10),0.3,4));

        enemies.add(new Enemy(enemyW,enemyH,new Point(22,10), new Point(24,10),0.3,4));

        enemies.add(new Enemy(enemyW,enemyH,new Point(22,14), new Point(24,14),0.4,4));
        enemies.add(new Enemy(enemyW,enemyH,new Point(6,16), new Point(6,18),0.5,4));

        /**
         * 5 lvl
         */

        enemies.add(new Enemy(enemyW,enemyH,new Point(8,4 ), new Point(10,4),0.4,5));
        //enemies.add(new Enemy(enemyW,enemyH,new Point(24,6 ), new Point(26,6),0.4,5));
        enemies.add(new Enemy(enemyW,enemyH,new Point(6,10 ), new Point(6,14),0.4,5));
        enemies.add(new Enemy(enemyW,enemyH,new Point(12,12 ), new Point(16,12),0.5,5));
        enemies.add(new Enemy(enemyW,enemyH,new Point(12,18 ), new Point(18,18),0.5,5));

        /**
         * 6 lvl
         */

        enemies.add(new Enemy(enemyW,enemyH,new Point(14,6 ), new Point(18,6),0.4,6));
        enemies.add(new Enemy(enemyW,enemyH,new Point(14,8 ), new Point(14,10),0.4,6));
        enemies.add(new Enemy(enemyW,enemyH,new Point(24,8), new Point(24,14),0.4,6));
        enemies.add(new Enemy(enemyW,enemyH,new Point(6,12), new Point(6,16),0.4,6));
        enemies.add(new Enemy(enemyW,enemyH,new Point(10,16), new Point(10,20),0.28,6));
        enemies.add(new Enemy(enemyW,enemyH,new Point(14,20), new Point(16,20),0.4,6));
        enemies.add(new Enemy(enemyW,enemyH,new Point(26,18), new Point(26,22),0.5,6));

        for(Enemy everyEnemy : enemies){
            everyEnemy.setEnemyRadiusAndCenter();
        }

    }


    private void enemyOnMap() {
      //  System.out.println("enemyOnMap");
        sf = new HashMap<Point, Point>();

        for (Enemy enemy : enemies) {
            if(enemy.getLvl()==gameLevel)
                sf.put(enemy.getStart(),enemy.getFinish());
        }
        System.out.println("Level "+gameLevel);

        for(int i=0;i<bricks.size();i++){
            for(int k=0;k<bricks.get(0).length();k++){
                if(i==0 || i==bricks.size()-1 || k==0 || k ==bricks.get(0).length()-1)
                    map[i][k]=9;
            }
        }
    }

    private void addEnemyToMaze() {
    //    System.out.println("addEnemyToMaze");
        int currentBRICK_SIZE_Y = BRICK_SIZE;
        int currentBRICK_SIZE_X = BRICK_SIZE;
        int height = calculateMazeLength(this.bricks.size());
        int length = calculateMazeLength(this.bricks.get(0).length());

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

                    Point test = new Point(i+1,j+1);

                    if (sf.containsKey(test)) {
                        //System.out.println(x+" "+y);
                        for (Enemy enemy : enemies) {
                            if(enemy.getStart().equals(test)){
                                enemy.setStart(new Point(x+1, y+1));

                            }
                        }
                    }

                    if (sf.containsValue(test)) {
                        System.out.println(test+" "+x+" "+y);
                        for (Enemy enemy : enemies) {
                            if(enemy.getFinish().equals(test)){
                                enemy.setFinish(new Point(x, y));

                            }
                        }
                    }
                    x += currentBRICK_SIZE_X;
                }
                y += currentBRICK_SIZE_Y;
            }
            j++;
        }
    }

    private void gameOver(){
     //   System.out.println("gameOver");
        inGame=false;
        gameFinished = true;
        writeToFileGameStatus(0 + "|" + defaultHeroLives);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        canMove = true;

        for (Enemy enemy : enemies) {
            if(enemy.isVisible() && inGame == true )
                enemy.move(mazeWidth,mazeHeight);
        }

        if(isWall()){
            canMove = false;
        }
        if(isEnemy()){
            minusLive();
            canMove = false;
        }
        if(canMove){
            myHero.changeHero_x();
            myHero.changeHero_y();
        }
        repaint();
    }

    private void minusLive(){
      //  System.out.println("minusLife");

        if(heroLives>1){
            heroLives--;
            writeToFileGameStatus((gameLevel-1) + "|" + heroLives);
           // System.out.println(heroLives);
            myHero.setHero_x(BORDER + BRICK_SIZE * outsideWallCoef + 1);
            myHero.setHero_y(BORDER + BRICK_SIZE * outsideWallCoef + 1);
        } else if(heroLives == 1){
            dying = true;
            writeToFileGameStatus((gameLevel-1) + "|" + defaultHeroLives);
        }


      /*  if(quantityOfPickedHeartsOnFinishedLevels>1){
            quantityOfPickedHeartsOnFinishedLevels--;
            writeToFileGameStatus((gameLevel-1) + "|" + quantityOfPickedHeartsOnFinishedLevels);
            myHero.setHero_x(BORDER + BRICK_SIZE * outsideWallCoef + 1);
            myHero.setHero_y(BORDER + BRICK_SIZE * outsideWallCoef + 1);
        } else if(quantityOfPickedHeartsOnFinishedLevels==1){
            dying = true;
            writeToFileGameStatus(0 + "|" + 3);
        }
       */
    }

    private boolean isIntersectionBetweenHeroAndHeart(int angle){
     //   System.out.println("isIntersectionBetweenHeroAndHeart");
        int x_center = myHero.getHeroImage().getWidth(null)/2;
        int y_center = myHero.getHeroImage().getHeight(null)/2;

        double rX = myHero.getHeroImage().getWidth(null)*cosOfAngle[angle]/2;
        double rY = myHero.getHeroImage().getHeight(null)*sinOfAngle[angle]/2;

        int deltX = x_center + (int)(rX);
        int deltY = y_center + (int)(rY);

        int x0 = deltX+(int)myHero.getHero_x();
        int y0 = deltY+(int)myHero.getHero_y();

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

        if(isIntersectionWithHeartByDiagonal()==true){

            return true;
        }

        return false;
    }

    private boolean isIntersectionWithHeartByDiagonal(){
       // System.out.println("isIntersectionWithHeartByDiagonal");
        /*horizontal diagonal*/
        int xFirst = (int)myHero.getHero_x();
        int xLast = (int)myHero.getHero_x() + myHero.getHeroImage().getWidth(null);
        int yDiagonal = (int)myHero.getHero_y()+myHero.getHeroImage().getHeight(null)/2;

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
        int yFirst = (int)myHero.getHero_y();
        int yLast = (int)myHero.getHero_y() + myHero.getHeroImage().getHeight(null);
        int xDiagonal = (int)myHero.getHero_x()+myHero.getHeroImage().getWidth(null)/2;

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
      //  System.out.println("isHeart");
        for (int i = 0; i < numberOfAngles; i++) {
            if(isIntersectionBetweenHeroAndHeart(i) == true){
                return true;
            }
        }

        return false;
    }

    private boolean isIntersectionBetweenHeroAndPortal(int angle){
    //    System.out.println("isIntersectionBetweenHeroAndPortal");
        int x_center = myHero.getHeroImage().getWidth(null)/2;
        int y_center = myHero.getHeroImage().getHeight(null)/2;

        double rX = myHero.getHeroImage().getWidth(null)*cosOfAngle[angle]/2;
        double rY = myHero.getHeroImage().getHeight(null)*sinOfAngle[angle]/2;

        int deltX = x_center + (int)(rX);
        int deltY = y_center + (int)(rY);

        int x0 = deltX+(int)myHero.getHero_x();
        int y0 = deltY+(int)myHero.getHero_y();

        if(x0>=portal_x && x0<=portal_x+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                && y0>=portal_y && y0<=portal_y+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
            return true;
        }

        if(isIntersectionWithPortalByDiagonal()==true){
            return true;
        }

        return false;
    }

    private boolean isIntersectionWithPortalByDiagonal(){
    //    System.out.println("isIntersectionWithPortalByDiagonal");
        /*horizontal diagonal*/
        int xFirst = (int)myHero.getHero_x();
        int xLast = (int)myHero.getHero_x() + myHero.getHeroImage().getWidth(null);
        int yDiagonal = (int)myHero.getHero_y()+myHero.getHeroImage().getHeight(null)/2;

        for(int x=xFirst; x <=xLast; x++){
            if(x>=portal_x && x<=portal_x+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && yDiagonal>=portal_y && yDiagonal<=portal_y+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                return true;
            }
        }

        /*vertical diagonal*/
        int yFirst = (int)myHero.getHero_y();
        int yLast = (int)myHero.getHero_y() + myHero.getHeroImage().getHeight(null);
        int xDiagonal = (int)myHero.getHero_x()+myHero.getHeroImage().getWidth(null)/2;

        for(int y=yFirst; y <=yLast; y++){
            if(xDiagonal>=portal_x && xDiagonal<=portal_x+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && y>=portal_y && y<=portal_y+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                return true;
            }
        }

        return false;
    }

    public boolean isPortal(){
      //  System.out.println("isPortal");
        for (int i = 0; i < numberOfAngles; i++) {
            if(isIntersectionBetweenHeroAndPortal(i) == true){
                writeToFileGameStatus(gameLevel + "|" + heroLives);
             //   System.out.println(heroLives);
                System.out.println("isPortal true -- next level");
                return true;
            }
        }

        return false;
    }

    private ArrayList<Enemy> getEnemiesFromCurrentLevel(){
     //   System.out.println("getEnemiesFromCurrentLevel");
        ArrayList<Enemy> enemiesFromLevel = new ArrayList<>();
        for(Enemy oneEnemy: enemies){
            if(oneEnemy.getLvl() == gameLevel){
                enemiesFromLevel.add(oneEnemy);
            }
        }
        return enemiesFromLevel;
    }


  /*
  private boolean isIntersectionBetweenHeroAndEnemy(int angle){
        int x0 = deltX[angle]+(int)myHero.getHero_x();
        int y0 = deltY[angle]+(int)myHero.getHero_y();
        for(Enemy oneEnemy: enemiesFromCurrentLevel) {
            if (x0 >= oneEnemy.getX() && x0 <= oneEnemy.getX() + oneEnemy.getEnemyImg().getWidth(null)
                    && y0 >= oneEnemy.getY() && y0 <= oneEnemy.getY() + oneEnemy.getEnemyImg().getHeight(null)) {
                return true;
            }

            if (isIntersectionWithEnemyByDiagonal() == true) {
                return true;
            }
        }
        return false;
    }

    private boolean isIntersectionWithEnemyByDiagonal(){

        for(Enemy oneEnemy: enemiesFromCurrentLevel) {
            int xFirst = (int) myHero.getHero_x();
            int xLast = (int) myHero.getHero_x() + myHero.getHeroImage().getWidth(null);
            int yDiagonal = (int) myHero.getHero_y() + myHero.getHeroImage().getHeight(null) / 2;

            for (int x = xFirst; x <= xLast; x++) {
                if (x >= oneEnemy.getX() && x <= oneEnemy.getX() + oneEnemy.getEnemyImg().getWidth(null)
                        && yDiagonal >= oneEnemy.getY() && yDiagonal <= oneEnemy.getY() + oneEnemy.getEnemyImg().getHeight(null)) {
                    return true;
                }
            }
            int yFirst = (int) myHero.getHero_y();
            int yLast = (int) myHero.getHero_y() + myHero.getHeroImage().getHeight(null);
            int xDiagonal = (int) myHero.getHero_x() + myHero.getHeroImage().getWidth(null) / 2;

            for (int y = yFirst; y <= yLast; y++) {
                if (xDiagonal >= oneEnemy.getX() && xDiagonal <= oneEnemy.getX() + oneEnemy.getEnemyImg().getWidth(null)
                        && y >= oneEnemy.getY() && y <= oneEnemy.getY() + oneEnemy.getEnemyImg().getHeight(null)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isEnemy(){
        for (int i = 0; i < numberOfAngles; i++) {
            if(isIntersectionBetweenHeroAndEnemy(i) == true){
                System.out.println("isEnemy true -- dying");
                return true;
            }
        }
        return false;
    }
   */
    public boolean isEnemy(){
        for(Enemy theEnemy : enemiesFromCurrentLevel){
        if(getDistance(myHero.getHeroCenter_x()+(int)myHero.getHero_x(), myHero.getHeroCenter_y()+(int)myHero.getHero_y(),
                theEnemy.getEnemyCenter_x()+(int)theEnemy.getX(), theEnemy.getEnemyCenter_y()+(int)theEnemy.getY())<=myHero.getHeroRadius()+theEnemy.getEnemyRadius()){
            return true;
        }
        }
        return false;
    }

    private boolean isWall(){
        if(myHero.getHero_x()+myHero.getDx()<=BORDER+BRICK_SIZE*outsideWallCoef || myHero.getHero_y()+myHero.getDy()<=BORDER+BRICK_SIZE*outsideWallCoef){
            return true;
        }
        int height = calculateMazeLength(this.bricks.size());
        int length = calculateMazeLength(this.bricks.get(0).length());
        if(myHero.getHero_x()+myHero.getHeroImage().getWidth(null)+myHero.getDx() >= BORDER+length-BRICK_SIZE*outsideWallCoef ||
                myHero.getHero_y()+myHero.getHeroImage().getHeight(null)+myHero.getDy() >= BORDER+height-BRICK_SIZE*outsideWallCoef){
            return true;
        }

       /* if(myHero.getDy()>0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i;
            }
        } else if (myHero.getDy()<0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i+180;
            }
        }

        if(myHero.getDx()>0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i-90;
            }
        } else if (myHero.getDx()<0){
            for(int i=0; i<checkedAngles.length; i++) {
                checkedAngles [i] = i+90;
            }
        }
        */

        for (int i = 0; i < numberOfAngles; i++) {
            if(isInnerWallForEllipse(i) == 1)
            {
                return true;
            }
        }

        return false;
    }

    private int isInnerWallForEllipse(int angle){

        int x_center = myHero.getHeroImage().getWidth(null)/2;
        int y_center = myHero.getHeroImage().getHeight(null)/2;

        double rX = myHero.getHeroImage().getWidth(null)*cosOfAngle[angle]/2;
        double rY = myHero.getHeroImage().getHeight(null)*sinOfAngle[angle]/2;

        int deltX = x_center + (int)(rX);
        int deltY = y_center + (int)(rY);

        //hero_x - реальная координата персонажа в окне
        //hero_x-BORDER-BRICK_SIZE*outsideWallCoef - координата персонажа относительно начала лабиринта

        int x0 = (int)(myHero.getDx()+deltX+myHero.getHero_x()-BORDER-BRICK_SIZE*outsideWallCoef);
        int y0 = (int)(myHero.getDy()+deltY+myHero.getHero_y()-BORDER-BRICK_SIZE*outsideWallCoef);
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

//        int enemyCount=0;
//        Point startPoint=new Point (0,0),finishPoint = new Point(0,0);

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

                    if (symbolsArray[i] == 'E') {
                        g2d.setColor(corridorColor);
                        //g2d.setColor(enemyColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);

                        map[j][i] = 7;
                    }

                    if (symbolsArray[i] == 'P') {
                        g2d.setColor(corridorColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        int picturePortShift = 7;
                        portal_x = x+(currentBRICK_SIZE_X/ picturePortShift)/2;
                        portal_y = y+(currentBRICK_SIZE_Y/ picturePortShift)/2;
                        int pictureLength = picturePortShift-1;

                        g2d.drawImage(portal,portal_x,portal_y, pictureLength*currentBRICK_SIZE_X/picturePortShift, pictureLength*currentBRICK_SIZE_Y/picturePortShift,this);

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

   //     drawParameters(g2d);
        drawLives(g2d);

        testEnemy();
        if (inGame) {
            playGame(g2d);
        } else {

        }
        if(dying == true){
            g2d.setColor(Color.YELLOW);
            Font myFont = new Font("Calibri", Font.BOLD, 48);
            g2d.setFont(myFont);
            g2d.fillRect(200, 300, 400, 100);
            g2d.setColor(Color.BLUE);
            g2d.drawString("Game over!!!", 280, 360);
            myFont = new Font("Calibri", Font.BOLD, 18);
            g2d.setFont(myFont);
            g2d.drawString("Press Enter to continue.", 310, 390);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void drawLives(Graphics2D g2d){
        g2d.setColor(Color.RED.brighter());
        Font myFont = new Font("Calibri", Font.BOLD, 32);
        g2d.setFont(myFont);
        g2d.drawString("hero_lives = " + heroLives + "     hero_speed = " + myHero.getHeroSpeed(gameLevel-1), 40, 20);
      //  g2d.drawString("hero_lives_on_level_start = " + heroLivesOnLevelStart, 40, 40);

    }

    private void drawParameters(Graphics2D g2d){
        g2d.setColor(Color.YELLOW);
        Font myFont = new Font("Calibri", Font.BOLD, 24);
        g2d.setFont(myFont);;
        g2d.drawString("hero_x = " + myHero.getHero_x(), 40, 200);
        g2d.drawString("hero_y = " + myHero.getHero_y(), 40, 220);
        g2d.drawString("hero_DX = " + myHero.getDx(), 40, 240);
        g2d.drawString("hero_DY = " + myHero.getDy(), 40, 260);

    }

    private void testEnemy(){

    }
    private void playGame(Graphics2D g2d) {

        if (dying) {

        } else {

            //moveHero();
            drawHero(g2d);
            drawEnemy(g2d);
            //    moveGhosts(g2d);
        }
    }

    private void moveHero() {

    }

    private void drawHero(Graphics2D g2d) {
        drawNewHero(g2d);
    }

    private void drawNewEnemy(Graphics2D g2d) {
       // g2d.drawImage(hero, myHero.getHero_x() + 1, myHero.getHero_y() + 1, this);

        for (Enemy enemy : enemies) {
            if(enemy.isVisible() && inGame == true && enemy.getLvl() == gameLevel )
                g2d.drawImage(enemy.getEnemyImg(),(int)enemy.getX(),(int)enemy.getY(),this);
                //g2d.drawImage();
            enemy.setVisible(true);
        }
    }

    private void drawEnemy(Graphics2D g2d) {
        drawNewEnemy(g2d);
    }

    private void drawNewHero(Graphics2D g2d) {
        g2d.drawImage(myHero.getHeroImage(), (int)myHero.getHero_x() + 1, (int)myHero.getHero_y() + 1, this);

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
        this.myHero.setDy(req_dy);
    }

    public void setReq_dx(int req_dx) {
        this.myHero.setDx(req_dx);
    }
}

