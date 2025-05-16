package apcs.project;

import javax.swing.JFrame;

public class GameViewer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setTitle("Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameComponent component = new GameComponent();
        frame.add(component);
        frame.setVisible(true);
    }
}
