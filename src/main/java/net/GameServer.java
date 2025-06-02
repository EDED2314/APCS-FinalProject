package net;

import packet.Packet;
import packet.Packet00Login;
import project.*;

import project.Court;

import java.io.IOException;
import java.net.*;

public class GameServer extends Thread {
    private DatagramSocket socket;

    //THIS NOW MANAGES EVERYTHING SO WE GOTTA CHANGE STUFF!
    private Court game;

    public GameServer(Court game, String ip) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(TrackClient.defaultPort, InetAddress.getByName(ip));
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
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

//
//            String message = new String(packet.getData());
//            System.out.println("CLIENT [" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "] > " + message);
//            if (message.trim().equalsIgnoreCase("ping")) {
//                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        switch (type) {
            case INVALID:
                break;
            case LOGIN:
                Packet00Login packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] Track: " + packet.getTrackId() + " has connected.");
                //create a track/player based off of the address and port
                //TODO: modify this to accept the data's id
                boolean fail = false;
                for (TrackClient track : game.getTracks()) {
                    if (track.getId().equals(packet.getTrackId())) {
                        //ping back the people and make then restart and rejoin cuz same id not good
                        fail = true;
                    }
                }
                if (!fail) {
                    TrackClient t = new TrackClient(address, port, packet.getTrackId());
                    game.addTrack(t);

//                TrackClient t = game.addTrack();
//                t.ipaddress = address;
//                t.port = port;
                }

                break;

            case DISCONNECT:
                break;
        }
    }

    public void sendData(byte[] data, InetAddress ip, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (TrackClient connectedPlayer : game.getTracks()) {
            sendData(data, connectedPlayer.ipaddress, connectedPlayer.port);
        }
    }
}
