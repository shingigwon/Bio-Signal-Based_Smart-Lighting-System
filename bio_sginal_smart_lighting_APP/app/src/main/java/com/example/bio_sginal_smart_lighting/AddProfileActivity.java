package com.example.bio_sginal_smart_lighting;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bio_sginal_smart_lighting.SocketIO.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private SocketManager socketManager;
    Uri selectedImageUri;
    ImageView profileImageView;
    TextView imageTextOverlay;
    EditText nameEditText;
    Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        socketManager = SocketManager.getInstance();
        socketManager.connect();

        profileImageView = findViewById(R.id.profileImageView);
        nameEditText = findViewById(R.id.edit_text_name);
        addButton = findViewById(R.id.button_add_profile);

        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(AddProfileActivity.this, ImageSelectActivity.class);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });


        addButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            if (!name.isEmpty()) {
                // 이름이 비어있지 않은 경우 결과를 MainActivity로 반환
                String createdate = currentDate();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("profile_image",selectedImageUri);
                resultIntent.putExtra("profile_name", name);
                resultIntent.putExtra("profile_created", createdate);


                // JSON 객체 생성
                JSONObject profileData = new JSONObject();
                try {
                    profileData.put("image", selectedImageUri);
                    profileData.put("name", name);
                    profileData.put("date", createdate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                socketManager.emitInsertProfile("InsertProFile", profileData);
                setResult(RESULT_OK, resultIntent);
                finish();
            }


        });
    }

    // 이미지 선택 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getParcelableExtra("profile_image");

            if (selectedImageUri != null) {
                profileImageView.setImageURI(selectedImageUri);  // 선택한 이미지를 ImageView에 표시

                imageTextOverlay = findViewById(R.id.imageTextOverlay);
                imageTextOverlay.setText("변경하려면 다시 클릭하세요");
                // 텍스트뷰를 숨김
//                imageTextOverlay.setVisibility(View.GONE);

            }
        }
    }

    private String currentDate(){
        // 현재 날짜 가져오기
        Date date = new Date();
        // 날짜 형식을 지정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        // 날짜를 문자열로 변환
        String formattedDate = dateFormat.format(date);

        return formattedDate;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 소켓 연결 해제
        socketManager.disconnect();
    }

}
