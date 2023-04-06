package com.example.se2_exploding_kittens.Network;

import com.example.se2_exploding_kittens.Lobby;
import com.example.se2_exploding_kittens.MessageCallback;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class LobbyListener implements Runnable {

    private boolean terminateListening = false;
    private MessageCallback packetReceivedCallback;
    private ArrayList <Lobby> lobbies = new ArrayList<Lobby>();

    public LobbyListener(MessageCallback packetReceivedCallback){
        this.packetReceivedCallback = packetReceivedCallback;
    }

    public void terminateListening(){
        terminateListening = true;
    }


    public void startListening() {
        try {
            DatagramSocket socket = new DatagramSocket(51600);

            byte[] buffer = new byte[1024];

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (!terminateListening) {
                // wait for UDP packet
                socket.receive(packet);

                String packetData = new String(packet.getData(), 0, packet.getLength());
                String packetSrcAddr = packet.getAddress().toString();

                String[] split = packetData.split("#");
                if(split.length == 2){
                    int lobbyPort = Integer.parseInt(split[1]);
                    Lobby lobby = new Lobby(packetSrcAddr, split[0],lobbyPort);
                    boolean lobbyExists = false;
                    for (Lobby l:lobbies) {
                        if(l.getName().equals(lobby.getName()) && l.getAddress().equals(lobby.getAddress())){
                            lobbyExists = true;
                            break;
                        }
                    }
                    if(!lobbyExists){
                        lobbies.add(lobby);
                        packetReceivedCallback.responseReceived(lobbies.indexOf(lobby)+"");
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Lobby> getLobbies(){
        return lobbies;
    }

    @Override
    public void run() {
        startListening();
    }
}
