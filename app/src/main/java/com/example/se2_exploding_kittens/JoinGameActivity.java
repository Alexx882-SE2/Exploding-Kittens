package com.example.se2_exploding_kittens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.se2_exploding_kittens.Network.LobbyLogic.LobbyListener;
import com.example.se2_exploding_kittens.Network.MessageCallback;
import com.example.se2_exploding_kittens.Network.LobbyLogic.Lobby;
import com.example.se2_exploding_kittens.Network.LobbyLogic.JoinLobbyCallback;


import java.util.ArrayList;

public class JoinGameActivity extends AppCompatActivity implements MessageCallback, JoinLobbyCallback {

    private LobbyListener ll;
    //private NetworkManager client ;

    private RecyclerView lobbyView;

    private ArrayList<Lobby> lobbies;

    private void addEvtHandler(Button btn, View.OnClickListener listener){
        btn.setOnClickListener(listener);
    }

    public void openJoinGameActivity(){
        ll.terminateListening();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        lobbyView = findViewById(R.id.lobbyView);
        ll = new LobbyListener(this);
        Thread listener = new Thread(ll);
        listener.start();

        lobbies = ll.getLobbies();

        Lobby_RecyclerViewAdapter lobby_recyclerViewAdapter = new Lobby_RecyclerViewAdapter(this, lobbies, this::JoinLobby);
        lobbyView.setAdapter(lobby_recyclerViewAdapter);
        lobbyView.setLayoutManager(new LinearLayoutManager(this));
        //client = new NetworkManager();
        //client.runAsClient(ll.getLobbies().get(0).getAddress(),ll.getLobbies().get(0).getPort());
        //client.subscribeCallbackToMessageID(this,200);
        //client.sendMessageFromTheClient(new Message(MessageType.MESSAGE,200,"200"));
    }
    @Override
    public void responseReceived(String text, Object sender) {
        if(sender instanceof LobbyListener){
            lobbyFound(text);
        }
    }

    private void lobbyFound(String text) {
        lobbies = ll.getLobbies();
        runOnUiThread(() -> lobbyView.getAdapter().notifyDataSetChanged());
    }

    @Override
    public void JoinLobby(Lobby lobby) {
        if(lobby == null)
            throw new NullPointerException();

        //https://www.tutorialspoint.com/how-to-pass-an-object-from-one-activity-to-another-in-android

        //client.runAsClient(lobby.getAddress(),lobby.getPort());
        Intent intent = new Intent(this, JoiningGameActivity.class);
        intent.putExtra("address", lobby.getAddress());
        intent.putExtra("port", lobby.getPort());
        intent.putExtra("name", lobby.getName());
        startActivity(intent);
    }
}