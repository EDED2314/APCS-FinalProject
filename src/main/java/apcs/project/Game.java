package apcs.project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JPanel implements Runnable, KeyListener {
    // Game dimensions
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // Game loop control
    private Thread gameThread;
    private volatile boolean running = false;

    // For FPS control
    private final int TARGET_FPS = 60;
    private long targetTime = 1000 / TARGET_FPS;

    public Game() {
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
        long startTime;
        long elapsed;
        long wait;

        // Game loop
        while (running) {
            startTime = System.nanoTime();

            update();
            repaint();

            elapsed = System.nanoTime() - startTime;
            wait = targetTime - elapsed / 1000000;

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
        // Update game state (player position, physics, etc.)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Clear the screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Render game objects
        render(g);

        // Display FPS or other debug info if needed
        g.setColor(Color.WHITE);
        g.drawString("FPS: " + TARGET_FPS, 10, 20);
    }

    private void render(Graphics g) {
        // Draw your game objects here
        g.setColor(Color.RED);
        g.fillRect(100, 100, 50, 50); // Example: draw a red square
    }

    // KeyListener methods
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Handle key presses
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Handle key releases
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in most games
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("2D Game");
        Game game = new Game();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.startGame();
    }
}