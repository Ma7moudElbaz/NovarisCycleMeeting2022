package com.novartis.winnovators.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novariscyclemeeting2022.R;
import com.novartis.winnovators.AppClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    TextView screenTitle;
    private Socket mSocket;

    RecyclerView recyclerView;

    ProgressBar loading;
    ArrayList<User_item> users_list;
    Users_adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());

        mSocket.on(Socket.EVENT_CONNECT, onConnected);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on("chatListRes", onChatListRes);
        mSocket.on("getMessagesResponse", getMessagesResponse);
        mSocket.on(Socket.EVENT_MESSAGE, onMessage);
//        mSocket.on("typing", onTyping);
        mSocket.on("getMessages", getMessages);
        mSocket.on("addMessageResponse", addMessageResponse);

        mSocket.connect();


        HashMap map = new HashMap();
        map.put("fromUserId", 103);
        map.put("toUserId", 109);
        final JSONObject obj = new JSONObject(map);

        mSocket.emit("getMessages", obj);

//        test();
    }

    private final Emitter.Listener onConnected = args -> runOnUiThread(() -> {
        mSocket.emit("chatList", 3);
        Log.e("connection success", Arrays.toString(args));
    });

    private final Emitter.Listener onConnectError = args -> runOnUiThread(() -> {
        Log.e("connection Failed", Arrays.toString(args));
    });

    private final Emitter.Listener onChatListRes = args -> runOnUiThread(() -> {
        Log.e("Chat List", Arrays.toString(args));
        loading.setVisibility(View.GONE);
        JSONObject data = (JSONObject) args[0];
        try {
            JSONArray usersArray = data.getJSONArray("chatList");
            setUsers_list(usersArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    });

    private final Emitter.Listener getMessagesResponse = args -> runOnUiThread(() -> Log.e("Messages Response", Arrays.toString(args)));

    private final Emitter.Listener getMessages = args -> runOnUiThread(() -> {
        Toast.makeText(getApplicationContext(), args.toString(), Toast.LENGTH_LONG).show();
        Log.e("Messages", Arrays.toString(args));

    });

    private final Emitter.Listener addMessageResponse = args -> runOnUiThread(() -> {
        Log.e("Add Message", Arrays.toString(args));
        JSONObject data = (JSONObject) args[0];
        String text = null;
        //extract data from fired event
        try {
            text = data.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//                    MessageList.add(new Message(reciver.getName(), text));
//                    chatBoxAdapter.notifyDataSetChanged();
        //set the adapter for the recycler view}
    });

    private final Emitter.Listener onTyping = args -> runOnUiThread(() -> Log.e("Typing", Arrays.toString(args)));

    private final Emitter.Listener onMessage = args -> runOnUiThread(() -> Log.e("On Message", Arrays.toString(args)));


    public void setUsers_list(JSONArray list) {
        users_list.clear();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                String id = currentObject.getString("id");
                String name = currentObject.getString("name");
                String socket_id = currentObject.getString("socket_id");
                String online = currentObject.getString("online");
                String updated_at = currentObject.getString("updated_at");
                String img_profile = "";
                users_list.add(new User_item(id, name, socket_id, online, updated_at, img_profile));
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initFields() {
        screenTitle = findViewById(R.id.screen_title);
        users_list = new ArrayList<>();
        loading = findViewById(R.id.loading);
        recyclerView = findViewById(R.id.recycler_view);

        SocketInstance socketInstance = new SocketInstance(getBaseContext());
        mSocket = socketInstance.getSocket();

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Users_adapter(getBaseContext(), users_list);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    private void test(){
        HashMap map = new HashMap();
        map.put("fromUserId", 109);
        map.put("toUserId", 103);
        map.put("toSocketId", null);
        map.put("message", "message");
        map.put("time", "01:48 PM");
        map.put("filePath", "");
        map.put("fileFormat", "");
        map.put("type", "text");
        map.put("date", "2019-05-7");

        JSONObject obj = new JSONObject(map);
        mSocket.emit("addMessage", obj);
    }
}