package com.company;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            StartPage ex = new StartPage("Colorport");
            ex.setVisible(true);
        });
    }
}
