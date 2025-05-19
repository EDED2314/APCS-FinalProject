package apcs.project;

import java.awt.*;

public class Ball extends CustomRectangle {
    static final int BALL_SPEED = 5;
    //case when there are any amount players
    Ball(int size, double vX, double vY) {
        super(0, 0, size, size, 0, vX, vY);
    }

    //idk what this is for but ok
    Ball(int size, int center_x, int center_y, double vX, double vY) {
        super(center_x, center_y, size, size, 0, vX, vY);
    }

    public void update() {
        super.update();
    }

    public void render(Graphics2D g2d) {
        super.render(g2d);
    }

    public void setNewVelocity(Player player){
        double distanceBetweenIntersection = CustomPoint.distance(player.center_x, player.center_y, super.center_x, super.center_y);
        double normalizedDistance = distanceBetweenIntersection/Player.height;
        double bounceAngle = normalizedDistance * Player.MAX_BOUNCE_ANGLE;
        double ballVx = BALL_SPEED*Math.cos(bounceAngle);
        double ballVy = BALL_SPEED*-Math.sin(bounceAngle);
        super.setVelocity(ballVx, ballVy);
    }

}
