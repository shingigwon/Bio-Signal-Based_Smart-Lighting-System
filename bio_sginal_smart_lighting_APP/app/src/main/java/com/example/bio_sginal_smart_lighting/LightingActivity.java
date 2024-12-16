package com.example.bio_sginal_smart_lighting;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import com.marcinmoskala.arcseekbar.ArcSeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bio_sginal_smart_lighting.SocketIO.SocketManager;

public class LightingActivity extends AppCompatActivity {
    private SocketManager socketManager;

    private ArcSeekBar arcSeekBar;

    private TextView brightnessPercentage;
    private Button minusButton, plusButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lighting);

        socketManager = SocketManager.getInstance();
        socketManager.connect();




        // UI 요소 초기화
        arcSeekBar= findViewById(R.id.arcSeekBar);
        brightnessPercentage = findViewById(R.id.brightness_percentage);
        minusButton = findViewById(R.id.minus_button);
        plusButton = findViewById(R.id.plus_button);

        // ArcSeekBar에 그라데이션 적용
        arcSeekBar.setProgressGradient(
                Color.parseColor("#00897b"), // Slightly Lighter Dark Green
                Color.parseColor("#00bfae"), // Light Green/Turquoise
                Color.parseColor("#00c853"), // Bright Green
                Color.parseColor("#004d40")  // Deep Green (Darker)
        );

        // 시크바의 초기값을 텍스트뷰에 설정
        updateBrightnessText(arcSeekBar.getProgress());

        arcSeekBar.setOnProgressChangedListener(progress -> {
            brightnessPercentage.setText(String.valueOf(progress));
            send("SET_BRIGHTNESS "+progress);
        });


        // minusButton 클릭 리스너 (-5씩 감소)
        minusButton.setOnClickListener(v -> {
            int currentProgress = arcSeekBar.getProgress();
            if (currentProgress >= 5) {
                arcSeekBar.setProgress(currentProgress - 5);  // 최소값은 0
            } else {
                arcSeekBar.setProgress(0);
            }
        });

        // plusButton 클릭 리스너 (+5씩 증가)
        plusButton.setOnClickListener(v -> {
            int currentProgress = arcSeekBar.getProgress();
            if (currentProgress <= 250) {
                arcSeekBar.setProgress(currentProgress + 5);  // 최대값은 100
            } else {
                arcSeekBar.setProgress(255);
            }
        });

        setupColorButtons();
    }
    // 밝기 퍼센트 텍스트 업데이트 함수
    private void updateBrightnessText(int progress) {

        brightnessPercentage.setText(String.valueOf(progress));

    }
    private void setupColorButtons() {
        // 버튼 객체 참조
        Button redButton = findViewById(R.id.redButton);
        Button orangeButton = findViewById(R.id.orangeButton);
        Button yellowButton = findViewById(R.id.yellowButton);
        Button greenButton = findViewById(R.id.greenButton);
        Button blueButton = findViewById(R.id.blueButton);
        Button indigoButton = findViewById(R.id.indigoButton);
        Button purpleButton = findViewById(R.id.purpleButton);
        Button offButton = findViewById(R.id.OFFButton);

        // 색상 변경
        redButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // 빨간색
        orangeButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF5722"))); // 주황색
        yellowButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFC107"))); // 노란색
        greenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // 초록색
        blueButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2196F3"))); // 파란색
        indigoButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3F51B5"))); // 남색
        purpleButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9C27B0"))); // 보라색
        offButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK)); // 검정색 (끄기 버튼)

        // 버튼 클릭 이벤트
        redButton.setOnClickListener(v -> {
            send("LIGHT_RED");
        });
        orangeButton.setOnClickListener(v -> {
            send("LIGHT_ORANGE");
        });
        yellowButton.setOnClickListener(v -> {
            send("LIGHT_YELLOW");
        });
        greenButton.setOnClickListener(v -> {
            send("LIGHT_GREEN");
        });
        blueButton.setOnClickListener(v -> {
            send("LIGHT_BLUE");
        });
        indigoButton.setOnClickListener(v -> {
            send("LIGHT_INDIGO");
        });
        purpleButton.setOnClickListener(v -> {
            send("LIGHT_PURPLE");
        });
        offButton.setOnClickListener(v -> {
            send("LIGHT_OFF");
        });
    }



    public void send(String color){
        socketManager.emitLighting("lightControl", color);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 소켓 연결 해제
//        socketManager.disconnect();
    }
}