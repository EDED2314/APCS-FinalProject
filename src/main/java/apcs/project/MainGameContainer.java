package apcs.project;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MainGameContainer extends JPanel implements Runnable, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TARGET_FPS = 60;
    private static final CustomPoint[] defaultConfig = new CustomPoint[]{
            new CustomPoint(10, 10),
            new CustomPoint(10, 590),
            new CustomPoint(10, 300),
            new CustomPoint(810, 10),
            new CustomPoint(810, 590),
            new CustomPoint(810, 300)};

    private Thread gameThread;
    private volatile boolean running = false;
    private boolean[] keys = new boolean[256];

    private ArrayList<Track> tracks;
    private ArrayList<Ball> balls;
//    //player stuff yk yk
//    private int playerX = 100, playerY = 100;
//    private int playerSpeed = 5;
//    private ArrayList<Rectangle> enemies = new ArrayList<Rectangle>();

    public MainGameContainer() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        Track left = new Track(defaultConfig[0], defaultConfig[1], defaultConfig[2], 0,0, 5 );
        Track right = new Track(defaultConfig[3], defaultConfig[4], defaultConfig[5], 0,0, 5 );
        tracks.add(left);
        tracks.add(right);

        //TODO: add random generation function
        //TODO: add random speed function
        Ball b1 = new Ball(10, 410, 300, 5, 0);
        b1.setVelocity(5,0);
        balls.add(b1);



    }

//    private void initializeEnemies() {
//        Random rand = new Random();
//        for (int i = 0; i < 10; i++) {
//            enemies.add(new Rectangle(
//                    rand.nextInt(WIDTH - 50),
//                    rand.nextInt(HEIGHT - 50),
//                    30, 30
//            ));
//        }
//    }

    public void startGame() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGame() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        // fps control is interesting
        long startTime;
        long elapsed;
        long wait;
        long targetTime = 1000 / TARGET_FPS;

        // Game loop!!!! 😍😍😍😍
        while (running) {
            startTime = System.nanoTime();


            //update function to update everything in that tick
            update();
            //repaint function after updating everything
            repaint();

            // FPS control
            elapsed = System.nanoTime() - startTime;
            wait = targetTime - elapsed / 1000000;
            //^^^what we do is make the system wait for that much time in case its too fast so it doesn't render infinitely

            if (wait > 0) {
                try {
                    Thread.sleep(wait);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void update() {
        //default config for now
        if (keys[KeyEvent.VK_A]) tracks.get(0).update(-1);
        if (keys[KeyEvent.VK_D]) tracks.get(0).update(1);

        if (keys[KeyEvent.VK_J]) tracks.get(1).update(-1);
        if (keys[KeyEvent.VK_L]) tracks.get(1).update(1);

        //TODO: calculate new traj for ball... hmm using velocity vectors?
        for (Ball ball: balls){
            //TODO: calculate new vel vector then set it

            ball.update(1);
        }

        //TODO: collsion detection

//        // Simple collision detection
//        Rectangle playerRect = new Rectangle(playerX, playerY, 50, 50);
//        for (Rectangle enemy : enemies) {
//            if (playerRect.intersects(enemy)) {
//                // Handle collision
//                System.out.println("Collision detected!");
//            }
//        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        render(g);

        //debug
//        g.setColor(Color.WHITE);
//        g.drawString("Player Position: " + playerX + ", " + playerY, 10, 20);
    }

    private void render(Graphics g) {
//        g.setColor(Color.BLUE);
//        g.fillRect(playerX, playerY, 50, 50);
//
//        g.setColor(Color.RED);
//        for (Rectangle enemy : enemies) {
//            g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Multiplayer Pong");
        MainGameContainer game = new MainGameContainer();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.startGame();
    }
}
