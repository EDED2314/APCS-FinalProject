package packet;

import net.GameClient;
import net.GameServer;
import project.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class Packet15BallsUpdate extends Packet {
    private ArrayList<Packet11BallUpdate> ballUpdates;

    public Packet15BallsUpdate(byte[] byteData) {
        super(PacketTypes.BALLS_UPDATE.getId());
        ballUpdates = new ArrayList<Packet11BallUpdate>();
        String rawString = readData(byteData);
        String[] chunks = Serializer.processMultipleChunks(rawString);
        for (String chunk : chunks){
           if (chunk.startsWith(Constants.BALL_PACKET_HEADER)){
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
        return (PacketTypes.BALLS_UPDATE.getId() + Serializer.serializeCourt(new ArrayList<>(), ballUpdates, Constants.BALL_MODE) ).getBytes();
    }

    public ArrayList<Packet11BallUpdate> getBallUpdates() {
        return ballUpdates;
    }

    public String toString(){
        return ballUpdates.toString();
    }

}
