package packet;

import net.GameClient;
import net.GameServer;

public class Packet00Login extends Packet {

    private String trackId;

    public Packet00Login(byte[] data) {
        super(00);
        this.trackId = readData(data);

    }

    public Packet00Login(String trackId) {
        super(00);
        this.trackId = trackId;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("00" + this.trackId).getBytes();
    }

    public String getTrackId() {
        return trackId;
    }
}
