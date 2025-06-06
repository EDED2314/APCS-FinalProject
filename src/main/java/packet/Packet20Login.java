package packet;

import net.GameClient;
import net.GameServer;

public class Packet20Login extends Packet {

    private String trackId;

    public Packet20Login(byte[] data) {
        super(PacketTypes.LOGIN.getId());
        this.trackId = readData(data);

    }

    public Packet20Login(String trackId) {
        super(PacketTypes.LOGIN.getId());
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
        return (PacketTypes.LOGIN.getId() + this.trackId).getBytes();
    }

    public String getTrackId() {
        return trackId;
    }
}
