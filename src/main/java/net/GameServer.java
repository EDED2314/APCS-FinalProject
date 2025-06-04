package net;

import packet.Packet;
import packet.Packet00Login;
import project.*;

import project.Court;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class GameServer extends Thread {
    private DatagramSocket socket;

    private CourtServer serverCourt;

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
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

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
        Packet packet = null;
        switch (type) {
            case INVALID:
                break;
            case LOGIN:
                packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] Track: " + ((Packet00Login) packet).getTrackId() + " has connected to the server.");
                addTrack(address, port,(Packet00Login) packet);
                break;
            case DISCONNECT:
                break;
        }
    }

    private void addTrack(InetAddress address, int port, Packet00Login packet){
        boolean fail = false;
        for (TrackClient track : serverCourt.getTracks()) {
            if (track.getId().equals(packet.getTrackId())) {
                throw new RuntimeException();
            }
        }
        if (!fail) {
            TrackClient t = new TrackClient(address, port, packet.getTrackId());
            serverCourt.addTrack(t);
            //server should send the newly connected player its location etc
            sendDataToAllClients(packet.getData());
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
        for (TrackClient connectedPlayer :  serverCourt.getTracks()) {
            sendData(data, connectedPlayer.ipaddress, connectedPlayer.port);
        }
    }
}
