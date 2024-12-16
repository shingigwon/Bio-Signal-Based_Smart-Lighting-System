const express = require("express");
const http = require("http");
const socketIO = require("socket.io");
const { exec } = require("child_process");
const axios = require("axios");
const {
  initializeSerialPort,
  sendDataToArduino,
} = require("./Arduino/Serial.js");
const { sensorSave, profileSave, getProfiles } = require("./SQL/mysql.js");

const app = express();
const server = http.createServer(app);
const io = socketIO(server);

const port = 3000;
let parser;

// 시리얼 포트 초기화
(async () => {
  try {
    parser = await initializeSerialPort(); // parser 초기화
  } catch (error) {
    console.error("시리얼 포트를 열 수 없습니다: ", error.message);
  }
})();

// Flask 서버 실행
const flaskProcess = exec(
  "python AI_Test/Model_Server.py",
  (error, stdout, stderr) => {
    if (error) {
      console.error(`Flask 서버 실행 중 오류 발생: ${error.message}`);
      return;
    }
    if (stderr) {
      console.error(`Flask 서버 오류: ${stderr}`);
      return;
    }
    console.log(`Flask 서버 출력: ${stdout}`);
  }
);

// Flask 서버 종료 처리
process.on("exit", () => {
  flaskProcess.kill();
});

io.on("connection", (socket) => {
  console.log(socket.id, "Connected"); // 연결됨을 콘솔에 출력

  // 프로필데이터 전송(DB->MainActivity)
  socket.on("requestProfiles", () => {
    getProfiles((error, profiles) => {
      if (error) {
        console.error("Failed to retrieve profiles:", error);
        socket.emit("error", { message: "Failed to retrieve profiles" });
        return;
      }
      socket.emit("responseProfiles", profiles);
    });
  });

  // 클라이언트에서 requestSensorData 이벤트 수신 시 센서 데이터 전송(Arduino->DetailActivity) 시작
  socket.on("requestSensorData", () => {
    console.log(`Client ${socket.id} requested sensor data`);

    // parser.on을 통해 센서 데이터를 전송
    const sendSensorData = (data) => {
      try {
        const jsonData = JSON.parse(data);
        const sensorData = {
          bpm: jsonData.BPM,
          gsr: jsonData.GSR,
          temperature: jsonData.Temperature,
          humidity: jsonData.Humidity,
        };
        console.log(
          `수신된 데이터 = 심박수 : ${sensorData.bpm}, 전도율 : ${sensorData.gsr}, 온도: ${sensorData.temperature}, 습도: ${sensorData.humidity}`
        );

        // Database 저장
        sensorSave(
          sensorData.bpm,
          sensorData.gsr,
          sensorData.temperature,
          sensorData.humidity
        );

        // Flask 서버로 데이터 전송
        axios
          .post("http://localhost:5000/predict", {
            gsr: sensorData.gsr,
          })
          .then((response) => {
            console.log("Flask 서버 응답:", response.data);

            // 클라이언트에게 센서 데이터 전송
            socket.emit("responseSensorData", {
              ...sensorData,
              stressLevel: response.data.stressLevel,
            });
          })
          .catch((error) => {
            console.error("Error communicating with Flask server:", error);
          });

        // socket.emit("responseSensorData", sensorData); // 클라이언트에게 센서 데이터 전송
      } catch (error) {
        console.error("Error parsing sensor data:", error);
      }
    };

    // parser.on("data")를 등록하여 센서 데이터 수신 시마다 sendSensorData 호출
    parser.on("data", sendSensorData);

    // 클라이언트가 disconnect 시 parser의 data 이벤트 리스너 제거
    socket.on("disconnect", () => {
      console.log(`Client ${socket.id} disconnected`);
      parser.removeListener("data", sendSensorData); // 해당 클라이언트에 대한 데이터 전송 중단
    });
  });

  // 클라이언트에서 SaveDB 이벤트 수신
  socket.on("InsertProFile", (profileData) => {
    console.log("Received Profile Information:", profileData);

    // DB 데이터 저장
    profileSave(profileData.image, profileData.name, profileData.date);
  });

  // 클라이언트에서 lightControl 이벤트 수신
  socket.on("lightControl", (lightData) => {
    console.log("Received light control data:", lightData);

    // 아두이노로 데이터 전송
    sendDataToArduino(lightData);
  });

  socket.on("disconnect", () => {
    console.log("User disconnected: " + socket.id);
  });
});

// 서버 실행
server.listen(port, () => {
  console.log(`서버가 http://localhost:${port} 에서 실행 중입니다.`);
});
