package project;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CourtClient extends Court {
    static final String FONT_PATH = "PressStart2P-Regular.ttf";
    Font customGameFont;

    public CourtClient(int radius, CustomPoint center, int playerNum, String playerId) {
        super(radius, center, playerNum, playerId);
        loadCustomFont();
    }

    private void loadCustomFont() {

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(FONT_PATH);
            if (is != null) {
                customGameFont = Font.createFont(Font.TRUETYPE_FONT, is);
                is.close();
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.err.println("Error loading font. Falling back to default font.");
            customGameFont = new Font("Monospaced", Font.BOLD, 24);
        }
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
        } else {
            renderWaitingMessage(g2d);
        }


    }

    private void renderWaitingMessage(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        Font originalFont = g2d.getFont();
        Font derivedFont = customGameFont.deriveFont(Font.BOLD, 36f);
        g2d.setFont(derivedFont);
        String waitingText = "WAITING FOR PLAYERS";
        FontMetrics metrics = g2d.getFontMetrics(derivedFont);

        Rectangle bounds = g2d.getClipBounds();
        int screenWidth = bounds.width;
        int screenHeight = bounds.height;

        int x = (screenWidth - metrics.stringWidth(waitingText)) / 2;
        int y = (screenHeight - metrics.getHeight()) / 2 + metrics.getAscent();

        g2d.drawString(waitingText, x, y);
        g2d.setFont(originalFont);

        g2d.setColor(Color.BLACK);
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
        Rectangle box = new Rectangle(10, 15, 120, 15 * super.getTracks().size() + 5);
        g2.setColor(Color.WHITE);
        g2.draw(box);

        Font originalFont = g2.getFont();
        Font derivedFont = customGameFont.deriveFont(Font.BOLD, 36f);
        g2.setFont(derivedFont);

        for (int t = 0; t < super.getTracks().size(); t++) {
            if (super.getTracks().get(t).getPlayer() != null) {
                String text = "Player " + super.getTracks().get(t).getId() + ": " + super.getTracks().get(t).getPlayer().score;
                g2.drawString(text, 15, 30 + t * 15);
            }

        }

        g2.setFont(originalFont);
        g2.setColor(Color.BLACK);
    }

}
