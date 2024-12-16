package com.example.bio_sginal_smart_lighting;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bio_sginal_smart_lighting.Adapter.ImageAdapter;

import java.util.ArrayList;

public class ImageSelectActivity extends AppCompatActivity {
    GridView gridView;
    // 이미지 URI 리스트
    private ArrayList<Uri> imageUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        // 이미지 URI 목록 초기화 (리소스 ID를 URI로 변환하거나 실제 URI 경로 사용)
        imageUris = new ArrayList<>();
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil1));
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil2));
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil3));
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil4));
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil5));
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil6));
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil7));
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil8));
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil9));
        imageUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.profil10));

        // GridView 초기화
        gridView = findViewById(R.id.gridView);
        ImageAdapter imageAdapter = new ImageAdapter(this, imageUris); // URI를 사용하는 어댑터
        gridView.setAdapter(imageAdapter);

        // GridView 항목 클릭 시 동작
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 선택한 이미지의 URI를 이전 액티비티로 전달
                Uri selectedImageUri = imageUris.get(position);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("profile_image", selectedImageUri);  // URI를 문자열로 전달
                setResult(RESULT_OK, resultIntent);
                finish(); // 액티비티 종료 후 이전 화면으로 돌아감
            }
        });
    }
}
