/*
 * Amanda McWhorter
 * CSC 413.02
 * Wingman Project
 * May 2, 2013
 * Wingman1942.java
 */
package Wingman1942;

import javax.swing.JFrame;

public class Wingman1942 extends JFrame {

    public Wingman1942() {
        add(new Environment());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setTitle("Wingman 1942");
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        Wingman1942 gm1942 = new Wingman1942();
       // gm1942.start();
    }
}