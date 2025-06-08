package packet;

import net.GameClient;
import net.GameServer;
import project.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class Packet14Sync extends Packet {
    private final ArrayList<Packet12SinglePlayerUpdate> playerUpdates;
    private final ArrayList<Packet11BallUpdate> ballUpdates;

    public Packet14Sync(byte[] byteData) {
        super(PacketTypes.SYNC.getId());
        playerUpdates = new ArrayList<Packet12SinglePlayerUpdate>();
        ballUpdates = new ArrayList<Packet11BallUpdate>();
        String rawString = readData(byteData);
        String[] chunks = Serializer.processMultipleChunks(rawString);
        for (String chunk : chunks){
            if (chunk.startsWith(Constants.TRACK_PACKET_HEADER)){
                playerUpdates.add(new Packet12SinglePlayerUpdate((  PacketTypes.SINGLE_PLAYER_UPDATE.getId()+     chunk).getBytes()));
            }else if (chunk.startsWith(Constants.BALL_PACKET_HEADER)){
                ballUpdates.add(new Packet11BallUpdate((PacketTypes.BALL_UPDATE.getId() +   chunk).getBytes()));
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
        return (PacketTypes.SYNC.getId() + Serializer.serializeCourt(playerUpdates, ballUpdates, Constants.BALL_MODE + Constants.TRACK_MODE) ).getBytes();
    }

    public ArrayList<Packet11BallUpdate> getBallUpdates() {
        return ballUpdates;
    }

    public ArrayList<Packet12SinglePlayerUpdate> getPlayerUpdates() {
        return playerUpdates;
    }
}
