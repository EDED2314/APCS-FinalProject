package project;

import java.awt.*;
import java.net.InetAddress;

public class PlayerClient extends Track {

    public InetAddress ipaddress;
    public int port;


    public PlayerClient(InetAddress ipaddress, int port){
        super();
        this.ipaddress = ipaddress;
        this.port = port;
    }

    @Override
    public void update(){
        super.update();
    }

    @Override
    public void render(Graphics2D g2d){
        super.render(g2d);
    }
}
