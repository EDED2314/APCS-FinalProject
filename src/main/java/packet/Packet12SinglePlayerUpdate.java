package packet;

import net.GameClient;
import net.GameServer;
import project.Constants;

public class Packet12SinglePlayerUpdate extends Packet {

    private String id;
    private double x;
    private double y;
    private int dir;
    private int score;
    private boolean fail = false;

    public Packet12SinglePlayerUpdate(byte[] byteData) {
        super(PacketTypes.SINGLE_PLAYER_UPDATE.getId());
        String rawString = readData(byteData);
        String[] data = Serializer.processChunk(rawString);
        if (!data[0].equals(Constants.TRACK_PACKET_HEADER)) {
            fail = true;
        }
        //t: id x y dir score
        id = data[1];
        x = Double.parseDouble(data[2]);
        y = Double.parseDouble(data[3]);
        dir = Integer.parseInt(data[4]);
        score = Integer.parseInt(data[5]);
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
        return (PacketTypes.SINGLE_PLAYER_UPDATE.getId() + Serializer.serializeTrack(id, x, y, dir, score)).getBytes();
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDir() {
        return dir;
    }

    public int getScore() {
        return score;
    }

    public boolean isFail() {
        return fail;
    }
}
