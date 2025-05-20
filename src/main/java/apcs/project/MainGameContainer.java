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
            new CustomPoint(790, 10),
            new CustomPoint(790, 590),
            new CustomPoint(790, 300),
            new CustomPoint(400, 300)};

    private Thread gameThread;
    private volatile boolean running = false;
    private boolean[] keys = new boolean[256];

    private ArrayList<Track> tracks;
    private ArrayList<Ball> balls;

    public MainGameContainer() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        balls = new ArrayList<Ball>();
        tracks = new ArrayList<Track>();

        Track left = new Track(defaultConfig[0], defaultConfig[1], defaultConfig[2], 90, 0, 5);
        Track right = new Track(defaultConfig[3], defaultConfig[4], defaultConfig[5], 90, 0, 5);
        tracks.add(left);
        tracks.add(right);
        Track top = new Track(defaultConfig[0], defaultConfig[3]);
        Track bottom = new Track(defaultConfig[1], defaultConfig[4]);
        tracks.add(top);
        tracks.add(bottom);

        Ball b1 = new Ball(10, (int) defaultConfig[6].x, (int) defaultConfig[6].y, 5, 0);
        initBallVelocity(b1);
        balls.add(b1);

    }

    private void initBallVelocity(Ball b) {
        double angle = (180 - Math.random() * (Ball.MAX_BOUNCE_ANGLE - 0.2) * 2 - (Ball.MAX_BOUNCE_ANGLE - 0.2)) + 0.01; //generate random angle from -45 45
        double dx = Ball.BALL_SPEED * Math.cos(angle);
        double dy = Ball.BALL_SPEED * Math.sin(angle);
        b.setVelocity(dx, dy);
        b.setVelocity(-3, 0);
    }

    private void initTrack() {

    }

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
        //in our coordinate system going down is positive 1 and going up is negative 1...
        if (keys[KeyEvent.VK_A]) { //-1
            tracks.get(0).getPlayer().setDir(-1);
            tracks.get(0).update();
        }
        if (keys[KeyEvent.VK_D]) { //1
            tracks.get(0).getPlayer().setDir(1);
            tracks.get(0).update();
        }
        if (keys[KeyEvent.VK_J]) {
            tracks.get(1).getPlayer().setDir(-1);
            tracks.get(1).update();
        }
        if (keys[KeyEvent.VK_L]) {
            tracks.get(1).getPlayer().setDir(1);
            tracks.get(1).update();
        }

        for (Ball ball : balls) {
            ball.update();
            //   System.out.println(ball.center_x + " " + ball.center_y );
            for (Track t : tracks) {
                if (t.getPlayer() != null) {
                    if (ball.intersects(t.getPlayer())) {
                        ball.setReboundVelocity(t.getPlayer());
                    }
                } else {
                    double dis = ball.getBallToTrackDistance(t);
                    if (dis < 5) {
                        ball.setWallReboundVelocity(t);
                    }

                }
            }

        }


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        render(g);

        //debug
        g.setColor(Color.RED);
        g.drawString("Ball Position: " + balls.get(0).center_x + ", " + balls.get(0).center_y, 10, 20);
    }

    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        for (Track t : tracks) {
            t.render(g2d);
        }
        for (Ball b : balls) {
            b.render(g2d);
        }
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
