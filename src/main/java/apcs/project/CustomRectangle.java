package apcs.project;


import java.awt.*;
import java.awt.geom.AffineTransform;

public class CustomRectangle {
    public double center_x;
    public double center_y;
    public double width;
    public double height;

    private CustomPoint[] edgePoints;

    private double angle; //radians

    public CustomRectangle(double center_x, double center_y, double width, double height, double angle) {
        this.center_x = center_x;
        this.center_y = center_y;
        this.width = width;
        this.height = height;
        this.angle = angle;

        //clockwise assignment for each of the edge points
        CustomPoint p1 = rotatePoint(new CustomPoint(leftX(), topY()));
        CustomPoint p2 = rotatePoint(new CustomPoint(rightX(), topY()));
        CustomPoint p3 = rotatePoint(new CustomPoint(rightX(), bottomY()));
        CustomPoint p4 = rotatePoint(new CustomPoint(leftX(), bottomY()));

        edgePoints = new CustomPoint[] {p1,p2,p3,p4};
    }

    private double leftX() {
        return center_x - width / 2;
    }

    private double rightX() {
        return center_x + width / 2;
    }

    private double topY() {
        return center_y - height / 2;
    }

    private double bottomY() {
        return center_y + height / 2;
    }

    // SAT algorithm
    //https://www.youtube.com/watch?app=desktop&v=-EsWKT7Doww
    public boolean intersects(CustomRectangle other) {
        return false;
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