package project;

import java.awt.*;

public class Ball extends CustomRectangle {
    static final int BALL_SIZE = 10;
    static final int BALL_SPEED = 3;
    static final double MAX_BOUNCE_ANGLE = 1.308996939; //75 deg
    static int ballsInited = 1;

    public int id;
    public String lastHitPaddleId;


    public Ball(double center_x, double center_y) {
        super(center_x, center_y, BALL_SIZE, BALL_SIZE, 0, 1, 1);
        id = ballsInited;
        ballsInited++;
    }

    public void update() {
        super.update();
    }

    public void render(Graphics2D g2d) {
        super.render(g2d, Color.WHITE);
    }

    public void assignNewLastHit(String paddleId){
        lastHitPaddleId = paddleId;
    }

    public void setWallReboundVelocity(Track t) {
        double[] normal = t.getWallNormal();

        // Calculate reflection using: V' = V - 2*(V·N)*N (thing from online)
        double dotProduct = this.dx * normal[0] + this.dy * normal[1];
        double newDx = this.dx - 2 * dotProduct * normal[0];
        double newDy = this.dy - 2 * dotProduct * normal[1];

        this.setVelocity(newDx, newDy);

    }

    public void setPaddleReboundVelocity(Player player) {

       // double[] playerToBallVector = {this.center_x - player.center_x, this.center_y - player.center_y};
        double[] tangentVector = {Math.cos(player.angle), Math.sin(player.angle)};
        //double[] tangentVector =  {-normal[1], normal[0]};
        double[] normal = {-tangentVector[1], tangentVector[0]};

//        //use dot product to project playerToBall vector onto tangent vector of to get distance from center B>
//        double impactPosition = (playerToBallVector[0] * tangentVector[0] + playerToBallVector[1] * tangentVector[1]);
//        double normalizedImpactPosition = impactPosition / ((double) Player.width / 2);
//        double constrainedImpactPosition = Math.max(-1, Math.min(1, normalizedImpactPosition)); //-1 <= pos <= 1

        double ballVx = this.dx;
        double ballVy = this.dy;

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


    public double getBallToTrackDistance(Track t) {
        //System.out.println(t);
        //find distance to line from point
        //basically cross product formula but in this case cross prod is det of matrix made of all comps

        // |Point1Q x v| / |v|
        // | [ [x1-center_x,y1-center_y], [x1-x2, y1-y2]] | / |v| ->

        //System.out.println(distance);
        double[] trackLineVector = {(t.getBounds()[0].x - t.getBounds()[1].x), (t.getBounds()[0].y - t.getBounds()[1].y)};
        double cross = (t.getBounds()[0].x - this.center_x) * trackLineVector[1] - (t.getBounds()[0].y - this.center_y) * trackLineVector[0];
        double magTrackLine = Math.sqrt(trackLineVector[0] * trackLineVector[0] + trackLineVector[1] * trackLineVector[1]);
        double distance = Math.abs(cross / magTrackLine);
        return distance;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Ball other) {
        return other.getId() == this.getId();
    }

}
