package packet;

import net.GameClient;
import net.GameServer;
import project.Constants;

import java.util.ArrayList;

public class Packet14Sync extends Packet {
    private ArrayList<Packet12SinglePlayerUpdate> playerUpdates;
    private ArrayList<Packet11BallUpdate> ballUpdates;

    public Packet14Sync(byte[] byteData) {
        super(PacketTypes.SYNC.getId());
        String rawString = readData(byteData);
        String[] chunks = Serializer.processMultipleChunks(rawString);
        for (String chunk : chunks){
            if (chunk.startsWith(Constants.TRACK_PACKET_HEADER)){
                playerUpdates.add(new Packet12SinglePlayerUpdate((PacketTypes.SINGLE_PLAYER_UPDATE.getId()  + chunk).getBytes()));
            }else if (chunk.startsWith(Constants.BALL_PACKET_HEADER)){
                ballUpdates.add(new Packet11BallUpdate((PacketTypes.BALL_UPDATE.getId() + chunk).getBytes()));
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
        return (PacketTypes.SYNC.getId() + Serializer.serializeCourt(playerUpdates, ballUpdates) ).getBytes();
    }

    public ArrayList<Packet11BallUpdate> getBallUpdates() {
        return ballUpdates;
    }

    public ArrayList<Packet12SinglePlayerUpdate> getPlayerUpdates() {
        return playerUpdates;
    }
}
