package com.example.bio_sginal_smart_lighting.SocketIO;
import android.util.Log;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketManager {

    private Socket mSocket;
    private static SocketManager socketInstance;


    public SocketManager() {
        try {
            // 서버 URL로 소켓 연결
            mSocket = IO.socket("http://10.0.2.2:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // Singleton 패턴을 사용하여 SocketManager의 인스턴스를 반환
    public static SocketManager getInstance() {
        if (socketInstance == null) {
            socketInstance = new SocketManager();
        }
        return socketInstance;
    }


    // 소켓 연결
    public void connect() {
        if (mSocket != null) {
            mSocket.connect();
            Log.d("SocketManager", "Socket connected");
        }
    }

    // 소켓 연결 해제
    public void disconnect() {
        if (mSocket != null) {
            mSocket.disconnect();
            Log.d("SocketManager", "Socket disconnected");
        }
    }


    // 특정 이벤트에 대한 리스너 설정
    public void on(String event, Emitter.Listener listener) {
        mSocket.on(event, listener);
    }

    // 특정 이벤트에 대한 리스너 제거
    public void off(String event, Emitter.Listener listener) {
        mSocket.off(event, listener);
    }


    public void emit(String event) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit(event);
            Log.d("SocketManager", "Emitted event: " + event);
        } else {
            Log.e("SocketManager", "Socket not connected, cannot emit event: " + event);
        }
    }

    public void emitLighting(String event, String data) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit(event, data);
            Log.d("SocketManager", "Emitted event: " + event + " with data: " + data);
        } else {
            Log.e("SocketManager", "Socket not connected, cannot emit event: " + event);
        }
    }

    public void emitInsertProfile(String event, JSONObject data) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit(event, data);
            Log.d("SocketManager", "Emitted event: " + event + " with data: " + data);
        } else {
            Log.e("SocketManager", "Socket not connected, cannot emit event: " + event);
        }
    }
}
