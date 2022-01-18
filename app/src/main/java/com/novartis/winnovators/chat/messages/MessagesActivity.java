package com.novartis.winnovators.chat.messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novariscyclemeeting2022.R;
import com.novartis.winnovators.utils.UserUtils;
import com.novartis.winnovators.chat.SocketInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MessagesActivity extends AppCompatActivity {

    TextView screenTitle, et_msg;
    ImageView send_msg;
    RecyclerView recyclerView;
    ProgressBar loading;
    ArrayList<Message_item> items_list;
    Messages_adapter adapter;
    int toUserId, fromUserId;
    String toUserName, toImageUrl;
    private Socket mSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        initFields();
        screenTitle.setOnClickListener(v -> onBackPressed());

        mSocket.on(Socket.EVENT_CONNECT, onConnected);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on("getMessagesResponse", getMessagesResponse);
        mSocket.connect();
        send_msg.setOnClickListener(v -> {
            if (et_msg.length() != 0) {
                sendMsg(et_msg.getText().toString());
            } else {
                Toast.makeText(getBaseContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
        });

        getChatMessages();
    }
    // Socket Listeners
    private final Emitter.Listener onConnected = args -> runOnUiThread(() -> Log.e("connection success", Arrays.toString(args)));

    private final Emitter.Listener onConnectError = args -> runOnUiThread(() -> {
        Log.e("connection Failed", Arrays.toString(args));
        Toast.makeText(getBaseContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
    });

    private final Emitter.Listener getMessagesResponse = args -> runOnUiThread(() -> {
        Log.e("Messages Response", Arrays.toString(args));
        loading.setVisibility(View.GONE);
        JSONObject data = (JSONObject) args[0];
        try {
            JSONArray usersArray = data.getJSONArray("result");
            setMessages_list(usersArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    });

    //My Methods
    private void sendMsg(String msg) {
        HashMap map = new HashMap();
        map.put("fromUserId", fromUserId);
        map.put("toUserId", toUserId);
        map.put("message", msg);

        JSONObject obj = new JSONObject(map);
        items_list.add(new Message_item(1, fromUserId, toUserId, msg, "", "", "", "", ""));
        adapter.notifyDataSetChanged();
        scrollToEnd();
        mSocket.emit("addMessage", obj);
        et_msg.setText("");
    }

    private void getChatMessages() {
        HashMap map = new HashMap();
        map.put("fromUserId", fromUserId);
        map.put("toUserId", toUserId);
        final JSONObject obj = new JSONObject(map);
        mSocket.emit("getMessages", obj);
    }

    public void setMessages_list(JSONArray list) {
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject currentObject = list.getJSONObject(i);
                int id = currentObject.getInt("id");
                int fromUserId = currentObject.getInt("fromUserId");
                int toUserId = currentObject.getInt("toUserId");
                String message = currentObject.getString("message");
                String time = currentObject.getString("time");
                String date = currentObject.getString("date");
                String type = currentObject.getString("type");
                String fileFormat = currentObject.getString("fileFormat");
                String filePath = currentObject.getString("filePath");
                items_list.add(new Message_item(id, fromUserId, toUserId, message, time, date, type, fileFormat, filePath));
            }
            adapter.notifyDataSetChanged();
            scrollToEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initFields() {
        toUserId = getIntent().getIntExtra("to_user_id", 0);
        toUserName = getIntent().getStringExtra("to_user_name");
        toImageUrl = getIntent().getStringExtra("to_image_url");
        fromUserId = UserUtils.getUserId(getBaseContext());
        screenTitle = findViewById(R.id.screen_title);
        screenTitle.setText(toUserName);
        et_msg = findViewById(R.id.et_msg);
        send_msg = findViewById(R.id.send_msg);
        items_list = new ArrayList<>();
        loading = findViewById(R.id.loading);
        recyclerView = findViewById(R.id.recycler_view);

        SocketInstance socketInstance = new SocketInstance(getBaseContext());
        mSocket = socketInstance.getSocket();

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Messages_adapter(getBaseContext(), items_list, toImageUrl);
        recyclerView.setAdapter(adapter);
    }

    private void scrollToEnd() {
        recyclerView.scrollToPosition(items_list.size() - 1);
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