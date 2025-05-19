package apcs.project;

import java.awt.*;

public class Track extends Player {
    static double boundaryMinDistance = 10;
    final private CustomPoint p1; //start point
    final private CustomPoint p2; //end point (boundary)
    // private CustomPoint p3; //center of track, where player will be rendered initially

    //angle is in degrees
    Track(CustomPoint bound1, CustomPoint bound2, CustomPoint center, double angle, double dx, double dy) {
        super(center.x, center.y, angle / 57.3, dx, dy);
        // p3 = center;
        p1 = bound1;
        p2 = bound2;

    }

    //track update logic
    public void update(int dir) {
        double dis1 = p1.distance(new CustomPoint(super.center_x, super.center_y));
        double dis2 = p2.distance(new CustomPoint(super.center_x, super.center_y));
        if (dis1 < boundaryMinDistance){
            if (dir == 1){
                super.update(dir);
            }else{
                System.out.println("Out of bounds");
            }
        }
        else if (dis2 < boundaryMinDistance){
            if (dir == -1){
                super.update(dir);
            }else{
                System.out.println("Out of bounds");
            }
        }else{
            super.update(dir);
        }



    }

    //track render logic
    public void render(Graphics2D g2d) {
        super.render(g2d);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        g2d.setStroke(new BasicStroke());
        g2d.setColor(Color.BLACK);
    }


}
