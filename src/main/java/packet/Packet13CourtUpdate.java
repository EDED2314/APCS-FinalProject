package packet;

import net.GameClient;
import net.GameServer;
import project.Constants;

import java.util.ArrayList;

public class Packet13CourtUpdate extends Packet {
    private ArrayList<Packet12SinglePlayerUpdate> playerUpdates;
    private ArrayList<Packet11BallUpdate> ballUpdates;

    public Packet13CourtUpdate(byte[] byteData) {
        super(PacketTypes.ALL_UPDATE.getId());
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
        return (PacketTypes.ALL_UPDATE.getId() + Serializer.serializeCourt(playerUpdates, ballUpdates) ).getBytes();
    }

    public ArrayList<Packet11BallUpdate> getBallUpdates() {
        return ballUpdates;
    }

    public ArrayList<Packet12SinglePlayerUpdate> getPlayerUpdates() {
        return playerUpdates;
    }
}
