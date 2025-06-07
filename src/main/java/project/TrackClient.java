package project;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TrackClient extends Track {
    public static String defaultIP = "localhost";
    public static int defaultPort = 1331;

    public InetAddress ipaddress;
    public int port;

    public TrackClient(CustomPoint bound1, CustomPoint bound2, CustomPoint center, double angle, double dx, double dy, InetAddress ipaddress, int port, String id) {
        super(bound1, bound2, center, angle, dx, dy, id);
        this.ipaddress = ipaddress;
        this.port = port;
    }

    public TrackClient(CustomPoint bound1, CustomPoint bound2, CustomPoint center, double angle, double dx, double dy, String id) {
        super(bound1, bound2, center, angle, dx, dy, id);
        try {
            this.ipaddress = InetAddress.getByName(defaultIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = defaultPort;
    }

    public TrackClient(InetAddress ipaddress, int port, String id) {
        super(new CustomPoint(0, 0), new CustomPoint(0, 0), id);
        this.ipaddress = ipaddress;
        this.port = port;
    }



    public void update() {
        super.update();
    }


    public void render(Graphics2D g2d, String id) {
        super.render(g2d, id);
    }
}
