package packet;

import net.GameClient;
import net.GameServer;
import project.Constants;

public class Packet11BallUpdate extends Packet {

    private int id;
    private double vx;
    private double vy;
    private double x;
    private double y;
    private boolean fail = false;

    public Packet11BallUpdate(byte[] byteData) {
        super(PacketTypes.BALL_UPDATE.getId());
        String rawString = readData(byteData);
        String[] data = Serializer.processChunk(rawString);
        if (!data[0].equals(Constants.BALL_PACKET_HEADER)) {
            fail = true;
        }
        //vx vy x y id
        vx = Double.parseDouble(data[1]);
        vy = Double.parseDouble(data[2]);
        x = Double.parseDouble(data[3]);
        y = Double.parseDouble(data[4]);
        id = Integer.parseInt(data[5]);
    }

    @Override
    public void writeData(GameClient client) {
        if (!fail) client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        if (!fail) server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return (PacketTypes.BALL_UPDATE.getId() + Serializer.serializeBall(vx, vy, x, y, id)).getBytes();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public boolean isFail() {
        return fail;
    }

    public int getId() {
        return id;
    }
}
