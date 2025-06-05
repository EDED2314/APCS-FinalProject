package packet;

import net.GameClient;
import net.GameServer;

import java.util.ArrayList;

public class PacketCourtUpdate extends Packet {
    private ArrayList<Packet12SinglePlayerUpdate> playerUpdates;
    private ArrayList<Packet11BallUpdate> ballUpdates;

    public PacketCourtUpdate(byte[] byteData) {
        super(13);
        String rawString = readData(byteData);
        String[] data = Serializer.processChunk(rawString);

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
        // return ("13" + need to serialize everything ).getBytes();
    }
}
