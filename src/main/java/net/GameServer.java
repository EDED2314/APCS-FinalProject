package net;

import packet.*;
import project.*;

import java.io.IOException;
import java.net.*;
import java.util.Objects;

public class GameServer extends Thread {
    private DatagramSocket socket;

    private final CourtServer serverCourt;

    public GameServer(CourtServer server, String ip) {
        serverCourt = server;
        try {
            this.socket = new DatagramSocket(TrackClient.defaultPort, InetAddress.getByName(ip));
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

    }


    public void run() {
        while (true) {
            byte[] data = new byte[2048];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

            if (serverCourt.getTracks().size() >= 3) updateBallAndBroadcast();
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
                packet = new Packet20Login(data);
                System.out.println("Request from [" + address.getHostAddress() + ":" + port + "] Track: " + ((Packet20Login) packet).getTrackId() + " has connected to the server.");
                addTrack(address, port, (Packet20Login) packet);
                syncTracksAndBallsToAllClients();
                //syncTracksAndBallsToClient(address, port);
                break;
            case DISCONNECT:

                break;
            case SINGLE_PLAYER_UPDATE:
                packet = new Packet12SinglePlayerUpdate(data);
                String id = ((Packet12SinglePlayerUpdate) packet).getId();
                System.out.println("Request from [" + address.getHostAddress() + ":" + port + "] Track: " + id + " sent a move update to the server");
                serverCourt.updateTrack(id, ((Packet12SinglePlayerUpdate) packet).getDir());
                packet = new Packet12SinglePlayerUpdate(Serializer.serializeTrack(serverCourt.getTrackClient(id)).getBytes());
                packet.writeData(this);
                break;

        }
    }


    private void updateBallAndBroadcast() {
        Constants.UpdateStatus status = serverCourt.updateBalls();
        Packet15BallsUpdate ballsUpdate = new Packet15BallsUpdate((Packet.PacketTypes.BALLS_UPDATE.getId() + Serializer.serializeCourt(serverCourt)).getBytes());
        sendDataToAllClients(ballsUpdate.getData());
        //Lowkey just do this cuz we can access array of balls and also scores just like accessed yay
        if (Objects.requireNonNull(status) == Constants.UpdateStatus.POINT) {
            Packet16PlayersPointUpdate playersPointUpdate = new Packet16PlayersPointUpdate((Packet.PacketTypes.PLAYER_POINTS_UPDATE.getId() + Serializer.serializeCourt(serverCourt)).getBytes());
            sendDataToAllClients(playersPointUpdate.getData());
        }
    }

    private void syncTracksAndBallsToAllClients() {
        Packet packet = new Packet14Sync((Packet.PacketTypes.SYNC.getId() + Serializer.serializeCourt(serverCourt)).getBytes());
        sendDataToAllClients(packet.getData());
    }


    private void syncTracksAndBallsToClient(InetAddress address, int port) {
        Packet packet = new Packet14Sync((Packet.PacketTypes.SYNC.getId() + Serializer.serializeCourt(serverCourt)).getBytes());
        sendData(packet.getData(), address, port);
    }

    private void addTrack(InetAddress address, int port, Packet20Login packet) {
        for (TrackClient track : serverCourt.getTracks()) {
            if (track.getId().equals(packet.getTrackId())) {
                throw new RuntimeException();
            }
        }

        TrackClient t = new TrackClient(address, port, packet.getTrackId());
        serverCourt.addTrack(t);
    }

    public void sendData(byte[] data, InetAddress ip, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public void sendDataToAllClientsExcluding(byte[] data, String trackId){
//        for (TrackClient connectedPlayer : serverCourt.getTracks()) {
//            if (!connectedPlayer.getId().equals(trackId)) sendData(data, connectedPlayer.ipaddress, connectedPlayer.port);
//        }
//    }

    public void sendDataToAllClients(byte[] data) {
        for (TrackClient connectedPlayer : serverCourt.getTracks()) {
            sendData(data, connectedPlayer.ipaddress, connectedPlayer.port);
        }
    }
}
