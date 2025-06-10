package project;


import net.GameClient;
import net.GameServer;

import packet.*;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Objects;

import javax.swing.*;


public class MainGameContainer extends JPanel implements Runnable, KeyListener {

    private String playerId;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int TARGET_FPS = 60;

    private Thread gameThread;
    private volatile boolean running = false;
    private boolean[] keys = new boolean[256];

    private CourtClient gameClient = new CourtClient(HEIGHT / 2, new CustomPoint(WIDTH / (double) 2, HEIGHT / (double) 2), 0, "");
    private CourtServer gameServer = new CourtServer(HEIGHT / 2, new CustomPoint(WIDTH / (double) 2, HEIGHT / (double) 2), 0, "");
    private GameClient socketClient;
    private GameServer socketServer;

    public MainGameContainer() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

    }


    public void startGame() {
        String id = JOptionPane.showInputDialog(this, "Please enter your student ID\n(or a username <= 6 characters)");
        playerId = id;

        gameClient.setPlayerId(id);
        gameServer.setPlayerId(id);

        if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
            socketServer = new GameServer(gameServer, "0.0.0.0");
            socketServer.start();
        }

        socketClient = new GameClient(gameClient, "100.67.145.80");
        socketClient.start();

        Packet20Login login = new Packet20Login(id);
        //let the login packet handler class use the client to send its data via client to server
        login.writeData(socketClient);

        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGame() {
        running = false;
        try {
            gameThread.join();
            if (socketServer == null) {
                Packet21Disconnect disconnect = new Packet21Disconnect(playerId);
                disconnect.writeData(socketClient);
            } else {
                Packet22MassDisconnect massDisconnect = new Packet22MassDisconnect();
                massDisconnect.writeData(socketClient);
            }
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

        // Game loop!!!!
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
        if (socketServer != null) socketServer.updateBallAndBroadcast();
        if (gameClient.getTrackClient(playerId) == null) return;
        if (gameClient.getTracks().size() < 3) return;
        Constants.UpdateStatus status = gameClient.update(keys, playerId);
        if (status != Constants.UpdateStatus.NONE) {
            Packet12SinglePlayerUpdate update = new Packet12SinglePlayerUpdate((Packet.PacketTypes.SINGLE_PLAYER_UPDATE + Serializer.serializeTrack(gameClient.getTrackClient(playerId))).getBytes());
            update.writeData(socketClient);
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        render(g);

        //debug
//        g.setColor(Color.RED);
//        g.drawString("Ball Position: " + gameClient.getBalls().get(0).center_x + ", " + gameClient.getBalls().get(0).center_y, 10, 20);
    }

    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        gameClient.render(g2d, playerId);
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

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(MainGameContainer.class.getResource("/icon.png")));

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(icon.getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.stopGame();
                frame.dispose();
                System.exit(0);
            }
        });

        game.startGame();
    }


}
