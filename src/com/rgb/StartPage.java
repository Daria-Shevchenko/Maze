package com.rgb;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;


/**
 * Created by Shevchenko Daria on 22.05.2020.
 */
public class StartPage extends JFrame{

    //Sound panel;
    static Sound loseMusic = new Sound(new File("src/music/start.wav"));
    static Sound winMusic = new Sound(new File("src/music/Queen.wav"));

    JPanel panel1;
    Font titleFont = new Font("Times New Roman", Font.PLAIN,90);
    Font normalFont = new Font("Times New Roman", Font.PLAIN,30);

    private final int width = 1050;
    private final int height = 700;

    private ArrayList<ArrayList> bricksLevels = new ArrayList<ArrayList>();

    public Maze panelWithMaze;

    private int lives = 0;

    StartPage(){}

    StartPage(String title){
        super(title);
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

    /**
     * start program
     */
    public void start(){
        winMusic.stop();
        loseMusic.stop();
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
     * Start mainPage
     */
    private void startPage(){
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
        startButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                firstMaze();
            }
        });


        JButton instruction =new JButton("INSTRUCTION");
        instruction.setBounds(420,490,200,50);
        instruction.setBackground(Color.black);
        instruction.setForeground(Color.WHITE);
        instruction.setFont(new Font("Times New Roman", Font.BOLD,20));
        instruction.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                instructions();

            }
        });

        panel1.add(instruction);
        panel1.add(titleSmall);
        panel1.add(title);
        panel1.add(startButton);
        panel1.setLayout(null);

    }

    /**
     * upload page with maze
     * */
    private void firstMaze(){
        panel1.removeAll();
        revalidate();
        repaint();
        panelWithMaze.setInGame(true);
        panelWithMaze.setBounds(10,10, 950,700);



        JButton back =new JButton("I I");
      //  back.setIcon(new ImageIcon("pause"));
        back.setBounds(975,18,50,50);
        back.setBackground(Color.black);
        back.setForeground(Color.WHITE);

        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                pausePage();
            }
        });
        panel1.add(back);
        panel1.add(panelWithMaze);
        panel1.setLayout(null);
        revalidate();
        repaint();
    }

    /**
     * open winning page
     */
    public void endPageWin(){
        winMusic.setVolume(0.98f);
        if(winMusic.isPlaying() == false){winMusic.play();}
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

        ImageIcon imageIcon = new ImageIcon("src/images/hero/gg4.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(60,60,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        JLabel img1 = new JLabel(imageIcon);
        img1.setBounds(470,300,60,60);
        panel1.add(img1);


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
     * open loser page
     */
    public void endPageLoser(){
        if(loseMusic.isPlaying() == false){loseMusic.play();}
        panelWithMaze.setInGame(false);
        panel1.removeAll();
        revalidate();
        repaint();

        panel1.setBackground(new Color(10, 10, 10));

        JLabel title = new JLabel("LOSER!!");
        title.setBounds(345,100,600,150);
        title.setForeground(Color.white);
        title.setFont(titleFont);

        ImageIcon imageIcon = new ImageIcon("src/images/hero/gg1.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(60,60,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        JLabel img1 = new JLabel(imageIcon);
        img1.setBounds(470,300,60,60);
        panel1.add(img1);



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

    /**method that recreate window
     */
    private void reCreateWindow(){
        panelWithMaze = new Maze(bricksLevels);
        this.addKeyListener(new TAdapter(panelWithMaze,this));
        setFocusable(true);
        setLocationRelativeTo(null);
    }

    /**
     * open pause page
     */
    public void pausePage(){
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

    /**
     * open page with instructions
     */

    public void instructions(){
        panel1.removeAll();
        revalidate();
        repaint();


        JTextPane textArea = new JTextPane();
        textArea.setText("Game instruction" + "\n" +
                "Aim of this game is to  go through all six colorful mazes and collect all colors!" + "\n" +
                "\n" +"Useful keys:" + "\n" +
                "Movement of hero -  ↑ up,  ↓ down, → right, ← left" + "\n" +
                "Collect lives, go through the portal – space key" + "\n" +
                "Pause game – escape key" + "\n" +
                "Increase speed – A" + "\n" +
                "Decrease speed – D" + "\n" +
                "Set default speed – S");
        textArea.setFont(new Font("Times New Roman", Font.PLAIN,20));
        textArea.setBounds(220,100,600,300);

        textArea.setBackground(Color.black);
        textArea.setForeground(Color.WHITE);

        SimpleAttributeSet aSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(aSet, "Times New Roman");
        StyleConstants.setAlignment(aSet, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontSize(aSet, 32);

        SimpleAttributeSet bSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
        StyledDocument doc = textArea.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), bSet, false);
        doc.setParagraphAttributes(0, 12, aSet, false);
        doc.setParagraphAttributes(100, 12, aSet, false);

        textArea.setOpaque(false);
        textArea.setEditable(false);

        panel1.add(textArea);



        JButton continueGame =new JButton("BACK");
        continueGame.setBounds(420,500,200,50);
        continueGame.setBackground(Color.black);
        continueGame.setForeground(Color.WHITE);
        continueGame.setFont(new Font("Times New Roman", Font.BOLD,20));
        continueGame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                start();
            }
        });


        panel1.add(continueGame);


        panel1.setLayout(null);
        revalidate();
        repaint();
    }

}
