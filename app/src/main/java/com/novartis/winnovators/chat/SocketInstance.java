package com.novartis.winnovators.chat;

import android.content.Context;

import com.novartis.winnovators.utils.UserUtils;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketInstance {
    private static Socket iSocket;
    private static final String SOCKET_URL = "https://redis.eventonlineregister.com:6380";


    public SocketInstance(Context context) {
        try {
            IO.Options opts = new IO.Options();
            opts.query = "id=" + UserUtils.getUserId(context);
            iSocket = IO.socket(SOCKET_URL,opts);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static Socket getSocket() {
        return iSocket;
    }
}
