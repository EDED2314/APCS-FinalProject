package apcs.project;

import java.awt.*;

public class Ball extends CustomRectangle {
    static final int BALL_SPEED = 2;
    static final double MAX_BOUNCE_ANGLE = 1.308996939; //75 deg

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

    public void setReboundVelocity(Player player) {

        double[] playerToBallVector = new double[]{this.center_x - player.center_x, this.center_y - player.center_y};
        double[] tangentVector = new double[]{Math.cos(player.angle), Math.sin(player.angle)};
        //double[] tangentVector = new double[] {-normal[1], normal[0]};
        double[] normal = new double[]{-tangentVector[1], tangentVector[0]};

        //use dot product to project playerToBall vector onto tangent vector of to get distance from center B>
        double impactPosition = (playerToBallVector[0] * tangentVector[0] + playerToBallVector[1] * tangentVector[1]);
        System.out.println(impactPosition);
        double normalizedImpactPosition = impactPosition / ((double) Player.height / 2);
        double constrainedImpactPosition = Math.max(-1, Math.min(1, normalizedImpactPosition)); //-1 <= pos <= 1

        double bounceAngle = constrainedImpactPosition * Ball.MAX_BOUNCE_ANGLE;

        double magVel = Math.hypot(this.dx, this.dy);
        double ballVx = magVel * Math.cos(bounceAngle);
        double ballVy = magVel * Math.sin(bounceAngle);

        //orientation flipping magic that I found online
        // dot product btwn normal and velcoity gives us how much it is moving towards the surface
        // then subtracting 2(V dot N) N is just saying we want to reverse it completely
        double dot = normal[0] * ballVx + normal[1] * ballVy;
        ballVx = ballVx - 2 * dot * normal[0];
        ballVy = ballVy - 2 * dot * normal[1];

        ballVx += player.dx * 0.2;
        ballVy += player.dy * 0.2;

        super.setVelocity(ballVx, ballVy);

    }

}
