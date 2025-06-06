package packet;

import net.GameClient;
import net.GameServer;
import project.Constants;

import java.util.ArrayList;

public class Packet16PlayersPointUpdate extends Packet {
    private ArrayList<Packet12SinglePlayerUpdate> playerUpdates;

    public Packet16PlayersPointUpdate(byte[] byteData) {
        super(PacketTypes.PLAYER_POINTS_UPDATE.getId());
        String rawString = readData(byteData);
        String[] chunks = Serializer.processMultipleChunks(rawString);
        for (String chunk : chunks){
            if (chunk.startsWith(Constants.TRACK_PACKET_HEADER)){
                playerUpdates.add(new Packet12SinglePlayerUpdate((PacketTypes.SINGLE_PLAYER_UPDATE.getId()  + chunk).getBytes()));
            }
        }
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
        return (PacketTypes.PLAYER_POINTS_UPDATE.getId() + Serializer.serializeCourt(playerUpdates, new ArrayList<>()) ).getBytes();
    }

    public ArrayList<Packet12SinglePlayerUpdate> getPlayerUpdates() {
        return playerUpdates;
    }
}
