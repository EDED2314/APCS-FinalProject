package apcs.project;

import java.awt.*;

public class Player extends CustomRectangle {
    static int width = 30;
    static int height = 10;

    Player(double centerX, double centerY, double angle, double dx, double dy) {
        super(centerX, centerY, width, height, angle, dx, dy);
    }

    public void update(int dir){
        super.update(dir);
    }


    public void render(Graphics2D g2d) {
        super.render(g2d);
    }
}
