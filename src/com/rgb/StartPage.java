package com.rgb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Shevchenko Daria on 22.05.2020.
 */
public class StartPage extends JFrame{

    JPanel panel1;
    Font titleFont = new Font("Times New Roman", Font.PLAIN,90);
    Font normalFont = new Font("Times New Roman", Font.PLAIN,30);

    private final int width = 1050;
    private final int height = 700;

    private ArrayList<ArrayList> bricksLevels = new ArrayList<ArrayList>();

    public Maze panelWithMaze;

    private int lives = 0;

    StartPage(){}
    // конструктор класу для апп, створення основного вікна
    StartPage(String title){
        super(title);
    //    System.out.println("StartPage - it is Constructor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                panelWithMaze.setInGame(false);
                panelWithMaze.setHeroLives(panelWithMaze.getHeroLivesOnLevelStart());
            }

            @Override
            public void windowClosing(WindowEvent e) {
                panelWithMaze.setInGame(false);
                panelWithMaze.setHeroLives(panelWithMaze.getHeroLivesOnLevelStart());
            }
        });
        this.setSize(width,height);


        bricksLevels.add(read("src/mazeFiles/maze_level_1.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_2.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_3.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_4.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_5.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_6.txt"));
      //  panelWithMaze = new Maze(bricksLevels);
      //  setFocusable(true);
      //  setLocationRelativeTo(null);
        start();


    }


    private static ArrayList<String> read(String filename){
        ArrayList<String> lines = new ArrayList<String>();
        try{
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = br.readLine()) != null){
                lines.add(line);
            }
            in.close();
        } catch (Exception e){
            System.err.println("Error: "+ e.getMessage());
        }

        return lines;
    }

    // метод, що розпочинає роботу
    public void start(){
     //   System.out.println("start");
     //   remove(panelWithMaze);
        panelWithMaze = null;
        if (panel1!=null) {
            remove(panel1);
            panel1=null;
        }
        setPreferredSize(new Dimension(width,height));
        reCreateWindow();
        startPage();
        add(panel1);

        this.pack();
        panel1.setVisible(true);
        this.setVisible(true);
    }

    /**
     * запускає стартову сторінку
     */
    private void startPage(){
     //   System.out.println("startPage");
        panel1 = new JPanel();
        panel1.setBackground(Color.black);

        JLabel title = new JLabel("COLORPORT");
        title.setBounds(250,100,600,150);
        title.setForeground(Color.WHITE);
        title.setFont(titleFont);

        JLabel titleSmall = new JLabel("in search of happiness...");
        titleSmall.setBounds(370,170,600,150);
        titleSmall.setForeground(Color.WHITE);
        titleSmall.setFont(new Font("Times New Roman", Font.PLAIN, 30));

        JButton startButton =new JButton("START!");
        startButton.setBackground(new Color(245, 183, 15));
        startButton.setForeground(Color.BLACK);
        startButton.setFont(normalFont);

        startButton.setBounds(420,400,200,80);
        startButton.addActionListener(e -> firstMaze());

        panel1.add(titleSmall);
        panel1.add(title);
        panel1.add(startButton);
        panel1.setLayout(null);

    }

    /**
     * відкриває 1 лабіринт
     */
    private void firstMaze(){
     //   System.out.println("firstMaze");
        panel1.removeAll();
        revalidate();
        repaint();
        panelWithMaze.setInGame(true);
        System.out.println("in game - " + panelWithMaze.isInGame());
        panelWithMaze.setBounds(160,10, 950,700);
        panel1.add(panelWithMaze);

        JLabel lev = new JLabel("LEVEL: " + panelWithMaze.gameLevel);
        lev.setBounds(10,90,100,30);
        lev.setFont(new Font("Times New Roman", Font.PLAIN,20));
        lev.setForeground(Color.white);
        panel1.add(lev);

        ImageIcon imageIcon = new ImageIcon("src/images/other/heart_red_s.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        JLabel img1 = new JLabel(imageIcon);
        img1.setBounds(10,150,50,50);
        panel1.add(img1);



        JButton back =new JButton("I I");
      //  back.setIcon(new ImageIcon("pause"));
        back.setBounds(10,10,50,50);
        back.setBackground(Color.black);
        back.setForeground(Color.WHITE);

        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                pausePage();

            }
        });
        panel1.add(back);
        panel1.setLayout(null);
        revalidate();
        repaint();
    }

    /**
     * відкриває фінальну сторінку з виграшем
     */
    public void endPageWin(){
     //   System.out.println("endPageWin");
        panel1.removeAll();
        panelWithMaze.removeAll();
        panelWithMaze.revalidate();
        panelWithMaze.repaint();
        revalidate();
        repaint();

        panel1.setBackground(new Color(219, 136, 42));

        JLabel title = new JLabel("WINNER!!");
        title.setBounds(290,100,600,150);
        title.setForeground(Color.black);
        title.setFont(titleFont);

        JLabel img = new JLabel(new ImageIcon("src/images/hero/gg4s.png"));
        img.setBounds(470,300,60,60);
        panel1.add(img);

        JLabel titleSmall = new JLabel("now you are happy!");
        titleSmall.setBounds(370,170,600,150);
        titleSmall.setForeground(Color.black);
        titleSmall.setFont(new Font("Times New Roman", Font.PLAIN, 30));

        JButton startButton =new JButton("START AGAIN!");
        startButton.setBackground(new Color(43, 99, 37));
        startButton.setForeground(Color.BLACK);
        startButton.setFont(normalFont);

        startButton.setBounds(370,400,250,80);
        startButton.addActionListener(e -> start());

        panel1.add(titleSmall);
        panel1.add(title);
        panel1.add(startButton);


        panel1.setLayout(null);
        revalidate();
        repaint();
    }

    /**
     * відкриває фінальну сторінку з програшем
     */
    public void endPageLoser(){
    //    System.out.println("endPageLoser");
        panel1.removeAll();
        revalidate();
        repaint();

        panel1.setBackground(new Color(10, 10, 10));

        JLabel title = new JLabel("LOSER!!");
        title.setBounds(345,100,600,150);
        title.setForeground(Color.white);
        title.setFont(titleFont);

        JLabel img = new JLabel(new ImageIcon("src/images/hero/gg1s.png"));
        img.setBounds(470,300,60,60);
        panel1.add(img);

        JLabel titleSmall = new JLabel("life is full of obstacles, but you shouldn't give up");
        titleSmall.setBounds(315,170,600,150);
        titleSmall.setForeground(Color.WHITE);
        titleSmall.setFont(new Font("Times New Roman", Font.PLAIN, 20));



        JButton startButton =new JButton("TRY AGAIN!");
        startButton.setBackground(new Color(60, 90, 107));
        startButton.setForeground(Color.BLACK);
        startButton.setFont(normalFont);

        startButton.setBounds(380,400,250,80);
        startButton.addActionListener(e -> start());


        panel1.add(titleSmall);
        panel1.add(title);
        panel1.add(startButton);

        panel1.setLayout(null);
        revalidate();
        repaint();
    }

    private void reCreateWindow(){
    //    System.out.println("reCreationWindow");
        panelWithMaze = new Maze(bricksLevels);
        this.addKeyListener(new TAdapter(panelWithMaze,this));
        setFocusable(true);
        setLocationRelativeTo(null);
    }

    /**
     * відкриває фінальну сторінку з паузою
     */
    public void pausePage(){
     //   System.out.println("pausePage");
        panelWithMaze.setInGame(false);
        lives = panelWithMaze.getHeroLivesOnLevelStart();
        panel1.removeAll();
        revalidate();
        repaint();

        JButton startAgain =new JButton("START AGAIN");
        startAgain.setBounds(420,200,200,50);
        startAgain.setBackground(Color.black);
        startAgain.setForeground(Color.WHITE);
        startAgain.setFont(new Font("Times New Roman", Font.BOLD,20));

        startAgain.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                reCreateWindow();
                firstMaze();
                panelWithMaze.setHeroLives(lives);
            }
        });

        JButton continueGame =new JButton("CONTINUE");
        continueGame.setBounds(420,300,200,50);
        continueGame.setBackground(Color.black);
        continueGame.setForeground(Color.WHITE);
        continueGame.setFont(new Font("Times New Roman", Font.BOLD,20));
        continueGame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                firstMaze();
            }
        });


        panel1.add(continueGame);
        panel1.add(startAgain);

        panel1.setLayout(null);
        revalidate();
        repaint();
    }

}
