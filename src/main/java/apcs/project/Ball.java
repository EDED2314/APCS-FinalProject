package apcs.project;

import java.awt.*;

public class Ball extends CustomRectangle {
    private double dx;
    private double dy;


    //case when there are any amount players
    Ball(int size, double vX, double vY) {
        super(0, 0, size, size, 0, vX, vY);
    }

    //idk what this is for but ok
    Ball(int size, int center_x, int center_y, double vX, double vY) {
        super(center_x, center_y, size, size, 0, vX, vY);
    }

    public void update(){

    }

    public void render(Graphics2D g2d) {
        super.render(g2d);
        g2d.dispose();
    }

}
