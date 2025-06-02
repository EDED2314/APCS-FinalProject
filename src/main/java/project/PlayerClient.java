package project;

import java.awt.*;
import java.net.InetAddress;

public class PlayerClient  {

    public InetAddress ipaddress;
    public int port;

    private Track track;

    public PlayerClient(InetAddress ipaddress, int port){
        track = new Track();
        this.ipaddress = ipaddress;
        this.port = port;
    }

    public PlayerClient(Track t,InetAddress ipaddress, int port ){
        this.track = t;
        this.ipaddress = ipaddress;
        this.port = port;
    }

    public void update(){
        track.update();
    }


    public void render(Graphics2D g2d){
        track.render(g2d);
    }
}
