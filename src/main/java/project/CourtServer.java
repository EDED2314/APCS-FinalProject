package project;

import java.util.ArrayList;
import java.util.Random;

public class CourtServer extends Court {

    public CourtServer(int radius, CustomPoint center, int playerNum, String playerId) {
        super(radius, center, playerNum, playerId);
        initBall();
    }

    public void initBall() {
        Ball b1 = new Ball( (int) super.getCenter().x, (int) super.getCenter().y);
        double angle = (Math.random() * (Ball.MAX_BOUNCE_ANGLE) * 2 - (Ball.MAX_BOUNCE_ANGLE)) + 0.01; //generate random angle from -45 45

        Random random = new Random();
        boolean randomBoolean = random.nextBoolean();
        if (randomBoolean) angle += Math.toRadians(180);

        double dx = Ball.BALL_SPEED * Math.cos(angle);
        double dy = -1 * Ball.BALL_SPEED * Math.sin(angle); //negative because y is flipped in the coordinate system
        b1.setVelocity(dx, dy);
        super.getBalls().add(b1);
    }


    public void updateTrack(String trackId, int direction) {
        TrackClient t = getTrackClient(trackId);
        t.getPlayer().setDir(direction);
        t.update();
    }


    public Constants.UpdateStatus updateBalls() {


        for (Ball ball : super.getBalls()) {
            ball.update();
            for (Track t : super.getTracks()) {
                if (t.getPlayer() != null) {
                    if (ball.intersects(t.getPlayer())) {
                        ball.setPaddleReboundVelocity(t.getPlayer());
                        ball.assignNewLastHit(t.getId());
                    }
                } else {
                    double dis = ball.getBallToTrackDistance(t);
                    if (dis < 5) {
                        ball.setWallReboundVelocity(t);
                    }

                }
            }

        }


        boolean updateClientsForPoints = false;
        if (super.getTracks().size() >= 2) {
            updateClientsForPoints = checkBallForGamePoint();
        }

        if (updateClientsForPoints) {
            return Constants.UpdateStatus.POINT;
        }
        return Constants.UpdateStatus.NONE;
    }


    private boolean checkBallForGamePoint() {
        boolean changed = false;
        for (int i = 0; i < super.getBalls().size(); i++) {
            Ball b = super.getBalls().get(i);
            if (Math.hypot(b.center_x - super.getCenter().x, b.center_y - super.getCenter().y) > getRadius()) {
                if (b.lastHitPaddleId == null) {
                    Track closest = super.getTracks().get(0);
                    double minDis = b.getBallToTrackDistance(super.getTracks().get(0));
                    for (Track t : super.getTracks()) {
                        double dis = b.getBallToTrackDistance(t);
                        if (dis < minDis) {
                            closest = t;
                            minDis = dis;
                        }
                        if (t.getId().equals(b.lastHitPaddleId)) {
                            t.getPlayer().score++;
                            changed = true;
                        }
                    }

                    ArrayList<Track> others = new ArrayList<Track>(super.getTracks());
                    others.remove(closest);
                    for (Track ot : others) {
                        if (ot.getPlayer() != null) ot.getPlayer().score++; changed = true;
                    }
                } else {
                    for (Track t : super.getTracks()) {
                        if (t.getId().equals(b.lastHitPaddleId)) {
                           if (t.getPlayer() != null)  t.getPlayer().score++; changed = true;
                        }
                    }
                }

                //respawn ball
                removeBall(i);
                i--;
                initBall();

            }
        }
        return changed;
    }

}