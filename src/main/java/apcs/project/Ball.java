package apcs.project;

import java.awt.*;
import java.awt.Rectangle;

//Should be a square ball cuz easier collision detection
public class Ball {
    private int size;
    private int center_x;
    private int center_y;

    Ball(int s) {
        size = s;
        center_x = 0;
        center_y = 0;
    }

    Ball(int s, int center_x, int center_y) {
        size = s;
        this.center_x = center_x;
        this.center_y = center_y;
    }

    public void draw(Graphics2D g2) {

        java.awt.Rectangle rect = new Rectangle(center_x - size/ 2, center_y - size / 2, size, size);
        g2.draw(rect);

    }

}
