package project;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Court {
    private static final CustomPoint[] defaultConfig = {
            new CustomPoint(10, 10),
            new CustomPoint(10, 590),
            new CustomPoint(10, 300),
            new CustomPoint(590, 10),
            new CustomPoint(590, 590),
            new CustomPoint(590, 300),
            new CustomPoint(300, 300)};

    private ArrayList<Track> tracks;
    private ArrayList<Ball> balls;

    private int radius;
    private CustomPoint center;


    public Court(int radius, CustomPoint center, int playerNum) {
        this.radius = radius;
        this.center = center;

        balls = new ArrayList<Ball>();

        refreshTrackConfiguration(playerNum);

        initBall();
    }

    private void initBall() {
        Ball b1 = new Ball(10, (int) center.x, (int) center.y);
        initBallVelocity(b1);
        balls.add(b1);
    }


    private void initBallVelocity(Ball b) {
        double angle = (Math.random() * (Ball.MAX_BOUNCE_ANGLE) * 2 - (Ball.MAX_BOUNCE_ANGLE)) + 0.01; //generate random angle from -45 45
        double dx = Ball.BALL_SPEED * Math.cos(angle);
        double dy = -1 * Ball.BALL_SPEED * Math.sin(angle); //negative because y is flipped in the coordinate system
        b.setVelocity(dx, dy);
    }



    private void refreshTrackConfiguration(int playerNumber) {
        tracks = new ArrayList<Track>();
        if (playerNumber == 1){
            // only start generating tracks at 2
            return;
        }
        if (playerNumber == 2) {
            Track left = new Track(defaultConfig[0], defaultConfig[1], defaultConfig[2], 270, 0, 5);
            Track right = new Track(defaultConfig[3], defaultConfig[4], defaultConfig[5], 270, 0, 5);
            tracks.add(left);
            tracks.add(right);
            Track top = new Track(defaultConfig[0], defaultConfig[3]);
            Track bottom = new Track(defaultConfig[1], defaultConfig[4]);
            tracks.add(top);
            tracks.add(bottom);
            return;
        }

        double interiorAngle = ((180 * (playerNumber - 2)) / (double) playerNumber);
        double adjustedAngle = 180 - interiorAngle;
        double adjustedAngleRadians = adjustedAngle / 57.3;
        for (int n = 0; n < playerNumber; n++) {
            Track t = getTrack(n, adjustedAngleRadians);
            tracks.add(t);
        }
    }


    private Track getTrack(int n, double adjustedAngleRadians) {
        int x1 = (int) (radius * Math.cos(n * adjustedAngleRadians) + center.x);
        int y1 = (int) (radius * Math.sin(n * adjustedAngleRadians) + center.y);
        int x2 = (int) (radius * Math.cos((n + 1) * adjustedAngleRadians) + center.x);
        int y2 = (int) (radius * Math.sin((n + 1) * adjustedAngleRadians) + center.y);

//        System.out.println((x1 - center.x) + ", " + (y1 - center.y) + " " + (x2 - center.x) + ", " + (y2 - center.y));
//        System.out.println((x1) + ", " + (y1) + " " + (x2) + ", " + (y2));

        double midx = (double) (x1 + x2) / 2;
        double midy = (double) (y1 + y2) / 2;

        double angle = Math.toDegrees(Math.atan2((y2 - y1), (x2 - x1)));
        if (x2 - x1 > 0 && y2 - y1 < 0) {
            //4 quadrant
            angle += 180;
        } else if (x2 - x1 < 0 && y2 - y1 < 0) {
            //3 quadrant
            angle += 180;
        }

        double velx = Player.SPEED * Math.cos(Math.toRadians(angle));
        double vely = Player.SPEED * Math.sin(Math.toRadians(angle));

        if (Math.abs(velx) < 0.001) velx = 0;
        if (Math.abs(vely) < 0.001) vely = 0;

        //   System.out.println("This track's v: " + velx + "," + vely);

        Track t = new Track(new CustomPoint(x1, y1), new CustomPoint(x2, y2), new CustomPoint(midx, midy), angle + Math.toRadians(90), velx, vely);
        return t;
    }

    public void update(boolean[] keys) {
        int start = KeyEvent.VK_1;

        for (Track t : tracks) {
            if (keys[start]) {
                t.getPlayer().setDir(-1);
                t.update();
            }
            start++;
            if (keys[start]) {
                t.getPlayer().setDir(1);
                t.update();
            }
            start++;
        }

        for (Ball ball : balls) {
            ball.update();
            //   System.out.println(ball.center_x + " " + ball.center_y );
            for (Track t : tracks) {
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

        checkBallForPoint();
 //       displayScores();

    }

    public void render(Graphics2D g2d) {
        for (Track t : tracks) {
            t.render(g2d);
        }
        for (Ball b : balls) {
            b.render(g2d);
        }
    }

    private void checkBallForPoint() {
        for (int i = 0; i < balls.size(); i++) {
            Ball b = balls.get(i);
            if (Math.hypot(b.center_x - center.x, b.center_y - center.y) > radius) {
                //do logic here
                // respawn ball and give point to the last player.
                //display new score.
                // need score board thing
                if (b.lastHitPaddleId == 0) {
                    Track closest = tracks.getFirst();
                    double minDis = b.getBallToTrackDistance(tracks.getFirst());
                    for (Track t : tracks) {
                        double dis = b.getBallToTrackDistance(t);
                        if (dis < minDis) {
                            closest = t;
                            minDis = dis;
                        }
                        if (t.getId() == b.lastHitPaddleId) {
                            t.getPlayer().score++;
                        }
                    }

                    ArrayList<Track> others = new ArrayList<Track>(tracks);
                    others.remove(closest);
                    for (Track ot : others) {
                        ot.getPlayer().score++;
                    }
                } else {
                    for (Track t : tracks) {
                        if (t.getId() == b.lastHitPaddleId) {
                            t.getPlayer().score++;
                        }
                    }
                }

                //respawn ball
                removeBall(i);
                i--;
                initBall();

            }
        }

    }

    private void displayScores() {
        System.out.println("Scores: ");
        for (Track t : tracks) {
            System.out.println("Track " + t.getId() + "| Score: " + t.getPlayer().score);
        }

    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void addBall(Ball e) {
        balls.add(e);
    }

    public Track addTrack(){
        refreshTrackConfiguration(tracks.size() + 1);
        return tracks.getLast();
    }

    public void addTrack(Track e) {
        tracks.add(e);
    }

    public void removeBall(int index) {
        balls.remove(index);
    }

    public void removeBall(Ball e) {
        balls.remove(e);
    }

    public void removeTrack(int index) {
        tracks.remove(index);
    }

    public void removeTrack(Track e) {
        tracks.remove(e);
    }


}
