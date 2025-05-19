package apcs.project;

import java.awt.*;

public class Track {
    static double boundaryMinDistance = 10;
    final private CustomPoint p1; //start point
    final private CustomPoint p2; //end point (boundary)
    private Player p;

    //angle is in degrees
    Track(CustomPoint bound1, CustomPoint bound2, CustomPoint center, double angle, double dx, double dy) {
        p1 = bound1;
        p2 = bound2;
        p = new Player(center.x, center.y, angle / 57.3, dx, dy);
    }

    public Player getPlayer() {
        return p;
    }


    //track update logic
    public void update() {
        double dis1 = p1.distance(new CustomPoint(p.center_x, p.center_y));
        double dis2 = p2.distance(new CustomPoint(p.center_x, p.center_y));
        if (dis1 < boundaryMinDistance) {
            if (p.dir == 1) {
                p.update();
            } else {
                System.out.println("Out of bounds");
            }
        } else if (dis2 < boundaryMinDistance) {
            if (p.dir == -1) {
                p.update();
            } else {
                System.out.println("Out of bounds");
            }
        } else {
            p.update();
        }


    }


    //track render logic
    public void render(Graphics2D g2d) {
        p.render(g2d);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        g2d.setStroke(new BasicStroke());
        g2d.setColor(Color.BLACK);
    }


}
