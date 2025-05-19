package apcs.project;

import java.awt.*;

public class Player extends CustomRectangle {
    static int width = 10;
    static int height = 30;

    Player(double centerX, double centerY, double angle, double dx, double dy) {
        super(centerX, centerY, width, height, angle, dx, dy);
    }

    public void update(){
        super.update();
    }


    public void render(Graphics2D g2d) {
        super.render(g2d);
    }
}
