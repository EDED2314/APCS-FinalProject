package packet;

import project.*;

import java.util.ArrayList;

public class Serializer {
    public static String serializeCourt(Court court) {
        StringBuilder ret = new StringBuilder();
        for (Ball b : court.getBalls()) {
            ret.append("|").append(serializeBall(b));
        }
        for (Track t : court.getTracks()) {
            ret.append("|").append(serializeTrack(t));
        }
        return ret.toString(); // tracks and balls
    }

    public static String serializeCourt( ArrayList<Packet12SinglePlayerUpdate> tracks, ArrayList<Packet11BallUpdate> balls){
        StringBuilder ret = new StringBuilder();
        for (Packet12SinglePlayerUpdate trackUpdate : tracks){
            ret.append("|").append(serializeTrack(trackUpdate.getId(), trackUpdate.getX(), trackUpdate.getY(), trackUpdate.getDir(), trackUpdate.getScore()));
        }
        for (Packet11BallUpdate ballUpdate : balls) {
            ret.append("|").append(serializeBall(ballUpdate.getVx(), ballUpdate.getVy(), ballUpdate.getX(), ballUpdate.getY(), ballUpdate.getId()));
        }
        return ret.toString();
    }

    public static String serializeBall(Ball ball) {
        return Constants.BALL_PACKET_HEADER +  ";" + ball.dx + ";" + ball.dy + ";" + ball.center_x + ";" + ball.center_y + ";" + ball.getId() ; //vx vy x y id
    }

    public static String serializeBall(double vx, double vy, double x, double y, int id) {
        return  Constants.BALL_PACKET_HEADER + ";" + vx + ";" + vy + ";" + x + ";" + y + ";" + id; //vx vy x y id
    }

    public static String serializeTrack(Track track) {
        Player player = track.getPlayer();
        return  Constants.TRACK_PACKET_HEADER + ";" + track.getId() + ";" + player.center_x + ";" + player.center_y + ";" + player.dir + ";" + player.score; //id x y dir score
    }

    public static String serializeTrack(String id, double x, double y, int dir, int score) {
        return Constants.TRACK_PACKET_HEADER + ";" + id + ";" + x + ";" + y + ";" + dir + ";" + score; //id x y dir score
    }

    public static String[] processChunk(String data) {
        return data.split(";");
    }

    public static String[] processMultipleChunks(String data) {
        return data.split("\\|");
    }
}
