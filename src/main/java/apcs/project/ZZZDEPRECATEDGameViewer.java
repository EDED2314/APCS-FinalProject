package apcs.project;

import javax.swing.JFrame;

public class ZZZDEPRECATEDGameViewer {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setTitle("Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ZZZDEPRECATEDGameComponent component = new ZZZDEPRECATEDGameComponent();
        frame.add(component);
        frame.setVisible(true);
    }
}
