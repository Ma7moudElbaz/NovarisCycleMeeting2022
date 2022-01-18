package com.novartis.winnovators.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.novariscyclemeeting2022.R;
import com.novartis.winnovators.utils.UserUtils;
import com.novartis.winnovators.chat.users.User_item;
import com.novartis.winnovators.chat.users.Users_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    TextView screenTitle;
    RecyclerView recyclerView;
    ProgressBar loading;
    ArrayList<User_item> users_list;
    Users_adapter adapter;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());

        mSocket.on(Socket.EVENT_CONNECT, onConnected);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on("chatListRes", onChatListRes);
        mSocket.connect();

        getUsersChatList();
    }

    // Socket Listeners
    private final Emitter.Listener onConnected = args -> runOnUiThread(() -> Log.e("connection success", Arrays.toString(args)));

    private final Emitter.Listener onConnectError = args -> runOnUiThread(() -> {
        Log.e("connection Failed", Arrays.toString(args));
    });

    private final Emitter.Listener onChatListRes = args -> runOnUiThread(() -> {
        Log.e("Chat List", Arrays.toString(args));
        loading.setVisibility(View.GONE);
        JSONObject data = (JSONObject) args[0];
        try {
            if (data.has("chatList")) {
                JSONArray usersArray = data.getJSONArray("chatList");
                setUsers_list(usersArray);
            } else {
                mSocket.emit("chatList", UserUtils.getUserId(getBaseContext()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    //my methods
    private void getUsersChatList() {
        mSocket.emit("chatList", UserUtils.getUserId(getBaseContext()));
    }

    public void setUsers_list(JSONArray list) {
        users_list.clear();
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                int id = currentObject.getInt("id");
                String name = currentObject.getString("name");
                String socket_id = currentObject.getString("socket_id");
                String online = currentObject.getString("online");
                String updated_at = currentObject.getString("updated_at");
                String img_profile = currentObject.getString("url");
                if (img_profile.equals("null")) {
                    img_profile = "https://winnovators.eventonlineregister.com/assets/admin/images/default-avatar.png";
                } else {
                    img_profile = "https://winnovators.eventonlineregister.com/" + img_profile;
                }
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

    @Override
    protected void onPause() {
        super.onPause();
        mSocket.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSocket.connect();
    }
}