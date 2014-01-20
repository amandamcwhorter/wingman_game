/*
 * Amanda McWhorter
 * CSC 413.02
 * Wingman Project
 * May 2, 2013
 * Bullet.java
 */
package Wingman1942;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Bullet {

    private int x, y;
    private Image image;
    boolean visible;
    private int width, height;

    private final int BOARD_HEIGHT = 480;
    private final int BULLET_SPEED = 2;

    public Bullet(int x, int y) {

        ImageIcon ii =
            new ImageIcon(this.getClass().getResource("Resources/bullet.png"));
        image = ii.getImage();
        visible = true;
        width = image.getWidth(null);
        height = image.getHeight(null);
        this.x = x;
        this.y = y;
    }


    public Image getImage() {
        return image;
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

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void update() {
        y -= BULLET_SPEED;
        if (y > BOARD_HEIGHT)
            visible = false;
    }
}