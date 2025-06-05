package project;

public class Constants {
    public enum UpdateStatus{
        BALLS_NEW_VELOCITY_AND_POINT,
        BALLS_NEW_VELOCITY,
        POINT,
        NONE,
        BACKWARDS,
        FORWARDS,
    }

    public static final String TRACK_PACKET_HEADER = "t";
    public static final String BALL_PACKET_HEADER = "b";
}
