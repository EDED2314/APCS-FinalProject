package project;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CourtClient extends Court {

    public CourtClient(int radius, CustomPoint center, int playerNum) {
        super(radius, center, playerNum);
    }

    public void render(Graphics2D g2d, String id) {
        if (super.getTracks().size() >= 2) {
            for (Track t : super.getTracks()) {
                t.render(g2d, id);
            }
        }

        for (Ball b : super.getBalls()) {
            b.render(g2d);
        }
    }

    public int update(boolean[] keys, String trackId) {
        for (TrackClient t : super.getTracks()) {
            if (t.getId().equals(trackId)) {
                if (keys[KeyEvent.VK_A]) {
                    return -1;
                    //send here
                    //     t.getPlayer().setDir(-1);
                    //     t.update();

                }
                if (keys[KeyEvent.VK_D]) {
                    return 1;
                    //   t.getPlayer().setDir(1);
                    // t.update();
                    //send broad here
                }
            }
        }
        return 0;
    }
}
