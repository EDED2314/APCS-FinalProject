package net;

public abstract class Packet {
    public static enum PacketTypes {
        INVALID(-1), LOGIN(00), DISCONNECT(01);

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

    public abstract  byte[] getData();

    public static PacketTypes lookupPacket(String id) {
        try{
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

class Packet00Login extends Packet {

    private int trackId;

    public Packet00Login(byte[] data) {
        super(00);
        this.trackId = Integer.parseInt(readData(data));

    }

    public Packet00Login(int trackId) {
        super(00);
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
        return ("00" + this.trackId).getBytes();
    }

    public int getTrackId(){
        return trackId;
    }
}
