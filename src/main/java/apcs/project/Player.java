package apcs.project;

import java.awt.*;

public class Player extends CustomRectangle {
    static final int width = 30;
    static final int height = 10;
    static final double MAX_BOUNCE_ANGLE = 1.308996939;

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
