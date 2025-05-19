package apcs.project;

import java.awt.*;
import java.awt.geom.*;


public class Player extends CustomRectangle {
    static int width = 10;
    static int height = 30;
    private double dx;
    private double dy;

    Player(double centerX, double centerY, double angle) {
        super(centerX, centerY, width, height, angle);
    }

    public void render(Graphics2D g2d) {
        super.render(g2d);
    }
}
