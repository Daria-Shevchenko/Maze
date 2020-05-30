package com.rgb;
/**
 *
 * Main - public class for launch the game
 * @author Shevchenko Daria
 */
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            /*ex is a frame with the game "Colorport"*/
            StartPage ex = new StartPage("Colorport");
            ex.setVisible(true);
        });
    }
}
