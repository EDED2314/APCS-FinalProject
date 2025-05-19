package apcs.project;


import java.awt.*;
import java.awt.geom.AffineTransform;

public class CustomRectangle {
    public double center_x;
    public double center_y;
    public double width;
    public double height;

    private CustomPoint p1;
    private CustomPoint p2;
    private CustomPoint p3;
    private CustomPoint p4;

    private double angle; //radians

    public CustomRectangle(double center_x, double center_y, double width, double height, double angle) {
        this.center_x = center_x;
        this.center_y = center_y;
        this.width = width;
        this.height = height;
        this.angle = angle;

        //clockwise assignment for each of the edge points
        p1 = rotatePoint(new CustomPoint(leftX(), topY()));
        p2 = rotatePoint(new CustomPoint(rightX(), topY()));
        p3 = rotatePoint(new CustomPoint(rightX(), bottomY()));
        p4 = rotatePoint(new CustomPoint(leftX(), bottomY()));
    }

    public double leftX() {
        return center_x - width / 2;
    }

    public double rightX() {
        return center_x + width / 2;
    }

    public double topY() {
        return center_y - height / 2;
    }

    public double bottomY() {
        return center_y + height / 2;
    }

    //TODO - rework intersect function because this is only working for 90, 0, 270 and 360 cases
    public boolean intersects(CustomRectangle other) {
        return leftX() < other.rightX() &&
                rightX() > other.leftX() &&
                topY() < other.bottomY() &&
                bottomY() > other.topY();
    }


    private CustomPoint rotatePoint(CustomPoint p) {
        double relX = p.x - center_x;
        double relY = p.y - center_y;

        //2D rotational matrices only work around the axis so we have to first modify points to be centered at the axis
        double rotated_X = Math.cos(angle) * relX - Math.sin(angle) * relY;
        double rotated_Y = Math.sin(angle) * relX + Math.cos(angle) * relY;

        //then we add the center x and y back ykwim
        return new CustomPoint(rotated_X + center_x, rotated_Y + center_y);
    }

    public void render(Graphics g) {

        // Much easier render method w/o using polygons yay
        Graphics2D g2d = (Graphics2D) g.create();

        AffineTransform oldTransform = g2d.getTransform();

        //smoother edges recommendation
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.rotate(angle, center_x, center_y);

        g2d.fillRect(
                (int) (center_x - width / 2),
                (int) (center_y - height / 2),
                (int) width,
                (int) height
        );

        g2d.setTransform(oldTransform);

    }

}