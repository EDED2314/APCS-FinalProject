package packet;

import net.GameClient;
import net.GameServer;

public class Packet21Disconnect extends Packet{

    private String trackId;

    public Packet21Disconnect(byte[] data) {
        super(PacketTypes.DISCONNECT.getId());
        this.trackId = readData(data);
    }

    public Packet21Disconnect(String trackId) {
        super(PacketTypes.DISCONNECT.getId());
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
        return (PacketTypes.DISCONNECT.getId() + this.trackId).getBytes();
    }

    public String getTrackId() {
        return trackId;
    }
}
