package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StartTheGame extends JFrame {

    private ArrayList<ArrayList> bricksLevels = new ArrayList<ArrayList>();

    public StartTheGame() {
        initUI();
    }

    private void initUI() {
       // bricksLevels.add(read("src/mazeFiles/maze1.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_1.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_2.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_3.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_4.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_5.txt"));
        bricksLevels.add(read("src/mazeFiles/maze_level_6.txt"));

        Maze panelWithMaze = new Maze(bricksLevels);
        add(panelWithMaze);
        setTitle("myMaze");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        int width = panelWithMaze.getPanelWidth();
        int height = panelWithMaze.getPanelHeight();
        //  border = panelWithMaze.getPanelBorder();
        setSize(width, height);
        setLocationRelativeTo(null);
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

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            StartTheGame ex = new StartTheGame();
            ex.setVisible(true);
        });


    }
}