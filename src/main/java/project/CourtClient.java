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

    public void update(boolean[] keys, String trackId) {
        TrackClient t = getTrackClient(trackId);
        if (keys[KeyEvent.VK_A]) {
            t.getPlayer().setDir(-1);
        }
        if (keys[KeyEvent.VK_D]) {
            t.getPlayer().setDir(1);
        }
    }

    public void updateTrack(String trackId, int direction) {
        TrackClient t = getTrackClient(trackId);
        t.getPlayer().setDir(direction);
        t.update();
    }
}
