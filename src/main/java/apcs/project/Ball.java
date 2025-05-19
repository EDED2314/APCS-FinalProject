package apcs.project;

import java.awt.*;

public class Ball {
    private int size;
    public double center_x;
    public double center_y;

    private CustomRectangle ball;

    Ball(int s) {
        size = s;
        center_x = 0;
        center_y = 0;
        ball = new CustomRectangle(0, 0 , size, size, 0);
    }

    Ball(int s, int center_x, int center_y) {
        size = s;
        this.center_x = center_x;
        this.center_y = center_y;
        ball = new CustomRectangle(center_x, center_y, size, size, 0);
    }

    public void render(Graphics2D g2d) {
        ball.render(g2d);
    }

}
