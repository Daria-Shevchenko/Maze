package com.rgb;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 *
 * Maze - element of the game that is responsible for drawing
 * all other elements (maze, hero, enemies, hearts, portal) on game screen
 * @author Kate Kolokhina and Khrystyna Boiko and Daria Shevchenko
 */
public class Maze extends JPanel implements ActionListener {



    /**
     * Sound panel
     * intoMouster - sound for collidion with monster
     */
    Sound intoMonster = new Sound(new File("src/music/intoMonster.wav"));
    /*alpha - portal transparency level*/
    private int alpha = 255;
    /*alphaSign - direction of transparency change*/
    private int alphaSign = 1;
    private int cheat = 0;
    public void setCheat (int newValue) {
        this.cheat = newValue;
    }
    public final static Sound lev1 = new Sound(new File("src/music/lev1.wav"));
    public final static Sound lev2 = new Sound(new File("src/music/lev2.wav"));
    public final static Sound lev3 = new Sound(new File("src/music/lev3.wav"));
    public final static Sound lev4 = new Sound(new File("src/music/lev4.wav"));
    public final static Sound lev5 = new Sound(new File("src/music/lev5.wav"));
    public final static Sound lev6 = new Sound(new File("src/music/lev6.wav"));
    /**enemySize is size of enemy picture*/
    private int enemySize;
    /**enemyProportion is proportion of enemy picture to the cell*/
    private double enemyProportion = 0.8;
    /**enemies is arraylist of all enemies*/
    private  ArrayList<Enemy> enemies = new ArrayList<>();
    /**sf is variable which has start and finish points of enemy movement*/
    private Map<Point, Point> sf;
    /**bricksLevels is an arraylist of arraylists with maze map strings*/
    private ArrayList<ArrayList> bricksLevels;
    /**gameLevel is level of the game*/
    public int gameLevel = 0;
    /**MAX_gamelevel is maximum level of the game*/
    private int MAX_gamelevel = 6;
    /**gameFinished is boolean for finish of the game*/
    private boolean gameFinished = false;
    /**inGame is boolean for game working*/
    private boolean inGame = false;
    /**dying is boolean for lose the game*/
    private boolean dying = false;
    /**canMove is boolean for ability to hero move */
    private boolean canMove;
    /**myHero is hero object*/
    private Hero myHero;
    /**heart1, heart2 are heart objects*/
    private Heart heart1 = new Heart(),
            heart2 = new Heart();
    /**defaultHeroLives is number of lives that hero has at the start of the game*/
    private int defaultHeroLives = 3;
    /**heroLives is current quantity of hero lives*/
    private int heroLives = defaultHeroLives;
    /**heroLivesOnLevelStart is quantity of hero lives at the beginning of the level*/
    private int heroLivesOnLevelStart = defaultHeroLives;
    /**pathToFileWithGameStatus is path to file with game status (level and hero lives)*/
    private String pathToFileWithGameStatus = "src/mazeFiles/levelStatus.txt";
    /**timer is timer for game*/
    private Timer timer;
    /**map is array with information about cells in maze
       0 - corridor
       1 - wall
       2 - portal
       3 - heart
       7 - corridor with enemy
       9 - outside wall
    */
    public int [][] map;
    /**bricks is arraylist of strings (map of maze)*/
    ArrayList<String> bricks;
    /**pictureShift is shift of picture from cell border*/
    private int pictureShift = 6;
    /**pictureLengthInCell is relative length of picture to cell length*/
    private int pictureLengthInCell = 4;
    /**heart, portal are images of heart and portal*/
    private Image heart,portal;
    /**portal_x - X coordinate of portal
     *portal_y - Y coordinate of portal*/
    private int portal_x=0, portal_y=0;
    /**brick_sizes_for_levels is array of brick sizes for all levels of the game*/
    private static final int [] brick_sizes_for_levels ={17, 15, 13, 13, 12, 11};
    /**BRICK_SIZE is size of brick on current level*/
    private static int BRICK_SIZE;
    /**coefficientCorridor is the number that shows how many times the corridor is larger than the inner wall*/
    private static final int coefficientCorridor = 4;
    /**outsideWallCoef is the number that shows how many times the outside wall is larger than the inner wall*/
    private static final int outsideWallCoef = 1;
    /**BORDER is length of border on jpanel*/
    private static final int BORDER = 10;
    /**BORDER_RIGHT is length of border on jpanel*/
    private static final int BORDER_RIGHT = 150;
    /**mazeWidth is maze width*/
    private int mazeWidth = 0;
    /**mazeHeight is maze height*/
    private int mazeHeight = 0;
    /**enemyW,enemyH are width and height of enemy*/
    /**numberOfAngles is all angles on circle*/
    private int numberOfAngles = 360;
    /**cosOfAngle is array of cosine of all angles on circle*/
    private double [] cosOfAngle = new double [numberOfAngles];
    /**sinOfAngle is array of sine of all angles on circle*/
    private double [] sinOfAngle = new double [numberOfAngles];
    /**deltX is distance on X to point on circle border*/
    private int [] deltX = new int [numberOfAngles];
    /**deltY is distance on Y to point on circle border*/
    private int [] deltY = new int [numberOfAngles];
    /**enemiesFromCurrentLevel is list of enemies on current level*/
    ArrayList<Enemy> enemiesFromCurrentLevel = new ArrayList<Enemy>();
    /**portal_images_for_levels is array of portal images for all levels*/
    private Image [] portal_images_for_levels = {
            new ImageIcon("src/images/portal/portal1.png").getImage(), new ImageIcon("src/images/portal/portal2.png").getImage(),
            new ImageIcon("src/images/portal/portal4.png").getImage(), new ImageIcon("src/images/portal/portal3.png").getImage(),
            new ImageIcon("src/images/portal/portal5.png").getImage(), new ImageIcon("src/images/portal/portal6.png").getImage()};
    /**mazeColorsForWalls is array of colors for walls for all levels*/
    Color [] mazeColorsForWalls = {Color.DARK_GRAY, new Color(28, 109, 200), new Color(113, 2, 155), new Color(20, 150, 14), new Color(172, 13, 17), new Color(193, 96, 6)};
    /**mazeColorsForCorridors is array of colors for corridors for all levels*/
    Color [] mazeColorsForCorridors = {Color.LIGHT_GRAY, new Color(134, 207, 247), new Color(202, 171, 254), new Color(154, 255, 125), new Color(240, 174, 210), new Color(250, 197, 86)};
    /**corridorColor is color of corridors on current level*/
    Color corridorColor;
    /**wallColor is color of walls on current level*/
    Color wallColor;
    /**Create a maze game
     *@param bricksLevels - an arraylist of arraylists with maze map strings*/
    public Maze(ArrayList<ArrayList> bricksLevels) {
        this.bricksLevels = bricksLevels;
        /*Set delay to timer and start the timer*/
        timer  = new Timer(5, this);
        timer.start();
        /*Create list of enemies*/
        listOfEnemies();
        /*Prepare game for next level*/
        nextLevel();
        /*Calculate and fill sinOfAngle, cosOfAngle, deltX, deltY with values*/
        initAnglesValues();
    }
    /**Calculate and fill sinOfAngle, cosOfAngle, deltX, deltY with values for each angle on the circle*/
    private void initAnglesValues(){
        /*x_center is half width of hero image*/
        int x_center = myHero.getHeroImage().getWidth(null) / 2;
        /*y_center is half height of hero image*/
        int y_center = myHero.getHeroImage().getHeight(null) / 2;
        for(int i=0; i<numberOfAngles; i++) {
            /*Fill sinOfAngle, cosOfAngle with cosines and sines*/
            sinOfAngle[i] = findSin(i);
            cosOfAngle[i] = findCos(i);
            /*rX, rY are distances from the center line of picture to point on circle border*/
            double rX = myHero.getHeroImage().getWidth(null) * cosOfAngle[i] / 2;
            double rY = myHero.getHeroImage().getHeight(null) * sinOfAngle[i] / 2;
            /*Fill deltX, deltY with values*/
            deltX [i] = x_center + (int) (rX);
            deltY [i] = y_center + (int) (rY);
        }
    }
    /**Calculate and return cosine of angle
     * @param degree - angle which cosine will be calculated
     * @return An integer representing cosine of angle*/
    private double findCos(double degree){
        /**Converting values to radians */
        double a = Math.toRadians(degree);
        double cos = Math.cos(a);
        /**Consider special angles in calculating*/
        if(degree!=0 && degree % 180 != 0 && degree % 90 == 0){cos = 0;}
        return cos;
    }
    /**Calculate and return sine of angle
     * @param degree - angle which sine will be calculated
     * @return An integer representing sine of angle*/
    private double findSin(double degree){
        /**Converting values to radians */
        double a = Math.toRadians(degree);
        double sin = Math.sin(a);
        /**Consider special angles in calculating*/
        if(degree == 0 || degree % 180 == 0){sin = 0;}
        return sin;
    }
    /**Gets distance between two points
     * @param x1 - X coordinate of first point
     * @param y1 - Y coordinate of first point
     * @param x2 - X coordinate of second point
     * @param y2 - Y coordinate of second point
     * @return An integer representing a distance between two points calculated by their X and Y coordinates*/
    private int getDistance(int x1, int y1, int x2, int y2){
        return (int)Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    /**Gets boolean of game finish
     * @return A boolean representing game finish*/
    public boolean isGameFinished() {
        return gameFinished;
    }
    /**Set gameFinished value
     * @param gameFinished - value for game finish boolean*/
    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }
    /**Gets boolean of game working
     * @return A boolean representing game working*/
    public boolean isInGame() {
        return inGame;
    }
    /**Set inGame value
     * @param inGame - value for game working boolean*/
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
    /**Gets boolean of hero death
     * @return A boolean representing if hero is alive or dead*/
    public boolean isDying() {
        return dying;
    }
    /**Set dying value
     * @param dying - value for hero live or death*/
    public void setDying(boolean dying) {
        this.dying = dying;
    }
    /**Add +1 to hero lives and write new information to file*/
    public void addHeroLives(){
        heroLives++;
        writeToFileGameStatus((gameLevel-1) + "|" + heroLives);
    }
    /**Set heroLives and heroLivesOnLevelStart with new values,
     * write new information to file
     * @param new_heroLives - quantity of hero lives*/
    public void setHeroLives(int new_heroLives) {
        this.heroLives = new_heroLives;
        this.heroLivesOnLevelStart = new_heroLives;
        writeToFileGameStatus((gameLevel-1) + "|" + heroLives);
    }
    /**Gets quantity of hero lives at the beginning of the level
     * @return An integer representing quantity of hero lives at the beginning of the level*/
    public int getHeroLivesOnLevelStart() {
        return heroLivesOnLevelStart;
    }
    /**Gets quantity of hero lives
     * @return An integer representing quantity of hero lives*/
    public int getHeroLives(){
        return heroLives;
    }
    /**Set hero speed on current level
     * @param new_heroSpeed - new hero speed
     * @param level - current level*/
    public void setMyHeroSpeed(double new_heroSpeed, int level) {
        this.myHero.setHeroSpeed(new_heroSpeed, level);
    }
    /**Gets hero speed
       @param level - level from which the hero speed is taken
       @return An integer representing hero speed on the level*/
    public double getMyHeroSpeed(int level){
        return this.myHero.getHeroSpeed(level);
    }
    /**Writes to file game status
     * @param data - information about game level and number of hero lives*/
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

    /** Reads from the file data about game level and number of hero lives
     * @return A string representing the result of reading the file.
     * Result is empty when operation is successful and contains error if not.*/
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

    /**Loading images for maze*/
    private void loadImages() {
        Toolkit t=Toolkit.getDefaultToolkit();
        heart = t.getImage("src/images/other/heart_red_s.png");
        portal = portal_images_for_levels[this.gameLevel-1];
     }
    /**Prepare for next level. Initialize variables with correct values for that level*/
    public void nextLevel(){
        readGameStatusFromFile();
        if(gameLevel<MAX_gamelevel) {
            changeMusic();
            gameLevel++;
            BRICK_SIZE = brick_sizes_for_levels[gameLevel-1];
            heart1.setShow(true);
            heart2.setShow(true);
            enemySize = (int)(BRICK_SIZE*coefficientCorridor*enemyProportion);
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

            enemyOnMap();
            addEnemyToMaze();
            enemiesFromCurrentLevel = getEnemiesFromCurrentLevel();
            //  this.setSize(mazeWidth + 2 * BORDER, mazeHeight + 2 * BORDER);
            this.setSize(mazeWidth + 2 * BORDER+BORDER_RIGHT, mazeHeight + 2 * BORDER);
            loadImages();

        } else {
            gameOver();
        }
    }

    public void changeMusic(){
        switch(gameLevel) {
            case 0:
                if(lev1.isPlaying() == false && dying==false)
                    lev1.play();
                break;
            case 1:
                lev1.stop();
                if(lev2.isPlaying() == false && dying==false)
                    lev2.play();
                break;
            case 2:
                lev2.stop();
                if(lev3.isPlaying() == false && dying==false)
                    lev3.play();
                break;
            case 3:
                lev3.stop();
                if(lev4.isPlaying() == false && dying==false)
                    lev4.play();
                break;
            case 4:
                lev4.stop();
                if(lev5.isPlaying() == false && dying==false)
                    lev5.play();
                break;
            case 5:
                lev5.stop();
                if(lev6.isPlaying() == false && dying==false)
                    lev6.play();
                break;
            default:
                break;
        }
    }


    /**
     * Create a list of enemies for game levels
     */
    private void listOfEnemies() {
        /**
         * 1 lvl
         */
        enemies.add(new Enemy(new Point(14,4), new Point(18,4),0.6,1));
        enemies.add(new Enemy(new Point(6,6), new Point(6,8),0.6,1));
        enemies.add(new Enemy(new Point(16,10), new Point(18,10),0.6,1));
        /**
         * 2 lvl
         */
        enemies.add(new Enemy(new Point(18,2), new Point(18,4),0.5,2));
        enemies.add(new Enemy(new Point(10,4), new Point(10,8),0.5,2));
        enemies.add(new Enemy(new Point(14,12), new Point(14,16),0.47,2));

        /**
         * 3 lvl
         */
        enemies.add(new Enemy(new Point(18,6 ), new Point(22,6),0.4,3));
        enemies.add(new Enemy(new Point(12,8), new Point(16,8),0.6,3));
        enemies.add(new Enemy(new Point(4,10), new Point(4,16),0.45,3));
        enemies.add(new Enemy(new Point(10,12), new Point(10,14),0.4,3));
        enemies.add(new Enemy(new Point(22,12), new Point(22,14),0.4,3));
        /**
         * 4 lvl
         */
        enemies.add(new Enemy(new Point(14,6 ), new Point(16,6),0.3,4));
        //enemies.add(new Enemy(enemyW,enemyH,new Point(14,8), new Point(14,10),0.3,4));

        enemies.add(new Enemy(new Point(22,10), new Point(24,10),0.3,4));

        enemies.add(new Enemy(new Point(22,14), new Point(24,14),0.4,4));
        enemies.add(new Enemy(new Point(6,16), new Point(6,18),0.5,4));

        /**
         * 5 lvl
         */

        enemies.add(new Enemy(new Point(8,4 ), new Point(10,4),0.4,5));
        //enemies.add(new Enemy(enemyW,enemyH,new Point(24,6 ), new Point(26,6),0.4,5));
        enemies.add(new Enemy(new Point(6,10 ), new Point(6,14),0.4,5));
        enemies.add(new Enemy(new Point(12,12 ), new Point(16,12),0.5,5));
        enemies.add(new Enemy(new Point(12,18 ), new Point(18,18),0.5,5));

        /**
         * 6 lvl
         */

        enemies.add(new Enemy(new Point(14,6 ), new Point(18,6),0.4,6));
        enemies.add(new Enemy(new Point(14,8 ), new Point(14,10),0.2,6));
        enemies.add(new Enemy(new Point(24,8), new Point(24,14),0.4,6));
        enemies.add(new Enemy(new Point(6,12), new Point(6,16),0.4,6));
        enemies.add(new Enemy(new Point(10,16), new Point(10,20),0.2,6));
        enemies.add(new Enemy(new Point(14,20), new Point(16,20),0.2,6));
        enemies.add(new Enemy(new Point(26,18), new Point(26,22),0.5,6));

    }

    /**
     * Add enemies to the game map
     */
    private void enemyOnMap() {
        sf = new HashMap<Point, Point>();


        for (Enemy enemy : enemies) {
            enemy.setWidth(enemySize);
            enemy.setHeight(enemySize);
            enemy.setEnemyRadiusAndCenter();
        }
        for (Enemy enemy : enemies) {
            if(enemy.getLvl()==gameLevel)
                sf.put(enemy.getStart(),enemy.getFinish());
        }

        for(int i=0;i<bricks.size();i++){
            for(int k=0;k<bricks.get(0).length();k++){
                if(i==0 || i==bricks.size()-1 || k==0 || k ==bricks.get(0).length()-1)
                    map[i][k]=9;
            }
        }
    }

    /**
     * Change the coordinates of enemies trajectories from the map representing to frame representing
     */
    private void addEnemyToMaze() {

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
                    int enemy_x = x+(currentBRICK_SIZE_X/ pictureShift)/2;
                    int enemy_y = y+(currentBRICK_SIZE_Y/ pictureShift)/2;
                    Point test = new Point(i+1,j+1);

                    if (sf.containsKey(test)) {
                        for (Enemy enemy : enemies) {
                            if(enemy.getStart().equals(test)){
                                enemy.setStart(new Point(enemy_x, enemy_y));

                            }
                        }
                    }

                    if (sf.containsValue(test)) {

                        for (Enemy enemy : enemies) {
                            if(enemy.getFinish().equals(test)){
                                enemy.setFinish(new Point(x+enemySize, y+enemySize));
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
    /**If player go through all levels and win, this method works.
     * Change game status in boolean and write new information to file */
    private void gameOver(){
        inGame=false;
        gameFinished = true;
        writeToFileGameStatus(0 + "|" + defaultHeroLives);
        lev6.stop();
    }

    @Override
    /**Override method that checks when action is performed
     * @param e -- An event which indicates that some actions were done*/
    public void actionPerformed(ActionEvent e) {
        canMove = true;

        for (Enemy enemy : enemies) {
            if(enemy.isVisible() && inGame == true )
                enemy.move(mazeWidth,mazeHeight);
        }

        if(isWall()){
            canMove = false;
        }
        if(isEnemy()&& inGame == true){
            intoMonster.play();
            minusLive();
            canMove = false;
        }
        if(canMove){
            myHero.changeHero_x();
            myHero.changeHero_y();
        }
        repaint();
    }
    /**Decrease hero lives by 1 and put hero on another location.
     * If there is no lives, then it is hero death.
     * Write new information to file*/
    private void minusLive(){
        if(heroLives>1){
            heroLives--;
            writeToFileGameStatus((gameLevel-1) + "|" + heroLives);
            myHero.setHero_x(BORDER + BRICK_SIZE * outsideWallCoef + 1);
            myHero.setHero_y(BORDER + BRICK_SIZE * outsideWallCoef + 1);
        } else if(heroLives == 1){
            dying = true;
            writeToFileGameStatus((gameLevel-1) + "|" + defaultHeroLives);
        }
    }
    /**Checks if there is intersection between hero and heart by one point of hero picture
     * @param angle - angle which indicates the point on the border of hero picture to be checked
     * @return A boolean representing if there is intersection between hero and heart*/
    private boolean isIntersectionBetweenHeroAndHeart(int angle){
        int x_center = myHero.getHeroImage().getWidth(null)/2;
        int y_center = myHero.getHeroImage().getHeight(null)/2;

        double rX = myHero.getHeroImage().getWidth(null)*cosOfAngle[angle]/2;
        double rY = myHero.getHeroImage().getHeight(null)*sinOfAngle[angle]/2;

        int deltX = x_center + (int)(rX);
        int deltY = y_center + (int)(rY);

        int x0 = deltX+(int)myHero.getHero_x();
        int y0 = deltY+(int)myHero.getHero_y();

        if( heart1.isShow() ==true && x0>=heart1.getX() && x0<=heart1.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                && y0>=heart1.getY() && y0<=heart1.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
            heart1.setShow(false);

            return true;
        }
        if(heart2.isShow() ==true && x0>=heart2.getX() && x0<=heart2.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                && y0>=heart2.getY() && y0<=heart2.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
            heart2.setShow(false);

            return true;
        }

        return false;
    }
    /**Checks if there is intersection between hero and heart by diagonals
     * @return A boolean representing if there is intersection between hero and heart by diagonals*/
    private boolean isIntersectionWithHeartByDiagonal(){
        /*horizontal diagonal*/
        int xFirst = (int)myHero.getHero_x();
        int xLast = (int)myHero.getHero_x() + myHero.getHeroImage().getWidth(null);
        int yDiagonal = (int)myHero.getHero_y()+myHero.getHeroImage().getHeight(null)/2;

        for(int x=xFirst; x <=xLast; x++){
            if(heart1.isShow() ==true && x>=heart1.getX() && x<=heart1.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && yDiagonal>=heart1.getY() && yDiagonal<=heart1.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                heart1.setShow(false);

                return true;

            }
            if(heart2.isShow() ==true && x>=heart2.getX() && x<=heart2.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
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
            if(heart1.isShow() ==true && xDiagonal>=heart1.getX() && xDiagonal<=heart1.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && y>=heart1.getY() && y<=heart1.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                heart1.setShow(false);
                return true;
            }
            if(heart2.isShow() ==true && xDiagonal>=heart2.getX() && xDiagonal<=heart2.getX()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift
                    && y>=heart2.getY() && y<=heart2.getY()+BRICK_SIZE*coefficientCorridor* pictureLengthInCell / pictureShift){
                heart2.setShow(false);
                return true;
            }
        }

        return false;
    }
    /**Checks if there is intersection between hero and heart
     * @return A boolean representing if there is intersection between hero and heart*/
    public boolean isHeart(){
        for (int i = 0; i < numberOfAngles; i++) {
            if(isIntersectionBetweenHeroAndHeart(i) == true){
                return true;
            }
        }

        if(isIntersectionWithHeartByDiagonal()==true){
            return true;
        }

        return false;
    }
    /**Checks if there is intersection between hero and portal by one point of hero picture
     * @param angle - angle which indicates the point on the border of hero picture to be checked
     * @return A boolean representing if there is intersection between hero and portal*/
    private boolean isIntersectionBetweenHeroAndPortal(int angle){
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

        return false;
    }
    /**Checks if there is intersection between hero and portal by diagonals
     * @return A boolean representing if there is intersection between hero and portal by diagonals*/
    private boolean isIntersectionWithPortalByDiagonal(){
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
    /**Checks if there is intersection between hero and portal
     * @return A boolean representing if there is intersection between hero and portal*/
    public boolean isPortal(){
        for (int i = 0; i < numberOfAngles; i++) {
            if(isIntersectionBetweenHeroAndPortal(i) == true){
                writeToFileGameStatus(gameLevel + "|" + heroLives);
                return true;
            }
        }

        if(isIntersectionWithPortalByDiagonal()==true){
            return true;
        }

        return false;
    }
    /**Gets arraylist of enemies from current level
     * @return An arraylist representing all enemies from current level*/
    private ArrayList<Enemy> getEnemiesFromCurrentLevel(){
        ArrayList<Enemy> enemiesFromLevel = new ArrayList<>();
        for(Enemy oneEnemy: enemies){
            if(oneEnemy.getLvl() == gameLevel){
                enemiesFromLevel.add(oneEnemy);
            }
        }
        return enemiesFromLevel;
    }
    /**Checks if there is intersection between hero and enemy
     * @return A boolean representing if there is intersection between hero and enemy*/
    public boolean isEnemy(){
        for(Enemy theEnemy : enemiesFromCurrentLevel){
        if(getDistance(myHero.getHeroCenter_x()+(int)myHero.getHero_x(), myHero.getHeroCenter_y()+(int)myHero.getHero_y(),
                theEnemy.getEnemyCenter_x()+(int)theEnemy.getX(), theEnemy.getEnemyCenter_y()+(int)theEnemy.getY())<=myHero.getHeroRadius()+theEnemy.getEnemyRadius()){
            return true;
        }
        }
        return false;
    }
    /**Checks if there is intersection between hero and wall
     * @return A boolean representing if there is intersection between hero and wall*/
    private boolean isWall(){
        /*Checking with outside walls*/
        if(myHero.getHero_x()+myHero.getDx()<=BORDER+BRICK_SIZE*outsideWallCoef || myHero.getHero_y()+myHero.getDy()<=BORDER+BRICK_SIZE*outsideWallCoef){
            return true;
        }
        int height = calculateMazeLength(this.bricks.size());
        int length = calculateMazeLength(this.bricks.get(0).length());
        if(myHero.getHero_x()+myHero.getHeroImage().getWidth(null)+myHero.getDx() >= BORDER+length-BRICK_SIZE*outsideWallCoef ||
                myHero.getHero_y()+myHero.getHeroImage().getHeight(null)+myHero.getDy() >= BORDER+height-BRICK_SIZE*outsideWallCoef){
            return true;
        }
        if (cheat == 1) {return false;}
        /*Checking with inner walls*/
        for (int i = 0; i < numberOfAngles; i++) {
            if(isInnerWallForEllipse(i) == 1)
            {
                return true;
            }
        }

        return false;
    }
    /**Checks if there is intersection between hero and inner wall
     * @param angle - angle which indicates the point on the border of hero picture to be checked
     * @return An integer representing whether the cell, where hero is located, is inner wall or corridor*/
    private int isInnerWallForEllipse(int angle){

        int x_center = myHero.getHeroImage().getWidth(null)/2;
        int y_center = myHero.getHeroImage().getHeight(null)/2;

        double rX = myHero.getHeroImage().getWidth(null)*cosOfAngle[angle]/2;
        double rY = myHero.getHeroImage().getHeight(null)*sinOfAngle[angle]/2;

        int deltX = x_center + (int)(rX);
        int deltY = y_center + (int)(rY);

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
    /**Draw maze and elements on it using the maze map
     * @param g2d - A Graphics2D used to drawing maze and other elements on jpanel*/
    private void drawMaze(Graphics2D g2d) {
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

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
                    /*Draw walls*/
                    if (symbolsArray[i] == '#') {
                        g2d.setColor(wallColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        map[j][i] = 1;
                    }
                    /*Draw corridors*/
                    if (symbolsArray[i] == '*') {
                        g2d.setColor(corridorColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        map[j][i] = 0;
                    }
                    /*Draw corridor where enemy will be*/
                    if (symbolsArray[i] == 'E') {
                        g2d.setColor(corridorColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);

                        map[j][i] = 7;
                    }
                    /*Draw portal*/
                    if (symbolsArray[i] == 'P') {
                        g2d.setColor(corridorColor);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        int picturePortShift = 7;
                        portal_x = x+(currentBRICK_SIZE_X/ picturePortShift)/2;
                        portal_y = y+(currentBRICK_SIZE_Y/ picturePortShift)/2;
                        int pictureLength = picturePortShift-1;
                        g2d.drawImage(portal,portal_x,portal_y, pictureLength*currentBRICK_SIZE_X/picturePortShift, pictureLength*currentBRICK_SIZE_Y/picturePortShift,this);

                        Color myColour = new Color(corridorColor.getRed(), corridorColor.getGreen(), corridorColor.getBlue(), alpha);
                        g2d.setColor(myColour);
                        g2d.fillRect(x, y, currentBRICK_SIZE_X, currentBRICK_SIZE_Y);
                        map[j][i] = 2;
                    }
                    /*Draw hearts*/
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
    /**Paints all components on jpanel
     * @param g - A Graphics used to drawing on jpanel*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    /**Draws all components in different methods
     * @param g - A Graphics used to drawing on jpanel*/
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if(alpha >= 254) { alphaSign = -1;}
        if(alpha <= 1) {alphaSign = 1;}
        alpha = (int)(alpha + 2*alphaSign);
        drawMaze(g2d);

      //  drawParameters(g2d);
        drawLives(g2d);

        if (inGame) {
            playGame(g2d);
        } else {

        }
        if(dying == true){
            changeMusic();
            TAdapter.startPage.endPageLoser();

        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }
    /**Draws number of hero lives and hero speed
     * @param g2d - A Graphics2D used to drawing number of hero lives and hero speed on jpanel*/
    private void drawLives(Graphics2D g2d){
        g2d.setColor(Color.white);
        Font myFont = new Font("Calibri", Font.PLAIN, 24);
        g2d.setFont(myFont);
        g2d.drawString("LEVEL: " + gameLevel, 820, 40);

        g2d.drawImage(heart,825,100,30,30,Color.black,this);
        g2d.drawString(" x " + heroLives, 860,120);


    }
    public void userPanel(){
        JLabel lev = new JLabel("LEVEL: " + gameLevel);
        lev.setBounds(10,90,100,30);
        lev.setFont(new Font("Times New Roman", Font.PLAIN,20));
        lev.setForeground(Color.white);
       // panel1.add(lev);

        ImageIcon imageIcon = new ImageIcon("src/images/other/heart_red_s.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        JLabel img1 = new JLabel(imageIcon);
        img1.setBounds(10,150,40,40);
       // panel1.add(img1);

        JLabel heartChecker = new JLabel(" x " + getHeroLives());
        heartChecker.setBounds(55,150,100,40);
        heartChecker.setForeground(Color.white);
        heartChecker.setFont(new Font("Times New Roman", Font.PLAIN,20));
      //  panel1.add(heartChecker);

    }
    /**Draws hero location and displacement
     * @param g2d - A Graphics2D used to drawing hero location and displacement on jpanel*/
    private void drawParameters(Graphics2D g2d){
        g2d.setColor(Color.YELLOW);
        Font myFont = new Font("Calibri", Font.BOLD, 24);
        g2d.setFont(myFont);
        g2d.drawString("hero_x = " + myHero.getHero_x(), 40, 200);
        g2d.drawString("hero_y = " + myHero.getHero_y(), 40, 220);
      //  g2d.drawString("hero_DX = " + myHero.getDx(), 40, 240);
       // g2d.drawString("hero_DY = " + myHero.getDy(), 40, 260);
      //  g2d.drawString("hero_Radius = " + myHero.getDy(), 40, 260);
      //  g2d.drawString("hero_DY = " + myHero.getDy(), 40, 260);
        int i = 1;
        int k = 200;
        for(Enemy theEnemy : enemiesFromCurrentLevel) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("Enemy radius = "+theEnemy.getEnemyRadius(), 550, 40);
            g2d.drawString("Enemy center x = "+theEnemy.getEnemyCenter_x(), 550, 60);
            g2d.drawString("Enemy center y = "+theEnemy.getEnemyCenter_y(), 550, 80);
            g2d.drawString("Hero radius = "+myHero.getHeroRadius(), 550, 100);
            g2d.drawString("Hero center x = "+myHero.getHeroCenter_x(), 550, 120);
            g2d.drawString("Hero center y= "+myHero.getHeroCenter_y(), 550, 140);

            g2d.drawString("Enemy x = "+(int)theEnemy.getX(), 400, k);
            g2d.drawString("Enemy y = "+(int)theEnemy.getY(), 400, (k+20));

                g2d.setColor(Color.YELLOW);
                g2d.setFont(myFont);
                g2d.drawString(i + " radius h + e " + (myHero.getHeroRadius() + theEnemy.getEnemyRadius()), 550, k);
                g2d.drawString(i + " distance h and e " + (getDistance(myHero.getHeroCenter_x() + (int) myHero.getHero_x(), myHero.getHeroCenter_y() + (int) myHero.getHero_y(),
                        theEnemy.getEnemyCenter_x() + (int) theEnemy.getX(), theEnemy.getEnemyCenter_y() + (int) theEnemy.getY())), 550, (k + 20));
                i++;
                k = k + 50;
        }
    }
    /**Draws hero and enemies
     * @param g2d - A Graphics2D used to drawing hero and enemies on jpanel*/
    private void playGame(Graphics2D g2d) {

        if (dying) {

        } else {
            drawHero(g2d);
            drawEnemy(g2d);
        }
    }
    /**Draws hero
     * @param g2d - A Graphics2D used to drawing hero on jpanel*/
    private void drawHero(Graphics2D g2d) {
        g2d.drawImage(myHero.getHeroImage(), (int)myHero.getHero_x(), (int)myHero.getHero_y(), this);
    }
    /**Draws enemies
     * @param g2d - A Graphics2D used to drawing enemies on jpanel*/
    private void drawEnemy(Graphics2D g2d) {
        for (Enemy enemy : enemies) {
            if(enemy.isVisible() && inGame == true && enemy.getLvl() == gameLevel )
                g2d.drawImage(enemy.getEnemyImg(),(int)enemy.getX(),(int)enemy.getY(),this);
            enemy.setVisible(true);
        }
    }
    /**Calculate maze length
     * @param numberOfColons - is number of colons in maze
     * @return An integer representing maze length*/
    public int calculateMazeLength(int numberOfColons){
        int mazeLength = 2*BRICK_SIZE*outsideWallCoef + (numberOfColons-3)/2*BRICK_SIZE+((numberOfColons-3)/2+1)*BRICK_SIZE*coefficientCorridor;
        return mazeLength;
    }
    /**@return An integer representing panel width*/
    public int getPanelWidth(){
        return this.mazeWidth+2*BORDER;
    }
    /**@return An integer representing panel height*/
    public int getPanelHeight(){
        return this.mazeHeight+2*BORDER;
    }
    /**@return An integer representing panel border*/
    public int getPanelBorder(){
        return this.BORDER;
    }
    /**Set Y displacement for hero
     * @param req_dy - Y displacement of hero*/
    public void setReq_dy(int req_dy) {
        this.myHero.setDy(req_dy);
    }
    /**Set X displacement for hero
     * @param req_dx - X displacement of hero*/
    public void setReq_dx(int req_dx) {
        this.myHero.setDx(req_dx);
    }
}

