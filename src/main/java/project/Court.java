package project;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import packet.Packet;

public abstract class Court {
    private ArrayList<TrackClient> tracks;
    private Map<String, Integer> trackMapper = new HashMap<>();;
    private ArrayList<Ball> balls;

    private final int radius;
    private final CustomPoint center;

    private String playerId;


    public Court(int radius, CustomPoint center, int playerNum, String playerId) {
        this.radius = radius;
        this.center = center;
        this.playerId = playerId;

        balls = new ArrayList<Ball>();
        tracks = refreshTrackConfiguration(playerNum);

        buildTrackMapper(tracks);
    }


    private void buildTrackMapper(ArrayList<TrackClient> trackList) {
        trackMapper.clear();
        for (int i = 0; i < trackList.size(); i++) {
            trackMapper.put(trackList.get(i).getId(), i);
        }
    }



    public ArrayList<TrackClient> refreshTrackConfiguration(int playerNumber) {
        ArrayList<TrackClient> localTracks = new ArrayList<TrackClient>();
        if (playerNumber < 3) {
            // only start generating tracks at 3
            return  new ArrayList<TrackClient>();
        }

        double interiorAngle = ((180 * (playerNumber - 2)) / (double) playerNumber);
        double adjustedAngle = 180 - interiorAngle;
        double adjustedAngleRadians = adjustedAngle / 57.3;
        for (int n = 0; n < playerNumber; n++) {
            TrackClient t = getNewTrack(n, adjustedAngleRadians);
            localTracks.add(t);
        }
        return localTracks;
    }

    private void refreshTrackConfiguration(TrackClient targetTrack, Packet.PacketTypes type) {
        switch (type) {
            case INVALID:
                break;
            case LOGIN:
                if (tracks.size()<2) {
                    //when the track has 2 people and a third one joins we want to run other stuff
                    tracks.add(targetTrack);
                    return;
                }
                tracks.add(targetTrack);
                int playerNumber = tracks.size();

                double interiorAngle = ((180 * (playerNumber - 2)) / (double) playerNumber);
                double adjustedAngle = 180 - interiorAngle;
                double adjustedAngleRadians = adjustedAngle / 57.3;
                for (int n = 0; n < playerNumber; n++) {
                    refreshTrack(n, adjustedAngleRadians, tracks.get(n));
                }
                break;

            case DISCONNECT:
                if (tracks.isEmpty()) {
                    //zero player case
                    return;
                }
                if (tracks.size() <= 3) {
                    //1,2,3 player case
                    tracks.remove(targetTrack);
                    return;
                }
                tracks.remove(targetTrack);
                //formula from above method
                adjustedAngleRadians = (180 - ((180 * (tracks.size() - 2)) / (double) tracks.size())) / 57.3;
                for (int n = 0; n < tracks.size(); n++) {
                    refreshTrack(n, adjustedAngleRadians, tracks.get(n));
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

        return new TrackClient(new CustomPoint(x1, y1), new CustomPoint(x2, y2), new CustomPoint(midx, midy), angle + Math.toRadians(90), velx, vely, String.valueOf(Track.tracksInit));
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

    public void addTrack(TrackClient t) {
        refreshTrackConfiguration(t, Packet.PacketTypes.LOGIN);
        buildTrackMapper(tracks);
    }

    public void setBalls(ArrayList<Ball> balls){
        this.balls = balls;
    }

    public void setTracks(ArrayList<TrackClient> tracks){
        this.tracks = tracks;
        buildTrackMapper(tracks);
    }


    public void removeTrack(TrackClient t){
        refreshTrackConfiguration(t, Packet.PacketTypes.DISCONNECT);
        buildTrackMapper(tracks);
    }

    public TrackClient getTrackClient(String id){
        if (trackMapper.get(id) == null){
            return null;
        }
        return tracks.get(trackMapper.get(id));
    }

    public int getRadius(){
        return radius;
    }

    public CustomPoint getCenter() {
        return center;
    }

    public void removeBall(int index) {
        balls.remove(index);
    }

    public String getPlayerId(){
        return playerId;
    }

    public void setPlayerId(String id){
        this.playerId = id;
    }
}
