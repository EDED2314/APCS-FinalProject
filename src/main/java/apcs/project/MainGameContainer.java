package apcs.project;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainGameContainer extends JPanel implements Runnable, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TARGET_FPS = 60;

    private Thread gameThread;
    private volatile boolean running = false;
    private boolean[] keys = new boolean[256];

    private Court game = new Court(HEIGHT / 2, new CustomPoint(WIDTH / (double) 2, HEIGHT / (double) 2), 10);

    public MainGameContainer() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

    }


    public void startGame() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

//    public void stopGame() {
//        running = false;
//        try {
//            gameThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }


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
        game.update(keys);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        render(g);

        //debug
        g.setColor(Color.RED);
        g.drawString("Ball Position: " + game.getBalls().getFirst().center_x + ", " + game.getBalls().getFirst().center_y, 10, 20);
    }

    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        game.render(g2d);
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
