package apcs.project;


import java.awt.*;
import java.awt.geom.AffineTransform;

public class CustomRectangle {
    public double center_x;
    public double center_y;
    public double dx;
    public double dy;

    final private double width;
    final private double height;
    final private CustomPoint[] corners;
    private double angle; //radians

    public CustomRectangle(double center_x, double center_y, double width, double height, double angle, double dx, double dy) {
        this.center_x = center_x;
        this.center_y = center_y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.dx = dx;
        this.dy = dy;

        //clockwise assignment for each of the edge points
        CustomPoint p1 = rotatePoint(new CustomPoint(leftX(), topY()));
        CustomPoint p2 = rotatePoint(new CustomPoint(rightX(), topY()));
        CustomPoint p3 = rotatePoint(new CustomPoint(rightX(), bottomY()));
        CustomPoint p4 = rotatePoint(new CustomPoint(leftX(), bottomY()));

        corners = new CustomPoint[]{p1, p2, p3, p4};
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

    // SAT algorithm -> works only for convex polys :( but its ok because our game is only convex B-)
    // we need to find a seperating axis, which is basically using dot prod to project everything, finding max and min of those project
    // then using that to see if they overlap, if they do then proceed to next one
    //https://www.youtube.com/watch?app=desktop&v=-EsWKT7Doww
    public boolean intersects(CustomRectangle other) {
        CustomPoint[] otherCorners = other.getCorners();

        // Check for separation on all axes
        // SAT algo says if there is a separating axis then there is NO intersection. Vice versa
        return !(hasSeparatingAxis(corners, otherCorners)) ||
                !(hasSeparatingAxis(otherCorners, corners));

    }

    //find seperating axis for any two polys
    //pre cond is corners of A and B same length and = 4 cuz rect
    public boolean hasSeparatingAxis(CustomPoint[] cornersA, CustomPoint[] cornersB) {
        for (int i = 0; i < 4; i++) {
            //checking all sides
            // first we calculate a normal for that side
            int next = (i + 1) % 4;
            double[] vectorP1 = new double[]{cornersA[next].x, cornersA[next].y};
            double[] vectorP2 = new double[]{cornersA[i].x, cornersA[i].y};

            double[] vectorDiff = new double[]{vectorP1[0] - vectorP2[0], vectorP1[1] - vectorP2[1]};
            double[] normal = new double[]{-vectorDiff[1], vectorDiff[0]};

            // normalizing it so it's not out of control
            double mag = Math.sqrt(normal[0] * normal[0] + normal[1] * normal[1]);
            normal[0] /= mag;
            normal[0] /= mag;

            double[] aProjections = projectPoints(cornersA, normal);
            double[] bProjections = projectPoints(cornersB, normal);

            // Check for overlap
            if (aProjections[1] < bProjections[0] || bProjections[1] < aProjections[0]) {
                return true; // Separating axis found
            }

        }
        return false;
    }

    private double[] projectPoints(CustomPoint[] points, double[] normal) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for (CustomPoint p : points) {
            //projecting w dot product points dot normal
            double projection = p.x * normal[0] + p.y * normal[1];
            min = Math.min(min, projection);
            max = Math.max(max, projection);
        }

        return new double[]{min, max};
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

    public void update(int dir) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        if (cos != 0){
            center_x += dx * dir * (cos/Math.abs(cos));
        }
        if (sin != 0){
            center_y += dy * dir * (sin/Math.abs(sin));
        }
    }

    public void render(Graphics2D g2d) {
        // Much easier render method w/o using polygons yay

        AffineTransform oldTransform = g2d.getTransform();

        //smoother edges recommendation
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.rotate(angle, center_x, center_y);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(
                (int) (center_x - width / 2),
                (int) (center_y - height / 2),
                (int) width,
                (int) height
        );
        g2d.setColor(Color.BLACK);

        g2d.setTransform(oldTransform);

    }

    public CustomPoint[] getCorners() {
        return corners;
    }

}