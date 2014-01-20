/*
 * Amanda McWhorter
 * CSC 413.02
 * Wingman Project
 * May 2, 2013
 * Enemy.java
 */
package Wingman1942;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Rectangle;
import javax.sound.sampled.*;
import java.applet.Applet;
import java.applet.*;


public class Enemy {

    private String craft = "Resources/enemy1_1.png";

    private int x;
    private int y;
    private int width;
    private int height;
    private boolean visible;
    private Image image;
    private ArrayList enemyBullet;
    

    public Enemy(int x, int y) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(craft));
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        visible = true;
        this.x = x;
        this.y = y;
        enemyBullet = new ArrayList();
        
        enemyFire();
    }


    public void update() {
        if (y > 480) 
            y = 0;           
        y += 2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public ArrayList getEnemyBullets() {
        return enemyBullet;
    }
    
    public void enemyFire() {
            enemyBullet.add(new Bullet(x + width/2, y + height));
            
    }
}

