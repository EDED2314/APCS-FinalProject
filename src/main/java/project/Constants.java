package project;

public class Constants {
    public enum UpdateStatus{
        POINT,
        NONE,
        FORWARD,
        BACKWARD,

    }

    public static final String TRACK_PACKET_HEADER = "t";
    public static final String BALL_PACKET_HEADER = "b";
    public static final String BALL_MODE = "B";
    public static final String TRACK_MODE = "T";
}
