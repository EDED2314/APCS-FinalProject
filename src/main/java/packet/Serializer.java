package packet;

import project.Ball;
import project.Court;
import project.Player;
import project.Track;

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

//    public static String serializeCourt( ){
//
//    }

    public static String serializeBall(Ball ball) {
        return "b;" + ball.dx + ";" + ball.dy + ";" + ball.center_x + ";" + ball.center_y; //vx vy x y
    }

    public static String serializeBall(double vx, double vy, double x, double y) {
        return "b;" + vx + ";" + vy + ";" + x + ";" + y; //vx vy x y
    }

    public static String serializeTrack(Track track) {
        Player player = track.getPlayer();
        return "t;" + track.getId() + ";" + player.center_x + ";" + player.center_y + ";" + player.dir + ";" + player.score; //id x y dir score
    }

    public static String serializeTrack(String id, double x, double y, int dir, int score) {
        return "t;" + id + ";" + x + ";" + y + ";" + dir + ";" + score; //id x y dir score
    }

    public static String[] processChunk(String data) {
        String[] ret = data.split(";");
        return ret;
    }

    public static ArrayList<String[]> processMultipleChunks(String data) {
        String[] chunks = data.split("|");
        ArrayList<String[]> ret = new ArrayList<>();
        for (String chunk : chunks) {
            String[] processedChunk = processChunk(chunk);
            ret.add(processedChunk);
        }
        return ret;
    }
}
