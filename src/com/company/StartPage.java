package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by Shevchenko Daria on 22.05.2020.
 */
public class StartPage extends JFrame {

    JPanel panel1;
    Font titleFont = new Font("Times New Roman", Font.PLAIN,90);
    Font normalFont = new Font("Times New Roman", Font.PLAIN,30);



    // конструктор класу для апп, створення основного вікна
    StartPage(){
        super("RGB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,500);

        start();

    }

    public void start(){
        if (panel1!=null) {
            remove(panel1);
        }
        setPreferredSize(new Dimension(800,600));

        startPage();
        add(panel1);
        this.pack();
        setVisible(true);
    }

    /**
     * запускає стартову сторінку
     */
    private void startPage(){

        panel1 = new JPanel();
        panel1.setBackground(Color.black);

        JLabel title = new JLabel("RGB");
        title.setBounds(310,100,600,150);
        title.setForeground(Color.WHITE);
        title.setFont(titleFont);

        JButton startButton =new JButton("START!");
        startButton.setBackground(Color.yellow);
        startButton.setForeground(Color.BLACK);
        startButton.setFont(normalFont);

        startButton.setBounds(300,400,200,80);
        startButton.addActionListener(e -> firstMaze());

        panel1.add(title);
        panel1.add(startButton);
        panel1.setLayout(null);

    }

    /**
     * відкриває 1 лабіринт
     */
    private void firstMaze(){
        panel1.removeAll();
        revalidate();
        repaint();


        JButton back =new JButton("Back");
        back.setBounds(650,500,95,30);
        back.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                start();

            }
        });
        panel1.add(back);
        panel1.setLayout(null);
        revalidate();
        repaint();
    }



}
