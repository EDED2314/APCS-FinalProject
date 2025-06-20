package packet;

import net.GameClient;
import net.GameServer;

public abstract class Packet {
    public enum PacketTypes {
        INVALID(-1),
        LOGIN(20),
        DISCONNECT(21),
        MASS_DISCONNECT(22),
        BALL_UPDATE(11),
        SINGLE_PLAYER_UPDATE(12),
        BALLS_AND_PLAYER_POINTS_UPDATE(13),
        SYNC(14),
        BALLS_UPDATE(15),
        PLAYER_POINTS_UPDATE(16),
        ;

        private final int packetId;

        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }

        public int getId() {
            return packetId;
        }

    }

    public byte packetId;

    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }

    public abstract void writeData(GameClient client);

    public abstract void writeData(GameServer server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }

    public abstract byte[] getData();

    public static PacketTypes lookupPacket(String id) {
        try {
            return lookupPacket(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            return PacketTypes.INVALID;
        }
    }

    public static PacketTypes lookupPacket(int id) {
        for (PacketTypes p : PacketTypes.values()) {
            //loop thru all packet types
            if (p.getId() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }
}

