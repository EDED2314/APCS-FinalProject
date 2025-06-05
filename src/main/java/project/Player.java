package project;

import java.awt.*;

public class Player extends CustomRectangle {
    static final int width = 30;
    static final int height = 10;
    static final int SPEED = 5;

    public int score;

    Player(double centerX, double centerY, double angle, double dx, double dy) {
        super(centerX, centerY, width, height, angle, dx, dy);
    }

    public void update(){
        super.update();
    }


    public void render(Graphics2D g2d, Color color){
        super.render(g2d, color);
    }

    public void setScore(int score){
        this.score = score;
    }
}
