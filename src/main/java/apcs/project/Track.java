package apcs.project;

import java.awt.*;

public class Track extends Player {
    private CustomPoint p1; //start point
    private CustomPoint p2; //end point (boundary)
    private CustomPoint p3; //center of track, where player will be rendered initially

    Track(CustomPoint bound1, CustomPoint bound2, CustomPoint center, double angle, double dx, double dy) {
        super(center.x, center.y, angle, dx, dy);
        p3 = center;
        p1 = bound1;
        p2 = bound2;

    }

    //track update logic
    public void update() {

    }

    //track render logic
    public void render(Graphics2D g2d) {
        super.render(g2d);

        g2d.setStroke(new BasicStroke( 3));
        g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        g2d.dispose();
    }


}
