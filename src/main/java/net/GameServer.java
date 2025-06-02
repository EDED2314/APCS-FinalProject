package net;

import project.Court;
import project.PlayerClient;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class GameServer extends  Thread{
    private DatagramSocket socket;
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
            String message = new String(packet.getData());
            System.out.println("CLIENT [" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "] > " + message);
            if (message.trim().equalsIgnoreCase("ping")) {
                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
            }
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
        for (PlayerClient connectedPlayer : connectedPlayers){
            sendData(data, connectedPlayer.ipaddress, connectedPlayer.port);
        }
    }
}
