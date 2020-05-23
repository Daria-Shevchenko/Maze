package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StartTheGame extends JFrame {

    ArrayList<String> bricks = read("src/mazeFiles/maze_level_1.txt");

    public StartTheGame() {
        initUI();
    }

    private void initUI() {

        MazeGame panelWithMaze = new MazeGame(bricks);
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