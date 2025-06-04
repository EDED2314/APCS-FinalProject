package project;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import packet.Packet;

public abstract class Court {
    private static final CustomPoint[] defaultConfig = {
            new CustomPoint(10, 10),
            new CustomPoint(10, 590),
            new CustomPoint(10, 300),
            new CustomPoint(590, 10),
            new CustomPoint(590, 590),
            new CustomPoint(590, 300),
            new CustomPoint(300, 300)};

    private ArrayList<TrackClient> tracks;
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
        tracks = new ArrayList<TrackClient>();
        if (playerNumber == 1) {
            // only start generating tracks at 2
            return;
        }
        if (playerNumber == 2) {
            TrackClient left = new TrackClient(defaultConfig[0], defaultConfig[1], defaultConfig[2], 270, 0, 5, String.valueOf(Track.tracksInit));
            TrackClient right = new TrackClient(defaultConfig[3], defaultConfig[4], defaultConfig[5], 270, 0, 5, String.valueOf(Track.tracksInit));
            tracks.add(left);
            tracks.add(right);
            TrackClient top = new TrackClient(defaultConfig[0], defaultConfig[3], String.valueOf(Track.tracksInit));
            TrackClient bottom = new TrackClient(defaultConfig[1], defaultConfig[4], String.valueOf(Track.tracksInit));
            tracks.add(top);
            tracks.add(bottom);
            return;
        }

        double interiorAngle = ((180 * (playerNumber - 2)) / (double) playerNumber);
        double adjustedAngle = 180 - interiorAngle;
        double adjustedAngleRadians = adjustedAngle / 57.3;
        for (int n = 0; n < playerNumber; n++) {
            TrackClient t = getNewTrack(n, adjustedAngleRadians);
            tracks.add(t);
        }
    }

    private void refreshTrackConfiguration(TrackClient targetTrack, Packet.PacketTypes type) {
        switch (type) {
            case INVALID:
                break;
            case LOGIN:
                if (tracks.size() == 0) {
                    // only start generating tracks at 2
                    tracks.add(targetTrack);
                    return;
                }
                if (tracks.size() == 1) {
                    tracks.add(targetTrack);
                    TrackClient top = new TrackClient(defaultConfig[0], defaultConfig[3], "top123456");
                    TrackClient bottom = new TrackClient(defaultConfig[1], defaultConfig[4], "bottom123456");
                    tracks.add(top);
                    tracks.add(bottom);
                    return;
                }
                int playerNumber = tracks.size() + 1;
                if (tracks.get(tracks.size() - 1).getId().equals("bottom123456")) {
                    // Right now the track array list like such: [0,1,2,3] -> [left right top bottom]. We want to remove top bottom cuz they fake players.
                    tracks.remove(3);
                    tracks.remove(2);
                    playerNumber -= 2;
                }

                tracks.add(targetTrack);

                double interiorAngle = ((180 * (playerNumber - 2)) / (double) playerNumber);
                double adjustedAngle = 180 - interiorAngle;
                double adjustedAngleRadians = adjustedAngle / 57.3;
                for (int n = 0; n < playerNumber; n++) {
                    refreshTrack(n, adjustedAngleRadians, tracks.get(n));
                }
                break;

            case DISCONNECT:
                if (tracks.size() == 0) {
                    //zero player case
                    return;
                }
                if (tracks.size() == 1) {
                    //1 player case
                    tracks.remove(targetTrack);
                    return;
                }
                if (tracks.get(tracks.size() - 1).getId().equals("bottom123456")) {
                    // two player case
                    // Right now the track array list like such: [0,1,2,3] -> [left right top bottom]. We want to remove top bottom cuz they fake players.
                    tracks.remove(3);
                    tracks.remove(2);
                    tracks.remove(targetTrack);
                    return;
                }

                if (tracks.size() == 3) {
                    //two player mode time!
                    tracks.add(targetTrack);
                    TrackClient top = new TrackClient(defaultConfig[0], defaultConfig[3], "top123456");
                    TrackClient bottom = new TrackClient(defaultConfig[1], defaultConfig[4], "bottom123456");
                    tracks.add(top);
                    tracks.add(bottom);
                    return;
                }
                tracks.remove(targetTrack);
                //formula from above method
                double adjustedAngleRadianss = (180 - ((180 * (tracks.size() - 2)) / (double) tracks.size())) / 57.3;
                for (int n = 0; n < tracks.size(); n++) {
                    refreshTrack(n, adjustedAngleRadianss, tracks.get(n));
                }
                break;
        }

    }

    private void refreshTrack(int n, double adjustedAngleRadians, TrackClient oldTrack) {
        int x1 = (int) (radius * Math.cos(n * adjustedAngleRadians) + center.x);
        int y1 = (int) (radius * Math.sin(n * adjustedAngleRadians) + center.y);
        int x2 = (int) (radius * Math.cos((n + 1) * adjustedAngleRadians) + center.x);
        int y2 = (int) (radius * Math.sin((n + 1) * adjustedAngleRadians) + center.y);
        double midx = (double) (x1 + x2) / 2;
        double midy = (double) (y1 + y2) / 2;
        double angle = Math.toDegrees(Math.atan2((y2 - y1), (x2 - x1)));
        if (x2 - x1 > 0 && y2 - y1 < 0) {
            angle += 180;
        } else if (x2 - x1 < 0 && y2 - y1 < 0) {
            angle += 180;
        }
        double velx = Player.SPEED * Math.cos(Math.toRadians(angle));
        double vely = Player.SPEED * Math.sin(Math.toRadians(angle));
        if (Math.abs(velx) < 0.001) velx = 0;
        if (Math.abs(vely) < 0.001) vely = 0;

        oldTrack.refresh(new CustomPoint(x1, y1), new CustomPoint(x2, y2), new CustomPoint(midx, midy), angle + Math.toRadians(90), velx, vely);
    }

    private TrackClient getNewTrack(int n, double adjustedAngleRadians) {
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

        TrackClient t = new TrackClient(new CustomPoint(x1, y1), new CustomPoint(x2, y2), new CustomPoint(midx, midy), angle + Math.toRadians(90), velx, vely, String.valueOf(Track.tracksInit));
        return t;
    }

//    public void update(boolean[] keys, String trackId) {
//
//    }
//
//    public void render(Graphics2D g2d, String id) {
//    }

    public boolean checkBallForGamePoint() {
        boolean changed = false;
        for (int i = 0; i < balls.size(); i++) {
            Ball b = balls.get(i);
            if (Math.hypot(b.center_x - center.x, b.center_y - center.y) > radius) {
                if (b.lastHitPaddleId == null) {
                    Track closest = tracks.get(0);
                    double minDis = b.getBallToTrackDistance(tracks.get(0));
                    for (Track t : tracks) {
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

                    ArrayList<Track> others = new ArrayList<Track>(tracks);
                    others.remove(closest);
                    for (Track ot : others) {
                        ot.getPlayer().score++;
                        changed = true;
                    }
                } else {
                    for (Track t : tracks) {
                        if (t.getId().equals(b.lastHitPaddleId)) {
                            t.getPlayer().score++;
                            changed = true;
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

    private void displayScores() {
        System.out.println("Scores: ");
        for (Track t : tracks) {
            System.out.println("Track " + t.getId() + "| Score: " + t.getPlayer().score);
        }

    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public ArrayList<TrackClient> getTracks() {
        return tracks;
    }

//    public void addBall(Ball e) {
//        balls.add(e);
//    }


    public void addTrack(TrackClient t) {
        refreshTrackConfiguration(t, Packet.PacketTypes.LOGIN);
    }

//    public void addTrack(TrackClient e) {
//        tracks.add(e);
//    }
//
    public void removeBall(int index) {
        balls.remove(index);
    }
//
//    public void removeBall(Ball e) {
//        balls.remove(e);
//    }
//
//    public void removeTrack(int index) {
//        tracks.remove(index);
//    }
//
//    public void removeTrack(Track e) {
//        tracks.remove(e);
//    }


}
