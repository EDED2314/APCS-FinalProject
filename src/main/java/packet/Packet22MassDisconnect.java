package packet;

import net.GameClient;
import net.GameServer;

public class Packet22MassDisconnect extends Packet{


    public Packet22MassDisconnect(byte[] data) {
        super(PacketTypes.MASS_DISCONNECT.getId());
    }

    public Packet22MassDisconnect() {
        super(PacketTypes.MASS_DISCONNECT.getId());
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
        return (String.valueOf(PacketTypes.MASS_DISCONNECT.getId())).getBytes();
    }

}
