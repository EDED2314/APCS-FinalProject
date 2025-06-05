package project;

public class CourtServer extends Court{


    public CourtServer(int radius, CustomPoint center, int playerNum) {
        super(radius, center, playerNum);
    }



    public void updateTrack(String trackId, int direction){
        for (Track t : super.getTracks()){
            if (t.getId().equals(trackId)){
                t.getPlayer().setDir(direction);
                t.update();
            }
        }
        // Find track t, and then update it.
        // later broad cast it.
    }


    public Constants.UpdateStatus updateBalls(){
        boolean updateClientsOnBallStatus = false;
        for (Ball ball : super.getBalls()) {
            ball.update();
            for (Track t : super.getTracks()) {
                if (t.getPlayer() != null) {
                    if (ball.intersects(t.getPlayer())) {
                        ball.setPaddleReboundVelocity(t.getPlayer());
                        ball.assignNewLastHit(t.getId());
                        updateClientsOnBallStatus = true;
                    }
                } else {
                    double dis = ball.getBallToTrackDistance(t);
                    if (dis < 5) {
                        ball.setWallReboundVelocity(t);
                        updateClientsOnBallStatus = true;
                    }

                }
            }

        }


        boolean updateClientsForPoints = false;
        if (super.getTracks().size() >= 2) {
            updateClientsForPoints = checkBallForGamePoint();
        }

        if (updateClientsForPoints && updateClientsOnBallStatus){
            return Constants.UpdateStatus.BALLS_NEW_VELOCITY_AND_POINT;
        }else if (updateClientsForPoints){
            return Constants.UpdateStatus.POINT;
        }else if (updateClientsOnBallStatus){
            return Constants.UpdateStatus.BALLS_NEW_VELOCITY;
        }
        return Constants.UpdateStatus.NONE;
    }

}