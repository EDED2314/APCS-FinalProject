package project;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CourtServer extends Court{

    public CourtServer(int radius, CustomPoint center, int playerNum) {
        super(radius, center, playerNum);
    }



    public void updateTrack(String trackId, int direction){
        // Find track t, and then update it.
        //     t.getPlayer().setDir(-1);
        //     t.update();
        // later broad cast it.
    }


    public boolean update(String trackId){
        boolean whetherUpdateAllClients = false;
        for (Ball ball : super.getBalls()) {
            ball.update();
            //   System.out.println(ball.center_x + " " + ball.center_y );
            for (Track t : super.getTracks()) {
                if (t.getPlayer() != null) {
                    if (ball.intersects(t.getPlayer())) {
                        ball.setPaddleReboundVelocity(t.getPlayer());
                        ball.assignNewLastHit(t.getId());
                        whetherUpdateAllClients = true;
                    }
                } else {
                    double dis = ball.getBallToTrackDistance(t);
                    if (dis < 5) {
                        ball.setWallReboundVelocity(t);
                        whetherUpdateAllClients = true;
                    }

                }
            }

        }
//        if (whetherUpdateAllClients){
//            //update all the clients on the new ball position
//        }

        //whetherUpdateAllClients = false;
        if (super.getTracks().size() >= 2) {
            whetherUpdateAllClients = checkBallForGamePoint();
        }
//        if (whetherUpdateAllClients){
//            //update stuff
//            //send court update
//            // t: ID, score
//            // ball position
//        }
        return whetherUpdateAllClients;
    }

}