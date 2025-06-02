package project;

import java.awt.*;

public class Track {
    static final double boundaryMinDistance = 10;
    static int tracksInit = 0;

    private CustomPoint p1; //start point
    private CustomPoint p2; //end point (boundary)
    private Player p;

    private final String id;

    //angle is in degrees
    Track(CustomPoint bound1, CustomPoint bound2, CustomPoint center, double angle, double dx, double dy, String id) {
        p1 = bound1;
        p2 = bound2;
        p = new Player(center.x, center.y, angle / 57.3, dx, dy);
        this.id = id;
        tracksInit++;
    }

    Track(CustomPoint bound1, CustomPoint bound2, String id) {
        p1 = bound1;
        p2 = bound2;
        p = null;
        this.id = id;
        tracksInit++;
    }

    public Player getPlayer() {
        return p;
    }

    public CustomPoint[] getBounds() {
        return new CustomPoint[]{p1, p2};
    }


    public void refresh(CustomPoint bound1, CustomPoint bound2, CustomPoint center, double angle, double dx, double dy) {
        p1 = bound1;
        p2 = bound2;
        p = new Player(center.x, center.y, angle / 57.3, dx, dy);
    }

    //track update logic
    public void update() {
        if (p == null) return;
        double dis1 = p1.distance(new CustomPoint(p.center_x, p.center_y));
        double dis2 = p2.distance(new CustomPoint(p.center_x, p.center_y));
        double[] p1p2 = {p2.x - p1.x, p2.y - p1.y}; //p1 to p2 vector
        double[] p2p1 = {p1.x - p2.x, p1.y - p2.y}; //from p2 to p1 vector
        double[] player = {p.dx * p.dir, p.dy * p.dir}; //player dirtection vector
        double p1p2dotplayer = p1p2[0] * player[0] + p1p2[1] * player[1];
        double p2p1dotplayer = p2p1[0] * player[0] + p2p1[1] * player[1];

        if (dis1 < boundaryMinDistance) {
            if (p1p2dotplayer < 0) {
                System.out.println("Out of bounds");
            } else {
                p.update();
            }
        } else if (dis2 < boundaryMinDistance) {
            if (p2p1dotplayer < 0) {
                System.out.println("Out of bounds");
            } else {
                p.update();
            }

        } else {
            p.update();
        }


    }


    //track render logic
    public void render(Graphics2D g2d) {
        if (p != null) p.render(g2d);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        g2d.setStroke(new BasicStroke());
        g2d.setColor(Color.BLACK);
    }

    public String toString() {
        return p1 + " " + p2;
    }

    public double[] getWallNormal() {
        CustomPoint p1 = this.getBounds()[0];
        CustomPoint p2 = this.getBounds()[1];

        // Horizontal walls (top/bottom)
        if (Math.abs(p1.y - p2.y) < 1e-6) {
            // Bottom wall normals point up, top point down
            return new double[]{0, p1.y == 10 ? 1 : -1};
        }
        // Vertical walls (left/right)
        else if (Math.abs(p1.x - p2.x) < 1e-6) {
            // Right wall normals point left, left wall right
            return new double[]{p1.x == 10 ? 1 : -1, 0};
        }

        // For diagonal walls (not in your current setup)
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double normalX = -dy;
        double normalY = dx;
        double length = Math.sqrt(normalX * normalX + normalY * normalY);
        return new double[]{normalX / length, normalY / length};
    }

    public String getId() {
        return id;
    }

    public boolean equals(TrackClient other) {
        return other.getId().equals(this.getId());
    }
}
