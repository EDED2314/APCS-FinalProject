package project;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class CourtClient extends Court {

    public CourtClient(int radius, CustomPoint center, int playerNum, String playerId) {
        super(radius, center, playerNum,  playerId );
    }

    public void render(Graphics2D g2d, String id) {
        if (super.getTracks().size() >= 3) {
            for (Track t : super.getTracks()) {
                t.render(g2d, id);
            }
            for (Ball b : super.getBalls()) {
                b.render(g2d);
            }
           displayScores(g2d);
        }


    }

    public Constants.UpdateStatus update(boolean[] keys, String trackId) {
        TrackClient t = getTrackClient(trackId);
        if (keys[KeyEvent.VK_A]) {
            t.getPlayer().setDir(-1);
            return Constants.UpdateStatus.BACKWARD;
        }
        if (keys[KeyEvent.VK_D]) {
            t.getPlayer().setDir(1);
            return Constants.UpdateStatus.FORWARD;

        }
        return Constants.UpdateStatus.NONE;
    }

    public void updateTrack(String trackId, int direction) {
        TrackClient t = getTrackClient(trackId);
        t.getPlayer().setDir(direction);
        t.update();
    }

    private void displayScores(Graphics2D g2) {
        Rectangle box = new Rectangle(10, 15, 120, 15*super.getTracks().size()+5);
        g2.setColor(Color.WHITE);
        g2.draw(box);


        for (int t = 0; t < super.getTracks().size(); t++) {
            if (super.getTracks().get(t).getPlayer() != null){
                g2.drawString("Player " + super.getTracks().get(t).getId() + ": " + super.getTracks().get(t).getPlayer().score, 15, 30+t*15);
            }

        }
        g2.setColor(Color.BLACK);
    }

}
