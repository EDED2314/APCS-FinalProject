package net;

import packet.Packet;
import packet.Packet00Login;
import packet.Packet12SinglePlayerUpdate;
import packet.Serializer;
import project.CourtClient;
import project.TrackClient;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {
    public InetAddress ipAddress;
    private DatagramSocket socket;
    private final CourtClient clientCourt;

    public GameClient(CourtClient game, String ipAddress) {
        this.clientCourt = game;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

    }


    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //System.out.println("SERVER > " + new String(packet.getData()));

            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] Track: " + ((Packet00Login) packet).getTrackId() + " has joined the game.");
                addTrack(address, port, (Packet00Login) packet);
                break;
            case DISCONNECT:
                //TODO: implemnt delete tracks
                break;
            case SINGLE_PLAYER_UPDATE:
                packet = new Packet12SinglePlayerUpdate(data);
                String id = ((Packet12SinglePlayerUpdate) packet).getId();
                System.out.println("[" + address.getHostAddress() + ":" + port + "] Track: " + id + " successfully relayed its player update to player ___");
                clientCourt.updateTrack(id, ((Packet12SinglePlayerUpdate) packet).getDir());
                //done!
                break;
        }
    }

    private void addTrack(InetAddress address, int port, Packet00Login packet) {

        for (TrackClient track : clientCourt.getTracks()) {
            if (track.getId().equals(packet.getTrackId())) {
                throw new RuntimeException();
            }
        }

        TrackClient t = new TrackClient(address, port, packet.getTrackId());
        clientCourt.addTrack(t);

    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, TrackClient.defaultPort);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
