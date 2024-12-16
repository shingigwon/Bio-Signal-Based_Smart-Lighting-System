package com.example.bio_sginal_smart_lighting;

import android.net.Uri;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bio_sginal_smart_lighting.SocketIO.Profile;
import com.example.bio_sginal_smart_lighting.Adapter.ProfileAdapter;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.example.bio_sginal_smart_lighting.SocketIO.SocketManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.socket.emitter.Emitter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SocketManager socketManager;
    RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private List<Profile> profiles;
    // 프로필 데이터 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        socketManager = SocketManager.getInstance();
//        socketManager.connect();

        // MainActivity에서 RecyclerView 설정
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2열 그리드 레이아웃
        profiles = new ArrayList<>();
        getProfiles();
        adapter = new ProfileAdapter(profiles);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri profileImageUri = data.getParcelableExtra("profile_image");
                String name = data.getStringExtra("profile_name");
                String createdate = data.getStringExtra("profile_created");
                if (name != null) {
                    profiles.add(new Profile(profileImageUri.toString(), name,createdate));
                    adapter.notifyItemInserted(profiles.size() - 1);

                }
            }
        }
    }
    private void getProfiles(){
        // 연결 이벤트가 발생한 후에 리스너 등록
        socketManager.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SocketManager", "Socket connected");
                socketManager.emit("requestProfiles");
            }
        });
        socketManager.on("responseProfiles", proFlieListener);

    }

    private Emitter.Listener proFlieListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // JSON을 List<Profile>로 변환
                    String json = args[0].toString();
                    Log.d("SocketManager", "Received JSON: " + json);


                    Type listType = new TypeToken<List<Profile>>() {}.getType();
                    profiles = new Gson().fromJson(json, listType);
                    adapter.updateProfiles(profiles);  // UI 갱신

//                    //DB프로필 확인
//                    for(int i=0; i<profiles.size(); i++){
//                        Log.d("ProfileData", "Image URL: " + profiles.get(i).getImageUri() + ", Name: " + profiles.get(i).getName() + ", CreateDate: " + profiles.get(i).getDate());
//                    }

                }
            });
        }
    };

    // MainActivity가 화면에 보일 때 소켓 연결
    @Override
    protected void onResume() {
        super.onResume();

        socketManager.connect(); // 소켓 연결

        // 소켓이 연결되면 프로필 데이터를 요청
        socketManager.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SocketManager", "Socket connected in MainActivity");
                socketManager.emit("requestProfiles");
            }
        });

        // 서버로부터 프로필 데이터를 수신하는 리스너 등록
        socketManager.on("responseProfiles", proFlieListener);
    }

    // MainActivity가 화면에서 벗어날 때 소켓 연결 해제
    @Override
    protected void onPause() {
        super.onPause();

        if (socketManager != null) {
            socketManager.off("responseProfiles", proFlieListener); // 리스너 제거
            socketManager.disconnect(); // 소켓 연결 해제
            Log.d("SocketManager", "Socket disconnected in MainActivity");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 소켓 연결 해제
        socketManager.off("responseProfiles", proFlieListener);
        socketManager.disconnect();

    }
}