/*
 * Amanda McWhorter
 * CSC 413.02
 * Wingman Project
 * May 2, 2013
 * Environment.java
 */
package Wingman1942;


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
import java.applet.AudioClip;



public class Environment extends JPanel implements Runnable, ActionListener {
    
    private Thread thread;
    
    private Timer timer;
    private MyPlane myPlane;
    private MyPlane2 myPlane2;
    private ArrayList enemy, explosion;
    private boolean ingame;
    private int B_WIDTH;
    private int B_HEIGHT;
    GameEvents gameEvents; //add game events
    //private String sea = "Resources/water.png";
    int move = 0, speed = 1, lives1 = 4, lives2 = 4, score1 = 0, score2 = 0;
    private Image image, island1, island2, island3, ex1, ex2, ex3, ex4, ex5, ex6,
            health1, health2, health3;
    private BufferedImage bimg;
    AudioClip audioClip;
    AudioClip audioClip1;
    Button play, exp, stop;
    Island I1, I2, I3;
    Healthbar player1health, player2health;
    Random generator = new Random(1234567);
    Explosion enemyExplosion = new Explosion();

    private int[][] pos = { 
        {320, -10}, {280, -150}, {360, -150},
        {240, -250}, {400, -250}, {100, -350}, 
        {200, -350}, {300, -500}, {400, -500}, 
        {500, -500}, {150, -500},{250, -550},
        {350, -500},{450, -500}, {550, -550}       
     };
    
    
    private Image [] en = {ex1, ex2, ex3, ex4, ex5, ex6};

    public Environment() {
        
        addKeyListener(new KeyControl());
        setFocusable(true);
        setBackground(Color.white);
        //drawBackGroundWithTileImage(B_WIDTH, B_HEIGHT, g2);
        setDoubleBuffered(true);
        ingame = true;

        setSize(640, 480);

        gameEvents = new GameEvents();
        
        myPlane = new MyPlane(120);
        gameEvents.addObserver(myPlane);
        
        myPlane2 = new MyPlane2(480);
        gameEvents.addObserver(myPlane2);

        initEnemy();
        
        island1 = getSprite("Resources/island1.png");
        island2 = getSprite("Resources/island2.png");
        island3 = getSprite("Resources/island3.png");
        
        I1 = new Island(island1, 100, 100, speed, generator);
        I2 = new Island(island2, 200, 400, speed, generator);
        I3 = new Island(island3, 300, 200, speed, generator);
        
        health1= getSprite("Resources/health1.png");
        health2= getSprite("Resources/health2.png");
        health3= getSprite("Resources/health3.png");   
        
        player1health = new Healthbar(health1, 15, 410);
        player2health = new Healthbar(health1, 490, 410);
        
        timer = new Timer(10, this);
        timer.start();
        
        ex1 = getSprite("Resources/explosion1_1.png");
        ex2 = getSprite("Resources/explosion1_2.png");
        ex3 = getSprite("Resources/explosion1_3.png");
        ex4 = getSprite("Resources/explosion1_4.png");
        ex5 = getSprite("Resources/explosion1_5.png");
        ex6 = getSprite("Resources/explosion1_6.png");
     
        playSound("Resources/background.wav");
    }

    public void addNotify() {
        super.addNotify();
        B_WIDTH = getWidth();
        B_HEIGHT = getHeight();   
    }

    public void initEnemy() {
        enemy = new ArrayList();

        for (int i=0; i<pos.length; i++ ) {
            enemy.add(new Enemy(pos[i][0], pos[i][1]));
        }
    }
    
    /*public void explosionAnimation(Graphics2D g2){
        explosion = new ArrayList();
        for(int i=0; i<en.length; i++){
            explosion.add(en[i]);
        }
        for(int j=0; j < explosion.size(); j++){
            g2.drawImage(explosion.getImage[j], explosion.getX(), explosion.getY(), this);
        }
        
            
    }*/

    public void paint(Graphics g) {
        super.paint(g);

        if (ingame) {

            
            Dimension d = getSize();
            Graphics2D g2 = createGraphics2D(d.width, d.height);          
            drawDemo(d.width, d.height, g2, g);
            g2.dispose();
            g.drawImage(bimg, 0, 0, this);
                           

            
        } else {
            String GameOver = "Game Over";        
            Font small = new Font("Helvetica", Font.BOLD, 24);
            FontMetrics metr = this.getFontMetrics(small);          
            g.setColor(Color.BLACK);
            g.setFont(small);
            g.drawString(GameOver, (B_WIDTH - metr.stringWidth(GameOver)) / 2,
                         B_HEIGHT/2);       
            audioClip.stop();
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
        
    }
    
    public void actionPerformed(ActionEvent e) {

        if (enemy.size()==0) {
            ingame = false;
        }

        ArrayList planeBullets = myPlane.getBullets();
        ArrayList plane2Bullets = myPlane2.getBullets();
        
        for (int i = 0; i < planeBullets.size(); i++) {
            Bullet m = (Bullet) planeBullets.get(i);
            if (m.isVisible()) 
                m.update();
            else planeBullets.remove(i);
        }
        
        for (int i = 0; i < plane2Bullets.size(); i++) {
            Bullet m = (Bullet) plane2Bullets.get(i);
            if (m.isVisible()) 
                m.update();
            else plane2Bullets.remove(i);
        }

        for (int i = 0; i < enemy.size(); i++) {
            Enemy a = (Enemy) enemy.get(i);
            if (a.isVisible()) 
                a.update();
            else enemy.remove(i);
        }

        checkCollisions();
        repaint();  
       
    }

    public void checkCollisions() {

        Rectangle r3 = myPlane.getBounds();
        Rectangle r4 = myPlane2.getBounds();

        for (int j = 0; j<enemy.size(); j++) {
            Enemy a = (Enemy) enemy.get(j);
            Rectangle r2 = a.getBounds();

            if (r3.intersects(r2)) {               
                a.setVisible(false);              
                lives1 -= 1;
                if(lives1==0)
                    ingame = false;
            }
            
            if (r4.intersects(r2)) {               
                a.setVisible(false);              
                lives2 -= 1;
                if(lives2==0)
                    ingame = false;
            }
            
            
        }

        ArrayList planeBullets = myPlane.getBullets();
        ArrayList plane2Bullets = myPlane2.getBullets();

        for (int i = 0; i < planeBullets.size(); i++) {
            Bullet m = (Bullet) planeBullets.get(i);

            Rectangle r1 = m.getBounds();

            for (int j = 0; j<enemy.size(); j++) {
                Enemy a = (Enemy) enemy.get(j);
                Rectangle r2 = a.getBounds();

                if (r1.intersects(r2)) {
                    m.setVisible(false);
                    a.setVisible(false);
                    playSound1("Resources/snd_explosion1.wav");
                   
                    score1 += 1;                  
                }
            }
        }
        
        for (int i = 0; i < plane2Bullets.size(); i++) {
            Bullet m = (Bullet) plane2Bullets.get(i);

            Rectangle r1 = m.getBounds();

            for (int j = 0; j<enemy.size(); j++) {
                Enemy a = (Enemy) enemy.get(j);
                Rectangle r2 = a.getBounds();

                if (r1.intersects(r2)) {
                    m.setVisible(false);
                    a.setVisible(false);
                    playSound1("Resources/snd_explosion1.wav");
                   // explosionAnimation(); 
                   
                    score2 += 1;                  
                }
            }
        }
    }


    private class KeyControl extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            gameEvents.setValue(e);
        }
    }

    public class GameEvents extends Observable {
       int type;
       Object event;
       
        public void setValue(KeyEvent e) {
               type = 1; // let's assume this mean key input. Should use CONSTANT value for this
               event = e;
               setChanged();
              // trigger notification
              notifyObservers(this);  
        }
               

       public void setValue(String planeBulletsg) {
          type = 2; // let's assume this mean key input. Should use CONSTANT value for this
          event = planeBulletsg;
          setChanged();
         // trigger notification
         notifyObservers(this);  
        }
    }
    
    public Image getSprite(String name) {
        URL url = Wingman1942.class.getResource(name);
        Image img = getToolkit().getImage(url);
        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }
    
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void run() {
    	
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
          
          try {
                thread.sleep(25);
            } catch (InterruptedException e) {
                break;
            }
            
        }
    	    	
       // thread = null;
    }
    
    public void playSound(String filename) {
        URL snd = Wingman1942.class.getResource(filename);
        audioClip= Applet.newAudioClip(snd);
        audioClip.loop();
    }
    
    public void playSound1(String filename) {
        URL snd = Wingman1942.class.getResource(filename);
        audioClip1= Applet.newAudioClip(snd);
        audioClip1.play();
    }
    
    public void drawBackGroundWithTileImage(int w, int h, Graphics2D g2){
       
        Image sea;
        sea = getSprite("Resources/water.png");
        int TileWidth = sea.getWidth(this);
        int TileHeight = sea.getHeight(this);

        int NumberX = (int) (w / TileWidth);
        int NumberY = (int) (h / TileHeight);

        Image Buffer = createImage(NumberX * TileWidth, NumberY * TileHeight);
        //Graphics BufferG = Buffer.getGraphics();


        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                g2.drawImage(sea, j * TileWidth, i * TileHeight + (move % TileHeight), TileWidth, TileHeight, this);
            }
        }
        move += speed;      
    }
    
    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }
    
    public void drawDemo(int w, int h, Graphics2D g2, Graphics g) {

        if (ingame) {
            drawBackGroundWithTileImage(w, h, g2);
            I1.update(w, h);
            I1.draw(g2, this);

            I2.update(w, h);
            I2.draw(g2, this);

            I3.update(w, h);
            I3.draw(g2, this);
            
            player1health.draw(g2, this);
            player2health.draw1(g2, this);

           if (myPlane.isVisible())
                g2.drawImage(myPlane.getImage(), myPlane.getX(), myPlane.getY(), 
                              this);
           ArrayList planeBullets = myPlane.getBullets();       
            
            for (int i = 0; i < planeBullets.size(); i++) {
                Bullet m = (Bullet)planeBullets.get(i);
                g2.drawImage(m.getImage(), m.getX(), m.getY(), this);
            }
            
            if (myPlane2.isVisible())
                g2.drawImage(myPlane2.getImage(), myPlane2.getX(), myPlane2.getY(), 
                              this);
           ArrayList plane2Bullets = myPlane2.getBullets();       
            
            for (int i = 0; i < plane2Bullets.size(); i++) {
                Bullet m = (Bullet)plane2Bullets.get(i);
                g2.drawImage(m.getImage(), m.getX(), m.getY(), this);
            }
        
            for (int i = 0; i < enemy.size(); i++) {
                Enemy a = (Enemy)enemy.get(i);
                if (a.isVisible()){                    
                    g2.drawImage(a.getImage(), a.getX(), a.getY(), this);                   
                    // a.enemyFire();
                }
            
                ArrayList as = a.getEnemyBullets();
                
                for (int j = 0; j < as.size(); j++) {
                    Bullet enemyBullet = new Bullet(a.getX(), a.getY());
                    g2.drawImage(enemyBullet.getImage(), enemyBullet.getX(), enemyBullet.getY(), this);
                }
            }              
            
            g2.setColor(Color.BLACK);
            g2.drawString("Enemies left: " + enemy.size(), 275, 420);
            g2.setColor(Color.BLACK);
            g2.drawString("Score: " + score1, 15, 350);
            g2.drawString("Score: " + score2, 550, 350);
            g2.setColor(Color.BLACK);
            g2.drawString("Lives: " + lives1, 15, 380);
            g2.drawString("Lives: " + lives2, 550, 380);
                   
        } 
    }
  
  public class Island {

        Image img;
        int x, y, speed;
        Random gen;

        Island(Image img, int x, int y, int speed, Random gen) {
            this.img = img;
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.gen = gen;
        }

        public void update(int w, int h) {
            y += speed;
            if (y >= h) {
                y = -100;
                x = Math.abs(gen.nextInt() % (w - 30));
            }
        }

        public void draw(Graphics g, ImageObserver obs) {
            g.drawImage(img, x, y, obs);
        }
    }  
  
  public class Healthbar {

        Image img;
        int x, y;
        Random gen;

        Healthbar(Image img, int x, int y) {
            this.img = img;
            this.x = x;
            this.y = y;    
        }

        public void update(Graphics g, int w, int h) {      
        }

        public void draw(Graphics g, ImageObserver obs) {
            if(lives1 ==4)
                g.drawImage(health1, x, y, obs);
            else if(lives1 == 3)
                g.drawImage(health2, x, y, obs);
            else if(lives1 == 2)
                g.drawImage(health3, x, y, obs);
        }
        
        public void draw1(Graphics g, ImageObserver obs) {
           if (lives2==4)
                g.drawImage(health1, x, y, obs);
            else if(lives2 == 3)
                g.drawImage(health2, x, y, obs);
            else if(lives2 == 2)
                g.drawImage(health3, x, y, obs);
        }
    }  
  
  public class Explosion{
      
      private int numFrames = 25;
      private int currentFrame = 0;
      private int px = 0, py = 0;
      private int width = 100;
      public boolean active = true;
      
      public void setPx(int x){
          this.px = x;
      }
      
      public void setPy(int y){
          this.py = y;       
      }
      
      public void playExplosion(Graphics g, Image[] frames){
          g.drawImage(frames[currentFrame], px-(width/2), py, null);
          currentFrame ++;
          if(currentFrame >= numFrames){
              currentFrame = 0;
              active = false;
          }
              
      }
  }
    
  public class ScoreBoard{
      
  }
}
