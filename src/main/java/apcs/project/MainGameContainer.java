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

    private static final double baseVelocity = 5;

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
        double angleRange = 45;
        double angle = (180 - Math.random() * angleRange * 2 - angleRange) + 0.01; //generate random angle from -45 45
        double dx = baseVelocity * Math.cos(angle);
        double dy = baseVelocity * Math.sin(angle);
        b.setVelocity(dx, dy);
        b.setVelocity(1, -2);
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
                        System.out.println("hellooooo");
                        ball.setNewVelocity(t.getPlayer());
                    }
                } else {
                    //System.out.println(t);
                    //find distance to line from point
                    //basically cross product formula but in this case cross prod is det of matrix made of all comps

                    // |Point1Q x v| / |v|
                    // | [ [x1-center_x,y1-center_y], [x1-x2, y1-y2]] | / |v| ->
                    double[] trackLineVector = new double[]{(t.getBounds()[0].x - t.getBounds()[1].x), (t.getBounds()[0].y - t.getBounds()[1].y)};
                    double cross = (t.getBounds()[0].x-ball.center_x) * trackLineVector[1] - (t.getBounds()[0].y-ball.center_y) * trackLineVector[0];
                    double magTrackLine = Math.sqrt(trackLineVector[0] * trackLineVector[0] + trackLineVector[1] * trackLineVector[1]);
                    double distance = Math.abs(cross / magTrackLine);
                    //System.out.println(distance);
                    if (distance < 5) {
                        // Get the wall's normal vector
                        double[] normal = getWallNormal(t);

                        // Calculate reflection using: V' = V - 2*(V·N)*N
                        double dotProduct = ball.dx * normal[0] + ball.dy * normal[1];
                        double newDx = ball.dx - 2 * dotProduct * normal[0];
                        double newDy = ball.dy - 2 * dotProduct * normal[1];

                        ball.setVelocity(newDx, newDy);
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
        g.drawString("Player Position: " + balls.get(0).center_x + ", " + balls.get(0).center_y, 10, 20);
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

    private double[] getWallNormal(Track t) {
        CustomPoint p1 = t.getBounds()[0];
        CustomPoint p2 = t.getBounds()[1];

        // Horizontal walls (top/bottom)
        if (Math.abs(p1.y - p2.y) < 1e-6) {
            // Bottom wall normals point up, top point down
            return new double[]{0, p1.y == 10 ? 1 : -1};
        }
        // Vertical walls (left/right)
        else if (Math.abs(p1.x - p2.x) < 1e-6) {
            // Right wall normals point left, left wall right
            return new double[]{p1.x == 10 ? 1 : -1, 0};
        }

        // For diagonal walls (not in your current setup)
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double normalX = -dy;
        double normalY = dx;
        double length = Math.sqrt(normalX * normalX + normalY * normalY);
        return new double[]{normalX/length, normalY/length};
    }
}
