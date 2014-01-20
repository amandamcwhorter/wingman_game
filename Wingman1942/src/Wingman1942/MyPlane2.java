/*
 * Amanda McWhorter
 * CSC 413.02
 * Wingman Project
 * May 2, 2013
 * MyPlane2.java
 */
package Wingman1942;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
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

public class MyPlane2 implements Observer{

    private String craft = "Resources/myplane_2.png";

    private int dx;
    private int dy;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean visible;
    private Image image;
    private ArrayList bullets;
    private int speed = 4, boom;

    public MyPlane2(int x) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(craft));
        image = ii.getImage();
        //this.image = image;
        width = image.getWidth(null);
        height = image.getHeight(null);
        bullets = new ArrayList();
        visible = true;
        this.x = x;
        y = 355;
    }


    public void update(Observable obj, Object arg) {  

        x += dx;
        y += dy;

        if (x < 1) {
            x = 1;
        }

        if (y < 1) {
            y = 1;
        }
        
        Environment.GameEvents ge = (Environment.GameEvents) arg;
            if(ge.type == 1) {
                KeyEvent e = (KeyEvent) ge.event;
                switch (e.getKeyCode()) {    
                    case KeyEvent.VK_A:
                        System.out.println("Left");
                        x -= this.speed;
	        	break; 
                    case KeyEvent.VK_D:
                        System.out.println("Right");
                        x += speed;
	        	break;
                    case KeyEvent.VK_W:
                        System.out.println("Up");
                        y -= speed;
                        break;
                    case KeyEvent.VK_S:
                        System.out.println("Down");
                        y += speed;                  
                    default:
                        if(e.getKeyChar() == 'f') {
                        System.out.println("Fire"); 
                        fire();
                 // }
                }
            }}
            else if(ge.type == 2) {
                String msg = (String)ge.event;
                if(msg.equals("Explosion"))
                    boom = 1;
                    
            }
        }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public ArrayList getBullets() {
        return bullets;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void fire() { 
        bullets.add(new Bullet((x + 16) , y));
    }

}