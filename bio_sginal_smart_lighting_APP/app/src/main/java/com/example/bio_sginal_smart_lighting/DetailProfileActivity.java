package com.example.bio_sginal_smart_lighting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bio_sginal_smart_lighting.SocketIO.Profile;
import com.example.bio_sginal_smart_lighting.SocketIO.SocketManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailProfileActivity extends AppCompatActivity {
    Context context=null;
    private SocketManager socketManager;
    ImageView profileImageView;
    TextView profileNameView, temperatureView, humidityView;
    Button lightControlButton;
    LineChart lineChart;
    LineData lineData;
    ArrayList<Entry> bpmValues, gsrValues;
    ArrayList<ILineDataSet> dataSets;
    LineDataSet bpmDataSet, gsrDataSet;

    String prevStressLevel = "Not-Connected";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);
        context = getApplicationContext();

        profileImageView = findViewById(R.id.profileImageDetail);
        profileNameView = findViewById(R.id.profileNameDetail);
        temperatureView = findViewById(R.id.temperatureNum);;
        humidityView = findViewById(R.id.humidityNum);
        lineChart = findViewById(R.id.lineChart);

        Uri profileImageUri = Uri.parse(getIntent().getStringExtra("profile_image"));
        String profileName = getIntent().getStringExtra("profile_name");

        profileImageView.setImageURI(profileImageUri);
        profileNameView.setText(profileName);


        lightControlButton = findViewById(R.id.LightControlButton);
        lightControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lightIntent = new Intent(getApplicationContext(), LightingActivity.class);
                startActivity(lightIntent);

            }
        });


        // ArrayList 초기화
        bpmValues = new ArrayList<>();
        gsrValues = new ArrayList<>();
        dataSets = new ArrayList<>();

        bpmDataSet = new LineDataSet(bpmValues, "BPM Data"); // bpm 데이터셋
        gsrDataSet = new LineDataSet(gsrValues, "GSR Data"); // gsr 데이터셋

        dataSets.add(bpmDataSet); // bpm 데이터셋 추가
        dataSets.add(gsrDataSet); // gsr 데이터셋 추가

        // BPM: black lines and points
        bpmDataSet.setColor(Color.RED);
        bpmDataSet.setCircleColor(Color.RED);

        // GSR: red lines and points
        gsrDataSet.setColor(Color.BLUE);
        gsrDataSet.setCircleColor(Color.BLUE);

        // 값 표시 비활성화
        bpmDataSet.setDrawValues(false); // BPM 데이터 포인트의 값 표시 비활성화
        gsrDataSet.setDrawValues(false); // GSR 데이터 포인트의 값 표시 비활성화

        // 차트 외관 사용자 정의
        lineChart.getDescription().setEnabled(false); // 설명 라벨 제거
        lineChart.getAxisRight().setEnabled(false); // 오른쪽 Y축 비활성화
        lineChart.getXAxis().setEnabled(false); // X축 비활성화
        lineChart.getAxisLeft().setTextSize(16f);
        lineChart.getLegend().setTextSize(20f);
        lineChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // 레전드 가운데 정렬
//        lineChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM); // X축을 하단에 위치


        // LineChart 설정
        lineData = new LineData(dataSets);
        lineChart.setData(lineData);

        socketManager = SocketManager.getInstance();

    }


    private Emitter.Listener sensorDataListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // args[0]를 JSONObject로 처리
                        JSONObject sensorData = (JSONObject) args[0]; // 직접적으로 JSONObject로 캐스팅
//                            Log.d("SensorData", "Received: " + sensorData.toString());

                        // JSON 데이터에서 값 추출
                        int bpm = sensorData.getInt("bpm");
                        int gsr = sensorData.getInt("gsr");
                        float tem = (float) sensorData.getDouble("temperature");
                        float hum = (float) sensorData.getDouble("humidity");
                        String stressLevel = sensorData.optString("stressLevel", "Not-Connected");
                        Log.d("SensorData", "Received: BPM = " + bpm + ", GSR = " + gsr +
                                "Stress Level = " + stressLevel);
                        Toast.makeText(context,stressLevel, Toast.LENGTH_SHORT).show();

                        if (!stressLevel.equals(prevStressLevel)) {
                            if (stressLevel.equals("Non-Stress"))
                                socketManager.emitLighting("lightControl", "LIGHT_GREEN");
                            else if (stressLevel.equals("High-Stress"))
                                socketManager.emitLighting("lightControl", "LIGHT_RED");
                            else if (stressLevel.equals("Low-Stress"))
                                socketManager.emitLighting("lightControl", "LIGHT_YELLOW");
                        }

                        // 현재 stressLevel을 이전으로 설정
                        prevStressLevel = stressLevel;



                        // X축 값을 위한 인덱스 (데이터 수에 따라)
                        int nextIndex = bpmValues.size();

                        // 새로운 데이터 추가
                        bpmValues.add(new Entry(nextIndex, bpm));
                        gsrValues.add(new Entry(nextIndex, gsr));

                        temperatureView.setText(tem + "°C");
                        humidityView.setText(hum + "%"); // humidityView로 수정하여 습도도 표시

                        // 차트에 데이터 반영
                        bpmDataSet.notifyDataSetChanged(); // bpm 데이터셋 갱신
                        gsrDataSet.notifyDataSetChanged(); // gsr 데이터셋 갱신
                        lineChart.getData().notifyDataChanged(); // 차트 데이터 갱신
                        lineChart.notifyDataSetChanged(); // 차트 업데이트
                        lineChart.invalidate(); // 차트 다시 그리기

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ClassCastException e) {
                        Log.e("responseSensorData", "Received data is not in the expected format", e);
                    }
                }
            });
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        // DetailActivity가 다시 화면에 나타날 때 소켓 재연결 및 센서 데이터 요청
        socketManager.connect();

        socketManager.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SocketManager", "Socket connected");
                socketManager.emit("requestSensorData");
            }
        });

        // 서버로부터 센서 데이터 수신 리스너 등록
        socketManager.on("responseSensorData", sensorDataListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (socketManager != null) {
            // DetailActivity가 일시 중지될 때 리스너 제거 및 소켓 연결 해제 (필요한 경우)
            socketManager.off("responseSensorData", sensorDataListener); // 리스너 제거
            socketManager.disconnect(); // 소켓 연결 해제
            Log.d("SocketManager", "Socket disconnected in MainActivity");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 소켓 연결 해제
        socketManager.off("responseSensorData",sensorDataListener);
        socketManager.disconnect();

    }


}