package net;

import packet.*;
import project.Ball;
import project.CourtClient;
import project.TrackClient;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

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
            byte[] data = new byte[2048];
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
//            case LOGIN:
//                packet = new Packet20Login(data);
//                System.out.println("[" + address.getHostAddress() + ":" + port + "] Track: " + ((Packet20Login) packet).getTrackId() + " has joined the game.");
//                addTrack(address, port, (Packet20Login) packet);
//                break;
            case DISCONNECT:
                //TODO: implemnt delete tracks
                break;
            case SINGLE_PLAYER_UPDATE:
                packet = new Packet12SinglePlayerUpdate(data);
                String id = ((Packet12SinglePlayerUpdate) packet).getId();
                System.out.println("Server-side Track from [" + address.getHostAddress() + ":" + port + "] with id: " + id + ", successfully relayed its player update to playerside track!");
                clientCourt.updateTrack(id, ((Packet12SinglePlayerUpdate) packet).getDir());
                //done!
                break;
            case SYNC:
                packet = new Packet14Sync(data);
                System.out.println("Player id: [" + clientCourt.getPlayerId() + "]: received sync request with server: " + "[" + address.getHostAddress() + ":" + port + "]");
                sync((Packet14Sync) packet);
                break;
            case BALLS_UPDATE:
                Packet15BallsUpdate ballsUpdatePacket = new Packet15BallsUpdate(data);
                System.out.println("Player id: [" + clientCourt.getPlayerId() + "]: ballsUpdate request received from " + "[" + address.getHostAddress() + ":" + port + "]");
                updateBalls(ballsUpdatePacket);
                break;
            case PLAYER_POINTS_UPDATE:
                Packet16PlayersPointUpdate playersPointUpdatePacket = new Packet16PlayersPointUpdate(data);
                System.out.println("[" + clientCourt.getPlayerId() + "]: player points update request received from " + "[" + address.getHostAddress() + ":" + port + "]");
                updatePlayerScores(playersPointUpdatePacket);
                break;
        }
    }

    private void updatePlayerScores(Packet16PlayersPointUpdate packet) {
        for (Packet12SinglePlayerUpdate singlePlayerUpdate : packet.getPlayerUpdates()) {
            TrackClient t = clientCourt.getTrackClient(singlePlayerUpdate.getId());
            t.getPlayer().setScore(singlePlayerUpdate.getScore());
        }
    }

    private void updateBalls(Packet15BallsUpdate packet) {
        for (Packet11BallUpdate singleBallUpdate : packet.getBallUpdates()) {
            for (Ball b : clientCourt.getBalls()) {
                if (singleBallUpdate.getId() == b.getId()) {
                    b.setVelocity(singleBallUpdate.getVx(), singleBallUpdate.getVy());
                    b.setCenter_x(singleBallUpdate.getX());
                    b.setCenter_y(singleBallUpdate.getY());
                }
            }
        }
    }

    private void sync(Packet14Sync packet) {
        ArrayList<Packet11BallUpdate> balls = packet.getBallUpdates();
        ArrayList<Packet12SinglePlayerUpdate> players = packet.getPlayerUpdates();

        if (players.size() < 3){
            //don't start syncing until >3 players!
            return;
        }

        ArrayList<Ball> trueBalls = new ArrayList<Ball>();
        for (Packet11BallUpdate ballUpdate : balls) {
            Ball b = new Ball(ballUpdate.getX(), ballUpdate.getY());
            b.setVelocity(ballUpdate.getVx(), ballUpdate.getVy());
            trueBalls.add(b);
        }
        ArrayList<TrackClient> trueTracks = clientCourt.refreshTrackConfiguration(players.size());
        System.out.println(trueTracks);
        System.out.println(players);
        for (int i = 0; i < players.size(); i++) {
            //in this case we hop that the true tracks should be the same order as the plauer update
            //this is because the trueTracks rn don't have any ID, so we can't use getTrackClient from the court class
            TrackClient t = trueTracks.get(i);
            Packet12SinglePlayerUpdate playerUpdate = players.get(i);
            t.setId(playerUpdate.getId());
            t.getPlayer().setDir(playerUpdate.getDir());
            t.getPlayer().setScore(playerUpdate.getScore());
            t.getPlayer().setCenter_x(playerUpdate.getX());
            t.getPlayer().setCenter_y(playerUpdate.getY());
        }

        clientCourt.setBalls(trueBalls);
        clientCourt.setTracks(trueTracks);
    }

//    private void addTrack(InetAddress address, int port, Packet20Login packet) {
//        if (clientCourt.getTrackClient(packet.getTrackId()) != null) {
//            throw new RuntimeException();
//        }
//        TrackClient t = new TrackClient(address, port, packet.getTrackId());
//        clientCourt.addTrack(t);
//    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, TrackClient.defaultPort);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
