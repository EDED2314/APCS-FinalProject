package net;

import project.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class GameServer extends Thread {
    private DatagramSocket socket;

    //THIS NOW MANAGES EVERYTHING SO WE GOTTA CHANGE STUFF!
    private Court game;
    private ArrayList<PlayerClient> connectedPlayers = new ArrayList<PlayerClient>();

    public GameServer(Court game, String ip) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(1331, InetAddress.getByName(ip));
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
                //PlayerClient player = new PlayerClient(address, port);
                Track t = game.addTrack();
                PlayerClient p = new PlayerClient(t, address, port);
                connectedPlayers.add(p);

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
        for (PlayerClient connectedPlayer : connectedPlayers) {
            sendData(data, connectedPlayer.ipaddress, connectedPlayer.port);
        }
    }
}
